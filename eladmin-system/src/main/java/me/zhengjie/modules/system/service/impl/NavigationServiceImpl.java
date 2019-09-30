package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.Navigation;
import me.zhengjie.modules.system.repository.NavigationRepository;
import me.zhengjie.modules.system.service.NavigationService;
import me.zhengjie.modules.system.service.dto.NavigationDTO;
import me.zhengjie.modules.system.service.dto.NavigationQueryCriteria;
import me.zhengjie.modules.system.service.mapper.NavigationMapper;
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
 * @date 2019-08-14
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class NavigationServiceImpl implements NavigationService {

    @Autowired
    private NavigationRepository navigationRepository;

    @Autowired
    private NavigationMapper navigationMapper;

    @Override
    public Object queryAll(NavigationQueryCriteria criteria, Pageable pageable) {
        Page<Navigation> page = navigationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(navigationMapper::toDto));
    }

    @Override
    public Object queryAll(NavigationQueryCriteria criteria) {
        return navigationMapper.toDto(navigationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public NavigationDTO findById(Integer id) {
        Optional<Navigation> navigation = navigationRepository.findById(id);
        ValidationUtil.isNull(navigation, "Navigation", "id", id);
        return navigationMapper.toDto(navigation.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NavigationDTO create(Navigation resources) {
        return navigationMapper.toDto(navigationRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Navigation resources) {
        Optional<Navigation> optionalNavigation = navigationRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalNavigation, "Navigation", "id", resources.getId());

        Navigation navigation = optionalNavigation.get();
        // 此处需自己修改
        resources.setId(navigation.getId());
        navigationRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        navigationRepository.deleteById(id);
    }
}