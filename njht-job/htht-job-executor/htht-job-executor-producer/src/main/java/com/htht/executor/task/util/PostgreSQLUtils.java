package com.htht.executor.task.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class PostgreSQLUtils {

    @Autowired
    @Qualifier("pgsqlJdbcTemplate")
    private JdbcTemplate pgsqlJdbcTemplate;

    private static PostgreSQLUtils postgreSQLUtils;

    @PostConstruct
    public void initJdbcTemplate(){
        postgreSQLUtils = this;
        postgreSQLUtils.pgsqlJdbcTemplate = this.pgsqlJdbcTemplate;
    }


    public static boolean save(String tableName, String modelName, Map<String, String> columnValueMap) {
        final String sql = generateInsertSql(tableName, modelName, columnValueMap);
        final boolean save = save(sql);
        return save;
    }


    public static void createTable(String model, String tableName) {
        String templateSql = "create SEQUENCE newTableName_fid_seq increment by 1 minvalue 1 no maxvalue start with 1;\n" +
                "\n" +
                "\n" +
                "CREATE TABLE \"" + model + "\".\"newTableName\" (\n" +
                "  \"fid\" int4 NOT NULL DEFAULT nextval('newTableName_fid_seq'::regclass),\n" +
                "  \"the_geom\" \"public\".\"geometry\",\n" +
                "  \"location\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
                "  \"ingestion\" timestamp(6),\n" +
                "  \"elevation\" int4,\n" +
                "  PRIMARY KEY (\"fid\")\n" +
                ")\n" +
                ";\n" +
                "\n" +
                "ALTER TABLE \"public\".\"newTableName\" \n" +
                "  OWNER TO \"postgres\";\n" +
                "\n" +
                "CREATE INDEX \"spatial_newTableName_the_geom\" ON \"public\".\"newTableName\" USING gist (\n" +
                "  \"the_geom\" \"public\".\"gist_geometry_ops_2d\"\n" +
                ");";
        final String createSql = templateSql.replace("newTableName", tableName);
        log.info("创建表sql:[{}]", createSql);
        try{
            postgreSQLUtils.pgsqlJdbcTemplate.execute(createSql);
            log.info("创建表[{}.{}]成功!", model, tableName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * 生成插入geoserver表的insert 语句
     *
     * @param tableName
     * @param modelName
     * @param columnValueMap
     * @return
     */
    public static String generateInsertSql(String tableName, String modelName, Map<String, String> columnValueMap) {
        Assert.isTrue(StringUtils.isNotBlank(tableName), "表名不能为空");
        Assert.isTrue(StringUtils.isNotBlank(modelName), "模式名不能为空");
        Assert.isTrue(MapUtils.isNotEmpty(columnValueMap), "列和值不能为空");
        StringBuilder insertDefineSql = new StringBuilder(" insert into ")
                .append("\"").append(modelName).append("\"").append(".")
                .append("\"").append(tableName).append("\"").append(" (");
        StringBuilder valuesSql = new StringBuilder(" values ( ");
        AtomicInteger index = new AtomicInteger(0);
        columnValueMap.forEach((columnName, value) -> {
            insertDefineSql.append("\"").append(columnName).append("\"");
            if (StringUtils.isNotBlank(value)) {
                valuesSql.append("'").append(value).append("'");
            } else {
                valuesSql.append("null");
            }
            int currentIndex = index.getAndIncrement();
            if (currentIndex < columnValueMap.size() - 1) {
                insertDefineSql.append(",");
                valuesSql.append(",");
            }
        });
        insertDefineSql.append(") ");
        valuesSql.append(") ");
        return insertDefineSql.append(valuesSql).append(";").toString();


    }


    /**
     * 保存信息
     *
     * @param sql
     * @return
     */
    public static boolean save(String sql) {

        int count = postgreSQLUtils.pgsqlJdbcTemplate.update(sql);
        return count>0;
    }

    /**
     * 查询信息
     *
     * @param table
     * @param tifName
     * @return
     */
    public static boolean selectByTifName(String table, String tifName) {
        String sql = "SELECT 1 FROM public.\""
                + table + "\"  WHERE location = '" + tifName + "'";
        try {
            postgreSQLUtils.pgsqlJdbcTemplate.execute(sql, (PreparedStatementCallback<Object>) preparedStatement -> {
                preparedStatement.execute();
                ResultSet result = preparedStatement.getResultSet();
                if (result.next()) {
                    return true;
                }
                return false;
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 执行查询语句
     *
     * @param sql
     * @return
     */
    public static List<LinkedHashMap<String, String>> executeSelect(String sql) {
        log.info("执行查询语句[{}]", sql);
        try (Connection connection = postgreSQLUtils.pgsqlJdbcTemplate.getDataSource().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet result = stmt.executeQuery(sql)) {
            final ArrayList<LinkedHashMap<String, String>> resultList = new ArrayList<>();
            while (result.next()) {
                final LinkedHashMap<String, String> map = new LinkedHashMap<>();
                final ResultSetMetaData metaData = result.getMetaData();
                final int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(metaData.getColumnName(i), result.getString(i));
                }
                resultList.add(map);
            }
            log.info("返回结果集,数据量[{}]", resultList.size());
            return resultList;
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException(e.getMessage());
        }finally {

        }
    }


    /**
     * 查询信息
     *
     * @param table
     * @return
     */
    public static String selectCoordinate(String table) {
        AtomicReference<PGobject> the_geom = new AtomicReference<>(new PGobject());
        String sql = "SELECT the_geom FROM public.\""
                + table + "\"";
        try {
            postgreSQLUtils.pgsqlJdbcTemplate.execute(sql, (PreparedStatementCallback<Object>) preparedStatement -> {
                preparedStatement.execute();
                ResultSet result = preparedStatement.getResultSet();
                while (result.next()) {
                    the_geom.set((PGobject) result.getObject("the_geom"));
                    break;
                }
                return the_geom;
            });
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return the_geom.get().toString();

    }

    public static boolean getTableName(String tableName) {
        try (Connection connection = postgreSQLUtils.pgsqlJdbcTemplate.getDataSource().getConnection();
             ResultSet set = connection.getMetaData().getTables(null, null, tableName, null)) {
            return set.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}