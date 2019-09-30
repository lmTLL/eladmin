package me.zhengjie.modules.system.service;


import me.zhengjie.modules.system.domain.Invitationcodes;
import me.zhengjie.modules.system.service.dto.InvitationcodesDTO;
import me.zhengjie.modules.system.service.dto.InvitationcodesQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
 * @author groot
 * @date 2019-07-05
 */
@CacheConfig(cacheNames = "invitationcodes")
public interface InvitationcodesService {

    /**
     * queryAll 分页
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    Object queryAll(InvitationcodesQueryCriteria criteria, Pageable pageable);

    /**
     * queryAll 不分页
     *
     * @param criteria
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    public Object queryAll(InvitationcodesQueryCriteria criteria);

    /**
     * findById
     *
     * @param invitationCode
     * @return
     */
    @CacheEvict(allEntries = true)
    InvitationcodesDTO findByInvitationCode(String invitationCode);

    /**
     * create
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    InvitationcodesDTO create(Invitationcodes resources);

    /**
     * update
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(Invitationcodes resources);

    /**
     * delete
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);

    @Cacheable(key = "#p0")
    void updateEnable(Long id);
}