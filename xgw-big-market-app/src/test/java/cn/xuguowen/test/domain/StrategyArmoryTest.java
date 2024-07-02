package cn.xuguowen.test.domain;

import cn.xuguowen.domain.strategy.service.armory.IStrategyArmory;
import lombok.extern.slf4j.Slf4j;
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

    @Test
    public void test_assembleLotteryStrategy() {
        strategyArmory.assembleLotteryStrategy(100002L);
    }

    @Test
    public void test_getRandomArard() {
        log.info("测试结果-{}-奖品ID值",strategyArmory.getRandomArard(100002L));
        log.info("测试结果-{}-奖品ID值",strategyArmory.getRandomArard(100002L));
        log.info("测试结果-{}-奖品ID值",strategyArmory.getRandomArard(100002L));
    }
}
