package cn.xuguowen.test.infrastructure;

import cn.xuguowen.infrastructure.persistent.dao.IStrategyRuleDao;
import cn.xuguowen.infrastructure.persistent.po.StrategyRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName: StrategyRuleDaoTest
 * Package: cn.xuguowen.test.infrastructure
 * Description:抽奖规则持久化层测试
 *
 * @Author 徐国文
 * @Create 2024/7/1 11:14
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestStrategyRuleDao {

    @Resource
    private IStrategyRuleDao strategyRuleDao;

    @Test
    public void queryStrategyRuleList_test() {
        List<StrategyRule> strategyRules = strategyRuleDao.queryStrategyRuleList();
        log.info("strategyRules:{}", strategyRules);
    }
}
