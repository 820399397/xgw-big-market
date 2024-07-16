package cn.xuguowen.domain.strategy.repository;

import cn.xuguowen.domain.strategy.model.entity.StrategyAwardEntity;
import cn.xuguowen.domain.strategy.model.entity.StrategyEntity;
import cn.xuguowen.domain.strategy.model.entity.StrategyRuleEntity;
import cn.xuguowen.domain.strategy.model.valobj.RuleTreeVO;
import cn.xuguowen.domain.strategy.model.valobj.StrategyAwardRuleModelVO;

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
     * @param key                                 redis key
     * @param rateRange                           抽奖概率范围
     * @param shuffleStrategyAwardSearchRateTable 概率范围查找表
     */
    void storeStrategyAwardSearchRateTable(String key, BigDecimal rateRange, Map<Integer, Long> shuffleStrategyAwardSearchRateTable);

    /**
     * 根据抽奖策略ID查询缓存获取抽奖概率范围值
     *
     * @param strategyId 抽奖策略ID
     * @return
     */
    Integer getRateRange(Long strategyId);

    /**
     * 根据抽奖策略ID查询缓存获取抽奖概率范围值
     * 适合于普通抽奖和权重抽奖
     *
     * @param key
     * @return
     */
    Integer getRateRange(String key);

    /**
     * 根据抽奖策略ID和概率范围值获取查找表中的某个奖品
     *
     * @param key    redis key
     * @param random 随机数
     * @return
     */
    Long getStrategyAwardAssemble(String key, int random);

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

    /**
     * 根据策略ID查询抽奖策略信息
     *
     * @param strategyId 抽奖策略ID
     * @return
     */
    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    /**
     * 查询抽奖策略规则
     *
     * @param strategyId 抽奖策略ID
     * @param ruleModel  抽奖策略权重
     * @return
     */
    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel);

    /**
     * 查询抽奖策略规则中的抽奖规则比值
     * @param strategyId    抽奖策略ID
     * @param awardId       奖品ID
     * @param ruleModel     抽奖规则类型 抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品) 、rule_weight - 权重抽奖、rule_blacklist - 黑名单】
     * @return
     */
    String queryStrategyRuleRuleValue(Long strategyId, Long awardId, String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Long awardId);

    String queryStrategyRuleValue(Long strategyId, String ruleModel);

    /**
     * 根据规则树ID查询规则信息：包含规则节点信息和规则节点连线信息
     * @param treeId    规则树ID
     * @return
     */
    RuleTreeVO queryRuleTreeVOByTreeId(String treeId);
}
