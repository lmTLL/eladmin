package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.PrizesNum;
import me.zhengjie.modules.system.service.dto.PrizesNumDTO;
import me.zhengjie.modules.system.service.dto.PrizesNumQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
* @author groot
* @date 2019-10-23
*/
@CacheConfig(cacheNames = "prizesNum")
public interface PrizesNumService {

/**
* queryAll 分页
* @param criteria
* @param pageable
* @return
*/
@Cacheable(keyGenerator = "keyGenerator")
Object queryAll(PrizesNumQueryCriteria criteria, Pageable pageable);

/**
* queryAll 不分页
* @param criteria
* @return
*/
@Cacheable(keyGenerator = "keyGenerator")
public Object queryAll(PrizesNumQueryCriteria criteria);

/**
* findById
* @param id
* @return
*/
@Cacheable(key = "#p0")
PrizesNumDTO findById(Long id);

/**
* create
* @param resources
* @return
*/
@CacheEvict(allEntries = true)
PrizesNumDTO create(PrizesNum resources);

/**
* update
* @param resources
*/
@CacheEvict(allEntries = true)
void update(PrizesNum resources);

/**
* delete
* @param id
*/
@CacheEvict(allEntries = true)
void delete(Long id);
}