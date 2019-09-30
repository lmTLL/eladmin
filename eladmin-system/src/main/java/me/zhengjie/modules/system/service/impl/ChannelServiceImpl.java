package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.Channel;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.modules.system.repository.ChannelRepository;
import me.zhengjie.modules.system.service.ChannelService;
import me.zhengjie.modules.system.service.dto.ChannelDTO;
import me.zhengjie.modules.system.service.dto.ChannelQueryCriteria;
import me.zhengjie.modules.system.service.mapper.ChannelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;

/**
 * @author groot
 * @date 2019-07-09
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelMapper channelMapper;

    @Override
    public Object queryAll(ChannelQueryCriteria criteria, Pageable pageable) {
        Page<Channel> page = channelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(channelMapper::toDto));
    }

    @Override
    public Object queryAll(ChannelQueryCriteria criteria) {
        return channelMapper.toDto(channelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public ChannelDTO findById(Long id) {
        Optional<Channel> channel = channelRepository.findById(id);
        ValidationUtil.isNull(channel, "Channel", "id", id);
        return channelMapper.toDto(channel.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChannelDTO create(Channel resources) {
        return channelMapper.toDto(channelRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Channel resources) {
        Optional<Channel> optionalChannel = channelRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalChannel, "Channel", "id", resources.getId());

        Channel channel = optionalChannel.get();
        // 此处需自己修改
        resources.setId(channel.getId());
        channelRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        channelRepository.deleteById(id);
    }
}