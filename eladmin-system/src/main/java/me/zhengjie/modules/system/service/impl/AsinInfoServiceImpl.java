package me.zhengjie.modules.system.service.impl;
import me.zhengjie.modules.system.domain.AsinInfo;
import me.zhengjie.modules.system.domain.Token;
import me.zhengjie.modules.system.repository.AsinInfoRepository;
import me.zhengjie.modules.system.service.AsinInfoService;
import me.zhengjie.modules.system.service.dto.AsinInfoDTO;
import me.zhengjie.modules.system.service.dto.AsinInfoQueryCriteria;
import me.zhengjie.modules.system.service.mapper.AsinInfoMapper;
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
* @date 2019-07-22
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AsinInfoServiceImpl implements AsinInfoService {

    @Autowired
    private AsinInfoRepository asinInfoRepository;

    @Autowired
    private AsinInfoMapper asinInfoMapper;

    @Override
    public Object queryAll(AsinInfoQueryCriteria criteria, Pageable pageable){
        Page<AsinInfo> page = asinInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(asinInfoMapper::toDto));
    }

    @Override
    public Object queryAll(AsinInfoQueryCriteria criteria){
        return asinInfoMapper.toDto(asinInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public AsinInfoDTO findById(Integer id) {
        Optional<AsinInfo> asinInfo = asinInfoRepository.findById(id);
        ValidationUtil.isNull(asinInfo,"AsinInfo","id",id);
        return asinInfoMapper.toDto(asinInfo.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AsinInfoDTO create(AsinInfo resources) {
        return asinInfoMapper.toDto(asinInfoRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AsinInfo resources) {
        Optional<AsinInfo> optionalAsinInfo = asinInfoRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalAsinInfo,"AsinInfo","id",resources.getId());

        AsinInfo asinInfo = optionalAsinInfo.get();
        // 此处需自己修改
        resources.setId(asinInfo.getId());
        asinInfoRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        asinInfoRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AsinInfo getByAsin(AsinInfo resources) {
        System.out.println("测试11111");
        AsinInfo as = asinInfoRepository.getByAsin(resources.getAsin(),resources.getSite(),resources.getOpenId());
        System.out.println(as);
        return as;
    }
//11
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Token getToken() {
        return asinInfoRepository.getToken();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCount(int count, Integer id) {
        return asinInfoRepository.updateCount(count,id);
    }

}