package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.Message;
import me.zhengjie.modules.system.service.dto.MessageDTO;
import me.zhengjie.modules.system.service.dto.MessageQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;

/**
 * @author groot
 * @date 2019-09-11
 */
@CacheConfig(cacheNames = "message")
public interface MessageService {

    /**
     * queryAll 分页
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @CacheEvict(allEntries = true)
    Object queryAll(MessageQueryCriteria criteria, Pageable pageable);

    /**
     * queryAll 不分页
     *
     * @param criteria
     * @return
     */
    @CacheEvict(allEntries = true)
    public Object queryAll(MessageQueryCriteria criteria);

    /**
     * findById
     *
     * @param id
     * @return
     */
    @CacheEvict(allEntries = true)
    MessageDTO findById(Long id);

    /**
     * create
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    MessageDTO create(Message resources);

    /**
     * update
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(Message resources);

    /**
     * delete
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);
}