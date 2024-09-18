package top.kelton.test.infrastructure.persistent;

import top.kelton.infrastructure.persistent.dao.IUserOrderDao;
import top.kelton.infrastructure.persistent.po.UserOrderPO;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 单元测试
 * @author  zzk
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserOrderTest {

    @Resource
    private IUserOrderDao userOrderDao;

    @Test
    public void test_selectByUserId() {
        List<UserOrderPO> list = userOrderDao.selectByUserId("xfg_FOawiP");
        log.info("测试结果：{}", JSON.toJSONString(list));
    }

    @Test
    public void test_insert() {
        for (int i = 0; i < 10; i++) {
            UserOrderPO userOrderPO = UserOrderPO.builder()
                    .userName("zzk")
                    .userId("zzk_" + RandomStringUtils.randomAlphabetic(6))
                    .userMobile("158*******")
                    .sku("13811216")
                    .skuName("iPhone 16")
                    .orderId(RandomStringUtils.randomNumeric(11))
                    .quantity(1)
                    .unitPrice(BigDecimal.valueOf(128))
                    .discountAmount(BigDecimal.valueOf(50))
                    .tax(BigDecimal.ZERO)
                    .totalAmount(BigDecimal.valueOf(78))
                    .orderDate(new Date())
                    .orderStatus(0)
                    .isDelete(0)
                    .uuid(UUID.randomUUID().toString().replace("-", ""))
                    .build();

            userOrderDao.insert(userOrderPO);
        }
    }

    /**
     * 路由测试
     */
    @Test
    public void test_idx() {
        for (int i = 0; i < 50; i++) {
            String user_id = "zzk_" + RandomStringUtils.randomAlphabetic(6);
            log.info("测试结果 {}", (user_id.hashCode() ^ (user_id.hashCode()) >>> 16) & 3);
        }
    }

}
