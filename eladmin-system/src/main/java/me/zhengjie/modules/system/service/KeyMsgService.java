package me.zhengjie.modules.system.service;


import me.zhengjie.modules.system.domain.KeyMsg;
import me.zhengjie.modules.system.service.dto.KeyMsgDTO;
import me.zhengjie.modules.system.service.dto.KeyMsgQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
 * @author groot
 * @date 2019-07-26
 */
@CacheConfig(cacheNames = "keyMsg")
public interface KeyMsgService {

    /**
     * queryAll 分页
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @CacheEvict(allEntries = true)
    Object queryAll(KeyMsgQueryCriteria criteria, Pageable pageable);

    /**
     * queryAll 不分页
     *
     * @param criteria
     * @return
     */
    @CacheEvict(allEntries = true)
    public Object queryAll(KeyMsgQueryCriteria criteria);

    /**
     * findById
     *
     * @param id
     * @return
     */
    @CacheEvict(allEntries = true)
    KeyMsgDTO findById(Long id);

    /**
     * create
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    KeyMsgDTO create(KeyMsg resources);

    /**
     * update
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(KeyMsg resources);

    /**
     * delete
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);
}