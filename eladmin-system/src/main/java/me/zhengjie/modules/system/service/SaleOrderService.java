package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.SaleOrder;
import me.zhengjie.modules.system.service.dto.SaleOrderDTO;
import me.zhengjie.modules.system.service.dto.SaleOrderQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;

/**
* @author groot
* @date 2019-07-09
*/
@CacheConfig(cacheNames = "saleOrder")
public interface SaleOrderService {

    /**
    * queryAll 分页
    * @param criteria
    * @param pageable
    * @return
    */
    @CacheEvict(allEntries = true)
    Object queryAll(SaleOrderQueryCriteria criteria, Pageable pageable);

    /**
    * queryAll 不分页
    * @param criteria
    * @return
    */
    @CacheEvict(allEntries = true)
    public Object queryAll(SaleOrderQueryCriteria criteria);

    /**
     * findById
     * @param id
     * @return
     */
    @CacheEvict(allEntries = true)
    SaleOrderDTO findById(Long id);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    SaleOrderDTO create(SaleOrder resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(SaleOrder resources);

    /**
     * delete
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);

    /**
     * sign
     * @param ids
     */
    @CacheEvict(allEntries = true)
    void sign(Long[] ids,String signdate) throws ParseException;

    /**
     * sign
     * @param ids
     */
    @CacheEvict(allEntries = true)
    void signHandle(Long[] ids);
    /**
     * sign
     * @param ids
     * @param remark
     */
    @CacheEvict(allEntries = true)
    void dissign(Long[] ids,String remark) throws Exception;
    /**
     * cancel
     * @param ids
     * @param ids
     */
    @CacheEvict(allEntries = true)
    void cancel(Long[] ids,String remark);

    /**
     * cancel
     * @param ids
     * @param ids
     */
    @CacheEvict(allEntries = true)
    void agree(Long[] ids) throws Exception;

    /**
     * cancel
     * @param ids
     * @param ids
     */
    @CacheEvict(allEntries = true)
    void disagree(Long[] ids,String remark) throws Exception;

    /**
     * cancel
     * @param id
     * @param accountImg
     * @param accountOrder
     */
    @CacheEvict(allEntries = true)
    void upload(Long id,String accountImg,String accountOrder,String status) throws Exception;

    @CacheEvict(allEntries = true)
    void followBack(Long id) throws Exception;
    /**
     * 生成销售单号
     * @return
     */
    String getSalesOrderNo();

    /**
     * cancel
     * @param ids id集合
     */
    @CacheEvict(allEntries = true)
    void signPayment(Long[] ids);
}