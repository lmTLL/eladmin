package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.Navigation;
import me.zhengjie.modules.system.service.dto.NavigationDTO;
import me.zhengjie.modules.system.service.dto.NavigationQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
 * @author groot
 * @date 2019-08-14
 */
@CacheConfig(cacheNames = "navigation")
public interface NavigationService {

    /**
     * queryAll 分页
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @CacheEvict(allEntries = true)
    Object queryAll(NavigationQueryCriteria criteria, Pageable pageable);

    /**
     * queryAll 不分页
     *
     * @param criteria
     * @return
     */
    @CacheEvict(allEntries = true)
    public Object queryAll(NavigationQueryCriteria criteria);

    /**
     * findById
     *
     * @param id
     * @return
     */
    @CacheEvict(allEntries = true)
    NavigationDTO findById(Integer id);

    /**
     * create
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    NavigationDTO create(Navigation resources);

    /**
     * update
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(Navigation resources);

    /**
     * delete
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Integer id);
}