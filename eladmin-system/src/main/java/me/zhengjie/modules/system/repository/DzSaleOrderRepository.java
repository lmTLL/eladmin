package me.zhengjie.modules.system.repository;
import me.zhengjie.modules.system.domain.DzSaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
* @author groot
* @date 2019-11-01
*/
public interface DzSaleOrderRepository extends JpaRepository<DzSaleOrder, Long>, JpaSpecificationExecutor {

    @Query(value = "SELECT MAX(t.dz_sale_number) FROM dz_sale_order t WHERE t.dz_sale_number LIKE ?1", nativeQuery = true)
    String getMaxSalesOrderNo(String salesOrderNo);

    /**
     * 修改状态
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update dz_sale_order set status=?2 where id=?1 ", nativeQuery = true)
    void sign(Long id,String status);

    /**
     * 修改状态
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update dz_sale_order set account_order=?2,account_img=?3,status='1',update_date=now()  where id=?1 ", nativeQuery = true)
    void payment(Long id,String accountOrder,String accountImg);
}