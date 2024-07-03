package cn.xuguowen.domain.strategy.repository;

import cn.xuguowen.domain.strategy.model.entity.StrategyAwardEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * ClassName: IStrategyRepository
 * Package: cn.xuguowen.domain.strategy.repository
 * Description:策略服务仓储接口
 *
 * @Author 徐国文
 * @Create 2024/7/2 13:28
 * @Version 1.0
 */
public interface IStrategyRepository {
    /**
     * 根据抽奖策略ID查询当前抽奖策略下的所有奖品信息
     *
     * @param strategyId 抽奖策略ID
     * @return
     */
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    /**
     * 将概率查找表存入到redis中
     *
     * @param strategyId                          抽奖策略ID
     * @param rateRange                           抽奖概率范围
     * @param shuffleStrategyAwardSearchRateTable 概率范围查找表
     */
    void storeStrategyAwardSearchRateTable(Long strategyId, BigDecimal rateRange, Map<Integer, Long> shuffleStrategyAwardSearchRateTable);

    /**
     * 根据抽奖策略ID查询缓存获取抽奖概率范围值
     *
     * @param strategyId 抽奖策略ID
     * @return
     */
    Integer getRateRange(Long strategyId);

    /**
     * 根据抽奖策略ID和概率范围值获取查找表中的某个奖品
     *
     * @param strategyId 抽奖策略ID
     * @param random     随机数
     * @return
     */
    Long getStrategyAwardAssemble(Long strategyId, int random);

    /**
     * 将累加概率范围存入redis中
     *
     * @param strategyId             抽奖策略ID
     * @param awardCumulativeRateMap 累加概率范围Map
     * @param totalAwardRate         总概率值
     */
    void storeCumulativeRateMap(Long strategyId, BigDecimal totalAwardRate, Map<Long, BigDecimal> awardCumulativeRateMap);

    /**
     * 获取累加概率范围Map
     *
     * @param strategyId 抽奖策略ID
     * @return
     */
    Map<Long, BigDecimal> getCumulativeRateMap(Long strategyId);

    /**
     * 获取总概率值
     *
     * @param strategyId 抽奖策略ID
     * @return
     */
    BigDecimal getTotalAwardRate(Long strategyId);
}
