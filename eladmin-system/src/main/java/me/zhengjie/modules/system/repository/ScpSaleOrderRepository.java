package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.ScpSaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
* @author groot
* @date 2019-10-25
*/
public interface ScpSaleOrderRepository extends JpaRepository<ScpSaleOrder, Long>, JpaSpecificationExecutor {
    @Query(value = "SELECT MAX(t.scp_sale_number) FROM scp_sale_order t WHERE t.scp_sale_number LIKE ?1", nativeQuery = true)
    String getMaxSalesOrderNo(String salesOrderNo);

    /**
     * 修改状态
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update scp_sale_order set status='1',delete_date=NOW() where id=?1 ", nativeQuery = true)
    void sign(Long id);

    /**
     * 修改状态
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update scp_sale_order set erp_sale_number=?2 where id=?1 ", nativeQuery = true)
    void updateErp(Long id,String erpSaleNumber);

    List<ScpSaleOrder> findAllByStatus(String status);
}