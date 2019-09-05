package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * @author groot
 * @date 2019-07-09
 */
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor {

    /**
     * findByName
     * @param name
     * @return
     */
    Role findByName(String name);

    Set<Role> findByUsers_Id(Long id);

    Set<Role> findByMenus_Id(Long id);

    /**
     * 新增邀请码
     * @param InvitationCode 邀请码
     * @param userid 增加人员id
     * @return
     */
    @Modifying
    @Query(value = "insert into invitationCodes(userId,invitationCode) values (?2,?1)",nativeQuery = true)
    int insertInvitationCode(String InvitationCode,String userid);
}
