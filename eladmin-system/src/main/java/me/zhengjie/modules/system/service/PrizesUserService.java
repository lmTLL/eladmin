package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.PrizesUser;
import me.zhengjie.modules.system.service.dto.PrizesUserDTO;
import me.zhengjie.modules.system.service.dto.PrizesUserQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
* @author groot
* @date 2019-10-23
*/
@CacheConfig(cacheNames = "prizesUser")
public interface PrizesUserService {

/**
* queryAll 分页
* @param criteria
* @param pageable
* @return
*/
@Cacheable(keyGenerator = "keyGenerator")
Object queryAll(PrizesUserQueryCriteria criteria, Pageable pageable);

/**
* queryAll 不分页
* @param criteria
* @return
*/
@Cacheable(keyGenerator = "keyGenerator")
public Object queryAll(PrizesUserQueryCriteria criteria);

/**
* findById
* @param id
* @return
*/
@Cacheable(key = "#p0")
PrizesUserDTO findById(Long id);

/**
* create
* @param resources
* @return
*/
@CacheEvict(allEntries = true)
PrizesUserDTO create(PrizesUser resources);

/**
* update
* @param resources
*/
@CacheEvict(allEntries = true)
void update(PrizesUser resources);

/**
* delete
* @param id
*/
@CacheEvict(allEntries = true)
void delete(Long id);
}