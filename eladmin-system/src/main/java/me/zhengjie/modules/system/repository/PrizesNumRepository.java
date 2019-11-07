package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.PrizesNum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
* @author groot
* @date 2019-10-23
*/
public interface PrizesNumRepository extends JpaRepository<PrizesNum, Long>, JpaSpecificationExecutor {
    PrizesNum findByPrizesNum(String prizesNum);

    @Transactional
    @Modifying
    @Query(value = "update prizes_num set num=?2 where id=?1 ", nativeQuery = true)
    void updateNum(Long id,Long num);
}