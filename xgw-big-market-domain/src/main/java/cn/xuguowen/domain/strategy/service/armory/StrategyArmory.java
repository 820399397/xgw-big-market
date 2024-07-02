package cn.xuguowen.domain.strategy.service.armory;

import cn.xuguowen.domain.strategy.model.entity.StrategyAwardEntity;
import cn.xuguowen.domain.strategy.repository.IStrategyRepository;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

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
public class StrategyArmory implements IStrategyArmory{

    @Resource
    private IStrategyRepository strategyRepository;


    /**
     * 装配抽奖策略
     * @param strategyId 抽奖策略ID
     */
    @Override
    public void assembleLotteryStrategy(Long strategyId) {
        // 1.根据抽奖策略ID查询当前抽奖策略下的所有奖品信息
        List<StrategyAwardEntity> strategyAwardEntityList = strategyRepository.queryStrategyAwardList(strategyId);
        log.info("策略装配库-装配抽奖策略-查询到奖品信息:{}", JSON.toJSONString(strategyAwardEntityList));
        if (CollectionUtils.isEmpty(strategyAwardEntityList)) {
            return;
        }

        // 2.获取最小概率值和概率值总和。这里也可以使用stream流的min()和reduce()方法来计算
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

        // 3. 计算一个概率范围
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);
        log.info("rateRange:{}", rateRange);

        // 4.生成策略奖品概率查找表「这里指需要在list集合中，存放上对应的奖品占位即可，占位越多等于概率越高」
        List<Long> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntityList) {
            Long awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();
            // 计算出每个概率值需要存放到查找表的数量，循环填充
            for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }
        log.info("size:{},strategyAwardSearchRateTables:{}", strategyAwardSearchRateTables.size(),JSON.toJSONString(strategyAwardSearchRateTables));

        // 5.对存储的奖品进行乱序操作
        Collections.shuffle(strategyAwardSearchRateTables);
        log.info("shuffle----strategyAwardSearchRateTables:{}", JSON.toJSONString(strategyAwardSearchRateTables));

        // 6.生成出Map集合，key值，对应的就是后续的概率值。通过概率来获得对应的奖品ID
        Map<Integer, Long> shuffleStrategyAwardSearchRateTable = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffleStrategyAwardSearchRateTable.put(i, strategyAwardSearchRateTables.get(i));
        }
        log.info("shuffleStrategyAwardSearchRateTable:{}",JSON.toJSONString(shuffleStrategyAwardSearchRateTable));

        // 7.存入redis中
        strategyRepository.storeStrategyAwardSearchRateTable(strategyId,new BigDecimal(shuffleStrategyAwardSearchRateTable.size()),shuffleStrategyAwardSearchRateTable);

    }

    @Override
    public Long getRandomArard(Long strategyId) {
        Integer rateRange = strategyRepository.getRateRange(strategyId);
        log.info("rateRange:{}",rateRange);
        int random = new SecureRandom().nextInt(rateRange);
        log.info("random:{}",random);
        return strategyRepository.getStrategyAwardAssemble(strategyId,random);
    }
}
