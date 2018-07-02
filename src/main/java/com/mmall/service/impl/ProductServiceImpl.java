package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductListVo;
import com.mmall.vo.ProductVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by txk on 2018/6/4.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ICategoryService iCategoryService;
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 更新或添加产品
     */
    public ServerResponse saveOrUpdateProduct(Product product){
        if(product==null){
            return ServerResponse.createByError(ResponseCode.illegal_argument.getCode(),ResponseCode.illegal_argument.getDesc());
        }
        if(StringUtils.isNotBlank(product.getSubImages())){
            String[] subimages=product.getSubImages().split(",");
            product.setMainImage(subimages[0]);
        }
        if(product.getId()!=null){
            int rowCount = productMapper.updateByPrimaryKey(product);
            if(rowCount>0){
                return ServerResponse.createBySuccessMassage("更新产品成功");
            }
            return ServerResponse.createByErrorMassage("更新产品失败");
        }else {
            int rowCount = productMapper.insert(product);
            if(rowCount>0){
                return ServerResponse.createBySuccessMassage("新增产品成功");
            }
            return ServerResponse.createByErrorMassage("新增产品失败");
        }
    }

    /**
     * 产品上下架
     */
    public ServerResponse setSaleStatus(Integer productId,Integer status){

        if(productId==null || status==null){
            return ServerResponse.createByError(ResponseCode.illegal_argument.getCode(),ResponseCode.illegal_argument.getDesc());
        }

        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount>0){
            return ServerResponse.createBySuccessMassage("修改产品状态成功");
        }
        return ServerResponse.createByErrorMassage("修改产品状态失败");
    }


    /**
     * 后台获取商品详情信息
     * @param productId
     * @return
     */
    public ServerResponse manageGetProductDetail(Integer productId){

        if(productId==null){
            return ServerResponse.createByError(ResponseCode.illegal_argument.getCode(),ResponseCode.illegal_argument.getDesc());
        }
        Product product= productMapper.selectByPrimaryKey(productId);
        ProductVo productVo = assembleProductVo(product);
        return ServerResponse.createBySuccess(productVo);

    }


    /**
     * 组装ProductVo
     * @param product
     * @return
     */
    public ProductVo assembleProductVo(Product product){
        ProductVo productVo = new ProductVo();
        productVo.setId(product.getId());
        productVo.setCategoryId(product.getId());
        productVo.setName(product.getName());
        productVo.setSubtitle(product.getSubtitle());
        productVo.setMainImage(product.getMainImage());
        productVo.setSubImages(product.getSubImages());
        productVo.setDetail(product.getDetail());
        productVo.setPrice(product.getPrice());
        productVo.setStock(product.getStock());
        productVo.setStatus(product.getStatus());

        //图片服务器地址
        String imageHost = PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/");
        productVo.setImageHost(imageHost);
        //父类产品的父类节点
        Product parentProduct = productMapper.selectByPrimaryKey(product.getCategoryId());
        if(parentProduct==null){
            productVo.setParentCategoryId(0);
        }else {
            productVo.setParentCategoryId(parentProduct.getCategoryId());
        }
        //设置更新时间和创建时间格式
        productVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return  productVo;
    }


    /**
     * 分页显示商品
     * @param pageNum
     * @param pageSize
     * @return
     */
    public  ServerResponse getProductList(Integer pageNum,Integer pageSize){

        if(pageNum==null || pageSize==null){
            return ServerResponse.createByError(ResponseCode.illegal_argument.getCode(),ResponseCode.illegal_argument.getDesc());
        }
        //startPage--start
        PageHelper.startPage(pageNum,pageSize);
        //填充自己的sql逻辑查询list
        List<Product> productList = productMapper.getProductList();
        List<ProductListVo> productListVoList=new ArrayList<ProductListVo>();
        for (Product productItem:productList) {

            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult=new PageInfo(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    /**
     * 组装vo类
     * @param productItem
     * @return
     */
    public ProductListVo assembleProductListVo(Product productItem){

        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(productItem.getId());
        productListVo.setName(productItem.getName());
        productListVo.setMainImage(productItem.getMainImage());
        productListVo.setSubtitle(productItem.getSubtitle());
        productListVo.setStatus(productItem.getStatus());
        productListVo.setCategoryId(productItem.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setPrice(productItem.getPrice());
        return productListVo;
    }

    public ServerResponse searchProductList(String  productName,Integer productId,Integer pageNum,Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        //重新组装productName方便查询
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        //填充自己的sql逻辑查询list
        List<Product> productList = productMapper.searchProductList(productName, productId);
        List<ProductListVo> productListVoList = new ArrayList<ProductListVo>();
        for (Product productItem : productList) {

            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }


    /**
     * 门户端获取商品详情信息
     * @param productId
     * @return
     */
    public ServerResponse getProductDetail(Integer productId){

        if(productId==null){
            return ServerResponse.createByError(ResponseCode.illegal_argument.getCode(),ResponseCode.illegal_argument.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product!=null){
            //判断产品是否上线
            if(product.getStatus()==Const.ProductStatus.ONSALE.getStatus()){
                ProductVo productVo=assembleProductVo(product);
                return ServerResponse.createBySuccess(productVo);
            }
            return ServerResponse.createByErrorMassage("该商品已下架或删除");
        }
        return ServerResponse.createByErrorMassage("不存在该商品");

    }

    public ServerResponse searchProductByKeywordAndCategoryId(Integer categoryId,String keyword, int pageNum, int pageSize, String orderBy){

        PageHelper.startPage(pageNum, pageSize);
        if(categoryId==null && StringUtils.isBlank(keyword)){
            return ServerResponse.createByError(ResponseCode.illegal_argument.getCode(),ResponseCode.illegal_argument.getDesc());
        }
        List<Integer> categoryIdList= new ArrayList<>();
        if(categoryId!=null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category==null && StringUtils.isBlank(keyword) ){
                //没有该分类，并且没有关键字，这时候返回一个空的结果集，不报错
                List<ProductVo> productListVo=Lists.newArrayList();
                PageInfo pageInfo=new PageInfo(productListVo);
                return  ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList= iCategoryService.getCategoryAndDeepChlidrenById(category.getId()).getData();
        }
        if(StringUtils.isNotBlank(keyword)){
            //拼接条件方便查询
            keyword=new StringBuffer().append("%").append(keyword).append("%").toString();
        }
        if(StringUtils.isNotBlank(orderBy)){
            String[] orderByArray=orderBy.split("_");
            String price=orderByArray[0];
            String order=orderByArray[1];
            PageHelper.orderBy(price+" "+order);
        }
        List<Product> productList = productMapper.selectBykeywordAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);
        List<ProductListVo> productListVoList=Lists.newArrayList();
        for (Product productItem:productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo=new PageInfo(productListVoList);
        return  ServerResponse.createBySuccess(pageInfo);

    }




}
