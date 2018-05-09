package cn.gzitrans.soft.business.dao;

import org.springframework.data.repository.CrudRepository;

import cn.gzitrans.soft.business.entity.UserLoginLogsEntity;


public interface UserLoginLogsDao extends CrudRepository<UserLoginLogsEntity, Long>{

}
