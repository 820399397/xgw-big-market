package cn.xuguowen.infrastructure.persistent.dao;

import cn.xuguowen.infrastructure.persistent.po.Strategy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: IStrategyDao
 * Package: cn.xuguowen.infrastructure.persistent.dao
 * Description:抽奖策略持久化
 *
 * @Author 徐国文
 * @Create 2024/7/1 10:56
 * @Version 1.0
 */
@Mapper
public interface IStrategyDao {

    List<Strategy> queryStrategyList();

    /**
     * 根据策略ID查询抽奖策略信息
     * @param strategyId    抽奖策略ID
     * @return
     */
    Strategy queryStrategyEntityByStrategyId(@Param("strategyId") Long strategyId);
}
