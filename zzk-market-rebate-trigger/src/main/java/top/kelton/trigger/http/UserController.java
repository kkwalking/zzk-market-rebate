package top.kelton.trigger.http;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.kelton.api.dto.SaveUserRequestDTO;
import top.kelton.domain.user.service.UserService;
import top.kelton.types.common.Constants;
import top.kelton.types.model.Response;

import javax.annotation.Resource;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {
    @Resource
    private UserService userService;

    /**
     * http://localhost:8090/success
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Response<String> save(@RequestBody SaveUserRequestDTO requestDTO) {
        log.info("测试调用");
        try {
            userService.saveUser(requestDTO.getName(), requestDTO.getAge());
            Response<String> response = Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data("success")
                    .build();
            log.info("保存用户成功:{}", JSON.toJSONString(response));
            return response;
        } catch (Exception e) {
            Response<String> response = Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .data("fail")
                    .build();
            log.info("保存用户失败:{}", JSON.toJSONString(response));
            return response;
        }
    }

}
