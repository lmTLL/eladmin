package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.AmzConfig;
import me.zhengjie.modules.system.repository.AmzConfigRepository;
import me.zhengjie.modules.system.service.AmzConfigService;
import me.zhengjie.modules.system.service.dto.AmzConfigQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author groot
 * @date 2019-08-27
 */
@RestController
@RequestMapping("api")
public class AmzConfigController {

    @Autowired
    private AmzConfigService amzConfigService;
    @Autowired
    private AmzConfigRepository amzConfigRepository;

    @Log("查询AmzConfig")
    @GetMapping(value = "/amzConfig")
    @PreAuthorize("hasAnyRole('ADMIN','AMZCONFIG_ALL','AMZCONFIG_SELECT')")
    public ResponseEntity getAmzConfigs(AmzConfigQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(amzConfigService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("根据账号查询AmzConfig")
    @GetMapping(value = "/amzConfig/getConfig")
    public AmzConfig getAmzConfigsByAmzAccount(String amzAccount) {
        System.out.println(amzAccount);
        return amzConfigRepository.findAmzConfigByAmzAccount(amzAccount);
    }


    @Log("新增AmzConfig")
    @PostMapping(value = "/amzConfig")
    @PreAuthorize("hasAnyRole('ADMIN','AMZCONFIG_ALL','AMZCONFIG_CREATE')")
    public ResponseEntity create(@Validated @RequestBody AmzConfig resources) {

        return new ResponseEntity(amzConfigService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改AmzConfig")
    @PutMapping(value = "/amzConfig")
    @PreAuthorize("hasAnyRole('ADMIN','AMZCONFIG_ALL','AMZCONFIG_EDIT')")
    public ResponseEntity update(@Validated @RequestBody AmzConfig resources) {
        amzConfigService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除AmzConfig")
    @DeleteMapping(value = "/amzConfig/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','AMZCONFIG_ALL','AMZCONFIG_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        amzConfigService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}