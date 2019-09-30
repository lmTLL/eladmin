package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.ZwSaleOrderUpdate;
import me.zhengjie.modules.system.service.ZwSaleOrderUpdateService;
import me.zhengjie.modules.system.service.dto.ZwSaleOrderUpdateQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author groot
 * @date 2019-09-27
 */
@RestController
@RequestMapping("api")
public class ZwSaleOrderUpdateController {

    @Autowired
    private ZwSaleOrderUpdateService zwSaleOrderUpdateService;

    @Log("查询ZwSaleOrderUpdate")
    @GetMapping(value = "/zwSaleOrderUpdate")
    @PreAuthorize("hasAnyRole('ADMIN','ZWSALEORDERUPDATE_ALL','ZWSALEORDERUPDATE_SELECT')")
    public ResponseEntity getZwSaleOrderUpdates(ZwSaleOrderUpdateQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(zwSaleOrderUpdateService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增ZwSaleOrderUpdate")
    @PostMapping(value = "/zwSaleOrderUpdate")
    @PreAuthorize("hasAnyRole('ADMIN','ZWSALEORDERUPDATE_ALL','ZWSALEORDERUPDATE_CREATE')")
    public ResponseEntity create(@Validated @RequestBody ZwSaleOrderUpdate resources) {
        return new ResponseEntity(zwSaleOrderUpdateService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改ZwSaleOrderUpdate")
    @PutMapping(value = "/zwSaleOrderUpdate")
    @PreAuthorize("hasAnyRole('ADMIN','ZWSALEORDERUPDATE_ALL','ZWSALEORDERUPDATE_EDIT')")
    public ResponseEntity update(@Validated @RequestBody ZwSaleOrderUpdate resources) {
        zwSaleOrderUpdateService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除ZwSaleOrderUpdate")
    @DeleteMapping(value = "/zwSaleOrderUpdate/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ZWSALEORDERUPDATE_ALL','ZWSALEORDERUPDATE_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        zwSaleOrderUpdateService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}