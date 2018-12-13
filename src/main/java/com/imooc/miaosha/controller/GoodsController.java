package com.imooc.miaosha.controller;

import com.imooc.miaosha.domain.Goods;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.KeyPrefix;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.vo.GoodsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
@Api(value = "商品信息的接口")
public class GoodsController {
    Logger logger = LoggerFactory.getLogger(GoodsController.class);
    @Autowired
    private MiaoshaUserService miaoshaUserService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    private ApplicationContext applicationContext;
    @RequestMapping("/to_list")
    @ApiOperation(value = "商品列表",notes ="列出商品列表")
    @ResponseBody
    public String to_list(MiaoshaUser user, Model model, HttpServletRequest request, HttpServletResponse response){
        model.addAttribute("user", user);
        List<GoodsVo> goodsList =  goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        SpringWebContext swc = new SpringWebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap(),applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",swc);
        redisService.set(GoodsKey.getGoodsList,"",html);
        return html;
    }
    @RequestMapping(value = "/to_detail/{goodsId}",produces = "text/html")
    @ResponseBody
    public String to_detail(MiaoshaUser user, Model model, @PathVariable(value = "goodsId") long goodsId,
                            HttpServletRequest request, HttpServletResponse response){
        GoodsVo goods = goodsService.getGoodsById(goodsId);
        model.addAttribute("user", user);
        model.addAttribute("goods", goods);
        String html = redisService.get(GoodsKey.getGoodsDetail,String.valueOf(goodsId),String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        SpringWebContext swc = new SpringWebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap(),applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",swc);
        redisService.set(GoodsKey.getGoodsList,String.valueOf(goodsId),html);
        return html;
    }
}
