package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.PrizesNum;
import me.zhengjie.modules.system.service.PrizesNumService;
import me.zhengjie.modules.system.service.dto.PrizesNumQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author groot
 * @date 2019-10-23
 */
@RestController
@RequestMapping("api")
public class PrizesNumController {

    @Autowired
    private PrizesNumService prizesNumService;

    @Log("查询PrizesNum")
    @GetMapping(value = "/prizesNum")
    @PreAuthorize("hasAnyRole('ADMIN','PRIZESNUM_ALL','PRIZESNUM_SELECT')")
    public ResponseEntity getPrizesNums(PrizesNumQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(prizesNumService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增PrizesNum")
    @PostMapping(value = "/prizesNum")
    @PreAuthorize("hasAnyRole('ADMIN','PRIZESNUM_ALL','PRIZESNUM_CREATE')")
    public ResponseEntity create() {
        PrizesNum resources=new PrizesNum();
        String string = "";
        for (int i = 0; i < 3; i++) {

            // 得到随机字母
            char c = (char) ((Math.random() * 26) + 97);
            // 拼接成字符串
            string += (c + "");
        }
        int i=10+(int)(Math.random()*90);
        resources.setPrizesNum(string+i);
        resources.setNum(1L);
        return new ResponseEntity(prizesNumService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改PrizesNum")
    @PutMapping(value = "/prizesNum")
    @PreAuthorize("hasAnyRole('ADMIN','PRIZESNUM_ALL','PRIZESNUM_EDIT')")
    public ResponseEntity update(@Validated @RequestBody PrizesNum resources) {
        prizesNumService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除PrizesNum")
    @DeleteMapping(value = "/prizesNum/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PRIZESNUM_ALL','PRIZESNUM_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        prizesNumService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}