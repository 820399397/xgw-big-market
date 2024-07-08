package cn.xuguowen.domain.strategy.model.entity;

import lombok.Builder;
import lombok.Data;

/**
 * ClassName: RaffleAwardEntity
 * Package: cn.xuguowen.domain.strategy.model.entity
 * Description:抽奖奖品实体
 *
 * @Author 徐国文
 * @Create 2024/7/5 11:48
 * @Version 1.0
 */
@Data
@Builder
public class RaffleAwardEntity {

    /** 抽奖策略ID */
    private Long strategyId;
    /** 奖品ID */
    private Long awardId;
    /** 奖品对接标识 - 每一个都是一个对应的发奖策略 */
    private String awardKey;
    /** 奖品配置信息 */
    private String awardConfig;
    /** 奖品内容描述 */
    private String awardDesc;

}
