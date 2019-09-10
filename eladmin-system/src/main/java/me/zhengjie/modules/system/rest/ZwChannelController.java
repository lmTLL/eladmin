package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.ZwChannel;
import me.zhengjie.modules.system.service.ZwChannelService;
import me.zhengjie.modules.system.service.dto.ZwChannelQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
* @author groot
* @date 2019-09-06
*/
@RestController
@RequestMapping("api")
public class ZwChannelController {

    @Autowired
    private ZwChannelService zwChannelService;

    @Log("查询ZwChannel")
    @GetMapping(value = "/zwChannel")
    @PreAuthorize("hasAnyRole('ADMIN','ZWCHANNEL_ALL','ZWCHANNEL_SELECT')")
    public ResponseEntity getZwChannels(ZwChannelQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(zwChannelService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增ZwChannel")
    @PostMapping(value = "/zwChannel")
    @PreAuthorize("hasAnyRole('ADMIN','ZWCHANNEL_ALL','ZWCHANNEL_CREATE')")
    public ResponseEntity create(@Validated @RequestBody ZwChannel resources){
        return new ResponseEntity(zwChannelService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改ZwChannel")
    @PutMapping(value = "/zwChannel")
    @PreAuthorize("hasAnyRole('ADMIN','ZWCHANNEL_ALL','ZWCHANNEL_EDIT')")
    public ResponseEntity update(@Validated @RequestBody ZwChannel resources){
        zwChannelService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除ZwChannel")
    @DeleteMapping(value = "/zwChannel/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ZWCHANNEL_ALL','ZWCHANNEL_DELETE')")
    public ResponseEntity delete(@PathVariable Long id){
        zwChannelService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}