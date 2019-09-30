package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.AmzConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author groot
 * @date 2019-08-27
 */
public interface AmzConfigRepository extends JpaRepository<AmzConfig, Long>, JpaSpecificationExecutor {
    @Query(value = "select * from amz_config where amz_account=?1", nativeQuery = true)
    AmzConfig findAmzConfigByAmzAccount(String amzAccount);
}