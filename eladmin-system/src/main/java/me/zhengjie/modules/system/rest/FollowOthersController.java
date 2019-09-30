package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.AsinInfo;
import me.zhengjie.modules.system.domain.FollowOthers;
import me.zhengjie.modules.system.service.FollowOthersService;
import me.zhengjie.modules.system.service.dto.AsinInfoQueryCriteria;
import me.zhengjie.modules.system.service.dto.FollowOthersQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author handsome
 * @date 2019-08-28
 */
@RestController
@RequestMapping("api")
public class FollowOthersController {

    @Autowired
    private FollowOthersService followOthersService;

    @Log("查询FollowOthers")
    @GetMapping(value = "/followOthers")
    @PreAuthorize("hasAnyRole('ADMIN','FOLLOWDETAILS_ALL','FOLLOWDETAILS_SELECT')")
    public ResponseEntity getfollowOtherss(FollowOthersQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(followOthersService.queryAll(criteria, pageable), HttpStatus.OK);
    }


    @Log("查询FollowOthers不分页")
    @GetMapping(value = "/followOthersAll/{asin}")
    //@PreAuthorize("hasAnyRole('ADMIN','FOLLOWDETAILS_ALL','FOLLOWDETAILS_SELECT')")
    public ResponseEntity getfollowOtherssAll(@PathVariable String asin) {
        FollowOthersQueryCriteria criteria = new FollowOthersQueryCriteria();
        criteria.setAsin(asin);
        return new ResponseEntity(followOthersService.queryAll(criteria), HttpStatus.OK);
    }

    @Log("查询FollowOthers不分页")
    @GetMapping(value = "/followOthersAll/")
    //@PreAuthorize("hasAnyRole('ADMIN','FOLLOWDETAILS_ALL','FOLLOWDETAILS_SELECT')")
    public ResponseEntity getfollowOtherssAlls(@Validated @RequestBody AsinInfo resources) {
        AsinInfoQueryCriteria criteria = new AsinInfoQueryCriteria();
        criteria.setAsin(resources.getAsin());
        criteria.setTitleListen(resources.getTitleListen());
        criteria.setFollowListen(resources.getFollowListen());
        criteria.setPriceListen(resources.getPriceListen());
        criteria.setFivepointListen(resources.getFivepointListen());
        return new ResponseEntity(followOthersService.queryAlls(criteria), HttpStatus.OK);
    }

    @Log("新增FollowOthers")
    @PostMapping(value = "/followOthers")
    @PreAuthorize("hasAnyRole('ADMIN','FOLLOWDETAILS_ALL','FOLLOWDETAILS_CREATE')")
    public ResponseEntity create(@Validated @RequestBody FollowOthers resources) {
        return new ResponseEntity(followOthersService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改FollowOthers")
    @PutMapping(value = "/followOthers")
    @PreAuthorize("hasAnyRole('ADMIN','FOLLOWDETAILS_ALL','FOLLOWDETAILS_EDIT')")
    public ResponseEntity update(@Validated @RequestBody FollowOthers resources) {
        followOthersService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除FollowOthers")
    @DeleteMapping(value = "/followOthers/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FOLLOWDETAILS_ALL','FOLLOWDETAILS_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id) {
        followOthersService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}