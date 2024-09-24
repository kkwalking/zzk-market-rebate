package top.kelton.test.infrastructure.persistent;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.kelton.infrastructure.persistent.dao.IStrategyRuleDao;
import top.kelton.infrastructure.persistent.po.StrategyRulePO;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyRuleDaoTest {

    @Resource
    private IStrategyRuleDao strategyRuleDao;

    @Test
    public void test_queryAwardList() {
        List<StrategyRulePO> awardPOS = strategyRuleDao.queryStrategyRuleList();
        awardPOS.stream().map(JSON::toJSONString).forEach(System.out::println);
    }
}
