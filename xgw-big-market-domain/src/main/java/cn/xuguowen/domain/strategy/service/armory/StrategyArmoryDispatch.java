package cn.xuguowen.domain.strategy.service.armory;

import cn.xuguowen.domain.strategy.model.entity.StrategyAwardEntity;
import cn.xuguowen.domain.strategy.model.entity.StrategyEntity;
import cn.xuguowen.domain.strategy.model.entity.StrategyRuleEntity;
import cn.xuguowen.domain.strategy.repository.IStrategyRepository;
import cn.xuguowen.types.enums.ResponseCode;
import cn.xuguowen.types.exception.AppException;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ClassName: StrategyArmory
 * Package: cn.xuguowen.domain.strategy.service.armory
 * Description:策略装配库(兵工厂)，负责初始化策略计算
 *
 * @Author 徐国文
 * @Create 2024/7/2 13:23
 * @Version 1.0
 */
@Service
@Slf4j
public class StrategyArmoryDispatch implements IStrategyArmory, IStrategyDispatch {

    @Resource
    private IStrategyRepository strategyRepository;

    /**
     * 装配抽奖策略
     * 空间换时间策略：通过预生成的查找表快速查找中奖结果，适合总概率值不大的情况，因为存储查找表所需空间较小。
     *
     * @param strategyId 抽奖策略ID
     */
    @Override
    public Boolean assembleLotteryStrategy(Long strategyId) {
        // 1.根据抽奖策略ID查询当前抽奖策略下的所有奖品信息
        List<StrategyAwardEntity> strategyAwardEntityList = strategyRepository.queryStrategyAwardList(strategyId);
        log.info("策略装配库-装配抽奖策略-查询到奖品信息:{}", JSON.toJSONString(strategyAwardEntityList));
        if (CollectionUtils.isEmpty(strategyAwardEntityList)) {
            return Boolean.TRUE;
        }

        // 2.装配抽奖奖品策略 - 装配查找表
        this.assembleLotteryStrategy(String.valueOf(strategyId),strategyAwardEntityList);

        // 3.权重策略配置 - 试用于 rule_weight 权重规则配置
        StrategyEntity strategyEntity = strategyRepository.queryStrategyEntityByStrategyId(strategyId);
        log.info("策略装配库-装配抽奖策略-查询到抽奖策略信息:{}",strategyEntity);
        String ruleWeight = strategyEntity.getRuleWeight();
        log.info("策略装配库-装配抽奖策略-查询到权重规则配置:{}",ruleWeight);
        if (Objects.isNull(ruleWeight)) return Boolean.TRUE;

        StrategyRuleEntity strategyRuleEntity = strategyRepository.queryStrategyRule(strategyId,ruleWeight);
        log.info("策略装配库-装配抽奖策略-查询到抽奖策略规则配置:{}",strategyRuleEntity);
        if (Objects.isNull(strategyRuleEntity)) {
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(),ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }

        Map<String, List<Long>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightValues();
        log.info("策略装配库-装配抽奖策略-权重规则Map:{}",ruleWeightValueMap);
        Set<String> keys = ruleWeightValueMap.keySet();
        for (String key : keys) {
            List<Long> ruleWeightValues = ruleWeightValueMap.get(key);
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntityList);
            strategyAwardEntitiesClone.removeIf(entity -> !ruleWeightValues.contains(entity.getAwardId()));
            log.info("策略装配库-装配抽奖策略-策略ID:{} 权重值:{} 匹配到奖品ID:{}",strategyId,key,strategyAwardEntitiesClone.stream().map(StrategyAwardEntity::getAwardId).collect(Collectors.toList()));
            this.assembleLotteryStrategy(String.valueOf(strategyId).concat("_").concat(key),strategyAwardEntitiesClone);
        }

