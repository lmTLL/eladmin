package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.FollowOthers;
import me.zhengjie.modules.system.service.dto.AsinInfoQueryCriteria;
import me.zhengjie.modules.system.service.dto.FollowOthersDTO;
import me.zhengjie.modules.system.service.dto.FollowOthersQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author handsome
 * @date 2019-08-28
 */
@CacheConfig(cacheNames = "followOthers")
public interface FollowOthersService {

    /**
     * queryAll 分页
     * @param criteria
     * @param pageable
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    Object queryAll(FollowOthersQueryCriteria criteria, Pageable pageable);

    /**
     * queryAll 不分页
     * @param criteria
     * @return
     */
    @CacheEvict(allEntries = true)
    public Object queryAll(FollowOthersQueryCriteria criteria);


    /**
     * queryAll 不分页
     * @param criteria
     * @return
     */
    @CacheEvict(allEntries = true)
    public Object queryAlls(AsinInfoQueryCriteria criteria);


    /**
     * findById
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    FollowOthersDTO findById(Integer id);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    FollowOthersDTO create(FollowOthers resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(FollowOthers resources);

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
    List<FollowOthers> getByAsin(String  asin);
}