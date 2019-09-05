package me.zhengjie.modules.system.repository;


import me.zhengjie.modules.system.domain.FileStatus;
import me.zhengjie.modules.system.domain.KeyMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
* @author groot
* @date 2019-07-26
*/
public interface KeyMsgRepository extends JpaRepository<KeyMsg, Long>, JpaSpecificationExecutor {
    @Query(value = "SELECT msg FROM key_msg t WHERE t.key_s=?1 ",nativeQuery = true)
    String findMsgByKey(String key);
}