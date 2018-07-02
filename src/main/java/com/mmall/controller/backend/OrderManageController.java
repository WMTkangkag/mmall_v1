package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by txk on 2018/6/27.
 */
@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User currentUser= (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请重新登陆");
        }
        if(iUserService.checkAdminRole(currentUser).isSuccess()){
            //若是管理员才执行添加操作
            return iOrderService.manageList(pageNum,pageSize);
        }else {
            return  ServerResponse.createByErrorMassage("不是管理员，没有权限");
        }
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(HttpSession session, Long orderNo){
        User currentUser= (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请重新登陆");
        }
        if(iUserService.checkAdminRole(currentUser).isSuccess()){
            //若是管理员才执行添加操作
            return iOrderService.manageDetail(orderNo);
        }else {
            return  ServerResponse.createByErrorMassage("不是管理员，没有权限");
        }
    }

    /**
     * 后台查询功能
     * @param session
     * @param orderNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(HttpSession session, Long orderNo, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User currentUser= (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请重新登陆");
        }
        if(iUserService.checkAdminRole(currentUser).isSuccess()){
            //若是管理员才执行添加操作
            return iOrderService.manageSearch(orderNo, pageNum, pageSize);
        }else {
            return  ServerResponse.createByErrorMassage("不是管理员，没有权限");
        }
    }

    /**
     * 后台发货管理
     * @param session
     * @param orderNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<String> orderSendGoods(HttpSession session, Long orderNo, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User currentUser= (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请重新登陆");
        }
        if(iUserService.checkAdminRole(currentUser).isSuccess()){
            //若是管理员才执行添加操作
            return iOrderService.manageSendGoods(orderNo);
        }else {
            return  ServerResponse.createByErrorMassage("不是管理员，没有权限");
        }
    }

}