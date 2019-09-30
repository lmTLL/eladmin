package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author groot
 * @date 2019-07-09
 */
public interface ChannelRepository extends JpaRepository<Channel, Long>, JpaSpecificationExecutor {
    @Query(value = "select * from channel where open_id = ?1", nativeQuery = true)
    List<Channel> findChannelByOpenId(String openId);
}