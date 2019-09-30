package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.ZwSaleOrderUpdate;
import me.zhengjie.modules.system.repository.ZwSaleOrderUpdateRepository;
import me.zhengjie.modules.system.service.ZwSaleOrderUpdateService;
import me.zhengjie.modules.system.service.dto.ZwSaleOrderUpdateDTO;
import me.zhengjie.modules.system.service.dto.ZwSaleOrderUpdateQueryCriteria;
import me.zhengjie.modules.system.service.mapper.ZwSaleOrderUpdateMapper;
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
 * @date 2019-09-27
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ZwSaleOrderUpdateServiceImpl implements ZwSaleOrderUpdateService {

    @Autowired
    private ZwSaleOrderUpdateRepository zwSaleOrderUpdateRepository;

    @Autowired
    private ZwSaleOrderUpdateMapper zwSaleOrderUpdateMapper;

    @Override
    public Object queryAll(ZwSaleOrderUpdateQueryCriteria criteria, Pageable pageable) {
        Page<ZwSaleOrderUpdate> page = zwSaleOrderUpdateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(zwSaleOrderUpdateMapper::toDto));
    }

    @Override
    public Object queryAll(ZwSaleOrderUpdateQueryCriteria criteria) {
        return zwSaleOrderUpdateMapper.toDto(zwSaleOrderUpdateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public ZwSaleOrderUpdateDTO findById(Long id) {
        Optional<ZwSaleOrderUpdate> zwSaleOrderUpdate = zwSaleOrderUpdateRepository.findById(id);
        ValidationUtil.isNull(zwSaleOrderUpdate, "ZwSaleOrderUpdate", "id", id);
        return zwSaleOrderUpdateMapper.toDto(zwSaleOrderUpdate.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ZwSaleOrderUpdateDTO create(ZwSaleOrderUpdate resources) {
        return zwSaleOrderUpdateMapper.toDto(zwSaleOrderUpdateRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ZwSaleOrderUpdate resources) {
        Optional<ZwSaleOrderUpdate> optionalZwSaleOrderUpdate = zwSaleOrderUpdateRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalZwSaleOrderUpdate, "ZwSaleOrderUpdate", "id", resources.getId());

        ZwSaleOrderUpdate zwSaleOrderUpdate = optionalZwSaleOrderUpdate.get();
        // 此处需自己修改
        resources.setId(zwSaleOrderUpdate.getId());
        zwSaleOrderUpdateRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        zwSaleOrderUpdateRepository.deleteById(id);
    }
}