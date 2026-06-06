package com.htht.job.admin.ftp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.job.admin.ftp.entity.FtpEntity;
import com.htht.job.admin.ftp.vo.FtpVo;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.ReturnT;

import java.util.List;

/**
 * 调度平台-FTP配置信息表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-06-27 17:05:24
 */
public interface FtpService extends IService<FtpEntity> {

    /**
     * 查询ftp 列表
     * @param name
     * @param page
     * @param size
     * @return
     */
    PageUtils queryPage(String name,Integer page,Integer size);

    /**
     * 测试连接
     * @param id
     * @return
     */
    ReturnT<String> testConnect(String id);

    /**
     * 新增
     * @param ftp
     */
    void insertInfo(FtpVo ftp);

    /**
     * 编辑
     * @param ftp
     */
    void edit(FtpVo ftp);

    /**
     * 键值对的形式返回数据信息
     * @return
     */
    List<CommonEntity> queryFtpList();
}

