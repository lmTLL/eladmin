package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.Message;
import me.zhengjie.modules.system.service.MessageService;
import me.zhengjie.modules.system.service.dto.MessageQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
* @author groot
* @date 2019-09-11
*/
@RestController
@RequestMapping("api")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Log("查询Message")
    @GetMapping(value = "/message")
    @PreAuthorize("hasAnyRole('ADMIN','MESSAGE_ALL','MESSAGE_SELECT')")
    public ResponseEntity getMessages(MessageQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(messageService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("查询Message不分页")
    @GetMapping(value = "/messageAll/{msgKey}")
    //@PreAuthorize("hasAnyRole('ADMIN','MESSAGE_ALL','MESSAGE_SELECT')")
    public ResponseEntity getMessageAll(@PathVariable String msgKey){
        MessageQueryCriteria criteria=new MessageQueryCriteria();
        criteria.setMsgKey(msgKey);
        System.out.println(criteria);
        return new ResponseEntity(messageService.queryAll(criteria),HttpStatus.OK);
    }

    @Log("新增Message")
    @PostMapping(value = "/message")
    @PreAuthorize("hasAnyRole('ADMIN','MESSAGE_ALL','MESSAGE_CREATE')")
    public ResponseEntity create(@Validated @RequestBody Message resources){
        System.out.println(resources);
        return new ResponseEntity(messageService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改Message")
    @PutMapping(value = "/message")
    @PreAuthorize("hasAnyRole('ADMIN','MESSAGE_ALL','MESSAGE_EDIT')")
    public ResponseEntity update(@Validated @RequestBody Message resources){
        messageService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除Message")
    @DeleteMapping(value = "/message/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MESSAGE_ALL','MESSAGE_DELETE')")
    public ResponseEntity delete(@PathVariable Long id){
        messageService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}