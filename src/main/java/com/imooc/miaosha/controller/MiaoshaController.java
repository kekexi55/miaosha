package com.imooc.miaosha.controller;

import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.rabbitmq.MiaoshaMessage;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.KeyPrefix;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MiaoshaService miaoshaService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MQSender mqSender;
    @RequestMapping("/do_miaosha")
    public Result<Integer> do_miaosha(MiaoshaUser user, Model model, @RequestParam(value ="goodsId") long goodsId){
        model.addAttribute("user",user);
        if(user == null){
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        long stock = redisService.decr(GoodsKey.getGoodsStock,goodsId+"");
        if(stock<0){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        MiaoshaOrder order = orderService.getMiaoshaOrderByOrderIdGoodsId(user.getId(),goodsId);
        if(order!=null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        MiaoshaMessage message = new MiaoshaMessage();
        message.setGoodsId(goodsId);
        message.setMiaoshaUser(user);
        mqSender.sendMiaoshaMessage(message);
//        GoodsVo goods = goodsService.getGoodsById(goodsId);
//        Integer stock = goods.getStockCount();
//        if (stock <= 0){
//            model.addAttribute("errmsg",CodeMsg.MIAO_SHA_OVER.getMsg());
//            return "miaosha_fail";
//        }
//        MiaoshaOrder order = orderService.getMiaoshaOrderByOrderIdGoodsId(user.getId(),goodsId);
//        if(order!=null){
//            model.addAttribute("errmsg",CodeMsg.REPEATE_MIAOSHA.getMsg());
//            return "miaosha_fail";
//        }
//        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
//        model.addAttribute("orderInfo", orderInfo);
//        model.addAttribute("goods", goods);
//        return "order_detail";
        return Result.success(0);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goods= goodsService.listGoodsVo();
        if(!goods.isEmpty()){
            for (GoodsVo vo:goods){
                redisService.set(GoodsKey.getGoodsStock,vo.getId()+"",vo.getStockCount());
            }
        }
    }
}
