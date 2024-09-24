package top.kelton.test.infrastructure.persistent;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.kelton.infrastructure.persistent.dao.IStrategyDao;
import top.kelton.infrastructure.persistent.po.StrategyPO;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyDaoTest {

    @Resource
    private IStrategyDao strategyDao;

    @Test
    public void test_queryAwardList() {
        List<StrategyPO> awardPOS = strategyDao.queryStrategyList();
        awardPOS.stream().map(JSON::toJSONString).forEach(System.out::println);
    }
}
