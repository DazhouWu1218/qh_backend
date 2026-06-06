package com.htht.job.core.biz.model;

import java.io.Serializable;

/**
 * Created by piesat on 2017-05-10 20:22:42
 * @author HTHT
 */
public class RegistryParam implements Serializable {
    private static final long serialVersionUID = 42L;

    private String registryGroup;
    private String registryKey;
    private String registryValue;

    /**
     * 物理内存
     */
    private Long totalPhysicalMemorySize;
    /**
     * 空闲内存
     */
    private Long freePhysicalMemorySize;

    /**
     * 当前系统cpu 使用率
     */
    private String systemCpuLoad;

    public RegistryParam(){}
    public RegistryParam(String registryGroup, String registryKey, String registryValue) {
        this.registryGroup = registryGroup;
        this.registryKey = registryKey;
        this.registryValue = registryValue;
    }

    public RegistryParam(String registryGroup, String registryKey, String registryValue, Long totalPhysicalMemorySize, Long freePhysicalMemorySize, String systemCpuLoad) {
        this.registryGroup = registryGroup;
        this.registryKey = registryKey;
        this.registryValue = registryValue;
        this.totalPhysicalMemorySize = totalPhysicalMemorySize;
        this.freePhysicalMemorySize = freePhysicalMemorySize;
        this.systemCpuLoad = systemCpuLoad;
    }

    public String getRegistryGroup() {
        return registryGroup;
    }

    public void setRegistryGroup(String registryGroup) {
        this.registryGroup = registryGroup;
    }

    public String getRegistryKey() {
        return registryKey;
    }

    public void setRegistryKey(String registryKey) {
        this.registryKey = registryKey;
    }

    public String getRegistryValue() {
        return registryValue;
    }

    public void setRegistryValue(String registryValue) {
        this.registryValue = registryValue;
    }

    public Long getTotalPhysicalMemorySize() {
        return totalPhysicalMemorySize;
    }

    public void setTotalPhysicalMemorySize(Long totalPhysicalMemorySize) {
        this.totalPhysicalMemorySize = totalPhysicalMemorySize;
    }

    public Long getFreePhysicalMemorySize() {
        return freePhysicalMemorySize;
    }

    public void setFreePhysicalMemorySize(Long freePhysicalMemorySize) {
        this.freePhysicalMemorySize = freePhysicalMemorySize;
    }

    public String getSystemCpuLoad() {
        return systemCpuLoad;
    }

    public void setSystemCpuLoad(String systemCpuLoad) {
        this.systemCpuLoad = systemCpuLoad;
    }

    @Override
    public String toString() {
        return "RegistryParam{" +
                "registryGroup='" + registryGroup + '\'' +
                ", registryKey='" + registryKey + '\'' +
                ", registryValue='" + registryValue + '\'' +
                ", totalPhysicalMemorySize=" + totalPhysicalMemorySize +
                ", freePhysicalMemorySize=" + freePhysicalMemorySize +
                ", systemCpuLoad='" + systemCpuLoad + '\'' +
                '}';
    }
}
