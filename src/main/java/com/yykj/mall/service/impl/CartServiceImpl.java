package com.yykj.mall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.yykj.mall.common.Const;
import com.yykj.mall.common.ResponseCode;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.dao.CartMapper;
import com.yykj.mall.dao.ProductMapper;
import com.yykj.mall.pojo.Cart;
import com.yykj.mall.pojo.Product;
import com.yykj.mall.service.ICartService;
import com.yykj.mall.util.BigDecimalUtil;
import com.yykj.mall.util.PropertiesUtil;
import com.yykj.mall.vo.CartProductVo;
import com.yykj.mall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Lee on 2017/8/21.
 */
@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count){
        if (productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null){
            //该商品未添加到数据库，需要添加进去
            cart = new Cart();
            cart.setQuantity(count);
            cart.setChecked(Const.Cart.CHECKED);
            cart.setUserId(userId);
            cartMapper.insert(cart);
        } else {
            //商品已经存在于购物车
            cart.setQuantity(cart.getQuantity() + count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return list(userId);
    }

    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count){
        if (count == null || productId == null || count == 0){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart != null){
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
            return list(userId);
        } else {
            return ServerResponse.createByErrorMessage("购物车中次商品不存在");
        }
    }

    //允许批量删除商品
    public ServerResponse<CartVo> deleteProducts(Integer userId, String productIds){
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productIdList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdAndProductIds(userId, productIdList);
        return list(userId);
    }

    public ServerResponse<CartVo> list(Integer userId){
        CartVo cartVo = getLimitedCartVo(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    public ServerResponse<CartVo> checkOrUnCheck(Integer userId, Integer productId, Integer checked){
        cartMapper.updateCheckedOrUnchecked(userId, productId, checked);
        return list(userId);
    }

    public ServerResponse<Integer> getProductCount(Integer userId){
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectProductCount(userId));
    }

    private CartVo getLimitedCartVo(Integer userId){
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        BigDecimal cartTotalPrice = new BigDecimal("0");//商业计算，使用参数为String类型构造器

        if (CollectionUtils.isNotEmpty(cartList)){
            for (Cart cartItem : cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());

                if (product != null) {
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductStock(product.getStock());

                    //判断库存是否足够
                    int buyLimitCount;
                    if (product.getStock() >= cartItem.getQuantity()){
                        //库存够
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_QUANTITY_SUCCESS);
                    } else {
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_QUANTITY_FAIL);

                        //更新数据库中购物车quantity信息
                        Cart cart = new Cart();
                        cart.setId(cartItem.getId());
                        cart.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cart);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.multiply(buyLimitCount, product.getPrice().doubleValue()));
                    cartProductVo.setProductChecked(cartItem.getChecked());

                    if (cartItem.getChecked() == Const.Cart.CHECKED){
                        //已勾选，计算入总价
                        cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
                    }
                    cartProductVoList.add(cartProductVo);
                }

            }
        }
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        cartVo.setAllChecked(getAllCheckedStatus(userId));
        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId){
        if (userId == null){
            return false;
        }
        return cartMapper.selectUnCheckedByUserId(userId) == 0;
    }
}
