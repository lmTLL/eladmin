package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.KeyMsg;
import me.zhengjie.modules.system.repository.KeyMsgRepository;
import me.zhengjie.modules.system.service.KeyMsgService;
import me.zhengjie.modules.system.service.dto.KeyMsgDTO;
import me.zhengjie.modules.system.service.dto.KeyMsgQueryCriteria;
import me.zhengjie.modules.system.service.mapper.KeyMsgMapper;
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
 * @date 2019-07-26
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KeyMsgServiceImpl implements KeyMsgService {

    @Autowired
    private KeyMsgRepository keyMsgRepository;

    @Autowired
    private KeyMsgMapper keyMsgMapper;

    @Override
    public Object queryAll(KeyMsgQueryCriteria criteria, Pageable pageable) {
        Page<KeyMsg> page = keyMsgRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(keyMsgMapper::toDto));
    }

    @Override
    public Object queryAll(KeyMsgQueryCriteria criteria) {
        return keyMsgMapper.toDto(keyMsgRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public KeyMsgDTO findById(Long id) {
        Optional<KeyMsg> keyMsg = keyMsgRepository.findById(id);
        ValidationUtil.isNull(keyMsg, "KeyMsg", "id", id);
        return keyMsgMapper.toDto(keyMsg.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KeyMsgDTO create(KeyMsg resources) {
        return keyMsgMapper.toDto(keyMsgRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(KeyMsg resources) {
        Optional<KeyMsg> optionalKeyMsg = keyMsgRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalKeyMsg, "KeyMsg", "id", resources.getId());

        KeyMsg keyMsg = optionalKeyMsg.get();
        // 此处需自己修改
        resources.setId(keyMsg.getId());
        keyMsgRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        keyMsgRepository.deleteById(id);
    }
}