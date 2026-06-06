package com.njht.webyun.publish.notice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.njht.webyun.common.UserUtil;
import com.njht.webyun.publish.common.util.FileUploadUtils;
import com.njht.webyun.publish.common.util.RedisService;
import com.njht.webyun.publish.notice.constant.NoticeConstant;
import com.njht.webyun.publish.notice.dao.NoticeInfoDao;
import com.njht.webyun.publish.notice.dto.NoticeInfoDto;
import com.njht.webyun.publish.notice.entity.NoticeInfoEntity;
import com.njht.webyun.publish.notice.entity.NoticeUserEntity;
import com.njht.webyun.publish.notice.service.NoticeInfoService;
import com.njht.webyun.publish.notice.service.NoticeUserService;
import com.njht.webyun.publish.notice.vo.NoticeInfoReqVo;
import com.njht.webyun.publish.notice.vo.NoticeInfoSearchVo;
import com.njht.webyun.publish.sys.service.UserService;
import com.njht.webyun.utils.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 通知公告
 * @author 代国军
 */
@Transactional(rollbackFor = Exception.class)
@Service("noticeInfoService")
@Slf4j
public class NoticeInfoServiceImpl extends ServiceImpl<NoticeInfoDao, NoticeInfoEntity> implements NoticeInfoService {

    @Value("${upload.path}")
    private String upLoadPath;

    @Value("${file.prefix}")
    private String filePreFix;

    @Autowired
    private UserService userService;

    @Autowired
    private NoticeUserService noticeUserService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public Map<String,Object> queryPage(NoticeInfoSearchVo params) {
        // 当前时间戳信息
        long timeMillis = System.currentTimeMillis();
        // 查询用户未读信息以及用户名信息
        List<String> unReadInfos = new ArrayList<>();
        if(params.getUserId() != null){
            Integer userId = params.getUserId();
            log.info("==============登录状态获取到用户id：[{}]==============",userId);
            unReadInfos = noticeUserService.getUnReadNoticeInfoByUserId(userId);
        }else{
            log.info("=====未登录状态，获取通知公告相关信息=======");
        }
        Map<String,Object> map = new HashMap<>(2);
        // 查询最新的置顶信息
        NoticeInfoDto topNotice = baseMapper.selectTopNoticeInfo(timeMillis);
        NoticeInfoReqVo topReqVo = new NoticeInfoReqVo();
        if(topNotice == null){
            // 没有指定消息，默认查询最新的消息当做指定消息
            topNotice =  baseMapper.selectNewNoticeInfo(timeMillis);
            if(topNotice == null){
                // 如果置顶消息还为空，返回null
                return new HashMap<>(1);
            }
        }
        BeanUtils.copyProperties(topNotice,topReqVo);
        topReqVo.setCreateTime(DateFormatUtils.timeMillistoDate(topNotice.getTime()));
        // 判断该条消息已读未读(包含则为未读)
        this.setTopAndRead(unReadInfos,topReqVo,params.getUserId());
        map.put("top",topReqVo);
        //查询分页公告信息
        PageUtil.setPageAndSize(params.getPage(),params.getSize(),1,10);
        List<NoticeInfoDto> records = baseMapper.selectNoticeListNoTopId(topNotice.getId(),timeMillis);
        PageUtils pageUtils = new PageUtils(new PageInfo<>(records));
        List<String> finalUnReadInfos = unReadInfos;
        List<NoticeInfoReqVo> collect = records.stream().map(noticeInfoEntity -> {
            NoticeInfoReqVo reqVo = new NoticeInfoReqVo();
            BeanUtils.copyProperties(noticeInfoEntity, reqVo);
            reqVo.setCreateTime(DateFormatUtils.timeMillistoDate(noticeInfoEntity.getTime()));
            this.setTopAndRead(finalUnReadInfos, reqVo,params.getUserId());
            return reqVo;
        }).collect(Collectors.toList());
        pageUtils.setList(collect);
        map.put("page",pageUtils);
        return map;
    }

