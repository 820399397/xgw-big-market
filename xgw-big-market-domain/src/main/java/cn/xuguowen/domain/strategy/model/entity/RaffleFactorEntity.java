package cn.xuguowen.domain.strategy.model.entity;

import lombok.Builder;
import lombok.Data;

/**
 * ClassName: RaffleFactorEntity
 * Package: cn.xuguowen.domain.strategy.model.entity
 * Description:抽奖因子实体
 *
 * @Author 徐国文
 * @Create 2024/7/5 11:46
 * @Version 1.0
 */
@Data
@Builder
public class RaffleFactorEntity {

    // 用户ID
    private String userId;

    // 抽奖策略ID
    private Long strategyId;
}
