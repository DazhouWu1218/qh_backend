package com.htht.executor.download.service.impl;

import com.htht.executor.cimiss.bean.ResultBean;
import com.htht.executor.download.service.CimissDataDealService;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.entity.paramtemplate.CimissDownParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Transactional(rollbackFor = Exception.class)
@Service("cimissDataDealService")
public class CimissDataDealServiceImpl implements CimissDataDealService {
	
	private static Logger logger = LoggerFactory.getLogger(CimissDataDealServiceImpl.class);

	private final static String cimiss_surf_chn_mul_hor_n = "cimiss_surf_chn_mul_hor_n";
	private final static String cimiss_surf_chn_mul_hor = "cimiss_surf_chn_mul_hor";

	@Autowired
    private DataSource dataSource;
	
	@Override
	public void dealStationData(CimissDownParam cimissParam, ResultBean resultBean) {
		logger.info("--CimissDataDealService--toTxt={}",cimissParam.getToTxt());
		dealStationDataToDB(cimissParam,resultBean);
	}

	private void dealStationDataToDB(CimissDownParam cimissParam,ResultBean resultBean) {

		// 表名
		String tableName = "cimiss_"+cimissParam.getDataCode().toLowerCase();
		if(cimiss_surf_chn_mul_hor_n.equalsIgnoreCase(tableName)){
			tableName = cimiss_surf_chn_mul_hor;
		}
		// 列名
		String[] columns = cimissParam.getElements().split(",");
		// 查询是否存在的条件
		List<String> queryConditions = new ArrayList<String>(0);
		boolean duplicateCheck = StringUtils.isNotBlank(cimissParam.getSpecialElements());
		if(duplicateCheck){
			queryConditions = Arrays.asList(cimissParam.getSpecialElements().split(","));
		}
		// 数据
		@SuppressWarnings("unchecked")
		List<Map<String, String>> data = resultBean.getData();
		String [] fileNames = resultBean.getFieldNames();
		if(CollectionUtils.isEmpty(data)){
			logger.info("--CimissDataDealService--dealStationData--没有数据，时间为：{}", cimissParam.getTime());
			XxlJobHelper.handleResult(ReturnT.FAIL_CODE,"站点数据没有数据，时间为"+cimissParam.getTime());
		}
		// 插入sql，查重sql
		StringBuffer insertSql = new StringBuffer("insert into ");
		StringBuffer valuesSql = new StringBuffer("values(");
		StringBuffer querySql = new StringBuffer("select * from ");
		spliceDbSql(tableName, columns, queryConditions,insertSql,valuesSql,querySql);

		try (	Connection connection = dataSource.getConnection();
				ResultSet set = connection.getMetaData().getTables(null, null, tableName, null);
				PreparedStatement psInsert = connection.prepareStatement(insertSql.toString());
				PreparedStatement psQuery = connection.prepareStatement(querySql.toString());
				Statement statement = connection.createStatement();
				) {
			
			if (set.next()) {
				logger.info("此表存在{}",tableName);
				System.out.println("此表存在");
				// 插入数据： 根据期号，站名查询记录是否存在，若不存在插入
				logger.info("插入数据： 根据期号，站名查询记录是否存在，若不存在插入");
				System.out.println("插入数据： 根据期号，站名查询记录是否存在，若不存在插入");
				
			} else {
				// 创建表
				System.out.println("此表不存在，创建");
				logger.info("此表不存在，创建{}",tableName);
				StringBuffer createSql = new StringBuffer("CREATE TABLE ");
				createCimissTab(tableName, columns, statement, createSql,fileNames);
				// 插入数据：全部插入
				System.out.println("插入数据：全部插入");
			}
			fastInsertDuplicateCheck(columns,queryConditions, data, psInsert,psQuery,duplicateCheck);
			XxlJobHelper.handleSuccess("cimiss数据下载成功");
			
		} catch (Exception e2) {
			logger.error(e2.getMessage(),e2);
		}
		
	}


	private void spliceDbSql(String tableName, String[] columns,
			List<String> queryConditions,StringBuffer insertSql,StringBuffer valuesSql,StringBuffer querySql) {

		insertSql.append(tableName);
		insertSql.append(" (");
		
		for (String column : columns) {
			insertSql.append(column);
			insertSql.append(",");
			valuesSql.append("?");
			valuesSql.append(",");
		}
		insertSql = insertSql.replace(insertSql.lastIndexOf(","), insertSql.lastIndexOf(",")+1, "");
		valuesSql = valuesSql.replace(valuesSql.lastIndexOf(","), valuesSql.lastIndexOf(",")+1, "");
		insertSql.append(")");
		valuesSql.append(")");
		insertSql.append(valuesSql);

		querySql.append(tableName);
		querySql.append(" where 1=1");
		for (String query : queryConditions) {
			querySql.append(" and ");
			querySql.append(query);
			querySql.append(" = ? ");
		}
	}


	private void createCimissTab(String tableName, String[] columns,
			Statement statement,StringBuffer createSql,String [] fileNames) throws SQLException {
		logger.info("columns_size: "+ columns.length);
		logger.info(Arrays.stream(columns).toString());
		logger.info("fileNames_size: "+ fileNames.length);
		logger.info(Arrays.stream(fileNames).toString());
		createSql.append(tableName);
		createSql.append(" (");
		createSql.append("id int NOT NULL PRIMARY KEY AUTO_INCREMENT");
		for (int i =0 ;i<columns.length ;i++) {
			String column = columns[i];
			String fileName = fileNames[i];
			boolean intColFlag = "Year".equalsIgnoreCase(column)||"Mon".equalsIgnoreCase(column)||"Day".equalsIgnoreCase(column)||"Hour".equalsIgnoreCase(column);
			createSql.append(", ");
			createSql.append(column.toLowerCase());
			if (column.equalsIgnoreCase("Datetime")){
				createSql.append(" datetime(0) ");
			}else {
				createSql.append(intColFlag ? " int " : " varchar(255) ");
			}
			createSql.append(" comment '" +fileName+"' ");
		}
		createSql.append(")");
		createSql.append(" ENGINE=InnoDB DEFAULT CHARSET=utf8");
		logger.info("createSql: --->" + createSql);
		statement.execute(createSql.toString());
	}



	private void fastInsertDuplicateCheck(String[] columns,List<String> queryConditions,
			List<Map<String, String>> data, PreparedStatement psInsert,PreparedStatement psQuery,boolean duplicateCheck)
			throws SQLException {
		int k = 0;
		if(duplicateCheck){
			for (Map<String, String> map : data) {
				for (int i = 0; i < columns.length; i++) {
					String value = map.get(columns[i]);
					if (queryConditions.contains(columns[i])) {
						int index = queryConditions.indexOf(columns[i]);
						psQuery.setString(index + 1, value);
					}

					psInsert.setString(i + 1, value);
				}
				ResultSet rs = psQuery.executeQuery();
				if (!rs.next()) {
					psInsert.addBatch();
					k++;
					if ((k + 1) % 100 == 0) {
						psInsert.executeBatch();
						psInsert.clearBatch();
					}
				}
			}
		}else{
			for (Map<String, String> map : data) {
				for (int i = 0; i < columns.length; i++) {
					String value = map.get(columns[i]);
					psInsert.setString(i + 1, value);
				}
				psInsert.addBatch();
				k++;
				if ((k + 1) % 100 == 0) {
					psInsert.executeBatch();
					psInsert.clearBatch();
				}
			}
		}
		
		psInsert.executeBatch();
		psInsert.clearBatch();
	}
	

}
