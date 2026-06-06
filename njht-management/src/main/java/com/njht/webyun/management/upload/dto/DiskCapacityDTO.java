package com.njht.webyun.management.upload.dto;

import com.njht.webyun.management.dataanalysis.systemdatacount.utils.FileCountUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: 代国军
 * @CreateDate: 2021/6/23 10:45
 * @Description: 磁盘容量统计
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiskCapacityDTO implements Serializable {

    private FileCountUtils.FileSizeDTO totalSpace = new FileCountUtils.FileSizeDTO("0", "0");
    private FileCountUtils.FileSizeDTO freeSpace = new FileCountUtils.FileSizeDTO("0", "0");
}
