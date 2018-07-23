package com.mmall.controller.portal;

import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ServerResponse getDetail(Integer productId){
        return iProductService.getProductDetail(productId);
    }

    @RequestMapping(value = "/{productId}" ,method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getDetailRESTful(@PathVariable Integer productId){
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


    //http://www.happymmall.com/product/100012/手机/1/10/price_asc  URI是这个，但是中间的内容不能为空，为空就会出现定位不准确
    @RequestMapping("/{categoryId}/{keyword}/{pageNum}/{pageSize}/{orderBy}")
    @ResponseBody
    public ServerResponse getListRESTful(@PathVariable(value = "categoryId") Integer categoryId,
                                         @PathVariable(value ="keyword") String keyword,
                                         @PathVariable(value ="pageNum") Integer pageNum,
                                         @PathVariable(value = "pageSize") Integer pageSize,
                                         @PathVariable(value = "orderBy") String orderBy){
        if(pageNum==null){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=10;
        }
        if(StringUtils.isBlank(orderBy)){
            orderBy="price_asc";
        }
        return iProductService.searchProductByKeywordAndCategoryId(categoryId,"",pageNum,pageSize,orderBy);
    }


    //http://www.happymmall.com/product/keyword/手机/1/10/price_asc
    @RequestMapping("/keyword/{keyword}/{pageNum}/{pageSize}/{orderBy}")
    @ResponseBody
    public ServerResponse getListRESTful(@PathVariable(value = "keyword") String keyword,
                                         @PathVariable(value ="pageNum") Integer pageNum,
                                         @PathVariable(value = "pageSize") Integer pageSize,
                                         @PathVariable(value = "orderBy") String orderBy){
        if(pageNum==null){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=10;
        }
        if(StringUtils.isBlank(orderBy)){
            orderBy="price_asc";
        }
        return iProductService.searchProductByKeywordAndCategoryId(null,keyword,pageNum,pageSize,orderBy);
    }

    //http://www.happymmall.com/product/categoryId/100012/1/10/price_asc 由于这方法的category可以不同时为空就编写一个定位的方
    @RequestMapping("/categoryId/{categoryId}/{pageNum}/{pageSize}/{orderBy}")
    @ResponseBody
    public ServerResponse getListRESTful(@PathVariable(value = "categoryId") Integer categoryId,
                                         @PathVariable(value ="pageNum") Integer pageNum,
                                         @PathVariable(value = "pageSize") Integer pageSize,
                                         @PathVariable(value = "orderBy") String orderBy){
        if(pageNum==null){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=10;
        }
        if(StringUtils.isBlank(orderBy)){
            orderBy="price_asc";
        }
        return iProductService.searchProductByKeywordAndCategoryId(categoryId,"",pageNum,pageSize,orderBy);
    }








}
