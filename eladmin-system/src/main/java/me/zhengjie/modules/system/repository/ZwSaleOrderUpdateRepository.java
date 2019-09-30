package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.ZwSaleOrderUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author groot
 * @date 2019-09-27
 */
public interface ZwSaleOrderUpdateRepository extends JpaRepository<ZwSaleOrderUpdate, Long>, JpaSpecificationExecutor {
}