package com.njht.webyun.system.mybatis;

import com.njht.webyun.common.UserUtil;
import com.njht.webyun.utils.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.*;

/**
 *  拦截器：拦截所有update操作（包括update，insert,delete）,将被执行操作的额该条数据进行日志记录，存于表sys_date_log中
 *  查看：sys_date_log的　BEHAVIOR_ID　值　与　sys_behavior_log表中的BEHAVIOR_ID值是一一对应的
 *  条件：
 *      1、传参时，采用封装的model实体类，不用map
 *      2、需要记录update数据变动日志的表(delete和insert 不用)，其对应实体类需要添加Integer属性,属性名为pk_primaryid, 如角色表就在model中增加 private Integer pk_roleId;
 *      3、update请求在调用mapper.xml前，需要给pk_primaryid赋值，值即该条数据的id值：primaryid
 */
@Component
@Intercepts({ @Signature(method = "update", type = Executor.class, args ={ MappedStatement.class, Object.class }) })
public class UpdateInterceptor implements Interceptor
{
    private static final String PK_PREFIX = "pk_";
    private static final String INSERT = "__INSERT__";
    private static final String DELETE = "__DELETE__";
    private static final String DATA_LOG_INSERT = "INSERT INTO SYS_DATA_LOG (BEHAVIOR_ID, LOGGED_DATE, TABLE_NAME, COLUMN_NAME, KEY_VALUE, OLD_VALUE, NEW_VALUE) VALUES (?,?,?,?,?,?,?)";

    @Value("${mybatis.excepted:com.aoto.system.dao.mapper.LogMapper}")
    private String[] excepted;
    Connection conn = null;

    @Override
    public Object intercept(Invocation invocation) throws Throwable
    {
        processIntercept(invocation);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target)
    {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties p){

    }

    public void setExcepted(String[] excepted)
    {
        this.excepted = excepted;
    }

    private void processIntercept(Invocation invocation) throws Throwable
    {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        /**
         * id为执行的mapper方法的全路径名，如com.chongdong.ByrAddressMapper.getListByParams
         */
        String id = mappedStatement.getId();

        if (exclude(sqlCommandType, id))
        {
            return;
        }
        
        /*if (!(args[1] instanceof Map))
        {
            return;
        }*/

        if(invocation.getTarget() instanceof  CachingExecutor){
            CachingExecutor ce = (CachingExecutor)invocation.getTarget();
            conn = ce.getTransaction().getConnection();
        }else if(invocation.getTarget() instanceof SimpleExecutor){
            SimpleExecutor executor = (SimpleExecutor) invocation.getTarget();
            conn = executor.getTransaction().getConnection();
        }else{
            if(conn == null){
                return;
            }
        }

        int behaviorId = UserUtil.getBehaviorId();
//        int behaviorId = new Random().nextInt(10)+10;

        if (SqlCommandType.INSERT.equals(sqlCommandType))
        {
            createInsertDataLog(mappedStatement, args[1], behaviorId);
        }
        else if (SqlCommandType.UPDATE.equals(sqlCommandType))
        {
            createUpdateDataLog(mappedStatement, args[1],behaviorId);
        }
        else if (SqlCommandType.DELETE.equals(sqlCommandType))
        {
            createDeleteDataLog( mappedStatement, args[1], behaviorId);
        }
    }

    //删除操作的日志记录
    private void createDeleteDataLog(MappedStatement mappedStatement, Object o, int behaviorId) throws SQLException
    {
        PrimaryKey primaryKey = findPrimaryKey(o);
        ArrayList<Integer> idList = new ArrayList<>();
        ArrayList<DataLog> list = new ArrayList<>();
        DataLog log = null;
        if (null == primaryKey || primaryKey.getId() <= 0)
        {
            if(o instanceof Integer){
                primaryKey = new PrimaryKey("",(Integer)o);

            }else if(o instanceof DefaultSqlSession.StrictMap){
                DefaultSqlSession.StrictMap<Object> map = (DefaultSqlSession.StrictMap<Object>)o;
                if(map.containsKey("list")){
                    idList = (ArrayList<Integer>)map.get("list");
                }else if(map.containsKey("collection")){
                    idList = (ArrayList<Integer>)map.get("collection");
                }else if(map.containsKey("array")){
                    idList = (ArrayList<Integer>)map.get("array");
                }
            }
            else{
                return;
            }
        }

        if (behaviorId <= 0) return;

        String sql = mappedStatement.getBoundSql(o).getSql().replaceAll("\r|\n","").toUpperCase();
        String tableName = StringUtils.trim(StringUtils.substringBetween(sql, "DELETE FROM", "WHERE"));
        if(idList.size()>0 && idList.get(0) instanceof Integer){
            for(Integer id: idList){
                log = new DataLog(behaviorId, tableName, DELETE, id, null, null);
                list.add(log);
            }
            insertDataLog(list);
        }
    }

