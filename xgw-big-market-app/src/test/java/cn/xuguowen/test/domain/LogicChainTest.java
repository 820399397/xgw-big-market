package cn.xuguowen.test.domain;

import cn.xuguowen.domain.strategy.service.armory.IStrategyArmory;
import cn.xuguowen.domain.strategy.service.rule.chain.ILogicChain;
import cn.xuguowen.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import cn.xuguowen.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;

/**
 * ClassName: LogicChainTest
 * Package: cn.xuguowen.test.domain
 * Description:抽奖责任链测试，验证不同的规则走不同的责任链
 *
 * @Author 徐国文
 * @Create 2024/7/12 12:26
 * @Version 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LogicChainTest {

    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private RuleWeightLogicChain ruleWeightLogicChain;
    @Resource
    private DefaultChainFactory defaultChainFactory;

    @Before
    public void setUp() {
        // 策略装配 100001、100002、100003
        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100001L));
        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100002L));
        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100003L));
    }

    @Test
    public void test_LogicChain_rule_blacklist() {
        ILogicChain logicChain = defaultChainFactory.openLogicChain(100001L);
        Long awardId = logicChain.logic("user001", 100001L);
        log.info("测试结果：{}", awardId);
    }

    @Test
    public void test_LogicChain_rule_weight() {
        // 通过反射 mock 规则中的值
        ReflectionTestUtils.setField(ruleWeightLogicChain, "userScore", 4900L);

        ILogicChain logicChain = defaultChainFactory.openLogicChain(100003L);
        Long awardId = logicChain.logic("xiaofuge", 100003L);
        log.info("测试结果：{}", awardId);
    }

    @Test
    public void test_LogicChain_rule_default() {
        ILogicChain logicChain = defaultChainFactory.openLogicChain(100001L);
        Long awardId = logicChain.logic("xiaofuge", 100001L);
        log.info("测试结果：{}", awardId);
    }


}
