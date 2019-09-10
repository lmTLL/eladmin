package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.ZwDealSite;
import me.zhengjie.modules.system.repository.ZwDealSiteRepository;
import me.zhengjie.modules.system.service.ZwDealSiteService;
import me.zhengjie.modules.system.service.dto.ZwDealSiteDTO;
import me.zhengjie.modules.system.service.dto.ZwDealSiteQueryCriteria;
import me.zhengjie.modules.system.service.mapper.ZwDealSiteMapper;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
* @author groot
* @date 2019-09-06
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ZwDealSiteServiceImpl implements ZwDealSiteService {

    @Autowired
    private ZwDealSiteRepository zwDealSiteRepository;

    @Autowired
    private ZwDealSiteMapper zwDealSiteMapper;

    @Override
    public Object queryAll(ZwDealSiteQueryCriteria criteria, Pageable pageable){
        Page<ZwDealSite> page = zwDealSiteRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(zwDealSiteMapper::toDto));
    }

    @Override
    public Object queryAll(ZwDealSiteQueryCriteria criteria){
        return zwDealSiteMapper.toDto(zwDealSiteRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public ZwDealSiteDTO findById(Long id) {
        Optional<ZwDealSite> zwDealSite = zwDealSiteRepository.findById(id);
        ValidationUtil.isNull(zwDealSite,"ZwDealSite","id",id);
        return zwDealSiteMapper.toDto(zwDealSite.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ZwDealSiteDTO create(ZwDealSite resources) {
        return zwDealSiteMapper.toDto(zwDealSiteRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ZwDealSite resources) {
        Optional<ZwDealSite> optionalZwDealSite = zwDealSiteRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalZwDealSite,"ZwDealSite","id",resources.getId());

        ZwDealSite zwDealSite = optionalZwDealSite.get();
        // 此处需自己修改
        resources.setId(zwDealSite.getId());
        zwDealSiteRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        zwDealSiteRepository.deleteById(id);
    }
}