    //插入操作的日志记录
    private void createInsertDataLog(MappedStatement mappedStatement, Object o, int behaviorId) throws SQLException
    {
        PrimaryKey primaryKey = findPrimaryKey(o);
        
        if (null == primaryKey)
        {
            return;
        }
        
        String sql = mappedStatement.getBoundSql(o).getSql().replaceAll("\r|\n","").toUpperCase();
        String tableName = StringUtils.trim(StringUtils.substringBetween(sql, "INTO", "("));
        
        if (0 == primaryKey.getId())
        {
            String seqSql = "SELECT tablenextid('" + tableName + "')";
            Statement statement = null;
            ResultSet rs = null;

            try
            {
                statement = conn.createStatement();
                rs = statement.executeQuery(seqSql);

                if (rs.next())
                {
                    primaryKey.setId(rs.getInt(1));
                }
            }
            finally
            {
                if (null != rs)
                {
                    rs.close();
                }

                if (null != statement)
                {
                    statement.close();
                }
            }
        }
        
        if (behaviorId <= 0)
        {
            return;
        }
        
        insertDataLog(new DataLog(behaviorId, tableName, INSERT, primaryKey.getId(), null, null));
    }

    //修改操作的日志记录
    private void createUpdateDataLog(MappedStatement mappedStatement, Object o, int behaviorId) throws SQLException
    {
        PrimaryKey primaryKey = findPrimaryKey(o);

        if (null == primaryKey || primaryKey.getId() <= 0)
        {
            return;
        }
        
        if (behaviorId <= 0)
        {
            return;
        }
        String sql = mappedStatement.getBoundSql(o).getSql().replaceAll("\r|\n","").toUpperCase();
        String tableName = StringUtils.trim(StringUtils.substringBetween(sql, "UPDATE", "SET"));
        String selectSql = "SELECT * FROM " + tableName + " WHERE " + primaryKey.getColumn() + " = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            statement = conn.prepareStatement(selectSql);
            statement.setInt(1, primaryKey.getId());
            rs = statement.executeQuery();

            if (!rs.next()) return;

            Object newValue = null;
            Object oldValue = null;
            String column = "";
            ArrayList<DataLog> list = new ArrayList<DataLog>();
            DataLog log = null;

            try {
                Field[] fs = o.getClass().getDeclaredFields();
                for (Field f:fs){
                    f.setAccessible(true);
                    newValue = f.get(o);
                    if(newValue == null || "serialVersionUID".equals(f.getName()) || f.getName().toUpperCase().startsWith("PK_")) {
                        continue;
                    }
                    column = StringUtils.camelCaseToSnakeCase(f.getName());
                    oldValue = rs.getObject(column);
                    if (equals(newValue, oldValue)) {
                        continue;
                    }
                    log = new DataLog(behaviorId, tableName, column, primaryKey.getId(), convertToString(oldValue), convertToString(newValue));
                    list.add(log);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            insertDataLog(list);
        }
        finally
        {
            if (null != rs)
            {
                rs.close();
            }

            if (null != statement)
            {
                statement.close();
            }
        }
    }
    
    private void insertDataLog( DataLog log) throws SQLException
    {
        ArrayList<DataLog> list = new ArrayList<DataLog>();
        list.add(log);
        
        insertDataLog(list);
    }
    
    private void insertDataLog( List<DataLog> list) throws SQLException
    {
        PreparedStatement statement = null;

        try
        {
            statement = conn.prepareStatement(DATA_LOG_INSERT);
            
            for (DataLog log : list)
            {
                statement.setInt(1, log.getBehaviorId());
                statement.setTimestamp(2, new Timestamp((new Date()).getTime()));
                statement.setString(3, log.getTableName());
                statement.setString(4, log.getColumnName());
                statement.setInt(5, log.getKeyValue());
                statement.setString(6, log.getOldValue());
                statement.setString(7, log.getNewValue());
                statement.execute();
            }
        }
        finally
        {
            if (null != statement)
            {
                statement.close();
            }
        }
    }
    
    private boolean equals(Object n, Object o) throws SQLException
    {
        if (null == n && null == o)
        {
            return true;
        }
        else if (null == n && null != o)
        {
            return (o instanceof String) && StringUtils.isEmpty((String) o);
        }
        else if (null != n && null == o)
        {
            return (n instanceof String) && StringUtils.isEmpty((String) n);
        }
        
        if (o instanceof Timestamp)
        {
            return ((Date) n).getTime() == ((Timestamp) o).getTime();
        }
        else if (o instanceof BigDecimal)
        {
            if (n instanceof Integer)
            {
                return (Integer)n == ((BigDecimal)o).intValue();
            }
            else if (n instanceof Boolean)
            {
                if (((BigDecimal)o).intValue() == 1 && ((Boolean)n).booleanValue())
                {
                    return true;
                }
                
                if (((BigDecimal)o).intValue() == 0 && !((Boolean)n).booleanValue())
                {
                    return true;
                }
            }
            else if (n instanceof Float)
            {
                return 0 == ((Float)n).compareTo(((BigDecimal)o).floatValue());
            }
            else if (n instanceof Double)
            {
                return 0 == ((Double)n).compareTo(((BigDecimal)o).doubleValue());
            }
        }
        else
        {
            return n.equals(o);
        }
        
        return false;
    }
    
    private String convertToString(Object obj) throws SQLException
    {        
        if (null == obj)
        {
            return null;
        }
        if (obj instanceof String)
        {
            return StringUtils.defaultIfEmpty((String)obj, StringUtils.EMPTY);
        }
        else if (obj instanceof Date)
        {
            return DateFormatUtils.format((Date)obj, "yyyy-MM-dd HH:mm:ss.SSS");
        }
        else if (obj instanceof Timestamp)
        {
            return ((Timestamp)obj).toString();
        }
        else if (obj instanceof Boolean)
        {
            return ((Boolean)obj).booleanValue() ? "1" : "0";
        }
        else 
        {
            return String.valueOf(obj);
        }
    }

    private boolean exclude(SqlCommandType sqlCommandType, String id)
    {
        if (SqlCommandType.SELECT.equals(sqlCommandType))
        {
            return true;
        }

        for (String namespace : excepted)
        {
            if (id.startsWith(namespace))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * 传入实体model
     * @param object
     * @return
    */
    private PrimaryKey findPrimaryKey(Object object)
    {
        Field[] fs = object.getClass().getDeclaredFields();
        String propertyWithoutPK = null;
        PrimaryKey primaryKey = null;
        try {
            for (Field f:fs){
                f.setAccessible(true);
                if (StringUtils.startsWithIgnoreCase(f.getName(), PK_PREFIX))
                {
                    propertyWithoutPK = StringUtils.substringAfter(f.getName(), PK_PREFIX);
                    int i = 0;
                    try{
                        i = (Integer) f.get(object);
                    }catch (NullPointerException e){
                        i = 0;
                    }
                    primaryKey = new PrimaryKey(propertyWithoutPK, i);
                    /*if (null != primaryKey)
                    {
                        f.set(this,primaryKey.getId());
                    }*/
                    break;
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return primaryKey;
    }

    /**
     * 传入实体类map对象
     * @param map
     * @return
     */
    private PrimaryKey findPrimaryKey(Map<String, Object> map)
    {
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        Map.Entry<String, Object> entry = null;
        String property = null;
        String propertyWithoutPK = null;
        PrimaryKey primaryKey = null;

        while (iterator.hasNext())
        {
            entry = iterator.next();
            property = entry.getKey();

            if (StringUtils.startsWithIgnoreCase(property, PK_PREFIX))
            {
                propertyWithoutPK = StringUtils.substringAfter(property, PK_PREFIX);
                primaryKey = new PrimaryKey(propertyWithoutPK, (Integer) entry.getValue());
                break;
            }
        }

        if (null != primaryKey)
        {
            map.remove(property);
            map.put(propertyWithoutPK, primaryKey.getId());
        }

        return primaryKey;
    }

    class PrimaryKey
    {
        private String column;
        private String property;
        private int id;

        public PrimaryKey(String property, int id)
        {
            this.property = property;
            this.column = StringUtils.camelCaseToSnakeCase(property);
            this.id = id;
        }

        public String getProperty()
        {
            return property;
        }

        public String getColumn()
        {
            return column;
        }

        public int getId()
        {
            return id;
        }
        
        public void setId(int id)
        {
            this.id = id;
        }
    }
    
    class DataLog
    {
        private int behaviorId;
        private String tableName;
        private String columnName;
        private int keyValue;
        private String oldValue;
        private String newValue;
        
        public DataLog(int behaviorId, String tableName, String columnName, int keyValue, String oldValue, String newValue)
        {
            this.behaviorId = behaviorId;
            this.tableName = tableName;
            this.columnName = columnName;
            this.keyValue = keyValue;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public int getBehaviorId()
        {
            return behaviorId;
        }

        public String getTableName()
        {
            return tableName;
        }

        public String getColumnName()
        {
            return columnName;
        }

        public int getKeyValue()
        {
            return keyValue;
        }

        public String getOldValue()
        {
            return oldValue;
        }

        public String getNewValue()
        {
            return newValue;
        }
    }
}
