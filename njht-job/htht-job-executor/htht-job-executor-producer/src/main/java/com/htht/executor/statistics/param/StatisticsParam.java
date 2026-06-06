package com.htht.executor.statistics.param;

import com.htht.job.core.util.GsonTool;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Author zhushizhen
 * @Date 2022-09-27 09:20
 **/
public class StatisticsParam {

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 时间类型
     */
    private String dateType;

    /**
     * 实时数据的天数
     */
    private String productRangeDay;

    /**
     * 历史时间
     */
    private String productRangeDate;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getProductRangeDay() {
        return productRangeDay;
    }

    public void setProductRangeDay(String productRangeDay) {
        this.productRangeDay = productRangeDay;
    }

    public String getProductRangeDate() {
        return productRangeDate;
    }

    public void setProductRangeDate(String productRangeDate) {
        if (StringUtils.isNotBlank(productRangeDate)) {
            // 将数据转成List并拿 , 拼接返回
            List<String> list = GsonTool.fromJsonList(productRangeDate, String.class);
            this.productRangeDate = String.join(",", list);
        } else {
            this.productRangeDate = productRangeDate;
        }
    }
}
