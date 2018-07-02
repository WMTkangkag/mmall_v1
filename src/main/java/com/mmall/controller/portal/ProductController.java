package com.mmall.controller.portal;

import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by txk on 2018/6/6.
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(int productId){
        return iProductService.getProductDetail(productId);
    }


    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(@RequestParam(value = "categoryId",required =false) Integer categoryId,
                                  //required=false的缺省参数都会赋值为null，而我的那个缺省参数是基础数据类型(int, long,double,char,byte,short)，没法赋值，就会导致找不到链接的问题
                                  @RequestParam(value = "keyword" ,required=false) String keyword,
                                  @RequestParam(value ="pageNum",defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize",defaultValue ="10") int pageSize,
                                  @RequestParam(value = "orderBy",defaultValue ="") String orderBy){
        return iProductService.searchProductByKeywordAndCategoryId(categoryId,keyword,pageNum,pageSize,orderBy);


    }



}
