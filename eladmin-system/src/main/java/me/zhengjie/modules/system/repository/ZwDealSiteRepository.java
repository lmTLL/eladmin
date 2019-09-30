package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.ZwDealSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author groot
 * @date 2019-09-06
 */
public interface ZwDealSiteRepository extends JpaRepository<ZwDealSite, Long>, JpaSpecificationExecutor {
}