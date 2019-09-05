package me.zhengjie.modules.system.service.impl;


import me.zhengjie.modules.system.domain.FollowOthers;
import me.zhengjie.modules.system.repository.FollowOthersRepository;
import me.zhengjie.modules.system.service.FollowOthersService;
import me.zhengjie.modules.system.service.dto.AsinInfoQueryCriteria;
import me.zhengjie.modules.system.service.dto.FollowOthersDTO;
import me.zhengjie.modules.system.service.dto.FollowOthersQueryCriteria;
import me.zhengjie.modules.system.service.mapper.FollowOthersMapper;
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
 * @author handsome
 * @date 2019-08-28
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FollowOthersServiceImpl implements FollowOthersService {

    @Autowired
    private FollowOthersRepository followOthersRepository;

    @Autowired
    private FollowOthersMapper followOthersMapper;

    @Override
    public Object queryAll(FollowOthersQueryCriteria criteria, Pageable pageable){
        Page<FollowOthers> page = followOthersRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(followOthersMapper::toDto));
    }

    @Override
    public Object queryAll(FollowOthersQueryCriteria criteria){
        return followOthersMapper.toDto(followOthersRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public Object queryAlls(AsinInfoQueryCriteria criteria) {

        return null;
    }

    @Override
    public FollowOthersDTO findById(Integer id) {
        Optional<FollowOthers> followOthers = followOthersRepository.findById(id);
        ValidationUtil.isNull(followOthers,"FollowDetails","id",id);
        return followOthersMapper.toDto(followOthers.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FollowOthersDTO create(FollowOthers resources) {
        return followOthersMapper.toDto(followOthersRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FollowOthers resources) {
        Optional<FollowOthers> optionalFollowOthers = followOthersRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalFollowOthers,"FollowDetails","id",resources.getId());

        FollowOthers followOthers = optionalFollowOthers.get();
        // 此处需自己修改
        resources.setId(followOthers.getId());
        followOthersRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        followOthersRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FollowOthers> getByAsin(String asin) {
        return followOthersRepository.getByAsin(asin);
    }
}