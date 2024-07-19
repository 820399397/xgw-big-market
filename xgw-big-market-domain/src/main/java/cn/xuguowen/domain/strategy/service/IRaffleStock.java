package cn.xuguowen.domain.strategy.service;

import cn.xuguowen.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

/**
 * ClassName: IRaffleStock
 * Package: cn.xuguowen.domain.strategy.service
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/7/19 14:42
 * @Version 1.0
 */
public interface IRaffleStock {

    /**
     * 获取奖品库存消息队列
     *
     * @return 奖品库存Key信息
     * @throws InterruptedException 异常
     */
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 更新奖品库存消耗记录
     *
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     */
    void updateStrategyAwardStock(Long strategyId, Long awardId);

}