    /**
     * 是否置顶，是否星标
     * @param unReadInfos
     * @param topReqVo
     * @return
     */
    private void setTopAndRead(List<String> unReadInfos, NoticeInfoReqVo topReqVo,Integer userId) {
        if(userId == null){
            //未登录
            topReqVo.setIsRead(1);
        }else if(!CollectionUtils.isEmpty(unReadInfos)){
            // 登录状态
            boolean contains = unReadInfos.contains(topReqVo.getId());
            if(contains){
                topReqVo.setIsRead(1);
            }else {
                topReqVo.setIsRead(0);
            }
        }else {
            // 登录状态，没有未读消息全部设置为已读
            topReqVo.setIsRead(0);
        }
        // 判断是否星标
        if(this.isStarInfo(topReqVo.getCreateTime())){
            topReqVo.setIsStar(1);
        }else {
            topReqVo.setIsStar(0);
        }
    }

    /**
     * 判断消息是否星标（一周以内是星标，一周以外不是星标）
     * @param createTime
     * @return
     */
    private boolean isStarInfo(Date createTime) {
        boolean flag = true;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH,-1);
        //一周前的时间
        long time = calendar.getTime().getTime();
        //如果公告时间大于一周前时间，超过一周不是星标
        if(time <= createTime.getTime()){
            flag = true;
        }else{
            flag = false;
        }
        return flag;

    }

    @Override
    public ReturnT saveOrUpdateNoticeInfo(String id,String title, String content, Integer isTop, Date time,
                                          MultipartFile file,String author) {
        String filePath = null;
        if(file != null && StringUtils.isNotBlank(file.getOriginalFilename())){
            //上传文件并返回文件路径
            ReturnT<String> r = FileUploadUtils.uploadFile(file, upLoadPath + File.separator + NoticeConstant.NOTICE_IMG_PREFIX);
            if(r.getCode() != ReturnT.SUCCESS_CODE){
                return r;
            }
            filePath = r.getData();
        }
        NoticeInfoEntity noticeInfoEntity = new NoticeInfoEntity();
        // 字段非空校验，区分修改以及查询
        if(StringUtils.isNotBlank(id)){
            noticeInfoEntity.setId(id);
        }
        if(StringUtils.isNotEmpty(content)){
            noticeInfoEntity.setContent(content);
        }
        if(StringUtils.isNotEmpty(content)){
            noticeInfoEntity.setTitle(title);
        }
        if(StringUtils.isNotEmpty(author)){
            noticeInfoEntity.setAuthor(author);
        }
        if(StringUtils.isNotEmpty(filePath)){
            noticeInfoEntity.setImgPath(filePath);
            noticeInfoEntity.setImgUrl(FileSearchUtils.getReplaceFilePath(filePath).replace(FileSearchUtils.getReplaceFilePath(filePreFix),""));
        }
        //默认数据不删除
        noticeInfoEntity.setDeleted(0);
        noticeInfoEntity.setIsTop(isTop==null?0:isTop);
        Integer userId = UserUtil.getCurrentUser().getUserId();
        noticeInfoEntity.setCreatedBy(userId);
        noticeInfoEntity.setLastUpdatedBy(userId);

        if(System.currentTimeMillis() >= time.getTime()){
            //（直接入库）默认发布时间为当前天
            noticeInfoEntity.setTime(String.valueOf(System.currentTimeMillis()));
            //入库 公告 以及中间表信息
            this.saveOrUpdate(noticeInfoEntity);
            this.saveNoticeUserToDb(noticeInfoEntity);
            return ReturnT.success(NoticeConstant.NOTICE_INFO_DB_MSG);
        }else{
            noticeInfoEntity.setTime(String.valueOf(time.getTime()));
            //定时任务 入库公告，不入库中间表相关信息,入库的同时往redis里添加
            this.saveOrUpdate(noticeInfoEntity);
            //  编辑公告时存在定时发布，删除以通知用户的通知信息
            noticeUserService.removeByNoticeId(noticeInfoEntity.getId());
            // （从数据库中查询所有定时任务时间戳信息，添加到redis中）。
            this.saveNoticeToRedis(noticeInfoEntity);
            return ReturnT.success(NoticeConstant.NOTICE_INFO_REDIS_MSG);
        }
    }

    @Override
    public boolean toTop(String id) {
        NoticeInfoEntity noticeInfoEntity = new NoticeInfoEntity();
        noticeInfoEntity.setId(id);
        noticeInfoEntity.setIsTop(1);
        return this.updateById(noticeInfoEntity);
    }

    @Override
    public void removeNoticeByIds(List<String> idList) {
        this.removeByIds(idList);
        noticeUserService.removeByNoticeIds(idList);
    }

    /**
     * 定时任务 用户公告中间表信息入库
     * 1.开启另一个线程
     * 2.缓存信息添加时效。缓存数据丢失之后从
     * 3.定时任务,发布公告的时候不能读redis，读可以并行，单是执行具体任务不能并行
     * 4.定时公告内容 每天执行一次
     */
    @Scheduled(cron = "0 0 0 1/1 * ?")
    @Override
    public void saveNoticeFromRedisToDb(){
        List<NoticeInfoEntity> infoEntityList = null;
        //读写锁
        RReadWriteLock lock = redissonClient.getReadWriteLock(NoticeConstant.NOTICE_READ_WRITE_LOCK);
        //添加读锁
        RLock rLock = lock.readLock();
        rLock.lock();
        try {
            //从redis 中 拿数据处理，写数据的时候不能从redis里面读(读数据可以并行)
            infoEntityList = this.getNoticeInfoFromRedis();
            if(CollectionUtils.isEmpty(infoEntityList)){
                log.info("=================没有还未发布的公告=======================");
                return;
            }
            // 执行任务不能并行
            this.executeNoticeTask();
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 执行发布公告的业务,通过redis分布式锁
     */
    @SneakyThrows
    private void executeNoticeTask() {
        RLock lock = redissonClient.getLock(NoticeConstant.NOTICE_REDIS_LOCK);
        //等待锁定获取长达 100 秒  在 10 秒后自动解锁
        boolean res = false;
        try {
            res = lock.tryLock( 100 , 10 , TimeUnit. SECONDS );
        } catch (InterruptedException e) {
            lock.unlock();
        }
        if(res){
            try{
                //判断缓存中是否还存在不存在该任务
                List<NoticeInfoEntity> infoEntityList = this.getNoticeInfoFromRedis();
                if(CollectionUtils.isEmpty(infoEntityList)){
                    // 缓存和数据库中都没有需要执行的定时任务，直接 return
                    log.info("===================定时任务已经被其他线程执行结束==============");
                    return;
                }
                //过滤出小于等于当前时间的定时任务，执行
                List<NoticeInfoEntity> collect = infoEntityList.stream()
                        .filter(noticeInfoEntity -> Long.parseLong(noticeInfoEntity.getTime()) <= System.currentTimeMillis()).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(collect)){
                    // 执行过滤出来的定时任务
                    log.info("================存在小于等于当前时间的定时任务,开始执行 =============");
                    this.executeNoticeUserTask(collect);
                    // 过滤出定时时间未到的任务重新添加缓存
                    List<NoticeInfoEntity> taskCollect = infoEntityList.stream()
                            .filter(noticeInfoEntity -> Long.parseLong(noticeInfoEntity.getTime()) > System.currentTimeMillis()).collect(Collectors.toList());
                    if(!CollectionUtils.isEmpty(taskCollect)){
                        // 把未执行的（时间还没有到）定时任务添加到缓存中
                        log.info("================把未执行的（时间还没有到）定时任务添加到缓存中 =============");
                        redisService.set(NoticeConstant.NOTICE_REDIS_INFO,objectMapper.writeValueAsString(taskCollect));
                    }else{
                        // 当前所有要执行的任务以执行完成
                        log.info("================当前所有要执行的任务以执行完成,清除缓存=============");
                        redisService.remove(NoticeConstant.NOTICE_REDIS_INFO);
                    }
                }else{
                    log.info("================ 当前没有定时发布公告任务 =============");
                }
            }finally {
                lock.unlock();
            }
        }
    }


    /**
     * 执行需要通知给用户的公告信息入库
     * @param collect
     */
    private void executeNoticeUserTask(List<NoticeInfoEntity> collect) {
        collect.forEach(this::saveNoticeUserToDb);
    }


    /**
     * 判断 缓存中存在不存在 该条数据 ，没有加缓存，存在返回。（往redis里面写数据 添加写锁，同时只能有一个地方往redis里写数据）
     * @param noticeInfoEntity
     */
    public void saveNoticeToRedis(NoticeInfoEntity noticeInfoEntity) {
        //读写锁
        RReadWriteLock lock = redissonClient.getReadWriteLock(NoticeConstant.NOTICE_READ_WRITE_LOCK);
        //添加写锁
        RLock rLock = lock.writeLock();
        rLock.lock();
        List<NoticeInfoEntity> noticeInfoEntities = this.getNoticeInfoFromRedis();
        try {
            if(noticeInfoEntity != null){
                // redis 中有数据，判断是否存在该条数据，存在返回，不存在添加
                List<NoticeInfoEntity> collect = noticeInfoEntities.stream().filter(item -> item.getId().equals(noticeInfoEntity.getId())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(collect)){
                    // redis 中不包含该条数据
                    noticeInfoEntities.add(noticeInfoEntity);
                    //加缓存
                    log.info("=============== 添加定时发布公告任务::{}==============",noticeInfoEntity.getId());
                    redisService.set(NoticeConstant.NOTICE_REDIS_INFO,objectMapper.writeValueAsString(noticeInfoEntities));
                }
            }
        } catch (Exception e){
            rLock.unlock();
        }finally {
            rLock.unlock();
        }
    }

    /**
     * 从redis中拿数据并返回
     * @return
     */
    @SneakyThrows
    public List<NoticeInfoEntity> getNoticeInfoFromRedis(){
        String s = (String)redisService.get(NoticeConstant.NOTICE_REDIS_INFO);
        List<NoticeInfoEntity> infoEntities = null;
        if(s!=null){
            infoEntities = objectMapper.readValue(s, new TypeReference<List<NoticeInfoEntity>>() {});
        }

        return Optional.ofNullable(infoEntities).orElse(new ArrayList<>());
    }


    /**
     * 从数据库中查询定时发布的通知公告信息
     * @return
     */
    @SneakyThrows
    public List<NoticeInfoEntity> getNoticeInfoFromDb(){
        //查数据库信息
        QueryWrapper<NoticeInfoEntity> qw = new QueryWrapper<>();
        qw.le("time",System.currentTimeMillis());
        return this.list(qw);
    }

    /**
     * 公告信息保存到数据库 (公告以及用户id为唯一标识)
     * @param noticeInfoEntity
     */
    public void saveNoticeUserToDb(NoticeInfoEntity noticeInfoEntity) {
        //获取中间表中用户未读信息
        List<Integer> userIdList =
                noticeUserService.selectUnReadInfoListByNoticeId(noticeInfoEntity.getId());
        //查询用户相关信息往用户通知公告中间表中新增数据
        List<NoticeUserEntity> noticeUserEntityList = userService.list().stream()
                // 过滤掉用户未读信息，防止重复入库
                .filter(userEntity -> !userIdList.contains(userEntity.getUserId()))
                .map(userEntity -> {
                    NoticeUserEntity noticeUserEntity = new NoticeUserEntity();
                    noticeUserEntity.setNoticeId(noticeInfoEntity.getId());
                    noticeUserEntity.setUserId(userEntity.getUserId());
                    return noticeUserEntity;
                }).collect(Collectors.toList());

        noticeUserService.saveBatch(noticeUserEntityList);
    }

}