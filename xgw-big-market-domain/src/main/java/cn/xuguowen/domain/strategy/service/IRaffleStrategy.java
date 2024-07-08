package cn.xuguowen.domain.strategy.service;

import cn.xuguowen.domain.strategy.model.entity.RaffleAwardEntity;
import cn.xuguowen.domain.strategy.model.entity.RaffleFactorEntity;

/**
 * ClassName: IRaffleStrategy
 * Package: cn.xuguowen.domain.strategy.service
 * Description:抽奖策略接口
 *
 * @Author 徐国文
 * @Create 2024/7/5 11:45
 * @Version 1.0
 */
public interface IRaffleStrategy {

    /**
     * 执行抽奖。用抽奖因子入参，执行抽奖计算，返回奖品信息
     * @param raffleFactorEntity 抽奖因子实体对象，根据入参信息计算抽奖结果
     * @return
     */
    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);
}
