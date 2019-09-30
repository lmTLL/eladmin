package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.Channel;
import me.zhengjie.modules.system.service.ChannelService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.ChannelQueryCriteria;
import me.zhengjie.modules.system.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author groot
 * @date 2019-07-09
 */
@RestController
@RequestMapping("api")
public class ChannelController {

    @Autowired
    private ChannelService channelService;
    @Autowired
    private UserService userService;

    @Log("查询Channel")
    @GetMapping(value = "/channel")
    @PreAuthorize("hasAnyRole('ADMIN','CHANNEL_ALL','CHANNEL_SELECT')")
    public ResponseEntity getChannels(ChannelQueryCriteria criteria, Pageable pageable) {
        Object o = channelService.queryAll(criteria, pageable);
        System.out.println(o.toString());
        return new ResponseEntity(channelService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增Channel")
    @PostMapping(value = "/channel")
    @PreAuthorize("hasAnyRole('ADMIN','CHANNEL_ALL','CHANNEL_CREATE')")
    public ResponseEntity create(@Validated @RequestBody Channel resources) {
        UserDTO byOpenId = userService.findByOpenId(resources.getOpenId());
        resources.setUserId(byOpenId.getId());
        return new ResponseEntity(channelService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改Channel")
    @PutMapping(value = "/channel")
    @PreAuthorize("hasAnyRole('ADMIN','CHANNEL_ALL','CHANNEL_EDIT')")
    public ResponseEntity update(@Validated @RequestBody Channel resources) {
        UserDTO byOpenId = userService.findByOpenId(resources.getOpenId());
        resources.setUserId(byOpenId.getId());
        channelService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除Channel")
    @DeleteMapping(value = "/channel/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CHANNEL_ALL','CHANNEL_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        channelService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("查询Channel所有不分页")
    @GetMapping(value = "/channelAll")
    //@PreAuthorize("hasAnyRole('ADMIN','CHANNEL_ALL','CHANNEL_SELECT')")
    public ResponseEntity getChannelsAll(ChannelQueryCriteria criteria) {
        criteria.setEnabled("1");
        return new ResponseEntity(channelService.queryAll(criteria), HttpStatus.OK);
    }
}