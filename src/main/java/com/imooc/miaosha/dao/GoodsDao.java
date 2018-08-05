package com.imooc.miaosha.dao;

import com.imooc.miaosha.domain.Goods;
import com.imooc.miaosha.domain.MiaoshaGoods;
import com.imooc.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface GoodsDao {
    @Select("select * from goods as goods left join miaosha_goods as mg on goods.id=mg.goods_id")
    public List<GoodsVo> listGoodsVo();
    @Select("select * from goods as goods left join miaosha_goods as mg on goods.id=mg.goods_id where goods.id=#{goodsId}")
    public GoodsVo getGoodsById(@Param("goodsId") long goodsId);
    @Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId}")
    public boolean reduceStock(MiaoshaGoods g);
}
