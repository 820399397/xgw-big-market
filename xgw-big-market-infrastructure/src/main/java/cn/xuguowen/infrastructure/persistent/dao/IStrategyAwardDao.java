package cn.xuguowen.infrastructure.persistent.dao;

import cn.xuguowen.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: IStrategyAwardDao
 * Package: cn.xuguowen.infrastructure.persistent.dao
 * Description:抽奖奖品持久化
 *
 * @Author 徐国文
 * @Create 2024/7/1 10:56
 * @Version 1.0
 */
@Mapper
public interface IStrategyAwardDao {

    List<StrategyAward> queryStrategyAwardList();

    /**
     * 根据抽奖策略ID查询属于当前抽奖策略的奖品列表信息
     * @param strategyId 抽奖策略ID
     * @return
     */
    List<StrategyAward> queryStrategyAwardListByStrategyId(@Param("strategyId") Long strategyId);

    String queryStrategyAwardRuleModels(StrategyAward strategyAward);

    /**
     * 更新奖品剩余数量
     * @param strategyAward
     */
    void updateStrategyAwardStock(StrategyAward strategyAward);
}
