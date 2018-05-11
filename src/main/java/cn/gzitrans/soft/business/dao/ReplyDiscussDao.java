package cn.gzitrans.soft.business.dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import cn.gzitrans.soft.business.entity.ReplyDiscussEntity;

public interface ReplyDiscussDao extends CrudRepository<ReplyDiscussEntity, Long>{

	@Query(value = "from ReplyDiscussEntity where discussId=? order by id desc")
	ArrayList<ReplyDiscussEntity> getListByDiscussId(Integer id);

	@Query(value = "select count(*) from reply_discuss where discuss_id =?", nativeQuery=true)
	Integer getCountByDiscussId(Long id);

	@Query(value = "select * from reply_discuss where discuss_id =? order by id desc limit 1", nativeQuery=true)
	ReplyDiscussEntity getEndReplyByDiscussId(Long id);

}
