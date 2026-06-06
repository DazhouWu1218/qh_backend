package com.njht.webyun.management.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.njht.webyun.management.order.entity.OrderInfoParam;
import com.njht.webyun.management.order.entity.OrderParam;
import com.njht.webyun.management.order.service.OrderDownLoadService;
import com.njht.webyun.management.order.service.OrderService;
import com.njht.webyun.management.order.vo.HthtOrderInfo;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author hg08
 */
@Api(tags = "订单")
@RestController
@RequestMapping("/orderOperate")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDownLoadService orderDownLoadService;

    /**
     * 生成订单
     * @param orderParam
     * @return
     */
    @ApiOperation(value = "生成订单")
    @PostMapping("/submitOrder")
    @ResponseBody
    public ReturnT<String> submitData(@RequestBody @Validated OrderParam orderParam, BindingResult bindingResult) {
        ReturnT returnT = new ReturnT<Object>();
        if (bindingResult.hasErrors()) {
            HashMap<String, String> errorMessages = new HashMap<>(10);
            bindingResult.getFieldErrors()
                    .forEach(fieldError -> errorMessages.put(fieldError.getField(), fieldError.getDefaultMessage()));
            returnT.setCode(HttpStatus.BAD_REQUEST.value());
            returnT.setMsg("参数错误");
            returnT.setData(errorMessages);
            return returnT;
        }

        if (orderParam.getUserId() == null || orderParam.getUserId().length() == 0
                || orderParam.getDataUse() == null || orderParam.getDataUse().length() == 0
                || orderParam.getOrderName() == null || orderParam.getOrderName().length() == 0
                || orderParam.getDatas() == null || orderParam.getDatas().size() <= 0) {
            returnT.setCode(HttpStatus.BAD_REQUEST.value());
            returnT.setMsg(HttpStatus.BAD_REQUEST.getReasonPhrase());
            return returnT;
        }
        try {
            String orderId = orderService.submitOrder(orderParam.getUserId(), orderParam.getDataUse(), orderParam.getOrderName(), orderParam.getDatas());
            returnT.setCode(ReturnT.SUCCESS_CODE);
            returnT.setData(orderId);
        } catch (Exception e) {
            returnT.setCode(0);
            returnT.setMsg("当前系统繁忙，请稍后再试");
        }
        return returnT;
    }

    /**
     * 根据用户查询订单
     *
     * @param userId
     * @param state
     * @param orderState
     * @param beginTime
     * @param endTime
     * @return
     * @throws JsonProcessingException
     */
    @ApiOperation(value = "用户订单列表")
    @PostMapping(value = "/getUserOrder")
    @ResponseBody
    public ReturnT<List<HthtOrderInfo>> queryUserOrder(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "systemUserId", required = false) String systemUserId,
            @RequestParam(value = "state", required = false) String state, @RequestParam(value = "orderState", required = false) String orderState,
            @RequestParam(value = "beginTime", required = true) String beginTime,
            @RequestParam(value = "endTime", required = true) String endTime) {
        ReturnT returnT = new ReturnT<Object>();
        Map<String,Object> map;
        try {
            map = orderService.queryUserOrder(userId,systemUserId,state, orderState, beginTime, endTime);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnT.failedMsg(e.getMessage());
        }
        returnT.setCode(ReturnT.SUCCESS_CODE);
        returnT.setData(map);
        return returnT;
    }


    /**
     * 审核订单
     *
     * @param orderId
     * @param orderState
     * @param orderReason
     * @return
     */
    @RequestMapping("/checkOrder")
    @ResponseBody
    public ReturnT<String> checkOrder(@RequestParam(value = "orderId", required = true) String orderId,
                                      @RequestParam(value = "orderState", required = true) String orderState,
                                      @RequestParam(value = "orderReason", required = true) String orderReason) {
        ReturnT returnT = new ReturnT<Object>();
        if (orderId != null && orderState != null) {
            try {
                orderService.checkOrder(orderId, orderState, orderReason);
                returnT.setCode(ReturnT.SUCCESS_CODE);
            } catch (Exception e) {
                returnT.setCode(0);

                returnT.setMsg("当前系统繁忙，请稍后再试");
            }
        } else {
            returnT.setCode(0);
            returnT.setMsg("请选择正确的用户订单");
        }
        return returnT;
    }


    /**
     * 查询订单详情
     *
     * @param orderId
     * @return
     */
    @RequestMapping("/queryOrderDetail")
    @ResponseBody
    public ReturnT<List<Map<String, Object>>> queryOrderDetail(@RequestParam(value = "orderId", required = true) String orderId) {
        ReturnT returnT = new ReturnT<Object>();
        try {
            List<Map<String, Object>> list = orderService.queryOrderDetail(orderId);
            if(list.isEmpty()){
                returnT.setCode(ReturnT.SUCCESS_CODE);
                returnT.setMsg("数据详情查询结果为空！");
            }
            returnT.setCode(ReturnT.SUCCESS_CODE);
            returnT.setMsg("查询成功！");
            returnT.setData(list);
        } catch (Exception e) {
            returnT.setCode(0);
            returnT.setMsg("当前系统繁忙，请稍后再试");
        }
        return returnT;

    }


    /**
     * 订单删除
     * @param orderIds
     * @return
     @RequestMapping("/deleteOrder")
     @ResponseBody public ReturnT<String> delOrder(@RequestParam(value = "orderIds", required = true) List<String> orderIds) {
     ReturnT returnT = new ReturnT<Object>();
     try {
     orderService.delOrderInNums(orderIds);
     returnT.setCode(ReturnT.SUCCESS_CODE);
     } catch (Exception e) {
     returnT.setCode(ReturnT.CODE_OK);
     returnT.setMsg("当前系统繁忙，请稍后再试");
     }
     return  returnT;
     }*/

    /**
     * 订单删除
     *
     * @return
     */
    @RequestMapping("/deleteOrder")
    @ResponseBody
    public ReturnT<String> delOrder(@RequestBody OrderInfoParam orderInfoParam) {
        ReturnT returnT = new ReturnT<Object>();

        try {
            orderService.delOrderInNums(orderInfoParam);
            returnT.setCode(ReturnT.SUCCESS_CODE);
        } catch (Exception e) {
            returnT.setCode(0);
            returnT.setMsg("当前系统繁忙，请稍后再试");
        }
        return returnT;
    }


    /**
     * 查询用户信息
     * @return
     */
    @RequestMapping("/selectUserName")
    @ResponseBody
    public ReturnT<Object> selectUserName() {
        ReturnT returnT = new ReturnT<Object>();
        Map<String, String> userNameList = orderService.selectUserName();
        returnT.setData(userNameList);
        returnT.setCode(ReturnT.SUCCESS_CODE);
        return returnT;
    }

    /**
     * 获取消息提醒信息
     * @param userName
     * @return
     */
    @GetMapping("/getOrderIMessage")
    @ResponseBody
    public ReturnT<Object> getOrderIMessage(@RequestParam("userName") String userName) {
        Map<String,Object> orderMessageMap = orderDownLoadService.getOrderIMessage(userName);
        return ReturnT.success(orderMessageMap);
    }

    /**
     * 修改消息状态为已读
     * @param idList
     * @return
     */
    @PostMapping("/updateOrderMessageInfo")
    @ResponseBody
    public ReturnT<Object> updateMessageInfo(@RequestParam("idList") List<String> idList) {
       String s = orderDownLoadService.updateMessageInfo(idList,1);
        return ReturnT.success(s);
    }

    /**
     * 修改消息状态为清空消息列表
     * @param idList
     * @return
     */
    @PostMapping("/deleteOrderMessageInfo")
    @ResponseBody
    public ReturnT<Object> deleteMessageInfo(@RequestParam("idList") List<String> idList) {
        String s = orderDownLoadService.updateMessageInfo(idList,2);
        return ReturnT.success(s);
    }
}