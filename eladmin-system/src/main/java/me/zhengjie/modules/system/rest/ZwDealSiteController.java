package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.ZwDealSite;
import me.zhengjie.modules.system.service.ZwDealSiteService;
import me.zhengjie.modules.system.service.dto.ZwDealSiteQueryCriteria;
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
public class ZwDealSiteController {

    @Autowired
    private ZwDealSiteService zwDealSiteService;

    @Log("查询ZwDealSite")
    @GetMapping(value = "/zwDealSite")
    @PreAuthorize("hasAnyRole('ADMIN','ZWDEALSITE_ALL','ZWDEALSITE_SELECT')")
    public ResponseEntity getZwDealSites(ZwDealSiteQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(zwDealSiteService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增ZwDealSite")
    @PostMapping(value = "/zwDealSite")
    @PreAuthorize("hasAnyRole('ADMIN','ZWDEALSITE_ALL','ZWDEALSITE_CREATE')")
    public ResponseEntity create(@Validated @RequestBody ZwDealSite resources) {
        return new ResponseEntity(zwDealSiteService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改ZwDealSite")
    @PutMapping(value = "/zwDealSite")
    @PreAuthorize("hasAnyRole('ADMIN','ZWDEALSITE_ALL','ZWDEALSITE_EDIT')")
    public ResponseEntity update(@Validated @RequestBody ZwDealSite resources) {
        zwDealSiteService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除ZwDealSite")
    @DeleteMapping(value = "/zwDealSite/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ZWDEALSITE_ALL','ZWDEALSITE_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        zwDealSiteService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }


    @Log("根据站点查询ZwDealSite不分页")
    @GetMapping(value = "/zwDealSiteAll/{site}")
    //@PreAuthorize("hasAnyRole('ADMIN','ZWDEALSITE_ALL','ZWDEALSITE_SELECT')")
    public ResponseEntity getZwDealSitesAll(@PathVariable String site) {
        ZwDealSiteQueryCriteria zwDealSiteQueryCriteria = new ZwDealSiteQueryCriteria();
        zwDealSiteQueryCriteria.setSite(site);
        return new ResponseEntity(zwDealSiteService.queryAll(zwDealSiteQueryCriteria), HttpStatus.OK);
    }
}