package me.zhengjie.modules.system.service.impl;
import me.zhengjie.modules.system.domain.PrizesNum;
import me.zhengjie.modules.system.repository.PrizesNumRepository;
import me.zhengjie.modules.system.service.PrizesNumService;
import me.zhengjie.modules.system.service.dto.PrizesNumDTO;
import me.zhengjie.modules.system.service.dto.PrizesNumQueryCriteria;
import me.zhengjie.modules.system.service.mapper.PrizesNumMapper;
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
public class PrizesNumServiceImpl implements PrizesNumService {

@Autowired
private PrizesNumRepository prizesNumRepository;

@Autowired
private PrizesNumMapper prizesNumMapper;

@Override
public Object queryAll(PrizesNumQueryCriteria criteria, Pageable pageable){
Page<PrizesNum> page = prizesNumRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
return PageUtil.toPage(page.map(prizesNumMapper::toDto));
}

@Override
public Object queryAll(PrizesNumQueryCriteria criteria){
return prizesNumMapper.toDto(prizesNumRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
}

@Override
public PrizesNumDTO findById(Long id) {
Optional<PrizesNum> prizesNum = prizesNumRepository.findById(id);
ValidationUtil.isNull(prizesNum,"PrizesNum","id",id);
return prizesNumMapper.toDto(prizesNum.get());
}

@Override
@Transactional(rollbackFor = Exception.class)
public PrizesNumDTO create(PrizesNum resources) {
return prizesNumMapper.toDto(prizesNumRepository.save(resources));
}

@Override
@Transactional(rollbackFor = Exception.class)
public void update(PrizesNum resources) {
Optional<PrizesNum> optionalPrizesNum = prizesNumRepository.findById(resources.getId());
ValidationUtil.isNull( optionalPrizesNum,"PrizesNum","id",resources.getId());

PrizesNum prizesNum = optionalPrizesNum.get();
// 此处需自己修改
resources.setId(prizesNum.getId());
prizesNumRepository.save(resources);
}

@Override
@Transactional(rollbackFor = Exception.class)
public void delete(Long id) {
prizesNumRepository.deleteById(id);
}
}