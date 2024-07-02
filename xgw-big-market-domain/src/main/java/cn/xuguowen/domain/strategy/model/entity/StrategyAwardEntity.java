package cn.xuguowen.domain.strategy.model.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * ClassName: StrategyAwardEntity
 * Package: cn.xuguowen.domain.strategy.model.entity
 * Description:抽奖策略奖品实体类
 *
 * @Author 徐国文
 * @Create 2024/7/2 13:28
 * @Version 1.0
 */
@Data
@Builder
public class StrategyAwardEntity {
    /** 抽奖策略ID */
    private Long strategyId;
    /** 抽奖奖品ID */
    private Long awardId;
    /** 奖品库存总量 */
    private Integer awardCount;
    /** 奖品库存剩余量 */
    private Integer awardCountSurplus;
    /** 奖品中奖概率 */
    private BigDecimal awardRate;
}
