package com.htht.job.core.util;

import com.htht.job.core.biz.model.RegistryParam;
import com.sun.management.OperatingSystemMXBean;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.concurrent.*;

/**
 * @author 代国军
 * @description: 获取系统内存，cpu等相关信息
 * @date 2022/7/27 13:32
 */
@Slf4j
public class SystemInfoUtil {

    private SystemInfoUtil() {
    }

    /**
     * 将内存以及cpu 的信息注册到调度中心
     * @param registryParam
     */
    public static void registryCpuAndMemoryInfo(RegistryParam registryParam) {
        Callable<String> task = () -> {
            OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
            // 物理内存
            long totalPhysicalMemorySize = operatingSystemMXBean.getTotalPhysicalMemorySize();
            registryParam.setTotalPhysicalMemorySize(totalPhysicalMemorySize);
            // 空闲物理内存
            long freePhysicalMemorySize = operatingSystemMXBean.getFreePhysicalMemorySize();
            registryParam.setFreePhysicalMemorySize(freePhysicalMemorySize);
            // cpu 使用率
            double systemCpuLoad = operatingSystemMXBean.getSystemCpuLoad();
            DecimalFormat df = new DecimalFormat("#0.00000");
            registryParam.setSystemCpuLoad(df.format(systemCpuLoad));
            return null;
        };

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            Future<String> future = executorService.submit(task);
            //任务处理超时时间设为 5 秒,防止因为获取不到内存信息导致执行器注册失败
            String obj = future.get(1000 * 5L, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex) {
            log.error("获取cpu 以及内存信息超时");
            ex.printStackTrace();
        } catch (Exception e) {
            log.error("获取cpu 以及内存信息失败");
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}
