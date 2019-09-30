package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.Navigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author groot
 * @date 2019-08-14
 */
public interface NavigationRepository extends JpaRepository<Navigation, Integer>, JpaSpecificationExecutor {
}