        return Boolean.TRUE;
    }

    private void assembleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntityList) {
        // 1.获取最小概率值和概率值总和。这里也可以使用stream流的min()和reduce()方法来计算
        BigDecimal minAwardRate = null;// 或者使用 new BigDecimal(Double.MAX_VALUE);
        BigDecimal totalAwardRate = BigDecimal.ZERO;
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntityList) {
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();
            if (Objects.isNull(minAwardRate) || awardRate.compareTo(minAwardRate) < 0) {
                minAwardRate = awardRate;
            }
            totalAwardRate = totalAwardRate.add(awardRate);
        }
        log.info("minAwardRate:{}", minAwardRate);
        log.info("totalAwardRate:{}", totalAwardRate);

        // 2. 计算一个概率范围
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);
        log.info("rateRange:{}", rateRange);

        // 3.生成策略奖品概率查找表「这里指需要在list集合中，存放上对应的奖品占位即可，占位越多等于概率越高」
        List<Long> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntityList) {
            Long awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();
            // 计算出每个概率值需要存放到查找表的数量，循环填充
            for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }
        log.info("size:{},strategyAwardSearchRateTables:{}", strategyAwardSearchRateTables.size(), JSON.toJSONString(strategyAwardSearchRateTables));

        // 4.对存储的奖品进行乱序操作
        Collections.shuffle(strategyAwardSearchRateTables);
        log.info("shuffle----strategyAwardSearchRateTables:{}", JSON.toJSONString(strategyAwardSearchRateTables));

        // 5.生成出Map集合，key值，对应的就是后续的概率值。通过概率来获得对应的奖品ID
        Map<Integer, Long> shuffleStrategyAwardSearchRateTable = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffleStrategyAwardSearchRateTable.put(i, strategyAwardSearchRateTables.get(i));
        }
        log.info("shuffleStrategyAwardSearchRateTable:{}", JSON.toJSONString(shuffleStrategyAwardSearchRateTable));

        // 6.存入redis中
        strategyRepository.storeStrategyAwardSearchRateTable(key, new BigDecimal(shuffleStrategyAwardSearchRateTable.size()), shuffleStrategyAwardSearchRateTable);
    }

    @Override
    public Long getRandomArard(Long strategyId) {
        Integer rateRange = strategyRepository.getRateRange(strategyId);
        log.info("rateRange:{}", rateRange);
        int random = new SecureRandom().nextInt(rateRange);
        log.info("random:{}", random);
        return strategyRepository.getStrategyAwardAssemble(String.valueOf(strategyId), random);
    }

    @Override
    public Long getRandomArard(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
        log.info("抽奖策略规则装配key:{}",key);
        Integer rateRange = strategyRepository.getRateRange(key);
        log.info("rateRange:{}", rateRange);
        int random = new SecureRandom().nextInt(rateRange);
        log.info("random:{}", random);
        return strategyRepository.getStrategyAwardAssemble(key, random);
    }

    /**
     * 装配抽奖策略的另一种实现方式：时间换空间
     * 时间换空间策略：在抽奖时通过计算随机值与概率范围进行对比，适合总概率值很大的情况，因为存储查找表所需空间太大。
     *
     * @param strategyId 抽奖策略ID
     */
    @Override
    public Boolean assemblyLotteryStrategyAlternative(Long strategyId) {
        // 1.根据抽奖策略ID查询当前抽奖策略下的所有奖品信息
        List<StrategyAwardEntity> strategyAwardEntityList = strategyRepository.queryStrategyAwardList(strategyId);
        log.info("策略装配库-装配抽奖策略-查询到奖品信息:{}", JSON.toJSONString(strategyAwardEntityList));
        if (CollectionUtils.isEmpty(strategyAwardEntityList)) {
            return Boolean.FALSE;
        }

        // 2.获取总概率值
        BigDecimal totalAwardRate = BigDecimal.ZERO;
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntityList) {
            totalAwardRate = totalAwardRate.add(strategyAwardEntity.getAwardRate());
        }
        log.info("totalAwardRate:{}", totalAwardRate);

        // 3.计算每个奖品的概率值
        BigDecimal cumulativeRate = BigDecimal.ZERO;
        Map<Long, BigDecimal> awardCumulativeRateMap = new LinkedHashMap<>();
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntityList) {
            cumulativeRate = cumulativeRate.add(strategyAwardEntity.getAwardRate());
            awardCumulativeRateMap.put(strategyAwardEntity.getAwardId(), cumulativeRate);
        }
        log.info("awardCumulativeRateMap:{}", JSON.toJSONString(awardCumulativeRateMap));

        // 4. 存入redis中
        strategyRepository.storeCumulativeRateMap(strategyId, totalAwardRate, awardCumulativeRateMap);

        return Boolean.TRUE;
    }


    /**
     * 针对于累加概率范围的抽奖
     *
     * @param strategyId 抽奖策略ID
     * @return
     */
    public Long getRandomAwardAlternative(Long strategyId) {
        // 1.获取总概率值
        BigDecimal totalAwardRate = strategyRepository.getTotalAwardRate(strategyId);

        // 2.获取每个奖品的概率值
        Map<Long, BigDecimal> awardCumulativeRateMap = strategyRepository.getCumulativeRateMap(strategyId);
        log.info("awardCumulativeRateMap:{}", JSON.toJSONString(awardCumulativeRateMap));
        if (awardCumulativeRateMap == null) {
            throw new IllegalArgumentException("Invalid strategyId: " + strategyId);
        }


        // 3.生成随机概率
        BigDecimal randomRate = totalAwardRate.multiply(BigDecimal.valueOf(new SecureRandom().nextDouble()));
        log.info("randomRate:{}", randomRate);

        // 4.根据随机概率匹配奖品
        for (Map.Entry<Long, BigDecimal> entry : awardCumulativeRateMap.entrySet()) {
            if (randomRate.compareTo(entry.getValue()) <= 0) {
                return entry.getKey();
            }
        }
        return null; // 如果没有匹配上，返回null
    }
}
