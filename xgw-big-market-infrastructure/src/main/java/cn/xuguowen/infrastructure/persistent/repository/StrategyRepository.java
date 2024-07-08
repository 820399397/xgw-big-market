package cn.xuguowen.infrastructure.persistent.repository;

import cn.xuguowen.domain.strategy.model.entity.StrategyAwardEntity;
import cn.xuguowen.domain.strategy.model.entity.StrategyEntity;
import cn.xuguowen.domain.strategy.model.entity.StrategyRuleEntity;
import cn.xuguowen.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import cn.xuguowen.domain.strategy.repository.IStrategyRepository;
import cn.xuguowen.infrastructure.persistent.dao.IStrategyAwardDao;
import cn.xuguowen.infrastructure.persistent.dao.IStrategyDao;
import cn.xuguowen.infrastructure.persistent.dao.IStrategyRuleDao;
import cn.xuguowen.infrastructure.persistent.po.Strategy;
import cn.xuguowen.infrastructure.persistent.po.StrategyAward;
import cn.xuguowen.infrastructure.persistent.po.StrategyRule;
import cn.xuguowen.infrastructure.persistent.redis.IRedisService;
import cn.xuguowen.types.common.Constants;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ClassName: StrategyRepository
 * Package: cn.xuguowen.infrastructure.persistent.repository
 * Description:策略服务仓储实现类
 *
 * @Author 徐国文
 * @Create 2024/7/2 13:27
 * @Version 1.0
 */
