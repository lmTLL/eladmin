package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.PrizesNum;
import me.zhengjie.modules.system.domain.PrizesUser;
import me.zhengjie.modules.system.repository.PrizesNumRepository;
import me.zhengjie.modules.system.repository.PrizesUserRepository;
import me.zhengjie.modules.system.service.PrizesUserService;
import me.zhengjie.modules.system.service.dto.PrizesUserQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author groot
 * @date 2019-10-23
 */
@RestController
@RequestMapping("api")
public class PrizesUserController {

    @Autowired
    private PrizesUserService prizesUserService;
    @Autowired
    private PrizesUserRepository prizesUserRepository;
    @Autowired
    private PrizesNumRepository  prizesNumRepository;
    @Autowired
    private WechatController wechatController;

    @Log("查询PrizesUser")
    @GetMapping(value = "/prizesUser")
    @PreAuthorize("hasAnyRole('ADMIN','PRIZESUSER_ALL','PRIZESUSER_SELECT')")
    public ResponseEntity getPrizesUsers(PrizesUserQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(prizesUserService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/prizesUser/updateNum")
    public Long getNum(String openId) {
        Long num = prizesUserRepository.getNum(openId);
        if (num>0){
            prizesUserRepository.updateNum(openId,num-1);
        }
        return num;
    }


    @GetMapping(value = "/prizesUser/addNum")
    public Long addNum(String openId,String prizesNum) throws Exception {
        PrizesNum byPrizesNum = prizesNumRepository.findByPrizesNum(prizesNum);
        Long num = prizesUserRepository.getNum(openId);
        PrizesUser byOpenId = prizesUserRepository.findByOpenId(openId);
        Map<String, Object> userInfoByOpenid = wechatController.getUserInfoByOpenid(openId);
        if (byPrizesNum!=null){
            //System.out.println(byOpenId.getNickname().startsWith("已使用抽奖码-"));
            if(byOpenId.getNickname().startsWith("已使用抽奖码-")){
                return -1L;
            }
            if (byPrizesNum.getNum()!= 0L){
                try {
                    byOpenId.setNickname(userInfoByOpenid.get("nickname").toString());
                    byOpenId.setHeadimgurl(userInfoByOpenid.get("headimgurl").toString());
                }catch (NullPointerException e){

                }
                prizesNumRepository.updateNum(byPrizesNum.getId(),0L);
                prizesUserRepository.updateNum(openId,5L);
                byOpenId.setNum(5L);
                try {
                    byOpenId.setNickname("已使用抽奖码-"+userInfoByOpenid.get("nickname").toString());
                }catch (Exception e){
                    byOpenId.setNickname("已使用抽奖码-"+byOpenId.getNickname());
                }
                prizesUserService.update(byOpenId);
                return 5L;
            }
        }
        return 0L;
    }
    @Log("新增PrizesUser")
    @PostMapping(value = "/prizesUser")
    @PreAuthorize("hasAnyRole('ADMIN','PRIZESUSER_ALL','PRIZESUSER_CREATE')")
    public ResponseEntity create(@Validated @RequestBody PrizesUser resources) {
        return new ResponseEntity(prizesUserService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改PrizesUser")
    @PutMapping(value = "/prizesUser")
    @PreAuthorize("hasAnyRole('ADMIN','PRIZESUSER_ALL','PRIZESUSER_EDIT')")
    public ResponseEntity update(@Validated @RequestBody PrizesUser resources) {
        prizesUserService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除PrizesUser")
    @DeleteMapping(value = "/prizesUser/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PRIZESUSER_ALL','PRIZESUSER_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        prizesUserService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}