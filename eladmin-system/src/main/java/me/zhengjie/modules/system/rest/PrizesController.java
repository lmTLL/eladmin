package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.Prizes;
import me.zhengjie.modules.system.domain.PrizesUser;
import me.zhengjie.modules.system.repository.PrizesUserRepository;
import me.zhengjie.modules.system.service.PrizesService;
import me.zhengjie.modules.system.service.dto.PrizesQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;


/**
 * @author groot
 * @date 2019-10-22
 */
@RestController
@RequestMapping("api")
public class PrizesController {

    @Autowired
    private PrizesService prizesService;
    @Autowired
    private PrizesUserRepository prizesUserRepository;

    @Log("查询Prizes")
    @GetMapping(value = "/prizes")
    @PreAuthorize("hasAnyRole('ADMIN','PRIZES_ALL','PRIZES_SELECT')")
    public ResponseEntity getPrizess(PrizesQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(prizesService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增Prizes")
    @PostMapping(value = "/prizes")
    @PreAuthorize("hasAnyRole('ADMIN','PRIZES_ALL','PRIZES_CREATE')")
    public ResponseEntity create(@Validated @RequestBody Prizes resources) {
        return new ResponseEntity(prizesService.create(resources), HttpStatus.CREATED);
    }


    @Log("新增Prizes")
    @GetMapping(value = "/prizes/addPrizes")
    public ResponseEntity create(String openId,String prizes) {
        Prizes resources=new Prizes();
        resources.setOpenId(openId);
        resources.setPrizesName(prizes);
        resources.setGetprizesDate(new Timestamp(new Date().getTime()));
        PrizesUser byOpenId = prizesUserRepository.findByOpenId(openId);
        resources.setNickname(byOpenId.getNickname());
        resources.setHeadimgurl(byOpenId.getHeadimgurl());
        resources.setNum(byOpenId.getNum());
        return new ResponseEntity(prizesService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改Prizes")
    @PutMapping(value = "/prizes")
    @PreAuthorize("hasAnyRole('ADMIN','PRIZES_ALL','PRIZES_EDIT')")
    public ResponseEntity update(@Validated @RequestBody Prizes resources) {
        prizesService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除Prizes")
    @DeleteMapping(value = "/prizes/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PRIZES_ALL','PRIZES_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        prizesService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}