package me.zhengjie.modules.system.service;
import me.zhengjie.modules.system.domain.DzSaleOrder;
import me.zhengjie.modules.system.domain.ErpSalesOrder;
import me.zhengjie.modules.system.service.dto.DzSaleOrderDTO;
import me.zhengjie.modules.system.service.dto.DzSaleOrderQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
 * @author groot
 * @date 2019-11-01
 */
@CacheConfig(cacheNames = "dzSaleOrder")
public interface DzSaleOrderService {

    /**
     * queryAll 分页
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @CacheEvict(allEntries = true)
    Object queryAll(DzSaleOrderQueryCriteria criteria, Pageable pageable);

    /**
     * queryAll 不分页
     *
     * @param criteria
     * @return
     */
    @CacheEvict(allEntries = true)
    public Object queryAll(DzSaleOrderQueryCriteria criteria);

    /**
     * findById
     *
     * @param id
     * @return
     */
    @CacheEvict(allEntries = true)
    DzSaleOrderDTO findById(Long id);

    /**
     * create
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    DzSaleOrderDTO create(DzSaleOrder resources);

    /**
     * update
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(DzSaleOrder resources);

    /**
     * delete
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);

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
}