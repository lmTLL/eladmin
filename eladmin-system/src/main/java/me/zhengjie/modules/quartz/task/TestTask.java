package me.zhengjie.modules.quartz.task;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.system.domain.Token;
import me.zhengjie.modules.system.repository.AsinInfoRepository;
import me.zhengjie.modules.system.repository.TokenRepository;
import me.zhengjie.modules.system.rest.WechatController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 测试用
 *
 * @author Zheng Jie
 * @date 2019-01-08
 */
@Slf4j
@Component
public class TestTask {
    @Autowired
    private TokenRepository tokenRepository;

    public void run() throws Exception {
        String token1 = WechatController.saveAccess_token();
        tokenRepository.upload(token1);
        log.info("执行成功");
    }

    public void run1(String str) {
        log.info("执行成功，参数为： {}" + str);
    }
}
