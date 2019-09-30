package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.ZwPostingEffect;
import me.zhengjie.modules.system.service.dto.ZwPostingEffectDTO;
import me.zhengjie.modules.system.service.dto.ZwPostingEffectQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;

/**
 * @author groot
 * @date 2019-09-19
 */
@CacheConfig(cacheNames = "zwPostingEffect")
public interface ZwPostingEffectService {

    /**
     * queryAll 分页
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @CacheEvict(allEntries = true)
    Object queryAll(ZwPostingEffectQueryCriteria criteria, Pageable pageable);

    /**
     * queryAll 不分页
     *
     * @param criteria
     * @return
     */
    @CacheEvict(allEntries = true)
    public Object queryAll(ZwPostingEffectQueryCriteria criteria);

    /**
     * findById
     *
     * @param id
     * @return
     */
    @CacheEvict(allEntries = true)
    ZwPostingEffectDTO findById(Long id);

    /**
     * create
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    ZwPostingEffectDTO create(ZwPostingEffect resources);

    /**
     * update
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(ZwPostingEffect resources);

    /**
     * delete
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);
}