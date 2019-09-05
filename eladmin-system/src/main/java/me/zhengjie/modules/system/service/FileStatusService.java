package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.FileStatus;
import me.zhengjie.modules.system.service.dto.FileStatusDTO;
import me.zhengjie.modules.system.service.dto.FileStatusQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
* @author groot
* @date 2019-07-24
*/
@CacheConfig(cacheNames = "fileStatus")
public interface FileStatusService {

    /**
    * queryAll 分页
    * @param criteria
    * @param pageable
    * @return
    */
    @CacheEvict(allEntries = true)
    Object queryAll(FileStatusQueryCriteria criteria, Pageable pageable);

    /**
    * queryAll 不分页
    * @param criteria
    * @return
    */
    @CacheEvict(allEntries = true)
    public Object queryAll(FileStatusQueryCriteria criteria);

    /**
     * findById
     * @param id
     * @return
     */
    @CacheEvict(allEntries = true)
    FileStatusDTO findById(Long id);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    FileStatusDTO create(FileStatus resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(FileStatus resources);

    /**
     * delete
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);
}