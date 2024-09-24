package top.kelton.test;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.kelton.infrastructure.persistent.po.UserPO;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Test
    public void test() {
        UserPO userPO = new UserPO();
        userPO.setId(1);
        userPO.setUserId("zzk01");
        userPO.setUserName("zzk");
        userPO.setAge(18);
        log.info("userPO: {}", JSON.toJSONString(userPO));
        User user = new User();
        user.setUserName(userPO.getUserName());
        user.setAge(String.valueOf(userPO.getAge()));
        log.info("user: {}", JSON.toJSONString(user));

    }

}
