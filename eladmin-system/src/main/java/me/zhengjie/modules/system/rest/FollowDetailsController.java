package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.FollowDetails;
import me.zhengjie.modules.system.service.FollowDetailsService;
import me.zhengjie.modules.system.service.dto.FollowDetailsQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
* @author groot
* @date 2019-07-23
*/
@RestController
@RequestMapping("api")
public class FollowDetailsController {

    @Autowired
    private FollowDetailsService followDetailsService;

    @Log("查询FollowDetails")
    @GetMapping(value = "/followDetails")
    @PreAuthorize("hasAnyRole('ADMIN','FOLLOWDETAILS_ALL','FOLLOWDETAILS_SELECT')")
    public ResponseEntity getFollowDetailss(FollowDetailsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(followDetailsService.queryAll(criteria,pageable),HttpStatus.OK);
    }


    @Log("查询FollowDetails不分页")
    @GetMapping(value = "/followDetailsAll/{asin}")
    //@PreAuthorize("hasAnyRole('ADMIN','FOLLOWDETAILS_ALL','FOLLOWDETAILS_SELECT')")
    public ResponseEntity getFollowDetailssAll(@PathVariable String asin){
        FollowDetailsQueryCriteria criteria=new FollowDetailsQueryCriteria();
        criteria.setAsin(asin);
        return new ResponseEntity(followDetailsService.queryAll(criteria),HttpStatus.OK);
    }


    @Log("新增FollowDetails")
    @PostMapping(value = "/followDetails")
    @PreAuthorize("hasAnyRole('ADMIN','FOLLOWDETAILS_ALL','FOLLOWDETAILS_CREATE')")
    public ResponseEntity create(@Validated @RequestBody FollowDetails resources){
        return new ResponseEntity(followDetailsService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改FollowDetails")
    @PutMapping(value = "/followDetails")
    @PreAuthorize("hasAnyRole('ADMIN','FOLLOWDETAILS_ALL','FOLLOWDETAILS_EDIT')")
    public ResponseEntity update(@Validated @RequestBody FollowDetails resources){
        followDetailsService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除FollowDetails")
    @DeleteMapping(value = "/followDetails/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FOLLOWDETAILS_ALL','FOLLOWDETAILS_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        followDetailsService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}