package com.njht.webyun.system.controller;



import com.njht.webyun.common.PageRespBean;
import com.njht.webyun.model.CurrentUser;
import com.njht.webyun.utils.MapUtil;
import com.njht.webyun.utils.PageUtil;
import com.njht.webyun.system.constant.LoginKeys;
import com.njht.webyun.system.constant.CommonKey;
import com.njht.webyun.system.model.base.BasePageModel;
import com.njht.webyun.system.model.log.BehaviorLogModel;
import com.njht.webyun.system.model.log.LoginLogModel;
import com.njht.webyun.system.model.log.LogQuery;
import com.njht.webyun.system.service.inf.LogService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(tags = "日志管理",value= "logmanage")
@RestController
@RequestMapping(value="/system/logs/")
public class LogController {

    @Autowired
    protected LogService logService;
    @Autowired
    protected SessionRegistry sessionRegistry;

    private final static Logger logger = LoggerFactory.getLogger(LogController.class);


    @PostMapping("/loginshow")
    @ApiOperation("查询登录日志列表")
    public Page<LoginLogModel> getLoginLogs(@RequestBody LogQuery logQuery) throws Exception{
        return logService.showLoginLogsByPage(logQuery);
    }


    @PostMapping("/behaviorshow")
    @ApiOperation("查询用户行为日志列表")
    public Page<LoginLogModel> getBehaviorLogs(@RequestBody LogQuery logQuery) throws Exception{
        return logService.showBehaviorLogsByPage(logQuery);
    }

    @PostMapping("/onlineshow")
    @ApiOperation("查询在线用户列表")
    public PageRespBean getOnlineUsers(@RequestBody BasePageModel basePageModel) {

        Object obj = sessionRegistry.getAllPrincipals();
        List<CurrentUser> list = (List<CurrentUser>)obj;
        list.sort((a, b) -> a.getLastLoginDate().compareTo(b.getLastLoginDate()));

        sessionRegistry.getAllPrincipals();
        List listByPage = null;
        try {
            listByPage = PageUtil.startPage(list, basePageModel.getPage(), basePageModel.getRows());
        } catch (Exception e) {
            logger.error("查询在线用户列表异常：{}",e);
            return PageRespBean.error(e.getMessage());
        }
        return PageRespBean.ok("OK", listByPage, (long) list.size());

    }

    /**
     * 新增用户行为日志
     * @param behaviorLoginLogModel
     * @return 新增项的主键值
     */

    @PostMapping("/behavioradd")
    @ApiOperation("新增用户行为日志")
    public PageRespBean newBehavior(@RequestBody BehaviorLogModel behaviorLoginLogModel) {

        Integer pk = null;
        try {
            pk = logService.createBehaviorLog(behaviorLoginLogModel.getCode(), behaviorLoginLogModel.getArgs());
            if (pk == null) {
                logger.error("生成日志失败");
//                return PageRespBean.error("查询用户失败");
                return PageRespBean.error(MapUtil.get(CommonKey.QUERY)+MapUtil.get(LoginKeys.LOG)+MapUtil.get(CommonKey.FAIL));
            }
        } catch (Exception e) {
            logger.error("查询日志失败：{}",e);
//            return PageRespBean.error("查询日志失败，数据库处理异常");
            return PageRespBean.error(MapUtil.get(CommonKey.QUERY)+MapUtil.get(LoginKeys.LOG)+MapUtil.get(CommonKey.FAIL)+","+MapUtil.get(CommonKey.DATABASE_ERROR));
        }
        logger.info(">>>>>>查询日志完成");
        return PageRespBean.ok("OK", pk, (long) 1);
    }

 /*   @PostMapping(value = "/heartbeat")
    @ApiOperation("心跳包")
    public void heartbeat(HttpServletRequest request) {
        HttpSession session = request.getSession();
        logger.debug("【收到心跳】："+session.getId());

    }*/

    @PostMapping(value = "/createBehaviorLog")
    public void createBehaviorLog(@RequestParam String action,@RequestParam String args, @RequestParam boolean dataChanged,@RequestParam int menuId) {

        logService.createBehaviorLog(action,args,dataChanged,menuId);
    }
}
