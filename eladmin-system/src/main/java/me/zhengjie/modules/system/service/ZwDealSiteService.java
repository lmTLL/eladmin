package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.ZwDealSite;
import me.zhengjie.modules.system.service.dto.ZwDealSiteDTO;
import me.zhengjie.modules.system.service.dto.ZwDealSiteQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;

/**
 * @author groot
 * @date 2019-09-06
 */
@CacheConfig(cacheNames = "zwDealSite")
public interface ZwDealSiteService {

    /**
     * queryAll 分页
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @CacheEvict(allEntries = true)
    Object queryAll(ZwDealSiteQueryCriteria criteria, Pageable pageable);

    /**
     * queryAll 不分页
     *
     * @param criteria
     * @return
     */
    @CacheEvict(allEntries = true)
    public Object queryAll(ZwDealSiteQueryCriteria criteria);

    /**
     * findById
     *
     * @param id
     * @return
     */
    @CacheEvict(allEntries = true)
    ZwDealSiteDTO findById(Long id);

    /**
     * create
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    ZwDealSiteDTO create(ZwDealSite resources);

    /**
     * update
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(ZwDealSite resources);

    /**
     * delete
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);
}