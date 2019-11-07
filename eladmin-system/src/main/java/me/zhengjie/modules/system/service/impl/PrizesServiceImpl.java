package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.Prizes;
import me.zhengjie.modules.system.repository.PrizesRepository;
import me.zhengjie.modules.system.service.PrizesService;
import me.zhengjie.modules.system.service.dto.PrizesDTO;
import me.zhengjie.modules.system.service.dto.PrizesQueryCriteria;
import me.zhengjie.modules.system.service.mapper.PrizesMapper;
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
 * @date 2019-10-22
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PrizesServiceImpl implements PrizesService {

    @Autowired
    private PrizesRepository prizesRepository;

    @Autowired
    private PrizesMapper prizesMapper;

    @Override
    public Object queryAll(PrizesQueryCriteria criteria, Pageable pageable) {
        Page<Prizes> page = prizesRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(prizesMapper::toDto));
    }

    @Override
    public Object queryAll(PrizesQueryCriteria criteria) {
        return prizesMapper.toDto(prizesRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public PrizesDTO findById(Long id) {
        Optional<Prizes> prizes = prizesRepository.findById(id);
        ValidationUtil.isNull(prizes, "Prizes", "id", id);
        return prizesMapper.toDto(prizes.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrizesDTO create(Prizes resources) {
        return prizesMapper.toDto(prizesRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Prizes resources) {
        Optional<Prizes> optionalPrizes = prizesRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalPrizes, "Prizes", "id", resources.getId());

        Prizes prizes = optionalPrizes.get();
// 此处需自己修改
        resources.setId(prizes.getId());
        prizesRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        prizesRepository.deleteById(id);
    }
}