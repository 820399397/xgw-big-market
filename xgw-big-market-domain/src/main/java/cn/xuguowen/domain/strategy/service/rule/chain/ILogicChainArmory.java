package cn.xuguowen.domain.strategy.service.rule.chain;

/**
 * ClassName: ILogicChainArmory
 * Package: cn.xuguowen.domain.strategy.service.rule.chain
 * Description:责任链装配
 *
 * @Author 徐国文
 * @Create 2024/7/12 12:22
 * @Version 1.0
 */
public interface ILogicChainArmory {

    ILogicChain next();

    ILogicChain appendNext(ILogicChain next);

}
