package cn.xuguowen.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: StrategyAwardStockKeyVO
 * Package: cn.xuguowen.domain.strategy.model.valobj
 * Description:扣减库存值对象
 *
 * @Author 徐国文
 * @Create 2024/7/19 14:33
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StrategyAwardStockKeyVO {

    private Long strategyId;

    private Long awardId;

}
