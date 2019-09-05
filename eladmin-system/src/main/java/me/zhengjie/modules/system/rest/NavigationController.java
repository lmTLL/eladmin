package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.Navigation;
import me.zhengjie.modules.system.service.NavigationService;
import me.zhengjie.modules.system.service.dto.NavigationQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
* @author groot
* @date 2019-08-14
*/
@RestController
@RequestMapping("api")
public class NavigationController {

    @Autowired
    private NavigationService navigationService;

    @Log("查询Navigation")
    @GetMapping(value = "/navigation")
    @PreAuthorize("hasAnyRole('ADMIN','NAVIGATION_ALL','NAVIGATION_SELECT')")
    public ResponseEntity getNavigations(NavigationQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(navigationService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("查询Navigation不分页")
    @GetMapping(value = "/navigationAll")
    //@PreAuthorize("hasAnyRole('ADMIN','NAVIGATION_ALL','NAVIGATION_SELECT')")
    public ResponseEntity getNavigationsAll(NavigationQueryCriteria criteria){
        return new ResponseEntity(navigationService.queryAll(criteria),HttpStatus.OK);
    }


    @Log("新增Navigation")
    @PostMapping(value = "/navigation")
    @PreAuthorize("hasAnyRole('ADMIN','NAVIGATION_ALL','NAVIGATION_CREATE')")
    public ResponseEntity create(@Validated @RequestBody Navigation resources){
        return new ResponseEntity(navigationService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改Navigation")
    @PutMapping(value = "/navigation")
    @PreAuthorize("hasAnyRole('ADMIN','NAVIGATION_ALL','NAVIGATION_EDIT')")
    public ResponseEntity update(@Validated @RequestBody Navigation resources){
        navigationService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除Navigation")
    @DeleteMapping(value = "/navigation/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','NAVIGATION_ALL','NAVIGATION_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        navigationService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}