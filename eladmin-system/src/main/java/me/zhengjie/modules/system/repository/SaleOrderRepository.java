package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.SaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author groot
 * @date 2019-07-09
 */
public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long>, JpaSpecificationExecutor {
    /**
     * 标记已赶走
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update sale_order set out_time=?2 ,overdue_time=?3,status='3' where id=?1 ", nativeQuery = true)
    void signSaleOrder(Long id, String outTime, Timestamp overdueTime);


    /**
     * 标记已处理
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update sale_order set new_order='1' where id=?1 ", nativeQuery = true)
    void signHandle(Long id);

    /**
     * 标记已处理
     *
     * @param
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update sale_order set new_order='1' where sale_number=?1 and channel_user_id=?2", nativeQuery = true)
    void signHandleBySaleNumber(String saleNumber, Long id);

    /**
     * 申请撤销订单
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update sale_order set status=?3 ,remark=?2 where id=?1 ", nativeQuery = true)
    void cancelOrder(Long id, String remark, String status);

    /**
     * 上传付款截图
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update sale_order set account_order=?3 ,account_img=?2,status=?5,account_time=?4 where id=?1 ", nativeQuery = true)
    void upload(Long id, String accountImg, String accountOrder, Timestamp accountTime, String status);

    @Query(value = "SELECT MAX(t.sale_number) FROM sale_order t WHERE t.sale_number LIKE ?1", nativeQuery = true)
    String getMaxSalesOrderNo(String salesOrderNo);


    @Query(value = "SELECT MAX(t.payment_remark) FROM sale_order t WHERE t.payment_remark LIKE ?1", nativeQuery = true)
    String getMaxSalesOrderPaymentRemark(String paymentRemark);

    /**
     * 记录回跟时间
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update sale_order set last_backtime=?2 where id=?1 ", nativeQuery = true)
    void lastBack(Long id, String lastBacktime);

    /**
     * 修改信息
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update sale_order set site=?2,asin=?3,follow_type=?4,follow_price=?5,follow_time=?6,follow_shop_url=?7,follow_shop_name=?8 where id=?1 ", nativeQuery = true)
    void updateInfo(Long id, String site, String asin, String followType, String followPrice, String followTime, String followShopUrl, String followShopName);

    /**
     * 标记已付款
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update sale_order set finance_payment=?2 where id=?1 ", nativeQuery = true)
    void signPayment(Long id, String financePayment);

    SaleOrder findBySaleNumber(String saleNumber);
}