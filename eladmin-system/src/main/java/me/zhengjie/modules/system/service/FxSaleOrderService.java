package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.ErpSalesOrder;
import me.zhengjie.modules.system.domain.FxSaleOrder;
import me.zhengjie.modules.system.service.dto.FxSaleOrderDTO;
import me.zhengjie.modules.system.service.dto.FxSaleOrderQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
 * @author groot
 * @date 2019-10-31
 */
@CacheConfig(cacheNames = "fxSaleOrder")
public interface FxSaleOrderService {

    /**
     * queryAll 分页
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @CacheEvict(allEntries = true)
    Object queryAll(FxSaleOrderQueryCriteria criteria, Pageable pageable);

    /**
     * queryAll 不分页
     *
     * @param criteria
     * @return
     */
    @CacheEvict(allEntries = true)
    public Object queryAll(FxSaleOrderQueryCriteria criteria);

    /**
     * findById
     *
     * @param id
     * @return
     */
    @CacheEvict(allEntries = true)
    FxSaleOrderDTO findById(Long id);

    /**
     * create
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    FxSaleOrderDTO create(FxSaleOrder resources);

    /**
     * update
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(FxSaleOrder resources);

    /**
     * payment
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void payment(ErpSalesOrder resources);

    /**
     * payment
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void sign(ErpSalesOrder resources);
    /**
     * delete
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);
}