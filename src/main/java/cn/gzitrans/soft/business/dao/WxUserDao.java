package cn.gzitrans.soft.business.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import cn.gzitrans.soft.business.entity.WxUserEntity;

public interface WxUserDao extends CrudRepository<WxUserEntity, Long>{

	@Query("from WxUserEntity where openId = ?1")
	WxUserEntity getByOpenId(String openid);

}
