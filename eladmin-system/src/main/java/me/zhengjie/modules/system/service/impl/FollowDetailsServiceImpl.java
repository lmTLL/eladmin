package me.zhengjie.modules.system.service.impl;


import me.zhengjie.modules.system.domain.FollowDetails;
import me.zhengjie.modules.system.repository.FollowDetailsRepository;
import me.zhengjie.modules.system.service.FollowDetailsService;
import me.zhengjie.modules.system.service.dto.FollowDetailsDTO;
import me.zhengjie.modules.system.service.dto.FollowDetailsQueryCriteria;
import me.zhengjie.modules.system.service.mapper.FollowDetailsMapper;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author groot
 * @date 2019-07-23
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FollowDetailsServiceImpl implements FollowDetailsService {

    @Autowired
    private FollowDetailsRepository followDetailsRepository;

    @Autowired
    private FollowDetailsMapper followDetailsMapper;

    @Override
    public Object queryAll(FollowDetailsQueryCriteria criteria, Pageable pageable) {
        Page<FollowDetails> page = followDetailsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(followDetailsMapper::toDto));
    }

    @Override
    public Object queryAll(FollowDetailsQueryCriteria criteria) {
        return followDetailsMapper.toDto(followDetailsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public FollowDetailsDTO findById(Integer id) {
        Optional<FollowDetails> followDetails = followDetailsRepository.findById(id);
        ValidationUtil.isNull(followDetails, "FollowDetails", "id", id);
        return followDetailsMapper.toDto(followDetails.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FollowDetailsDTO create(FollowDetails resources) {
        return followDetailsMapper.toDto(followDetailsRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FollowDetails resources) {
        Optional<FollowDetails> optionalFollowDetails = followDetailsRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalFollowDetails, "FollowDetails", "id", resources.getId());

        FollowDetails followDetails = optionalFollowDetails.get();
        // 此处需自己修改
        resources.setId(followDetails.getId());
        followDetailsRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        followDetailsRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FollowDetails> getByAsin(String asin) {
        return followDetailsRepository.getByAsin(asin);
    }
}