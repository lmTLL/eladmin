package me.zhengjie.modules.system.repository;


import me.zhengjie.modules.system.domain.FollowDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author groot
 * @date 2019-07-23
 */
public interface FollowDetailsRepository extends JpaRepository<FollowDetails, Integer>, JpaSpecificationExecutor {


    @Query(value = "select * from follow_details t where t.asin = ?1 and shop_name!='Amazon_WareHouse'", nativeQuery = true)
    List<FollowDetails> getByAsin(String asin);


}