@Repository
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IRedisService redisService;

    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyRuleDao strategyRuleDao;

    /**
     * 根据抽奖策略ID查询当前抽奖策略下的所有奖品信息
     *
     * @param strategyId 抽奖策略ID
     * @return
     */
    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        // 1.先从redis缓存中获取
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntityList = redisService.getValue(cacheKey);
        if (!CollectionUtils.isEmpty(strategyAwardEntityList)) {
            return strategyAwardEntityList;
        }

        // 2.从数据库中查询
        List<StrategyAward> strategyAwardList = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        strategyAwardEntityList = new ArrayList<>(strategyAwardList.size());
        for (StrategyAward strategyAward : strategyAwardList) {
            StrategyAwardEntity strategyAwardEntity = StrategyAwardEntity.builder()
                    .strategyId(strategyAward.getStrategyId())
                    .awardId(strategyAward.getAwardId())
                    .awardCount(strategyAward.getAwardCount())
                    .awardCountSurplus(strategyAward.getAwardCountSurplus())
                    .awardRate(strategyAward.getAwardRate())
                    .build();
            strategyAwardEntityList.add(strategyAwardEntity);
        }

        // 3.将其存入到redis中
        redisService.setValue(cacheKey, strategyAwardEntityList);

        // 4.返回结果
        return strategyAwardEntityList;
    }

    /**
     * 将概率查找表存入到redis中
     *
     * @param key                                 redis key
     * @param rateRange                           抽奖概率范围
     * @param shuffleStrategyAwardSearchRateTable 概率范围查找表
     */
    @Override
    public void storeStrategyAwardSearchRateTable(String key, BigDecimal rateRange, Map<Integer, Long> shuffleStrategyAwardSearchRateTable) {
        // 1. 存储抽奖策略范围值
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key, rateRange.intValue());
        // 2. 存储概率查找表
        Map<Integer, Long> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key);
        cacheRateTable.putAll(shuffleStrategyAwardSearchRateTable);
    }

    /**
     * 根据抽奖策略ID查询缓存获取抽奖概率范围值
     *
     * @param strategyId 抽奖策略ID
     * @return
     */
    @Override
    public Integer getRateRange(Long strategyId) {
        return this.getRateRange(String.valueOf(strategyId));
    }

    @Override
    public Integer getRateRange(String key) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key);
    }

    /**
     * 根据抽奖策略ID和概率范围值获取查找表中的某个奖品
     *
     * @param key    抽奖策略ID
     * @param random 随机数
     * @return
     */
    @Override
    public Long getStrategyAwardAssemble(String key, int random) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key, random);
    }

    /**
     * 将累加概率范围存入redis中
     *
     * @param strategyId             抽奖策略ID
     * @param awardCumulativeRateMap 累加概率范围Map
     * @param totalAwardRate         总中奖概率
     */
    @Override
    public void storeCumulativeRateMap(Long strategyId, BigDecimal totalAwardRate, Map<Long, BigDecimal> awardCumulativeRateMap) {
        // 1. 存储总中奖概率
        redisService.setValue(Constants.RedisKey.STRATEGY_TOTAL_AWARD_KEY + strategyId, totalAwardRate);
        // 2. 存储每个奖品的概率值
        Map<Long, BigDecimal> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_CUMULATIVE_RATE_MAP_KEY + strategyId);
        cacheRateTable.putAll(awardCumulativeRateMap);
    }

    /**
     * 获取累加概率范围Map
     *
     * @param strategyId 抽奖策略ID
     * @return
     */
    @Override
    public Map<Long, BigDecimal> getCumulativeRateMap(Long strategyId) {
        return redisService.getMap(Constants.RedisKey.STRATEGY_CUMULATIVE_RATE_MAP_KEY + strategyId);
    }

    /**
     * 获取总概率值
     *
     * @param strategyId 抽奖策略ID
     * @return
     */
    @Override
    public BigDecimal getTotalAwardRate(Long strategyId) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_TOTAL_AWARD_KEY + strategyId);
    }

    /**
     * 根据策略ID查询抽奖策略信息
     *
     * @param strategyId 抽奖策略ID
     * @return
     */
    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        // 1.先从缓存中获取
        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if (Objects.nonNull(strategyEntity)) return strategyEntity;

        // 2.从数据库查询
        Strategy strategy = strategyDao.queryStrategyEntityByStrategyId(strategyId);
        strategyEntity = StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .strategyDesc(strategy.getStrategyDesc())
                .ruleModels(strategy.getRuleModels())
                .build();

        // 3.存入缓存
        redisService.setValue(cacheKey, strategyEntity);

        // 4.返回结果
        return strategyEntity;
    }

    /**
     * 查询抽奖策略规则
     *
     * @param strategyId 抽奖策略ID
     * @param ruleModel  抽奖策略权重
     * @return
     */
    @Override
    public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel) {
        StrategyRule strategyRule = new StrategyRule();
        strategyRule.setStrategyId(strategyId);
        strategyRule.setRuleModel(ruleModel);
        StrategyRule strategyRuleDB = strategyRuleDao.queryStrategyRule(strategyRule);

        return StrategyRuleEntity.builder()
                .strategyId(strategyRuleDB.getStrategyId())
                .awardId(strategyRuleDB.getAwardId())
                .ruleType(strategyRuleDB.getRuleType())
                .ruleModel(strategyRuleDB.getRuleModel())
                .ruleValue(strategyRuleDB.getRuleValue())
                .ruleDesc(strategyRuleDB.getRuleDesc())
                .build();
    }

    /**
     * 查询抽奖策略规则中的抽奖规则比值
     *
     * @param strategyId 抽奖策略ID
     * @param awardId    奖品ID
     * @param ruleModel  抽奖规则类型 抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品) 、rule_weight - 权重抽奖、rule_blacklist - 黑名单】
     * @return
     */
    @Override
    public String queryStrategyRuleRuleValue(Long strategyId, Long awardId, String ruleModel) {
        StrategyRule strategyRule = new StrategyRule();
        strategyRule.setStrategyId(strategyId);
        strategyRule.setAwardId(awardId);
        strategyRule.setRuleModel(ruleModel);
        return strategyRuleDao.queryStrategyRuleRuleValue(strategyRule);
    }

    @Override
    public StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Long awardId) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setStrategyId(strategyId);
        strategyAward.setAwardId(awardId);
        String ruleModels = strategyAwardDao.queryStrategyAwardRuleModels(strategyAward);
        return StrategyAwardRuleModelVO.builder().ruleModels(ruleModels).build();
    }
}
