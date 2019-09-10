package me.zhengjie.modules.system.repository;


import me.zhengjie.modules.system.domain.ZwSaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Timestamp;

/**
* @author groot
* @date 2019-09-05
*/
public interface ZwSaleOrderRepository extends JpaRepository<ZwSaleOrder, Long>, JpaSpecificationExecutor {
    @Query(value = "SELECT MAX(t.zw_sale_number) FROM zw_sale_order t WHERE t.zw_sale_number LIKE ?1",nativeQuery = true)
    String getMaxSalesOrderNo(String salesOrderNo);

    /**
     * 上传付款截图
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update zw_sale_order set account_order=?3 ,account_img=?2,status=?5,account_time=?4 where id=?1 ",nativeQuery = true)
    void upload(Long id, String accountImg, String accountOrder, Timestamp accountTime, String status);

    /**
     * 效果反馈
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update zw_sale_order set posting_img=?2 ,posting_effect=?3,status=?4,new_order='1' where id=?1 ",nativeQuery = true)
    void feedback(Long id, String postingImg, String effect,String status);
}