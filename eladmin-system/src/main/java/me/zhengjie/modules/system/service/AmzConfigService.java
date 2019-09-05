package me.zhengjie.modules.system.service;
import me.zhengjie.modules.system.domain.AmzConfig;
import me.zhengjie.modules.system.service.dto.AmzConfigDTO;
import me.zhengjie.modules.system.service.dto.AmzConfigQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;

/**
* @author groot
* @date 2019-08-27
*/
@CacheConfig(cacheNames = "amzConfig")
public interface AmzConfigService {

    /**
    * queryAll 分页
    * @param criteria
    * @param pageable
    * @return
    */
    @CacheEvict(allEntries = true)
    Object queryAll(AmzConfigQueryCriteria criteria, Pageable pageable);

    /**
    * queryAll 不分页
    * @param criteria
    * @return
    */
    @CacheEvict(allEntries = true)
    public Object queryAll(AmzConfigQueryCriteria criteria);

    /**
     * findById
     * @param id
     * @return
     */
    @CacheEvict(allEntries = true)
    AmzConfigDTO findById(Long id);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    AmzConfigDTO create(AmzConfig resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(AmzConfig resources);

    /**
     * delete
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);
}