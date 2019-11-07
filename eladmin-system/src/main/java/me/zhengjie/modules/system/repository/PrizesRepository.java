package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.Prizes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
* @author groot
* @date 2019-10-22
*/
public interface PrizesRepository extends JpaRepository<Prizes, Long>, JpaSpecificationExecutor {
    Prizes findByOpenId(String openId);

    @Query(value = "SELECT * FROM(SELECT COUNT(*) num,id,open_id,nickname,headimgurl,prizes_name,getprizes_date,vx_id FROM `prizes` where nickname!='' GROUP BY prizes_name ) t ORDER BY t.num ", nativeQuery = true)
    List<Prizes> getNum();
}