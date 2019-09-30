package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.AsinInfo;
import me.zhengjie.modules.system.domain.Token;
import me.zhengjie.modules.system.service.dto.AsinInfoDTO;
import me.zhengjie.modules.system.service.dto.AsinInfoQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
 * @author groot
 * @date 2019-07-22
 */
@CacheConfig(cacheNames = "asinInfo")
public interface AsinInfoService {

    /**
     * queryAll 分页
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @CacheEvict(allEntries = true)
    Object queryAll(AsinInfoQueryCriteria criteria, Pageable pageable);

    /**
     * queryAll 不分页
     *
     * @param criteria
     * @return
     */
    @CacheEvict(allEntries = true)
    public Object queryAll(AsinInfoQueryCriteria criteria);

    /**
     * findById
     *
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    AsinInfoDTO findById(Integer id);

    /**
     * create
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    AsinInfoDTO create(AsinInfo resources);

    /**
     * update
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(AsinInfo resources);

    /**
     * delete
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Integer id);

    /**
     * getByAsin
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    AsinInfo getByAsin(AsinInfo resources);


    /**
     * getToken
     *
     * @param
     */
    @CacheEvict(allEntries = true)
    Token getToken();

    /**
     * getToken
     *
     * @param
     */
    @CacheEvict(allEntries = true)
    int updateCount(int count, Integer id);
}