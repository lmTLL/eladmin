package me.zhengjie.modules.system.service.impl;
import me.zhengjie.modules.system.domain.AmzConfig;
import me.zhengjie.modules.system.repository.AmzConfigRepository;
import me.zhengjie.modules.system.service.AmzConfigService;
import me.zhengjie.modules.system.service.dto.AmzConfigDTO;
import me.zhengjie.modules.system.service.dto.AmzConfigQueryCriteria;
import me.zhengjie.modules.system.service.mapper.AmzConfigMapper;
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
* @date 2019-08-27
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AmzConfigServiceImpl implements AmzConfigService {

    @Autowired
    private AmzConfigRepository amzConfigRepository;

    @Autowired
    private AmzConfigMapper amzConfigMapper;

    @Override
    public Object queryAll(AmzConfigQueryCriteria criteria, Pageable pageable){
        Page<AmzConfig> page = amzConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(amzConfigMapper::toDto));
    }

    @Override
    public Object queryAll(AmzConfigQueryCriteria criteria){
        return amzConfigMapper.toDto(amzConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public AmzConfigDTO findById(Long id) {
        Optional<AmzConfig> amzConfig = amzConfigRepository.findById(id);
        ValidationUtil.isNull(amzConfig,"AmzConfig","id",id);
        return amzConfigMapper.toDto(amzConfig.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AmzConfigDTO create(AmzConfig resources) {
        return amzConfigMapper.toDto(amzConfigRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AmzConfig resources) {
        Optional<AmzConfig> optionalAmzConfig = amzConfigRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalAmzConfig,"AmzConfig","id",resources.getId());

        AmzConfig amzConfig = optionalAmzConfig.get();
        // 此处需自己修改
        resources.setId(amzConfig.getId());
        amzConfigRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        amzConfigRepository.deleteById(id);
    }
}