package cn.gzitrans.soft.business.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.gzitrans.soft.business.dao.ReplyDiscussDao;
import cn.gzitrans.soft.business.entity.ReplyDiscussEntity;

@Service
public class ReplyDiscussService {
	
	@Autowired
	private ReplyDiscussDao replyDiscussDao;

	
	/**
	 * 保存评论的回复
	 * @param replyDiscuss
	 */
	public void save(ReplyDiscussEntity replyDiscuss) {
		replyDiscussDao.save(replyDiscuss);
	}


	/**
	 * 根据评论id获取该评论下的所有回复信息
	 * @param id
	 * @return
	 */
	public ArrayList<ReplyDiscussEntity> getListByDiscussId(Integer id) {
		return replyDiscussDao.getListByDiscussId(id);
	}


	/**
	 * 根据评论id获取该评论下的回复数量
	 * @param id
	 * @return
	 */
	public Integer getCountByDiscussId(Long id) {
		return replyDiscussDao.getCountByDiscussId(id);
	}


	/**
	 * 根据评论id获取评论下的最后一条回复信息
	 * @param id
	 * @return
	 */
	public ReplyDiscussEntity getEndReplyByDiscussId(Long id) {
		return replyDiscussDao.getEndReplyByDiscussId(id);
	}

}
