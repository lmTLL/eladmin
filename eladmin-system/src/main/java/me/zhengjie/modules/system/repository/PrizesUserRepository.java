package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.PrizesUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
* @author groot
* @date 2019-10-23
*/
public interface PrizesUserRepository extends JpaRepository<PrizesUser, Long>, JpaSpecificationExecutor {
    PrizesUser findByOpenId(String openId);


    /**
     * 标记已付款
     *
     * @param
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update prizes_user set num=?2 where open_id=?1 ", nativeQuery = true)
    void updateNum(String opneId, Long num);


    @Query(value = "SELECT num FROM prizes_user where  open_id = ?1", nativeQuery = true)
    Long getNum(String opneId);
}