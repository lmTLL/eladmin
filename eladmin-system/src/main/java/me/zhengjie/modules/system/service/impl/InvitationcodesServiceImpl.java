package me.zhengjie.modules.system.service.impl;


import me.zhengjie.modules.system.domain.Invitationcodes;
import me.zhengjie.modules.system.repository.InvitationcodesRepository;
import me.zhengjie.modules.system.service.InvitationcodesService;
import me.zhengjie.modules.system.service.dto.InvitationcodesDTO;
import me.zhengjie.modules.system.service.dto.InvitationcodesQueryCriteria;
import me.zhengjie.modules.system.service.mapper.InvitationcodesMapper;
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
* @date 2019-07-05
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class InvitationcodesServiceImpl implements InvitationcodesService {

    @Autowired
    private InvitationcodesRepository invitationcodesRepository;

    @Autowired
    private InvitationcodesMapper invitationcodesMapper;

    @Override
    public Object queryAll(InvitationcodesQueryCriteria criteria, Pageable pageable){
        Page<Invitationcodes> page = invitationcodesRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(invitationcodesMapper::toDto));
    }

    @Override
    public Object queryAll(InvitationcodesQueryCriteria criteria){
        return invitationcodesMapper.toDto(invitationcodesRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public InvitationcodesDTO findByInvitationCode(String invitationCode) {
        Optional<Invitationcodes> invitationcodes = invitationcodesRepository.findByInvitationCode(invitationCode);
        ValidationUtil.isNull(invitationcodes,"Invitationcodes","invitation_code",invitationCode);
        return invitationcodesMapper.toDto(invitationcodes.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvitationcodesDTO create(Invitationcodes resources) {
        return invitationcodesMapper.toDto(invitationcodesRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Invitationcodes resources) {
        Optional<Invitationcodes> optionalInvitationcodes = invitationcodesRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalInvitationcodes,"Invitationcodes","id",resources.getId());

        Invitationcodes invitationcodes = optionalInvitationcodes.get();
        // 此处需自己修改
        resources.setId(invitationcodes.getId());
        invitationcodesRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        invitationcodesRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEnable(Long id) {
        invitationcodesRepository.updateEnable(id);
    }
}