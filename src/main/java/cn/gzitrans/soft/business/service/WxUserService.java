package cn.gzitrans.soft.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gzitrans.soft.business.dao.WxUserDao;
import cn.gzitrans.soft.business.entity.WxUserEntity;

@Service
public class WxUserService {
	
	@Autowired
	private WxUserDao wxUserDao;
	
	
	/**
	 * 新关注用户-保存用户
	 * @param discussEntity
	 */
	public void save(WxUserEntity wxUserEntity) {
		wxUserDao.save(wxUserEntity);
	}


	/**
	 * 通过openid查找用户
	 * @param openid
	 * @return
	 */
	public WxUserEntity getByOpenId(String openid) {
		return wxUserDao.getByOpenId(openid);
	}

	/**
	 * 修改用户信息
	 * @param entity
	 */
	public void update(WxUserEntity entity) {
		wxUserDao.save(entity);
	}

}
