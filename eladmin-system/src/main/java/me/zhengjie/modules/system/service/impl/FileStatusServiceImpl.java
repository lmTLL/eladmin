package me.zhengjie.modules.system.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import me.zhengjie.modules.system.domain.FileStatus;
import me.zhengjie.modules.system.repository.FileStatusRepository;
import me.zhengjie.modules.system.service.FileStatusService;
import me.zhengjie.modules.system.service.dto.FileStatusDTO;
import me.zhengjie.modules.system.service.dto.FileStatusQueryCriteria;
import me.zhengjie.modules.system.service.mapper.FileStatusMapper;
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
* @date 2019-07-24
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FileStatusServiceImpl implements FileStatusService {

    @Autowired
    private FileStatusRepository fileStatusRepository;

    @Autowired
    private FileStatusMapper fileStatusMapper;

    @Override
    public Object queryAll(FileStatusQueryCriteria criteria, Pageable pageable){
        Page<FileStatus> page = fileStatusRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(fileStatusMapper::toDto));
    }

    @Override
    public Object queryAll(FileStatusQueryCriteria criteria){
        return fileStatusMapper.toDto(fileStatusRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public FileStatusDTO findById(Long id) {
        Optional<FileStatus> fileStatus = fileStatusRepository.findById(id);
        ValidationUtil.isNull(fileStatus,"FileStatus","id",id);
        return fileStatusMapper.toDto(fileStatus.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileStatusDTO create(FileStatus resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId()); 
        return fileStatusMapper.toDto(fileStatusRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FileStatus resources) {
        Optional<FileStatus> optionalFileStatus = fileStatusRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalFileStatus,"FileStatus","id",resources.getId());

        FileStatus fileStatus = optionalFileStatus.get();
        // 此处需自己修改
        resources.setId(fileStatus.getId());
        fileStatusRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        fileStatusRepository.deleteById(id);
    }
}