package me.zhengjie.modules.system.service;
import me.zhengjie.modules.system.domain.ErpSalesOrder;
import me.zhengjie.modules.system.domain.ScpSaleOrder;
import me.zhengjie.modules.system.service.dto.ScpSaleOrderDTO;
import me.zhengjie.modules.system.service.dto.ScpSaleOrderQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
 * @author groot
 * @date 2019-10-25
 */
@CacheConfig(cacheNames = "scpSaleOrder")
public interface ScpSaleOrderService {

    /**
     * queryAll 分页
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @CacheEvict(allEntries = true)
    Object queryAll(ScpSaleOrderQueryCriteria criteria, Pageable pageable);

    /**
     * queryAll 不分页
     *
     * @param criteria
     * @return
     */
    @CacheEvict(allEntries = true)
    public Object queryAll(ScpSaleOrderQueryCriteria criteria);

    /**
     * findById
     *
     * @param id
     * @return
     */
    @CacheEvict(allEntries = true)
    ScpSaleOrderDTO findById(Long id);

    /**
     * create
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    ScpSaleOrderDTO create(ScpSaleOrder resources);

    /**
     * update
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(ScpSaleOrder resources);


    /**
     * payment
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void payment(ErpSalesOrder resources);
    /**
     * delete
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);

    /**
     * 生成销售单号
     *
     * @return
     */
    String getSalesOrderNo();
}