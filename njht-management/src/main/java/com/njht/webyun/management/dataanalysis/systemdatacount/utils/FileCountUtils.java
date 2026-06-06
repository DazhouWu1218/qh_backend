package com.njht.webyun.management.dataanalysis.systemdatacount.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@Slf4j
public class FileCountUtils {

    /**
     * @param byteNumber
     * @return
     */
    public static String byteToGB(Long byteNumber) {
        final BigDecimal byteBigDecimal = new BigDecimal(byteNumber);
        final BigDecimal gbBigDecimal = byteBigDecimal.divide(new BigDecimal(1024L * 1024L * 1024L), 2, BigDecimal.ROUND_HALF_UP);
        if (gbBigDecimal.equals(new BigDecimal(0))) {
            return "0.00";
        }
        return gbBigDecimal.toString();
    }


    @Data
    @AllArgsConstructor
    public static class FileSizeDTO implements Serializable {
        private String unit = "0";
        private String size = "0";
    }

    /**
     * byte 转换为格式良好的单位
     *
     * @return
     */
    public static FileSizeDTO byteToPrettyUnit(Long byteNumber) {
//        log.info("开始将byte数值:{}转换为合理单位", byteNumber);
        long start = System.currentTimeMillis();
        FileSizeDTO fileSizeDTO;
        if (byteNumber == null || byteNumber == 0L) {
            fileSizeDTO = new FileSizeDTO("B", "0");
        }
        final DecimalFormat decimalFormat = new DecimalFormat("#.00");
        if (byteNumber < 1024L) {
            fileSizeDTO = new FileSizeDTO("B", byteNumber.toString());
        } else if (byteNumber < 1024L * 1024L) {
            final double value = (double) byteNumber / 1024L;
            fileSizeDTO = new FileSizeDTO("KB", decimalFormat.format(value));
        } else if (byteNumber < 1024L * 1024L * 1024L) {
            final double value = (double) byteNumber / (1024L * 1024L);
            fileSizeDTO = new FileSizeDTO("MB", decimalFormat.format(value));
        } else if (byteNumber < (1024L * 1024L * 1024L * 1024L)) {
            final double value = (double) byteNumber / (1024L * 1024L * 1024L);
            fileSizeDTO = new FileSizeDTO("GB", decimalFormat.format(value));
        } else {
            final double value = (double) byteNumber / (1024L * 1024L * 1024L * 1024L);
            fileSizeDTO = new FileSizeDTO("TB", decimalFormat.format(value));
        }
//        log.info("完成将byte数值:{}转换为合理单位,耗时:{}", byteNumber, System.currentTimeMillis() - start);
        return fileSizeDTO;
    }

}
