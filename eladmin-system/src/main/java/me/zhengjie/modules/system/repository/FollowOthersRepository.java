package me.zhengjie.modules.system.repository;


import me.zhengjie.modules.system.domain.FollowOthers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author handsome
 * @date 2019-08-28
 */
public interface FollowOthersRepository extends JpaRepository<FollowOthers, Integer>, JpaSpecificationExecutor {


    @Query(value = "select id,title,price,asin,five_points,start_date from follow_others t where t.asin = ?1 ", nativeQuery = true)
    List<FollowOthers> getByAsin(String asin);
}