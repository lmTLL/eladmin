package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.FileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
* @author groot
* @date 2019-07-24
*/
public interface FileStatusRepository extends JpaRepository<FileStatus, Long>, JpaSpecificationExecutor {
    @Query(value = "SELECT * FROM file_status t WHERE t.nickname=?1 and t.filename=?2",nativeQuery = true)
    FileStatus findFileStatusByNickname(String nickname,String filename);

    @Transactional
    @Modifying
    @Query(value = "update file_status set new_status='0' where filename=?1 and nickname=?2",nativeQuery = true)
    void sign(String filename,String nickname);

    @Transactional
    @Modifying
    @Query(value = "update file_status set new_status='1' where filename=?1 and nickname=?2",nativeQuery = true)
    void signByNickname(String filename,String nickname);
}