package com.yykj.mall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.yykj.mall.common.Const;
import com.yykj.mall.common.GenericBuilder;
import com.yykj.mall.common.ResponseCode;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.dao.CartMapper;
import com.yykj.mall.dao.ProductMapper;
import com.yykj.mall.entity.Cart;
import com.yykj.mall.entity.Product;
import com.yykj.mall.service.ICartService;
import com.yykj.mall.util.BigDecimalUtil;
import com.yykj.mall.util.PropertiesUtil;
import com.yykj.mall.dto.CartProductDTO;
import com.yykj.mall.dto.CartDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public ServerResponse<CartDTO> add(Integer userId, Integer productId, Integer count){
        if (productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null){
            //该商品未添加到数据库，需要添加进去
            cart = GenericBuilder.of(Cart::new)
                    .with(Cart::setUserId, userId)
                    .with(Cart::setProductId, productId)
                    .with(Cart::setChecked, Const.Cart.CHECKED)
                    .with(Cart::setQuantity, count)
                    .build();
            cartMapper.insert(cart);
        } else {
            //商品已经存在于购物车
            cart.setQuantity(cart.getQuantity() + count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return list(userId);
    }

    @Override
    @Transactional
    public ServerResponse<CartDTO> update(Integer userId, Integer productId, Integer count){
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
    @Override
    @Transactional
    public ServerResponse<CartDTO> deleteProducts(Integer userId, String productIds){
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productIdList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdAndProductIds(userId, productIdList);
        return list(userId);
    }

    @Override
    public ServerResponse<CartDTO> list(Integer userId){
        CartDTO cartDTO = getLimitedCartVo(userId);
        return ServerResponse.createBySuccess(cartDTO);
    }

    @Override
    public ServerResponse<CartDTO> checkOrUnCheck(Integer userId, Integer productId, Integer checked){
        cartMapper.updateCheckedOrUnchecked(userId, productId, checked);
        return list(userId);
    }

    @Override
    public ServerResponse<Integer> getProductCount(Integer userId){
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectProductCount(userId));
    }

    private CartDTO getLimitedCartVo(Integer userId){

        List<Cart> cartList = cartMapper.selectByUserId(userId);
        List<CartProductDTO> cartProductDTOList = Lists.newArrayList();
        BigDecimal cartTotalPrice = new BigDecimal("0");//商业计算，使用参数为String类型构造器

        if (CollectionUtils.isNotEmpty(cartList)){
            for (Cart cartItem : cartList){


                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());

                if (product != null) {
                    CartProductDTO cartProductDTO = GenericBuilder.of(CartProductDTO::new)
                            .with(CartProductDTO::setId, cartItem.getId())
                            .with(CartProductDTO::setUserId, userId)
                            .with(CartProductDTO::setProductId, cartItem.getProductId())
                            .with(CartProductDTO::setProductMainImage, product.getMainImage())
                            .with(CartProductDTO::setProductName, product.getName())
                            .with(CartProductDTO::setProductSubtitle, product.getSubtitle())
                            .with(CartProductDTO::setProductStatus, product.getStatus())
                            .with(CartProductDTO::setProductStock, product.getStock())
                            .with(CartProductDTO::setProductPrice, product.getPrice())
                            .build();
                    //判断库存是否足够
                    int buyLimitCount;
                    if (product.getStock() >= cartItem.getQuantity()){
                        //库存够
                        buyLimitCount = cartItem.getQuantity();
                        cartProductDTO.setLimitQuantity(Const.Cart.LIMIT_QUANTITY_SUCCESS);
                    } else {
                        //如果库存不够，则把库存改成商品库存
                        buyLimitCount = product.getStock();
                        cartProductDTO.setLimitQuantity(Const.Cart.LIMIT_QUANTITY_FAIL);

                        //更新数据库中购物车quantity信息
                        Cart cart = GenericBuilder.of(Cart::new)
                                .with(Cart::setId, cartItem.getId())
                                .with(Cart::setQuantity, cartItem.getQuantity())
                                .build();
                        cartMapper.updateByPrimaryKeySelective(cart);
                    }
                    cartProductDTO.setQuantity(buyLimitCount);
                    cartProductDTO.setProductTotalPrice(BigDecimalUtil.multiply(buyLimitCount, product.getPrice().doubleValue()));
                    cartProductDTO.setProductChecked(cartItem.getChecked());

                    if (cartItem.getChecked() == Const.Cart.CHECKED){
                        //已勾选，计算入总价
                        cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductDTO.getProductTotalPrice().doubleValue());
                    }
                    cartProductDTOList.add(cartProductDTO);
                }

            }
        }

        CartDTO cartDTO = GenericBuilder.of(CartDTO::new)
                .with(CartDTO::setCartProductDTOList, cartProductDTOList)
                .with(CartDTO::setCartTotalPrice, cartTotalPrice)
                .with(CartDTO::setImageHost, PropertiesUtil.getProperty("ftp.server.http.prefix"))
                .with(CartDTO::setAllChecked, getAllCheckedStatus(userId))
                .build();
        return cartDTO;
    }

    private boolean getAllCheckedStatus(Integer userId){
        if (userId == null){
            return false;
        }
        return cartMapper.selectUnCheckedByUserId(userId) == 0;
    }
}
