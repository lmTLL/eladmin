package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.WaitPayment;
import me.zhengjie.modules.system.repository.WaitPaymentRepository;
import me.zhengjie.modules.system.service.WaitPaymentService;
import me.zhengjie.modules.system.service.dto.WaitPaymentDTO;
import me.zhengjie.modules.system.service.dto.WaitPaymentQueryCriteria;
import me.zhengjie.modules.system.service.mapper.WaitPaymentMapper;
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
 * @date 2019-08-01
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WaitPaymentServiceImpl implements WaitPaymentService {

    @Autowired
    private WaitPaymentRepository waitPaymentRepository;

    @Autowired
    private WaitPaymentMapper waitPaymentMapper;

    @Override
    public Object queryAll(WaitPaymentQueryCriteria criteria, Pageable pageable) {
        Page<WaitPayment> page = waitPaymentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(waitPaymentMapper::toDto));
    }

    @Override
    public Object queryAll(WaitPaymentQueryCriteria criteria) {
        return waitPaymentMapper.toDto(waitPaymentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public WaitPaymentDTO findById(Long id) {
        Optional<WaitPayment> waitPayment = waitPaymentRepository.findById(id);
        ValidationUtil.isNull(waitPayment, "WaitPayment", "id", id);
        return waitPaymentMapper.toDto(waitPayment.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WaitPaymentDTO create(WaitPayment resources) {
        return waitPaymentMapper.toDto(waitPaymentRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(WaitPayment resources) {
        Optional<WaitPayment> optionalWaitPayment = waitPaymentRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalWaitPayment, "WaitPayment", "id", resources.getId());

        WaitPayment waitPayment = optionalWaitPayment.get();
        // 此处需自己修改
        resources.setId(waitPayment.getId());
        waitPaymentRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        waitPaymentRepository.deleteById(id);
    }
}