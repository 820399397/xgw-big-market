package cn.xuguowen.infrastructure.persistent.repository;

import cn.xuguowen.domain.strategy.model.entity.StrategyAwardEntity;
import cn.xuguowen.domain.strategy.repository.IStrategyRepository;
import cn.xuguowen.infrastructure.persistent.dao.IStrategyAwardDao;
import cn.xuguowen.infrastructure.persistent.po.StrategyAward;
import cn.xuguowen.infrastructure.persistent.redis.IRedisService;
import cn.xuguowen.types.common.Constants;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**
     * 根据抽奖策略ID查询当前抽奖策略下的所有奖品信息
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
     * @param strategyId 抽奖策略ID
     * @param rateRange  抽奖概率范围
     * @param shuffleStrategyAwardSearchRateTable   概率范围查找表
     */
    @Override
    public void storeStrategyAwardSearchRateTable(Long strategyId, BigDecimal rateRange, Map<Integer, Long> shuffleStrategyAwardSearchRateTable) {
        // 1. 存储抽奖策略范围值
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId, rateRange.intValue());
        // 2. 存储概率查找表
        Map<Integer, Long> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId);
        cacheRateTable.putAll(shuffleStrategyAwardSearchRateTable);
    }

    /**
     * 根据抽奖策略ID查询缓存获取抽奖概率范围值
     * @param strategyId    抽奖策略ID
     * @return
     */
    @Override
    public Integer getRateRange(Long strategyId) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId);
    }

    /**
     * 根据抽奖策略ID和概率范围值获取查找表中的某个奖品
     * @param strategyId    抽奖策略ID
     * @param random        随机数
     * @return
     */
    @Override
    public Long getStrategyAwardAssemble(Long strategyId, int random) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId, random);
    }
}
