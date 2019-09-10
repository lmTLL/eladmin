package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.ZwSaleOrder;
import me.zhengjie.modules.system.service.dto.ZwSaleOrderDTO;
import me.zhengjie.modules.system.service.dto.ZwSaleOrderQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;

/**
* @author groot
* @date 2019-09-05
*/
@CacheConfig(cacheNames = "zwSaleOrder")
public interface ZwSaleOrderService {

    /**
    * queryAll 分页
    * @param criteria
    * @param pageable
    * @return
    */
    @CacheEvict(allEntries = true)
    Object queryAll(ZwSaleOrderQueryCriteria criteria, Pageable pageable);

    /**
    * queryAll 不分页
    * @param criteria
    * @return
    */
    @CacheEvict(allEntries = true)
    public Object queryAll(ZwSaleOrderQueryCriteria criteria);

    /**
     * findById
     * @param id
     * @return
     */
    @CacheEvict(allEntries = true)
    ZwSaleOrderDTO findById(Long id);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    ZwSaleOrderDTO create(ZwSaleOrder resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(ZwSaleOrder resources);

    /**
     * delete
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);
    /**
     * cancel
     * @param id
     * @param accountImg
     * @param accountOrder
     */
    @CacheEvict(allEntries = true)
    void upload(Long id,String accountImg,String accountOrder,String status) throws Exception;

    /**
     * cancel
     * @param id
     * @param effectImgs 效果图
     * @param effect 发帖效果
     */
    @CacheEvict(allEntries = true)
    void feedback(Long id,String[] effectImgs,String effect);
}