package cn.xuguowen.infrastructure.persistent.repository;

import cn.xuguowen.domain.strategy.model.entity.StrategyAwardEntity;
import cn.xuguowen.domain.strategy.model.entity.StrategyEntity;
import cn.xuguowen.domain.strategy.model.entity.StrategyRuleEntity;
import cn.xuguowen.domain.strategy.model.valobj.*;
import cn.xuguowen.domain.strategy.repository.IStrategyRepository;
import cn.xuguowen.infrastructure.persistent.dao.*;
import cn.xuguowen.infrastructure.persistent.po.*;
import cn.xuguowen.infrastructure.persistent.redis.IRedisService;
import cn.xuguowen.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: StrategyRepository
 * Package: cn.xuguowen.infrastructure.persistent.repository
 * Description:策略服务仓储实现类
 *
 * @Author 徐国文
 * @Create 2024/7/2 13:27
 * @Version 1.0
 */
@Slf4j
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

    @Resource
    private IRuleTreeDao ruleTreeDao;

    @Resource
    private IRuleTreeNodeDao ruleTreeNodeDao;

    @Resource
    private IRuleTreeNodeLineDao ruleTreeNodeLineDao;

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

    @Override
    public String queryStrategyRuleValue(Long strategyId, String ruleModel) {
        return this.queryStrategyRuleRuleValue(strategyId, null, ruleModel);
    }

    /**
     * 根据规则树ID查询规则信息：包含规则节点信息和规则节点连线信息
     *
     * @param treeId 规则树ID
     * @return
     */
    @Override
    public RuleTreeVO queryRuleTreeVOByTreeId(String treeId) {
        // 优先从缓存中获取
        String cacheKey = Constants.RedisKey.RULE_TREE_VO_KEY + treeId;
        RuleTreeVO ruleTreeVOCache = redisService.getValue(cacheKey);
        if (Objects.nonNull(ruleTreeVOCache)) return ruleTreeVOCache;

        // 1.从数据库查询规则树、规则树节点和规则树节点线
        RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId(treeId);
        List<RuleTreeNode> ruleTreeNodeList = ruleTreeNodeDao.queryRuleTreeNodeByTreeId(treeId);
        List<RuleTreeNodeLine> ruleTreeNodeLineList = ruleTreeNodeLineDao.queryRuleTreeNodeLineByTreeId(treeId);

        // 创建一个Map来存储每个规则树节点对应的规则树节点线
        Map<String, List<RuleTreeNodeLineVO>> ruleTreeNodeLineMap = new HashMap<>();
        for (RuleTreeNodeLine ruleTreeNodeLine : ruleTreeNodeLineList) {
            RuleTreeNodeLineVO ruleTreeNodeLineVO = RuleTreeNodeLineVO.builder()
                    .treeId(ruleTreeNodeLine.getTreeId())
                    .ruleNodeFrom(ruleTreeNodeLine.getRuleNodeFrom())
                    .ruleNodeTo(ruleTreeNodeLine.getRuleNodeTo())
                    .ruleLimitTypeVO(RuleLimitTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitType()))
                    .ruleLogicTypeVO(RuleLogicCheckTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitValue()))
                    .build();

            List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList = ruleTreeNodeLineMap.computeIfAbsent(ruleTreeNodeLine.getRuleNodeFrom(), k -> new ArrayList<>());
            ruleTreeNodeLineVOList.add(ruleTreeNodeLineVO);
        }

        // 创建一个Map来存储每个规则树节点对应的RuleTreeNodeVO对象
        Map<String, RuleTreeNodeVO> treeNodeMap = new HashMap<>();
        for (RuleTreeNode ruleTreeNode : ruleTreeNodeList) {
            RuleTreeNodeVO ruleTreeNodeVO = RuleTreeNodeVO.builder()
                    .treeId(ruleTreeNode.getTreeId())
                    .ruleKey(ruleTreeNode.getRuleKey())
                    .ruleDesc(ruleTreeNode.getRuleDesc())
                    .ruleValue(ruleTreeNode.getRuleValue())
                    .treeNodeLineVOList(ruleTreeNodeLineMap.get(ruleTreeNode.getRuleKey()))
                    .build();
            treeNodeMap.put(ruleTreeNode.getRuleKey(), ruleTreeNodeVO);
        }

        RuleTreeVO ruleTreeVODB = RuleTreeVO.builder()
                .treeId(ruleTree.getTreeId())
                .treeName(ruleTree.getTreeName())
                .treeDesc(ruleTree.getTreeDesc())
                .treeRootRuleNode(ruleTree.getTreeNodeRuleKey())
                .treeNodeMap(treeNodeMap)
                .build();

        // 存入redis中
        redisService.setValue(cacheKey, ruleTreeVODB);

        return ruleTreeVODB;
    }

    /**
     * 缓存奖品库存到Redis
     *
     * @param cacheKey   缓存key
     * @param awardCount 奖品数量
     */
    @Override
    public void cacheStrategyAwardCount(String cacheKey, Integer awardCount) {
        // 避免每次重新装配
        if (redisService.isExists(cacheKey)) return;
        // 将奖品的数量存入redis中
        redisService.setAtomicLong(cacheKey, awardCount);
    }

    /**
     * 从redis中扣减奖品的库存
     *
     * @param cacheKey 缓存key
     * @return
     */
    @Override
    public Boolean subtractionAwardStock(String cacheKey) {
        // 1.扣减缓存
        long surplus = redisService.decr(cacheKey);
        if (surplus < 0) {
            // 库存小于0，恢复为0个
            redisService.setValue(cacheKey, 0);
            return Boolean.FALSE;
        }

        // 2.兜底操作：setNx 锁的目的是兜底，比如活动配置有 10 个库存，消耗开始 9、8、7、6 但因为一些问题，无论是redis还是其他系统导致的，运营需要重新调整恢复库存。但这个时候恢复错了为9个，但已经消耗到6个。那么 8、7、6 就会产生新的加锁key，这个加锁key会被redis已经加锁的key拦截，避免超卖。因为加锁不是竞争，不好费性能但可以做兜底，是个不错的选择。【实际中系统运行最容易出问题的点，就是运营配置问题和调整活动】
        // 2.1 按照cacheKey decr 后的值，如 99、98、97 和 key 组成为库存锁的key进行使用。
        // 2.2 加锁为了兜底，如果后续有恢复库存，手动处理等，也不会超卖。因为所有的可用库存key，都被加锁了。
        String lockKey = cacheKey + Constants.UNDERLINE + surplus;
        Boolean lock = redisService.setNx(lockKey);
        if (Boolean.FALSE.equals(lock)) {
            log.info("策略奖品库存加锁失败 {}", lockKey);
        }
        return lock;
    }

    /**
     * 写入奖品库存消费队列
     * @param strategyAwardStockKeyVO
     */
    @Override
    public void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUERY_KEY;
        RBlockingQueue<StrategyAwardStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        RDelayedQueue<StrategyAwardStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.offer(strategyAwardStockKeyVO, 3, TimeUnit.SECONDS);
    }

    /**
     * 获取奖品库存消费队列
     * @return
     */
    @Override
    public StrategyAwardStockKeyVO takeQueueValue() {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUERY_KEY;
        RBlockingQueue<StrategyAwardStockKeyVO> destinationQueue  = redisService.getBlockingQueue(cacheKey);
        return destinationQueue.poll();
    }

    /**
     * 更新奖品库存消耗
     *
     * @param strategyId 策略ID
     * @param awardId 奖品ID
     */
    @Override
    public void updateStrategyAwardStock(Long strategyId, Long awardId) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setStrategyId(strategyId);
        strategyAward.setAwardId(awardId);
        strategyAwardDao.updateStrategyAwardStock(strategyAward);
    }
}
