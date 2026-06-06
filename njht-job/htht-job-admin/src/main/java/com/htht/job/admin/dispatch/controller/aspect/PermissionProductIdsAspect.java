package com.htht.job.admin.dispatch.controller.aspect;

import com.htht.job.admin.common.ThreadLocalParams;
import com.htht.job.admin.dispatch.controller.annotation.PermissionProductIds;
import com.htht.job.admin.feign.DataCenterFeignService;
import com.htht.job.core.exception.CommonException;
import com.njht.entity.base.BaseEntity;
import com.njht.entity.category.DataCategoryEntity;
import com.njht.webyun.security.auth.AuthLogic;
import com.njht.webyun.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class PermissionProductIdsAspect {

    @Autowired
    private DataCenterFeignService dataCenterFeignService;

    public static AuthLogic authLogic = new AuthLogic();


    public PermissionProductIdsAspect(){

    }

    public static final String POINTCUT_SIGN = "@annotation(com.htht.job.admin.dispatch.controller.annotation.PermissionProductIds)";

    /**
     * 声明AOP签名
     */
    @Pointcut(POINTCUT_SIGN)
    public void pointcut() {
    }

    /**
     * 环绕切入
     *
     * @param joinPoint 切面对象
     * @return 底层方法执行后的返回值
     * @throws Throwable 底层方法抛出的异常
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable
    {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取注解参数值
        PermissionProductIds permissionProductIds = methodSignature.getMethod().getAnnotation(PermissionProductIds.class);
        String spELString = permissionProductIds.value();
        // 通过joinPoint获取被注解方法
        String value = generateKeyBySpEL(spELString, joinPoint);
        List<String> productIds = new ArrayList<>();
        List<String> tempIds = new ArrayList<>();
        if (value != null) {
            tempIds = Arrays.asList(value);
        }
        List<DataCategoryEntity> dbCategoryList = dataCenterFeignService.categoryList(null, null).getData();
        List<String> categoryIdList = getCategoryIdListByRole(dbCategoryList);
        iteratorProductIds(productIds,tempIds,categoryIdList,dbCategoryList);
        ThreadLocalParams.productIds.set(productIds);
        try {
            // 执行原有逻辑
            Object obj = joinPoint.proceed();
            ThreadLocalParams.productIds.remove();
            return obj;
        }
        catch (Throwable e) {
            throw new CommonException(e.getMessage());
        }
    }

    private String generateKeyBySpEL(String spELString, ProceedingJoinPoint joinPoint) {
        if(StringUtils.isBlank(spELString)){
            return null;
        }
        // 通过joinPoint获取被注解方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //创建解析器
        SpelExpressionParser parser = new SpelExpressionParser();
        //获取表达式
        Expression expression = parser.parseExpression(spELString);
        //设置解析上下文(有哪些占位符，以及每种占位符的值)
        EvaluationContext context = new StandardEvaluationContext();
        //获取参数值
        Object[] args = joinPoint.getArgs();
        //获取运行时参数的名称
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
            context.setVariable(parameterNames[i],args[i]);
        }
        //解析,获取替换后的结果
        String result = expression.getValue(context).toString();
        log.info(result);
        return result;
    }

    /**
     * 过滤角色产品目录节点
     * @return
     */
    public List<String> getCategoryIdListByRole( List<DataCategoryEntity> dbCategoryList) {
        return Optional.ofNullable(dbCategoryList).orElse(new ArrayList<>())
                .stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
    }

    /**
     * 目录节点递归方法
     * @param productIds
     * @param tempIds
     * @return
     */
    public List<String> iteratorProductIds(List<String> productIds,List<String> tempIds, List<String> categoryIdList,List<DataCategoryEntity> dbCategoryList){
        if (!tempIds.isEmpty()) {
            productIds.addAll(tempIds);
            for (String parentId: tempIds) {
                List<DataCategoryEntity> collect = dbCategoryList.stream().filter(item -> parentId.equals(item.getParentId())).collect(Collectors.toList());
                tempIds = collect
                        .stream()
                        .filter(s->categoryIdList.contains(s.getId()))
                        .map(BaseEntity::getId).collect(Collectors.toList());
                iteratorProductIds(productIds,tempIds,categoryIdList,dbCategoryList);
            }
        }
        return tempIds;
    }



}
