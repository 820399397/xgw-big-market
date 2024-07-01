package cn.xuguowen.test.infrastructure;

import cn.xuguowen.infrastructure.persistent.dao.IAwardDao;
import cn.xuguowen.infrastructure.persistent.po.Award;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName: TestAwardDao
 * Package: cn.xuguowen.test.infrastructure
 * Description:奖品持久化层测试
 *
 * @Author 徐国文
 * @Create 2024/7/1 11:10
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestAwardDao {

    @Resource
    private IAwardDao awardDao;

    @Test
    public void queryAwardList_test() {
        List<Award> awards = awardDao.queryAwardList();
        log.info("awards:{}", JSON.toJSONString(awards));
    }
}
