package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.AsinInfo;
import me.zhengjie.modules.system.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
* @author groot
* @date 2019-07-22
*/
public interface AsinInfoRepository extends JpaRepository<AsinInfo, Integer>, JpaSpecificationExecutor {


    @Query(value = "SELECT id,asin,title,site,exclude_shop,start_date,update_date,open_id,t.count,start_count,follow_listen,price_listen,title_listen,fivepoint_listen,nick_name,head_imgurl FROM asin_info t where t.asin = ?1 and t.site = ?2 and t.open_id = ?3",nativeQuery = true)
    AsinInfo getByAsin(String asin,String site,String openId);


    @Query(value = "SELECT * FROM token where id=1",nativeQuery = true)
    Token getToken();

    @Transactional
    @Modifying
    @Query(value = "update asin_info set count=?1 where id=?2",nativeQuery = true)
    int updateCount( int count,Integer id);



//    @Query(value = "SELECT MAX(t.sale_number) FROM sale_order t WHERE t.sale_number LIKE ?1",nativeQuery = true)
//    String getMaxSalesOrderNo(String salesOrderNo);

}