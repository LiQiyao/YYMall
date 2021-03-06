package com.yykj.mall.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yykj.mall.common.Const;
import com.yykj.mall.common.GenericBuilder;
import com.yykj.mall.common.ResponseCode;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.dao.*;
import com.yykj.mall.entity.*;
import com.yykj.mall.service.IOrderService;
import com.yykj.mall.util.BigDecimalUtil;
import com.yykj.mall.util.DateTimeUtil;
import com.yykj.mall.util.FTPUtil;
import com.yykj.mall.util.PropertiesUtil;
import com.yykj.mall.dto.OrderDTO;
import com.yykj.mall.dto.OrderItemDTO;
import com.yykj.mall.dto.OrderProductDTO;
import com.yykj.mall.dto.ShippingDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Lee on 2017/8/26.
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ShippingMapper shippingMapper;

    private static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    // 支付宝当面付2.0服务
    private static AlipayTradeService tradeService;

    static {
        /**
         * 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的alipay.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("alipay.properties");

        /**
         * 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

    }

    @Override
    public ServerResponse getCartCheckedProduct(Integer userId){

        //从购物车中获取数据

        List<Cart> cartList = cartMapper.selectCheckedByUserId(userId);
        ServerResponse serverResponse =  this.getCartOrderItem(cartList);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        List<OrderItem> orderItemList =( List<OrderItem> ) serverResponse.getData();

        List<OrderItemDTO> orderItemDTOList = Lists.newArrayList();

        BigDecimal payment = new BigDecimal("0");
        for(OrderItem orderItem : orderItemList){
            payment = BigDecimalUtil.add(payment.doubleValue(),orderItem.getTotalPrice().doubleValue());
            orderItemDTOList.add(assembleOrderItemDTO(orderItem));
        }
        OrderProductDTO orderProductDTO = GenericBuilder.of(OrderProductDTO::new)
                .with(OrderProductDTO::setProductTotalPrice, payment)
                .with(OrderProductDTO::setOrderItemDTOList, orderItemDTOList)
                .with(OrderProductDTO::setImageHost, PropertiesUtil.getProperty("ftp.server.http.prefix"))
                .build();
        return ServerResponse.createBySuccess(orderProductDTO);
    }

    /**
     * 直接点击购买后，显示预订单页中的商品详情
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    @Override
    public ServerResponse<OrderProductDTO> getSelectedProduct(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        //校验商品和库存
        Product product = productMapper.selectByPrimaryKey(productId);
        if (Const.ProductStatus.ON_SALE.getCode() != product.getStatus()){
            return ServerResponse.createByErrorMessage("商品" + product.getName() + "已下架！");
        }

        //校验库存
        if (count > product.getStock()){
            return ServerResponse.createByErrorMessage("商品" + product.getName() + "库存不足！");
        }

        OrderItem orderItem = GenericBuilder.of(OrderItem::new)
                .with(OrderItem::setUserId, userId)
                .with(OrderItem::setProductId, product.getId())
                .with(OrderItem::setProductName, product.getName())
                .with(OrderItem::setProductImage, product.getMainImage())
                .with(OrderItem::setCurrentUnitPrice, product.getPrice())
                .with(OrderItem::setQuantity, count)
                .with(OrderItem::setTotalPrice, BigDecimalUtil.multiply(product.getPrice().doubleValue(), count))
                .build();
        OrderItemDTO orderItemDTO = assembleOrderItemDTO(orderItem);
        List<OrderItemDTO> orderItemDTOList = Lists.newArrayList();
        orderItemDTOList.add(orderItemDTO);
        OrderProductDTO orderProductDTO = GenericBuilder.of(OrderProductDTO::new)
                .with(OrderProductDTO::setProductTotalPrice, orderItem.getTotalPrice())
                .with(OrderProductDTO::setOrderItemDTOList, orderItemDTOList)
                .with(OrderProductDTO::setImageHost, PropertiesUtil.getProperty("ftp.server.http.prefix"))
                .build();
        return ServerResponse.createBySuccess(orderProductDTO);
    }

    @Override
    @Transactional
    public ServerResponse cancel(Integer userId, Long orderNo){
        Order order = orderMapper.selectByOrderNoAndUserId(orderNo, userId);
        if (order == null){
            return ServerResponse.createByErrorMessage("您没有此订单！");
        }
        if (order.getStatus() != Const.OrderStatusEnum.UNPAID.getCode()){
            return ServerResponse.createByErrorMessage("订单无法取消！");
        }
        Order updateOrder = new Order();
        updateOrder.setUserId(userId);
        updateOrder.setStatus(Const.OrderStatusEnum.CANCELED.getCode());
        int res = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if (res > 0){
            return ServerResponse.createBySuccessMessage("取消订单成功！");
        }
        return ServerResponse.createByErrorMessage("取消订单失败！");
    }

    @Override
    @Transactional
    public ServerResponse createByCart(Integer userId, Integer shippingId){

        List<Cart> cartList = cartMapper.selectCheckedByUserId(userId);

        ServerResponse<List<OrderItem>> serverResponse = this.getCartOrderItem(cartList);
        if (!serverResponse.isSuccess()){
            return serverResponse;
        }
        List<OrderItem> orderItemList = serverResponse.getData();
        BigDecimal payment = getOrderTotalPrice(orderItemList);

        Order order = this.assembleOrder(userId,shippingId,payment);
        if (order == null){
            return ServerResponse.createByErrorMessage("生成订单失败！");
        }
        long orderNo = order.getOrderNo();
        for (OrderItem orderItem : orderItemList){
            orderItem.setOrderNo(orderNo);
        }

        //mybatis 批量插入OrderItem
        orderItemMapper.batchInsert(orderItemList);

        //减库存
        this.reduceProductStock(orderItemList);

        //清空购物车
        this.cleanCart(cartList);

        OrderDTO orderDTO = assembleOrderDTO(order, orderItemList);

        return ServerResponse.createBySuccess(orderDTO);
    }

    @Override
    @Transactional
    public ServerResponse createByProductIdAndCount(Integer userId, Integer shippingId, Integer productId, Integer count) {
        if (productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        //校验商品和库存
        Product product = productMapper.selectByPrimaryKey(productId);
        if (Const.ProductStatus.ON_SALE.getCode() != product.getStatus()){
            return ServerResponse.createByErrorMessage("商品" + product.getName() + "已下架！");
        }

        //校验库存
        if (count > product.getStock()){
            return ServerResponse.createByErrorMessage("商品" + product.getName() + "库存不足！");
        }

        OrderItem orderItem = GenericBuilder.of(OrderItem::new)
                .with(OrderItem::setUserId, userId)
                .with(OrderItem::setProductId, product.getId())
                .with(OrderItem::setProductName, product.getName())
                .with(OrderItem::setProductImage, product.getMainImage())
                .with(OrderItem::setCurrentUnitPrice, product.getPrice())
                .with(OrderItem::setQuantity, count)
                .with(OrderItem::setTotalPrice, BigDecimalUtil.multiply(product.getPrice().doubleValue(), count))
                .build();

        BigDecimal payment = orderItem.getTotalPrice();

        Order order = this.assembleOrder(userId,shippingId,payment);
        if (order == null){
            return ServerResponse.createByErrorMessage("生成订单失败！");
        }
        long orderNo = order.getOrderNo();
        orderItem.setOrderNo(orderNo);

        orderItemMapper.insertSelective(orderItem);
        List<OrderItem> orderItemList = Lists.newArrayList();
        orderItemList.add(orderItem);
        //减库存
        this.reduceProductStock(orderItemList);
        //组装订单DTO
        OrderDTO orderDTO = assembleOrderDTO(order, orderItemList);

        return ServerResponse.createBySuccess(orderDTO);
    }

    @Override
    public ServerResponse<OrderDTO> getDetail(Integer userId, Long orderNo){
        Order order = orderMapper.selectByOrderNoAndUserId(orderNo, userId);
        if(order != null){
            List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoAndUserId(orderNo,userId);
            OrderDTO orderDTO = assembleOrderDTO(order,orderItemList);
            return ServerResponse.createBySuccess(orderDTO);
        }
        return  ServerResponse.createByErrorMessage("未有找到该订单！");
    }

    @Override
    public ServerResponse<PageInfo> getList(Integer userId, int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectByUserId(userId);
        List<OrderDTO> orderDTOList = assembleOrderDTOList(orderList,userId);
        PageInfo pageResult = new PageInfo(orderDTOList);
        pageResult.setList(orderDTOList);
        return ServerResponse.createBySuccess(pageResult);
    }

    private List<OrderDTO> assembleOrderDTOList(List<Order> orderList, Integer userId){
        List<OrderDTO> orderDTOList = Lists.newArrayList();
        for(Order order : orderList){
            List<OrderItem>  orderItemList;
            if(userId == null){
                //管理员查询
                orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
            }else{
                orderItemList = orderItemMapper.selectByOrderNoAndUserId(order.getOrderNo(),userId);
            }
            OrderDTO orderDTO = assembleOrderDTO(order,orderItemList);
            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }

    private OrderDTO assembleOrderDTO(Order order, List<OrderItem> orderItemList){
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);

        orderDTO.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());
        orderDTO.setStatusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getValue());

        orderDTO.setPaymentTime(DateTimeUtil.dateToString(order.getPaymentTime()));
        orderDTO.setSendTime(DateTimeUtil.dateToString(order.getSendTime()));
        orderDTO.setEndTime(DateTimeUtil.dateToString(order.getEndTime()));
        orderDTO.setCreateTime(DateTimeUtil.dateToString(order.getCreateTime()));
        orderDTO.setCloseTime(DateTimeUtil.dateToString(order.getCloseTime()));
        orderDTO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if(shipping != null){
            orderDTO.setReceiverName(shipping.getReceiverName());
            orderDTO.setShippingDTO(assembleShippingDTO(shipping));
        }
        List<OrderItemDTO> orderItemDTOList = Lists.newArrayList();

        for(OrderItem orderItem : orderItemList){
            OrderItemDTO orderItemDTO = assembleOrderItemDTO(orderItem);
            orderItemDTOList.add(orderItemDTO);
        }
        orderDTO.setOrderItemDTOList(orderItemDTOList);
        return orderDTO;
    }


    private OrderItemDTO assembleOrderItemDTO(OrderItem orderItem){
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        BeanUtils.copyProperties(orderItem, orderItemDTO);

        orderItemDTO.setCreateTime(DateTimeUtil.dateToString(orderItem.getCreateTime()));
        return orderItemDTO;
    }

    private ShippingDTO assembleShippingDTO(Shipping shipping){
        ShippingDTO shippingDTO = new ShippingDTO();
        BeanUtils.copyProperties(shipping, shippingDTO);
        return shippingDTO;
    }

    private void cleanCart(List<Cart> cartList){
        for(Cart cart : cartList){
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
    }

    private boolean reduceProductStock(List<OrderItem> orderItemList){

        for (OrderItem orderItem : orderItemList){
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
        return true;
    }

    private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment){

        long orderNo = this.generateOrderNo();
        Order order = GenericBuilder.of(Order::new)
                .with(Order::setOrderNo, orderNo)
                .with(Order::setStatus, Const.OrderStatusEnum.UNPAID.getCode())
                .with(Order::setPostage, 0)
                .with(Order::setPaymentType, Const.PaymentTypeEnum.ONLINE_PAY.getCode())
                .with(Order::setPayment, payment)
                .with(Order::setUserId, userId)
                .with(Order::setShippingId, shippingId)
                .build();
        //发货时间等等
        //付款时间等等
        int res = orderMapper.insert(order);
        if(res > 0){
            return order;
        }
        return null;
    }

    private long generateOrderNo(){
        long ct = System.currentTimeMillis();
        return ct + new Random().nextInt(200);
    }

    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList){
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList){
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    private ServerResponse<List<OrderItem>> getCartOrderItem(List<Cart> cartList){
        List<OrderItem> orderItemList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(cartList)){
            return ServerResponse.createByErrorMessage("购物车中已选中商品为空！");
        }
        //校验购物车数据的正确性，包括商品的状态和数量
        OrderItem orderItem;
        for (Cart cartItem : cartList){
            Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
            if (Const.ProductStatus.ON_SALE.getCode() != product.getStatus()){
                return ServerResponse.createByErrorMessage("商品" + product.getName() + "已下架！");
            }

            //校验库存
            if (cartItem.getQuantity() > product.getStock()){
                return ServerResponse.createByErrorMessage("商品" + product.getName() + "库存不足！");
            }
            orderItem = GenericBuilder.of(OrderItem::new)
                    .with(OrderItem::setUserId, cartItem.getUserId())
                    .with(OrderItem::setProductId, product.getId())
                    .with(OrderItem::setProductName, product.getName())
                    .with(OrderItem::setProductImage, product.getMainImage())
                    .with(OrderItem::setCurrentUnitPrice, product.getPrice())
                    .with(OrderItem::setQuantity, cartItem.getQuantity())
                    .with(OrderItem::setTotalPrice, BigDecimalUtil.multiply(product.getPrice().doubleValue(),cartItem.getQuantity()))
                    .build();
            orderItemList.add(orderItem);
        }
        return ServerResponse.createBySuccess(orderItemList);
    }


    @Override
    @Transactional
    public ServerResponse pay(Long orderNo, Integer userId, String path){
        if (orderNo == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Order order = orderMapper.selectByOrderNoAndUserId(orderNo, userId);
        if (order == null){
            return ServerResponse.createByErrorMessage("查无此订单！");
        }
        Map<String, String> resultMap = Maps.newHashMap();
        resultMap.put("orderNo", orderNo.toString());
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = orderNo.toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("耀眼商城扫码支付,订单号:").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body =  new StringBuilder().append("订单").append(outTradeNo).append("，购买商品共").append(totalAmount).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoAndUserId(orderNo, userId);
        for(OrderItem orderItem : orderItemList){
            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(),
                    BigDecimalUtil.multiply(orderItem.getCurrentUnitPrice().doubleValue(),new Double(100).doubleValue()).longValue(),
                    orderItem.getQuantity());
            goodsDetailList.add(goods);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功");

                AlipayTradePrecreateResponse response = result.getResponse();
                printResponse(response);

                File file = new File(path);
                if (!file.exists()){
                    file.setWritable(true);
                    file.mkdirs();
                }

                // 需要修改为运行机器上的路径
                String qrPath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                String qrFileName = String.format("/qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);
                File targetFile = new File(path, qrFileName);
                log.info("qrPath:" + qrPath);
                try {
                    FTPUtil.uploadFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    log.error("上传二维码异常", e);
                }
                String qrURL = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFile.getName();
                resultMap.put("qrURL", qrURL);
                return ServerResponse.createBySuccess(resultMap);
            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByErrorMessage("系统异常，预下单状态未知!!!");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }
    }


    // 简单打印应答
    private void printResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    @Override
    @Transactional
    public ServerResponse alipayCallback(Map<String, String> params){
        Long orderNo = Long.parseLong(params.get("out_trade_no"));
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            return ServerResponse.createByErrorMessage("该订单不是本商城的订单！");
        }
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");
        log.info("交易状态：" + tradeStatus);
        if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createByErrorMessage("支付宝重复调用");
        }

        if (Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)){
            log.info("支付成功");
            order.setPaymentTime(DateTimeUtil.stringToDate(params.get("gmt_payment")));
            order.setStatus(Const.OrderStatusEnum.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }
        PayInfo payInfo = GenericBuilder.of(PayInfo::new)
                .with(PayInfo::setUserId, order.getId())
                .with(PayInfo::setOrderNo, order.getOrderNo())
                .with(PayInfo::setPayPlatform, Const.PayPlatformEnum.ALIPAY.getCode())
                .with(PayInfo::setPlatformNumber, tradeNo)
                .with(PayInfo::setPlatformStatus, tradeStatus)
                .build();
        log.info("支付信息：" + payInfo);
        payInfoMapper.insert(payInfo);
        //TODO
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<Boolean> queryOrderPayStatus(Integer userId, Long orderNo){
        Order order = orderMapper.selectByOrderNoAndUserId(orderNo, userId);
        if (order != null && order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }

    //管理员端
    @Override
    public ServerResponse<PageInfo> manageList(int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectAll();
        List<OrderDTO> orderDTOList = this.assembleOrderDTOList(orderList,null);
        PageInfo pageResult = new PageInfo(orderList);
        pageResult.setList(orderDTOList);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<OrderDTO> manageDetail(Long orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order != null){
            List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
            OrderDTO orderDTO = assembleOrderDTO(order,orderItemList);
            return ServerResponse.createBySuccess(orderDTO);
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }

    @Override
    public ServerResponse<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectByVagueOrderNo("%" + orderNo + "%");
        if(orderList.size() > 0){
            List<OrderDTO> orderDTOList = this.assembleOrderDTOList(orderList,null);
            PageInfo pageResult = new PageInfo(orderList);
            pageResult.setList(orderDTOList);
            return ServerResponse.createBySuccess(pageResult);
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }

    @Override
    public ServerResponse<String> manageSendGoods(Long orderNo){
        Order order= orderMapper.selectByOrderNo(orderNo);
        if(order != null){
            if(order.getStatus() == Const.OrderStatusEnum.PAID.getCode()){
                order.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
                order.setSendTime(new Date());
                orderMapper.updateByPrimaryKeySelective(order);
                return ServerResponse.createBySuccess("发货成功");
            }
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }
}
