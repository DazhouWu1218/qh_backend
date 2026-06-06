package com.njht.entity.dataReport;

import com.baomidou.mybatisplus.annotation.TableName;
import com.njht.entity.base.BaseEntity;
import lombok.Data;

/**
 * 数据监控基础信息
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-08-29 10:36:44
 */
@Data
@TableName("htht_cluster_schedule_data_base_info")
public class DataBaseInfoEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String name;
	/**
	 * 周期
	 */
	private String cycle;
	/**
	 * 文件路径
	 */
	private String filePath;
	/**
	 * 文件正则
	 */
	private String fileRegex;
	/**
	 * 每一期文件总个数
	 */
	private Long sumNum;
	/**
	 * 标识（区分下载，预处理，产品生产）
	 */
	private String identify;

	/**
	 * 校正时间（以北京时为基准），世界时-8
	 */
	private int correctionTime;

	/**
	 * 时间范围(默认为空，周期为小时或者分钟时使用)
	 */
	private String timeRange;

}
