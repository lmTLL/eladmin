package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.WaitPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
* @author groot
* @date 2019-08-01
*/
public interface WaitPaymentRepository extends JpaRepository<WaitPayment, Long>, JpaSpecificationExecutor {
    @Query(value = "SELECT * FROM wait_payment t WHERE t.payment_remarks= ?1",nativeQuery = true)
    WaitPayment getwaitPayment(String paymentRemarks);


    @Query(value = "SELECT * FROM wait_payment t WHERE t.payment_type='0'",nativeQuery = true)
    List<WaitPayment> getAllwaitPayment();

    @Transactional
    @Modifying
    @Query(value = "update wait_payment set payment_id=?2,payment_type=?3 where payment_remarks=?1 ",nativeQuery = true)
    void updateType(String paymentRemarks,String paymentId,String paymentType);
}