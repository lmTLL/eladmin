package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.ZwPostingEffect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author groot
* @date 2019-09-19
*/
public interface ZwPostingEffectRepository extends JpaRepository<ZwPostingEffect, Long>, JpaSpecificationExecutor {
}