package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.FollowDetails;
import me.zhengjie.modules.system.service.dto.FollowDetailsDTO;
import me.zhengjie.modules.system.service.dto.FollowDetailsQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
* @author groot
* @date 2019-07-23
*/
@CacheConfig(cacheNames = "followDetails")
public interface FollowDetailsService {

    /**
    * queryAll 分页
    * @param criteria
    * @param pageable
    * @return
    */
    @Cacheable(keyGenerator = "keyGenerator")
    Object queryAll(FollowDetailsQueryCriteria criteria, Pageable pageable);

    /**
    * queryAll 不分页
    * @param criteria
    * @return
    */
    @CacheEvict(allEntries = true)
    public Object queryAll(FollowDetailsQueryCriteria criteria);

    /**
     * findById
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    FollowDetailsDTO findById(Integer id);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    FollowDetailsDTO create(FollowDetails resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(FollowDetails resources);

    /**
     * delete
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Integer id);

    /**
     * getByAsin
     * @param asin
     */
    @CacheEvict(allEntries = true)
    List<FollowDetails> getByAsin(String  asin);




}