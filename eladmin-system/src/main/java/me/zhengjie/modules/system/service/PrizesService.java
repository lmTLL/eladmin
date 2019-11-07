package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.Prizes;
import me.zhengjie.modules.system.service.dto.PrizesDTO;
import me.zhengjie.modules.system.service.dto.PrizesQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
* @author groot
* @date 2019-10-22
*/
@CacheConfig(cacheNames = "prizes")
public interface PrizesService {

/**
* queryAll 分页
* @param criteria
* @param pageable
* @return
*/
@Cacheable(keyGenerator = "keyGenerator")
Object queryAll(PrizesQueryCriteria criteria, Pageable pageable);

/**
* queryAll 不分页
* @param criteria
* @return
*/
@Cacheable(keyGenerator = "keyGenerator")
public Object queryAll(PrizesQueryCriteria criteria);

/**
* findById
* @param id
* @return
*/
@Cacheable(key = "#p0")
PrizesDTO findById(Long id);

/**
* create
* @param resources
* @return
*/
@CacheEvict(allEntries = true)
PrizesDTO create(Prizes resources);

/**
* update
* @param resources
*/
@CacheEvict(allEntries = true)
void update(Prizes resources);

/**
* delete
* @param id
*/
@CacheEvict(allEntries = true)
void delete(Long id);
}