package com.htht.job.admin.ftp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.job.admin.ftp.FtpConstant;
import com.htht.job.admin.ftp.dao.FtpDao;
import com.htht.job.admin.ftp.entity.FtpEntity;
import com.htht.job.admin.ftp.service.FtpService;
import com.htht.job.admin.ftp.util.ApacheFtpUtil;
import com.htht.job.admin.ftp.vo.FtpReqVo;
import com.htht.job.admin.ftp.vo.FtpVo;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.entity.PageEntity;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.Query;
import com.njht.webyun.utils.ReturnT;
import com.njht.webyun.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 调度平台-FTP配置信息表
 * @author daiguojun
 * @date 2022-06-27 17:05:24
 */
@Service("ftpService")
public class FtpServiceImpl extends ServiceImpl<FtpDao, FtpEntity> implements FtpService {

    @Override
    public PageUtils queryPage(String name,Integer page,Integer size) {
        PageEntity pageEntity = new PageEntity(page,size);
        LambdaQueryWrapper<FtpEntity> qw = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            qw.like(FtpEntity::getName,name).or().like(FtpEntity::getIpAddr,name).or().like(FtpEntity::getUserName,name);
        }
        // 分页查询
        IPage<FtpEntity> iPage = this.page(
                new Query<FtpEntity>().getPage(pageEntity),
                qw
        );
        PageUtils pageUtils = new PageUtils(iPage);
        // 设置返回结果，去除密码，日期等没用的信息
        List<FtpReqVo> collect = Optional.ofNullable(iPage.getRecords()).orElse(new ArrayList<>())
                .stream()
                .map(ftpEntity -> {
                    FtpReqVo reqVo = new FtpReqVo();
                    BeanUtils.copyProperties(ftpEntity, reqVo);
                    return reqVo;
                }).collect(Collectors.toList());
        pageUtils.setList(collect);
        return pageUtils;
    }

    @Override
    public ReturnT<String> testConnect(String id) {
        FtpEntity ftpEntity = this.getById(id);
        if (ftpEntity == null) {
            return ReturnT.failedMsg(FtpConstant.FAIL_MSG);
        }
        ApacheFtpUtil af = new ApacheFtpUtil(ftpEntity);
        boolean b = af.connectServer();
        if (b) {
            // 成功登录后关闭连接
            af.closeServer();
        }
        return b?ReturnT.success(FtpConstant.SUCCESS_MSG):ReturnT.failedMsg(FtpConstant.FAIL_MSG);
    }

    @Override
    public void insertInfo(FtpVo ftp) {
        FtpEntity ftpEntity = new FtpEntity();
        BeanUtils.copyProperties(ftp,ftpEntity);
        this.save(ftpEntity);
    }

    @Override
    public void edit(FtpVo ftp) {
        FtpEntity ftpEntity = new FtpEntity();
        BeanUtils.copyProperties(ftp,ftpEntity);
        this.updateById(ftpEntity);
    }

    @Override
    public List<CommonEntity> queryFtpList() {
        List<FtpEntity> list = this.list();
        return Optional.ofNullable(list).orElse(new ArrayList<>())
                .stream()
                .map(ftpEntity -> new CommonEntity(ftpEntity.getId(),ftpEntity.getName()))
                .collect(Collectors.toList());
    }

}