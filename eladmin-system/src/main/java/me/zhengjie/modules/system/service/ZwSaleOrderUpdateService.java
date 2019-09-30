package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.ZwSaleOrderUpdate;
import me.zhengjie.modules.system.service.dto.ZwSaleOrderUpdateDTO;
import me.zhengjie.modules.system.service.dto.ZwSaleOrderUpdateQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
 * @author groot
 * @date 2019-09-27
 */
@CacheConfig(cacheNames = "zwSaleOrderUpdate")
public interface ZwSaleOrderUpdateService {

    /**
     * queryAll 分页
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    Object queryAll(ZwSaleOrderUpdateQueryCriteria criteria, Pageable pageable);

    /**
     * queryAll 不分页
     *
     * @param criteria
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    public Object queryAll(ZwSaleOrderUpdateQueryCriteria criteria);

    /**
     * findById
     *
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    ZwSaleOrderUpdateDTO findById(Long id);

    /**
     * create
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    ZwSaleOrderUpdateDTO create(ZwSaleOrderUpdate resources);

    /**
     * update
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(ZwSaleOrderUpdate resources);

    /**
     * delete
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);
}