package cn.xuguowen.test.infrastructure;

import cn.xuguowen.infrastructure.persistent.dao.IRuleTreeDao;
import cn.xuguowen.infrastructure.persistent.dao.IRuleTreeNodeDao;
import cn.xuguowen.infrastructure.persistent.dao.IRuleTreeNodeLineDao;
import cn.xuguowen.infrastructure.persistent.po.RuleTree;
import cn.xuguowen.infrastructure.persistent.po.RuleTreeNode;
import cn.xuguowen.infrastructure.persistent.po.RuleTreeNodeLine;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName: TestRuleTreeDao
 * Package: cn.xuguowen.test.infrastructure
 * Description:规则树相关的测试
 *
 * @Author 徐国文
 * @Create 2024/7/16 10:56
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestRuleTreeDao {

    @Resource
    private IRuleTreeDao ruleTreeDao;

    @Resource
    private IRuleTreeNodeDao ruleTreeNodeDao;

    @Resource
    private IRuleTreeNodeLineDao ruleTreeNodeLineDao;

    @Test
    public void test_queryRuleTreeByTreeId() {
        RuleTree treeLock = ruleTreeDao.queryRuleTreeByTreeId("tree_lock");
        log.info("treeLock:{}", JSON.toJSONString(treeLock));
    }

    @Test
    public void test_queryRuleTreeNodeByTreeId() {
        List<RuleTreeNode> ruleTreeNodeList = ruleTreeNodeDao.queryRuleTreeNodeByTreeId("tree_lock");
        log.info("ruleTreeNodeList:{}", JSON.toJSONString(ruleTreeNodeList));
    }

    @Test
    public void name() {
        List<RuleTreeNodeLine> ruleTreeNodeLineList = ruleTreeNodeLineDao.queryRuleTreeNodeLineByTreeId("tree_lock");
        log.info("ruleTreeNodeLineList:{}", JSON.toJSONString(ruleTreeNodeLineList));
    }
}
