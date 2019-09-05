package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.AsinInfo;
import me.zhengjie.modules.system.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Timestamp;

/**
* @author groot
* @date 2019-07-22
*/
public interface TokenRepository extends JpaRepository<Token, Integer>, JpaSpecificationExecutor {

/**
 */
    @Transactional
    @Modifying
    @Query(value = "update token set token=?1 where id=1 ",nativeQuery = true)
    void upload(String token);
}