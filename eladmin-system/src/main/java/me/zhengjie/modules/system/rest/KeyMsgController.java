package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.KeyMsg;
import me.zhengjie.modules.system.service.KeyMsgService;
import me.zhengjie.modules.system.service.dto.KeyMsgQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author groot
 * @date 2019-07-26
 */
@RestController
@RequestMapping("api")
public class KeyMsgController {

    @Autowired
    private KeyMsgService keyMsgService;

    @Log("查询KeyMsg")
    @GetMapping(value = "/keyMsg")
    @PreAuthorize("hasAnyRole('ADMIN','KEYMSG_ALL','KEYMSG_SELECT')")
    public ResponseEntity getKeyMsgs(KeyMsgQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(keyMsgService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增KeyMsg")
    @PostMapping(value = "/keyMsg")
    @PreAuthorize("hasAnyRole('ADMIN','KEYMSG_ALL','KEYMSG_CREATE')")
    public ResponseEntity create(@Validated @RequestBody KeyMsg resources) {
        return new ResponseEntity(keyMsgService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改KeyMsg")
    @PutMapping(value = "/keyMsg")
    @PreAuthorize("hasAnyRole('ADMIN','KEYMSG_ALL','KEYMSG_EDIT')")
    public ResponseEntity update(@Validated @RequestBody KeyMsg resources) {
        keyMsgService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除KeyMsg")
    @DeleteMapping(value = "/keyMsg/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','KEYMSG_ALL','KEYMSG_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        keyMsgService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}