package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.Channel;
import me.zhengjie.modules.system.service.dto.ChannelDTO;
import me.zhengjie.modules.system.service.dto.ChannelQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
 * @author groot
 * @date 2019-07-09
 */
@CacheConfig(cacheNames = "channel")
public interface ChannelService {

    /**
     * queryAll 分页
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    Object queryAll(ChannelQueryCriteria criteria, Pageable pageable);

    /**
     * queryAll 不分页
     *
     * @param criteria
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    public Object queryAll(ChannelQueryCriteria criteria);

    /**
     * findById
     *
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    ChannelDTO findById(Long id);

    /**
     * create
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    ChannelDTO create(Channel resources);

    /**
     * update
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(Channel resources);

    /**
     * delete
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);
}