package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.ZwPostingEffect;
import me.zhengjie.modules.system.service.ZwPostingEffectService;
import me.zhengjie.modules.system.service.dto.ZwPostingEffectQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
* @author groot
* @date 2019-09-19
*/
@RestController
@RequestMapping("api")
public class ZwPostingEffectController {

    @Autowired
    private ZwPostingEffectService zwPostingEffectService;

    @Log("查询ZwPostingEffect")
    @GetMapping(value = "/zwPostingEffect/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ZWPOSTINGEFFECT_ALL','ZWPOSTINGEFFECT_SELECT')")
    public ResponseEntity getZwPostingEffects(@PathVariable Long id){
        ZwPostingEffectQueryCriteria criteria=new ZwPostingEffectQueryCriteria();
        criteria.setZwSaleId(id);
        return new ResponseEntity(zwPostingEffectService.queryAll(criteria),HttpStatus.OK);
    }

    @Log("新增ZwPostingEffect")
    @PostMapping(value = "/zwPostingEffect")
    @PreAuthorize("hasAnyRole('ADMIN','ZWPOSTINGEFFECT_ALL','ZWPOSTINGEFFECT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody ZwPostingEffect resources){
        return new ResponseEntity(zwPostingEffectService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改ZwPostingEffect")
    @PutMapping(value = "/zwPostingEffect")
    @PreAuthorize("hasAnyRole('ADMIN','ZWPOSTINGEFFECT_ALL','ZWPOSTINGEFFECT_EDIT')")
    public ResponseEntity update(@Validated @RequestBody ZwPostingEffect resources){
        zwPostingEffectService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除ZwPostingEffect")
    @DeleteMapping(value = "/zwPostingEffect/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ZWPOSTINGEFFECT_ALL','ZWPOSTINGEFFECT_DELETE')")
    public ResponseEntity delete(@PathVariable Long id){
        zwPostingEffectService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}