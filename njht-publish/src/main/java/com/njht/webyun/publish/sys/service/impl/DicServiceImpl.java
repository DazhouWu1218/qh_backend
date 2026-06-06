package com.njht.webyun.publish.sys.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.webyun.constant.DbConstant;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.publish.index.vo.IndexBaseReqVo;
import com.njht.webyun.publish.sys.constant.DicConstant;
import com.njht.webyun.publish.sys.dao.DicDao;
import com.njht.webyun.publish.sys.entity.DicEntity;
import com.njht.webyun.publish.sys.service.DicService;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service("dicService")
@DS(DbConstant.MYSQL)
public class DicServiceImpl extends ServiceImpl<DicDao, DicEntity> implements DicService {

    @Autowired
    private DicService dicService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<DicEntity> page = this.page(
                new Query<DicEntity>().getPage(params),
                new QueryWrapper<DicEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public IndexBaseReqVo getTileInfo(String index) {

        //发布平台字典相关信息
        List<DicEntity> list = this.getDicList(index);
        //查询结果根据DIC_KEY 分组
        Map<String, List<DicEntity>> map = list.stream().collect(Collectors.groupingBy(DicEntity::getDicKey));
        //标题
        String title = map.get(DicConstant.PUBLISH_INDEX_TITLR).get(0).getDicValue();
        // 轮播图第一张标题
        String chartTitle = map.get(DicConstant.PUBLISH_INDEX_CHART_TITLE).get(0).getDicValue();
        // 轮播图第一张标题底部文字
        String s = map.get(DicConstant.PUBLISH_INDEX_CHART_TITLE_LIST).get(0).getDicValue();
        List<String> chartTitleList = Arrays.asList(s.split(","));
        //未登录跳转链接
        String newsLink = map.get(DicConstant.PUBLISH_INDEX_CHART_NEWS_LINK).get(0).getDicValue();
        return new IndexBaseReqVo(title,chartTitle,newsLink,chartTitleList);
    }

    /**
     * 字典信息
     * @param index
     * @return
     */
    private List<DicEntity> getDicList(String index) {
        QueryWrapper<DicEntity> qw = new QueryWrapper<>();
        qw.eq("DIC_TYPE",index);
        return this.list(qw);
    }

    @Override
    public List<CommonEntity> getFeedBackCategoryInfo() {
        List<DicEntity> dicList = this.getDicList(DicConstant.PUBLISH_FEEDBACK);
        return dicList.stream().map(dicEntity -> new CommonEntity(dicEntity.getDicKey(), dicEntity.getDicValue())).collect(Collectors.toList());
    }

    @Override
    public Map<String,String> getFeedBackCategoryMap(){
        List<DicEntity> dicList = this.getDicList(DicConstant.PUBLISH_FEEDBACK);
        Map<String,String> map = new HashMap<>(16);
        dicList.forEach(dicEntity ->
            map.put(dicEntity.getDicKey(),dicEntity.getDicValue())
        );
        return map;
    }

    @Override
    public List<CommonEntity> getIdentifyList() {
        List<CommonEntity> commonEntityList = new ArrayList<>();
        // 将枚举信息塞到对象中并返回给前端
        List<DicEntity> dicList = this.getDicList("CATEGORY_TREE");

        for (DicEntity item:dicList) {
            CommonEntity entity = new CommonEntity(item.getDicKey(),item.getDicValue());
            commonEntityList.add(entity);
        }
        return commonEntityList;
    }
}