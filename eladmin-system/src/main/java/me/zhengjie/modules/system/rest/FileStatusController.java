package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.FileStatus;
import me.zhengjie.modules.system.service.FileStatusService;
import me.zhengjie.modules.system.service.dto.FileStatusQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
* @author groot
* @date 2019-07-24
*/
@RestController
@RequestMapping("api")
public class FileStatusController {

    @Autowired
    private FileStatusService fileStatusService;

    @Log("查询FileStatus")
    @GetMapping(value = "/fileStatus")
    @PreAuthorize("hasAnyRole('ADMIN','FILESTATUS_ALL','FILESTATUS_SELECT')")
    public ResponseEntity getFileStatuss(FileStatusQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(fileStatusService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增FileStatus")
    @PostMapping(value = "/fileStatus")
    @PreAuthorize("hasAnyRole('ADMIN','FILESTATUS_ALL','FILESTATUS_CREATE')")
    public ResponseEntity create(@Validated @RequestBody FileStatus resources){
        return new ResponseEntity(fileStatusService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改FileStatus")
    @PutMapping(value = "/fileStatus")
    @PreAuthorize("hasAnyRole('ADMIN','FILESTATUS_ALL','FILESTATUS_EDIT')")
    public ResponseEntity update(@Validated @RequestBody FileStatus resources){
        fileStatusService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除FileStatus")
    @DeleteMapping(value = "/fileStatus/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FILESTATUS_ALL','FILESTATUS_DELETE')")
    public ResponseEntity delete(@PathVariable Long id){
        fileStatusService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}