package cn.xuguowen.test.domain;

import cn.xuguowen.domain.strategy.service.armory.IStrategyArmory;
import cn.xuguowen.domain.strategy.service.armory.IStrategyDispatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * ClassName: StrategyArmoryTest
 * Package: cn.xuguowen.test.domain
 * Description:策略装配库(兵工厂)测试
 *
 * @Author 徐国文
 * @Create 2024/7/2 13:40
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class StrategyArmoryTest {
    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IStrategyDispatch strategyDispatch;

    @Before
    public void before() {
        Boolean flag = strategyArmory.assembleLotteryStrategy(100001L);
        log.info("装配结果-{}", flag);
    }

    @Test
    public void test_getRandomArard() {
        log.info("测试结果-{}-奖品ID值", strategyDispatch.getRandomArard(100001L));
        log.info("测试结果-{}-奖品ID值", strategyDispatch.getRandomArard(100001L));
        log.info("测试结果-{}-奖品ID值", strategyDispatch.getRandomArard(100001L));
    }

    /**
     * 测试权重抽奖
     */
    @Test
    public void test_getRandomArard_RuleWeight() {
        log.info("测试权重抽奖结果-{}-奖品ID值", strategyDispatch.getRandomArard(100001L,"4000:102,103,104,105"));
        log.info("测试权重抽奖结果-{}-奖品ID值", strategyDispatch.getRandomArard(100001L,"5000:102,103,104,105,106,107"));
        log.info("测试权重抽奖结果-{}-奖品ID值", strategyDispatch.getRandomArard(100001L,"6000:102,103,104,105,106,107,108,109"));
    }



    @Test
    public void test_assemblyLotteryStrategyAlternative() {
        strategyArmory.assemblyLotteryStrategyAlternative(100003L);
    }

    @Test
    public void test_getRandomAwardAlternative() {
        log.info("测试结果-{}-奖品ID值", strategyDispatch.getRandomAwardAlternative(100003L));
        log.info("测试结果-{}-奖品ID值", strategyDispatch.getRandomAwardAlternative(100003L));
        log.info("测试结果-{}-奖品ID值", strategyDispatch.getRandomAwardAlternative(100003L));
        log.info("测试结果-{}-奖品ID值", strategyDispatch.getRandomAwardAlternative(100003L));
        log.info("测试结果-{}-奖品ID值", strategyDispatch.getRandomAwardAlternative(100003L));
    }
}
