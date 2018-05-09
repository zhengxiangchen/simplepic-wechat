package cn.gzitrans.soft.business.dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import cn.gzitrans.soft.business.entity.UserLikePictureLogsEntity;


public interface UserLikePictureLogsDao extends CrudRepository<UserLikePictureLogsEntity, Long> {

	
	@Query("from UserLikePictureLogsEntity where openId = ? and pictureUploadLogsId = ?")
	UserLikePictureLogsEntity findOnEntity(String openId, Long pictureUploadLogsId);

	@Query("from UserLikePictureLogsEntity where openId = ?")
	ArrayList<UserLikePictureLogsEntity> getMyLikes(String openId);

}
