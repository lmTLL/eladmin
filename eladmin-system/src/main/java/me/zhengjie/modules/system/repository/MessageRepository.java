package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author groot
* @date 2019-09-11
*/
public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor {
}