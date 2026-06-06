package com.njht.entity.dataReport;

import com.baomidou.mybatisplus.annotation.TableName;
import com.njht.entity.base.BaseEntity;
import lombok.Data;

/**
 * 数据下载信息统计表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-08-29 10:36:44
 */
@Data
@TableName("htht_cluster_schedule_data_report")
public class DataReportEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 备注
	 */
	private String bz;
	/**
	 * 期次
	 */
	private String issue;
	/**
	 * 周期
	 */
	private String cycle;
	/**
	 * 当前期次文件个数
	 */
	private Long fileNum;
	/**
	 * 期次总个数
	 */
	private Long sumNum;
	/**
	 * 文件大小
	 */
	private Long fileSize;
	/**
	 * 标识（区分下载，预处理，产品生产）
	 */
	private String identify;
	/**
	 * 状态（0 数据未到 1数据完整，2数据缺失）
	 */
	private String status;
	/**
	 * 数据id
	 */
	private String dataId;

	private String dataName;

	public Long getSumNum() {
		if (sumNum == 0L) {
			return 1L;
		} else {
			// 分母不能为0
			return sumNum;
		}
	}

	public void setSumNum(Long sumNum) {
		this.sumNum = sumNum;
	}
}
