package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import com.sun.corba.se.spi.activation.Server;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by txk on 2018/6/8.
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService{

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;


    /**
     * 购物车添加商品...
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    public ServerResponse addProduct2Cart(Integer userId,Integer productId,Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByError(ResponseCode.illegal_argument.getCode(), ResponseCode.illegal_argument.getDesc());
        }
        Cart cart = cartMapper.selectByUserIdAndProductId(userId,productId);
        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + count);
            int rowCount = cartMapper.updateByPrimaryKey(cart);
            if(rowCount==0){
                return ServerResponse.createByErrorMassage("购物车添加商品失败");
            }
        } else {
            Cart cartItem = new Cart();
            cartItem.setProductId(productId);
            cartItem.setQuantity(count);
            cartItem.setUserId(userId);
            //一旦加入购物车默认是被选中状态
            cartItem.setChecked(Const.Cart.CHECKED);
            int rowCount = cartMapper.insert(cartItem);
            if(rowCount==0){
                return ServerResponse.createByErrorMassage("购物车添加商品失败");
            }
        }
        return this.list(userId);
    }

    /**
     * 购物车List列表
     * @param userId
     * @return
     */
    public ServerResponse<CartVo> list(Integer userId){
            CartVo cartVo = this.getCartVoList(userId);
            return ServerResponse.createBySuccess(cartVo);
        }

    /**
     * 购物车更新
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    public ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count){

        if(productId==null || count==null){
            return ServerResponse.createByError(ResponseCode.illegal_argument.getCode(),ResponseCode.illegal_argument.getDesc());
        }
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if(cart!=null){
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.list(userId);
    }

    /**
     * 删除购物车商品失败
     * @param userId
     * @param productIds
     * @return
     */
    public ServerResponse<CartVo> deleteProduct(Integer userId,String productIds){
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productList)){
            return ServerResponse.createByError(ResponseCode.illegal_argument.getCode(),ResponseCode.illegal_argument.getDesc());
        }
        int rowCount = cartMapper.deleteByProductListAndUserId(userId, productList);
        if(rowCount>0){
            return this.list(userId);
        }
        return ServerResponse.createByErrorMassage("购物车删除商品失败");
    }

    /**
     * 全选或全不选或单选或单独反选
     * @param userId
     * @param checked
     * @return
     */
    public ServerResponse<CartVo>  selectOrUnSelectAll(Integer userId,Integer checked,Integer productId){
        cartMapper.checkOrUncheckProduct(userId, checked,productId);
        return this.list(userId);
    }

    /**
     * 返回用户购物车中产品数量
     * @param userId
     * @return
     */
    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId==null){
            ServerResponse.createBySuccess(0);
        }
        int count = cartMapper.selectCartProductCount(userId);
        return ServerResponse.createBySuccess(count);
    }



    /**
     * 通过userid查询购物车列表
     * @param userId
     * @return
     */
    public CartVo getCartVoList(Integer userId){
            CartVo cartVo=new CartVo();
            List<Cart> cartList = cartMapper.selectByUserId(userId);
            BigDecimal cartTotalPrice=new BigDecimal("0");
            List<CartProductVo> cartProductVoList=Lists.newArrayList();
            if(CollectionUtils.isEmpty(cartList)){
                return cartVo;
            }
            for (Cart cartItem: cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(cartItem.getUserId());
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if(product!=null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    int buyLimitCount=0;
                    if(product.getStock()>=cartItem.getQuantity()){
                        //库存充足
                        buyLimitCount=cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_QUANTITY_SUCCESS);
                    }else {
                        buyLimitCount=product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_QUANTITY_FAIL);
                        //购物车中更新有效库存
                        Cart cart4Quantity = new Cart();
                        cart4Quantity.setId(cartItem.getId());
                        cart4Quantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cart4Quantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(cartProductVo.getQuantity().doubleValue(),cartProductVo.getProductPrice().doubleValue()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                //计算勾选的总价
                if(cartItem.getChecked()==Const.Cart.CHECKED){
                    cartTotalPrice=BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
        }
            cartVo.setCartProductVoList(cartProductVoList);
            cartVo.setCartTotalPrice(cartTotalPrice);
            cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
            cartVo.setAllChecked(this.getAllCheckedStatus(userId));
            return cartVo;
        }


    /**
     * 判断购物车是否全选
     * @param userId
     * @return
     */
    public Boolean getAllCheckedStatus(Integer userId){
        if(userId==null){
            return false;
        }
        return cartMapper.countAllUnChecked(userId)==0;
    }



    //全反选
    //单独选
    //单独反选
    //查询当前用户的购物车中的产品数量，如果一个产品的数量是10个，那么产品的数量为10







}
