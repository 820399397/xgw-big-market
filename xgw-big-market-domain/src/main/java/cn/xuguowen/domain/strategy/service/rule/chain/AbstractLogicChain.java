package cn.xuguowen.domain.strategy.service.rule.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName: AbstructLogicChain
 * Package: cn.xuguowen.domain.strategy.service.rule.chain
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/7/12 10:26
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractLogicChain implements ILogicChain {

    private ILogicChain next;

    @Override
    public ILogicChain next() {
        return next;
    }

    @Override
    public ILogicChain appendNext(ILogicChain next) {
        this.next = next;
        return next;
    }

    protected abstract String ruleModel();
}
