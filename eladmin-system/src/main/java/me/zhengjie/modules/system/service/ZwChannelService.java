package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.ZwChannel;
import me.zhengjie.modules.system.service.dto.ZwChannelDTO;
import me.zhengjie.modules.system.service.dto.ZwChannelQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;

/**
* @author groot
* @date 2019-09-06
*/
@CacheConfig(cacheNames = "zwChannel")
public interface ZwChannelService {

    /**
    * queryAll 分页
    * @param criteria
    * @param pageable
    * @return
    */
    @CacheEvict(allEntries = true)
    Object queryAll(ZwChannelQueryCriteria criteria, Pageable pageable);

    /**
    * queryAll 不分页
    * @param criteria
    * @return
    */
    @CacheEvict(allEntries = true)
    public Object queryAll(ZwChannelQueryCriteria criteria);

    /**
     * findById
     * @param id
     * @return
     */
    @CacheEvict(allEntries = true)
    ZwChannelDTO findById(Long id);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    ZwChannelDTO create(ZwChannel resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(ZwChannel resources);

    /**
     * delete
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);
}