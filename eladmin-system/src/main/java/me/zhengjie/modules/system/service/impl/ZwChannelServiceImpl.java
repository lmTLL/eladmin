package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.ZwChannel;
import me.zhengjie.modules.system.repository.ZwChannelRepository;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.ZwChannelService;
import me.zhengjie.modules.system.service.dto.UserDTO;
import me.zhengjie.modules.system.service.dto.ZwChannelDTO;
import me.zhengjie.modules.system.service.dto.ZwChannelQueryCriteria;
import me.zhengjie.modules.system.service.mapper.ZwChannelMapper;
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
public class ZwChannelServiceImpl implements ZwChannelService {

    @Autowired
    private ZwChannelRepository zwChannelRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ZwChannelMapper zwChannelMapper;

    @Override
    public Object queryAll(ZwChannelQueryCriteria criteria, Pageable pageable){
        Page<ZwChannel> page = zwChannelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(zwChannelMapper::toDto));
    }

    @Override
    public Object queryAll(ZwChannelQueryCriteria criteria){
        return zwChannelMapper.toDto(zwChannelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public ZwChannelDTO findById(Long id) {
        Optional<ZwChannel> zwChannel = zwChannelRepository.findById(id);
        ValidationUtil.isNull(zwChannel,"ZwChannel","id",id);
        return zwChannelMapper.toDto(zwChannel.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ZwChannelDTO create(ZwChannel resources) {
        UserDTO byOpenId = userService.findByOpenId(resources.getOpenId());
        resources.setUserId(byOpenId.getId());
        return zwChannelMapper.toDto(zwChannelRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ZwChannel resources) {
        Optional<ZwChannel> optionalZwChannel = zwChannelRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalZwChannel,"ZwChannel","id",resources.getId());

        ZwChannel zwChannel = optionalZwChannel.get();
        // 此处需自己修改
        resources.setId(zwChannel.getId());
        zwChannelRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        zwChannelRepository.deleteById(id);
    }
}