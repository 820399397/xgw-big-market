package cn.xuguowen.test.infrastructure;

import cn.xuguowen.domain.strategy.model.valobj.RuleTreeVO;
import cn.xuguowen.domain.strategy.repository.IStrategyRepository;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * ClassName: TestStrategyRepository
 * Package: cn.xuguowen.test.infrastructure
 * Description:抽奖策略仓储层测试
 *
 * @Author 徐国文
 * @Create 2024/7/16 11:06
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestStrategyRepository {
    
    @Resource
    private IStrategyRepository strategyRepository;

    @Test
    public void test_queryRuleTreeVOByTreeId() {
        RuleTreeVO ruleTreeVO = strategyRepository.queryRuleTreeVOByTreeId("tree_lock");
        /**
         * 默认情况下，JSON.toJSONString() 方法在序列化对象时会忽略 null 值的字段。
         * 这意味着如果 RuleTreeNodeVO 对象的 treeNodeLineVOList 属性为 null，这个属性不会出现在序列化后的 JSON 字符串中。
         * 你可以通过配置 JSON.toJSONString() 的序列化特性来控制这种行为。以下是一些示例代码，展示了如何在序列化时包含 null 值：
         * String jsonString = JSON.toJSONString(ruleTreeVO, SerializerFeature.WriteMapNullValue);
         */
        log.info("ruleTreeVO:{}", JSON.toJSONString(ruleTreeVO, JSONWriter.Feature.WriteMapNullValue));
    }
}
