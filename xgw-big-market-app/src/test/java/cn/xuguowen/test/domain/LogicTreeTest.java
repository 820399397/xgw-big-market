package cn.xuguowen.test.domain;

import cn.xuguowen.domain.strategy.model.valobj.RuleLimitTypeVO;
import cn.xuguowen.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import cn.xuguowen.domain.strategy.model.valobj.RuleTreeNodeLineVO;
import cn.xuguowen.domain.strategy.model.valobj.RuleTreeNodeVO;
import cn.xuguowen.domain.strategy.model.valobj.RuleTreeVO;
import cn.xuguowen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import cn.xuguowen.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ClassName: LogicTreeTest
 * Package: cn.xuguowen.test.domain
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/7/15 15:50
 * @Version 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LogicTreeTest {

    @Resource
    private DefaultTreeFactory defaultTreeFactory;

    /**
     * rule_lock 放行、rule_stock 拦截、rule_luck_award 放行，获得最后的结果。
     * 说白了就是：用户抽取到的奖品是 需要 抽取N次后解锁的奖品信息。此时需要走抽奖中校验。
     *           然后走到库存树节点，库存树节点接管。所以要继续走到下一个节点上：兜底奖品树节点
     */
    @Test
    public void test_tree_rule() {
        // 构建参数
        RuleTreeNodeVO rule_lock = RuleTreeNodeVO.builder()
                .treeId("100000001")
                .ruleKey("rule_lock")
                .ruleDesc("限定用户已完成N次抽奖后解锁")
                .ruleValue("1")
                .treeNodeLineVOList(new ArrayList<RuleTreeNodeLineVO>() {{
                    add(RuleTreeNodeLineVO.builder()
                            .treeId("100000001")
                            .ruleNodeFrom("rule_lock")
                            .ruleNodeTo("rule_luck_award")
                            .ruleLimitTypeVO(RuleLimitTypeVO.EQUAL)
                            .ruleLogicTypeVO(RuleLogicCheckTypeVO.TAKE_OVER)
                            .build());

                    add(RuleTreeNodeLineVO.builder()
                            .treeId("100000001")
                            .ruleNodeFrom("rule_lock")
                            .ruleNodeTo("rule_stock")
                            .ruleLimitTypeVO(RuleLimitTypeVO.EQUAL)
                            .ruleLogicTypeVO(RuleLogicCheckTypeVO.ALLOW)
                            .build());
                }})
                .build();

        RuleTreeNodeVO rule_luck_award = RuleTreeNodeVO.builder()
                .treeId("100000001")
                .ruleKey("rule_luck_award")
                .ruleDesc("限定用户已完成N次抽奖后解锁")
                .ruleValue("1")
                .treeNodeLineVOList(null)
                .build();

        RuleTreeNodeVO rule_stock = RuleTreeNodeVO.builder()
                .treeId("100000001")
                .ruleKey("rule_stock")
                .ruleDesc("库存处理规则")
                .ruleValue(null)
                .treeNodeLineVOList(new ArrayList<RuleTreeNodeLineVO>() {{
                    add(RuleTreeNodeLineVO.builder()
                            .treeId("100000001")
                            .ruleNodeFrom("rule_lock")
                            .ruleNodeTo("rule_luck_award")
                            .ruleLimitTypeVO(RuleLimitTypeVO.EQUAL)
                            .ruleLogicTypeVO(RuleLogicCheckTypeVO.TAKE_OVER)
                            .build());
                }})
                .build();

        RuleTreeVO ruleTreeVO = new RuleTreeVO();
        ruleTreeVO.setTreeId("100000001");
        ruleTreeVO.setTreeName("决策树规则；增加dall-e-3画图模型");
        ruleTreeVO.setTreeDesc("决策树规则；增加dall-e-3画图模型");
        ruleTreeVO.setTreeRootRuleNode("rule_lock");

        ruleTreeVO.setTreeNodeMap(new HashMap<String, RuleTreeNodeVO>() {{
            put("rule_lock", rule_lock);
            put("rule_stock", rule_stock);
            put("rule_luck_award", rule_luck_award);
        }});

        IDecisionTreeEngine treeComposite = defaultTreeFactory.openLogicTree(ruleTreeVO);

        DefaultTreeFactory.StrategyAwardVO strategyAwardVO = treeComposite.process("xgw", 100001L, 100L);
        log.info("测试结果：{}", JSON.toJSONString(strategyAwardVO));

    }
}
