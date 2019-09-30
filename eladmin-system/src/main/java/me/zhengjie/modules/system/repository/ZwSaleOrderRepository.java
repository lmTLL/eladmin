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
    @Query(value = "SELECT MAX(t.zw_sale_number) FROM zw_sale_order t WHERE t.zw_sale_number LIKE ?1", nativeQuery = true)
    String getMaxSalesOrderNo(String salesOrderNo);

    /**
     * 上传付款截图
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update zw_sale_order set account_order=?3 ,account_img=?2,status=?5,account_time=?4 where id=?1 ", nativeQuery = true)
    void upload(Long id, String accountImg, String accountOrder, Timestamp accountTime, String status);

    /**
     * 效果反馈
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update zw_sale_order set posting_img=?2 ,posting_effect=?3,status=?4,new_order='1' where id=?1 ", nativeQuery = true)
    void feedback(Long id, String postingImg, String effect, String status);

    /**
     * 标记安排
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update zw_sale_order set new_order='1' where id=?1 ", nativeQuery = true)
    void arrange(Long id);

    /**
     * 撤销订单
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update zw_sale_order set status=?2,new_order='1',finance_payment='1' where id=?1 ", nativeQuery = true)
    void revoke(Long id, String status);


    /**
     * 修改备注
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update zw_sale_order set channel_remark=?2 where id=?1 ", nativeQuery = true)
    void updateChannelRemark(Long id, String channelRemark);

    ZwSaleOrder findByZwSaleNumber(String zwSaleNumber);

    /**
     * 标记已付款
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update zw_sale_order set finance_payment=?2 where id=?1 ", nativeQuery = true)
    void signPayment(Long id, String financePayment);


    /**
     * 站外订单修改
     *
     * @param id SaleOrder id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update zw_sale_order set site=?2,link=?3,product_name=?4,deal_site=?5,deal_price=?6,original_price=?7,code=?8,code_work=?9,discount=?10,start_date=?11,end_date=?12,estimated_time=?13 where id=?1 ", nativeQuery = true)
    void update(Long id, String site, String link, String productName, String dealSite, String dealPrice, String originalPrice, String code, String codeWork, String discount, String startDate, String endDate, String estimatedTime);
}