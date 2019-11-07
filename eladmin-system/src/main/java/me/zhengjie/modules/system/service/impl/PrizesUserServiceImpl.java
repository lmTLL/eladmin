package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.PrizesUser;
import me.zhengjie.modules.system.repository.PrizesUserRepository;
import me.zhengjie.modules.system.service.PrizesUserService;
import me.zhengjie.modules.system.service.dto.PrizesUserDTO;
import me.zhengjie.modules.system.service.dto.PrizesUserQueryCriteria;
import me.zhengjie.modules.system.service.mapper.PrizesUserMapper;
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
 * @date 2019-10-23
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PrizesUserServiceImpl implements PrizesUserService {

    @Autowired
    private PrizesUserRepository prizesUserRepository;

    @Autowired
    private PrizesUserMapper prizesUserMapper;

    @Override
    public Object queryAll(PrizesUserQueryCriteria criteria, Pageable pageable) {
        Page<PrizesUser> page = prizesUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(prizesUserMapper::toDto));
    }

    @Override
    public Object queryAll(PrizesUserQueryCriteria criteria) {
        return prizesUserMapper.toDto(prizesUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public PrizesUserDTO findById(Long id) {
        Optional<PrizesUser> prizesUser = prizesUserRepository.findById(id);
        ValidationUtil.isNull(prizesUser, "PrizesUser", "id", id);
        return prizesUserMapper.toDto(prizesUser.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrizesUserDTO create(PrizesUser resources) {
        return prizesUserMapper.toDto(prizesUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PrizesUser resources) {
        Optional<PrizesUser> optionalPrizesUser = prizesUserRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalPrizesUser, "PrizesUser", "id", resources.getId());

        PrizesUser prizesUser = optionalPrizesUser.get();
// 此处需自己修改
        resources.setId(prizesUser.getId());
        prizesUserRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        prizesUserRepository.deleteById(id);
    }
}