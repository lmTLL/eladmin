package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.ZwPostingEffect;
import me.zhengjie.modules.system.repository.ZwPostingEffectRepository;
import me.zhengjie.modules.system.service.ZwPostingEffectService;
import me.zhengjie.modules.system.service.dto.ZwPostingEffectDTO;
import me.zhengjie.modules.system.service.dto.ZwPostingEffectQueryCriteria;
import me.zhengjie.modules.system.service.mapper.ZwPostingEffectMapper;
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
* @date 2019-09-19
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ZwPostingEffectServiceImpl implements ZwPostingEffectService {

    @Autowired
    private ZwPostingEffectRepository zwPostingEffectRepository;

    @Autowired
    private ZwPostingEffectMapper zwPostingEffectMapper;

    @Override
    public Object queryAll(ZwPostingEffectQueryCriteria criteria, Pageable pageable){
        Page<ZwPostingEffect> page = zwPostingEffectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(zwPostingEffectMapper::toDto));
    }

    @Override
    public Object queryAll(ZwPostingEffectQueryCriteria criteria){
        return zwPostingEffectMapper.toDto(zwPostingEffectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public ZwPostingEffectDTO findById(Long id) {
        Optional<ZwPostingEffect> zwPostingEffect = zwPostingEffectRepository.findById(id);
        ValidationUtil.isNull(zwPostingEffect,"ZwPostingEffect","id",id);
        return zwPostingEffectMapper.toDto(zwPostingEffect.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ZwPostingEffectDTO create(ZwPostingEffect resources) {
        return zwPostingEffectMapper.toDto(zwPostingEffectRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ZwPostingEffect resources) {
        Optional<ZwPostingEffect> optionalZwPostingEffect = zwPostingEffectRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalZwPostingEffect,"ZwPostingEffect","id",resources.getId());

        ZwPostingEffect zwPostingEffect = optionalZwPostingEffect.get();
        // 此处需自己修改
        resources.setId(zwPostingEffect.getId());
        zwPostingEffectRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        zwPostingEffectRepository.deleteById(id);
    }
}