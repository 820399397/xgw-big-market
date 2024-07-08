package cn.xuguowen.test.domain;

import cn.xuguowen.domain.strategy.model.entity.RaffleAwardEntity;
import cn.xuguowen.domain.strategy.model.entity.RaffleFactorEntity;
import cn.xuguowen.domain.strategy.service.IRaffleStrategy;
import cn.xuguowen.domain.strategy.service.armory.IStrategyArmory;
import cn.xuguowen.domain.strategy.service.rule.impl.RuleLockLogicFilter;
import cn.xuguowen.domain.strategy.service.rule.impl.RuleWeightLogicFilter;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;

/**
 * ClassName: RaffleStrategyTest
 * Package: cn.xuguowen.test.domain
 * Description:抽奖策略规则校验测试
 *
 * @Author 徐国文
 * @Create 2024/7/5 12:19
 * @Version 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleStrategyTest {

    @Resource
    private IRaffleStrategy raffleStrategy;

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private RuleWeightLogicFilter ruleWeightLogicFilter;

    @Resource
    private RuleLockLogicFilter ruleLockLogicFilter;

    @Before
    public void setUp() {
        Boolean flag = strategyArmory.assembleLotteryStrategy(100003L);
        log.info("装配结果-{}", flag);

        // 通过反射的方式设置RuleWeightLogicFilter类中的userScore属性的值
        ReflectionTestUtils.setField(ruleWeightLogicFilter, "userScore", 4050L);

        // 通过反射的方式设置RuleLockLogicFilter类中的userRaffleCount属性的值
        ReflectionTestUtils.setField(ruleLockLogicFilter, "userRaffleCount", 0L);
    }

    /**
     * 抽奖次数校验，抽奖n次后解锁。100003 策略，你可以通过调整 @Before 的 setUp 方法中个人抽奖次数来验证。比如最开始设置0，之后设置10
     * ReflectionTestUtils.setField(ruleLockLogicFilter, "userRaffleCount", 10L);
     */
    @Test
    public void test_performRaffle_ruleLock(){
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("xgw")
                .strategyId(100003L)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_performRaffle() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("xgw")
                .strategyId(100001L)
                .build();

        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_performRaffle_blacklist() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("user003")  // 黑名单用户 user001,user002,user003
                .strategyId(100001L)
                .build();

        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }


}
