package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.Dict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author groot
 * @date 2019-07-09
 */
public interface DictRepository extends JpaRepository<Dict, Long>, JpaSpecificationExecutor {
}