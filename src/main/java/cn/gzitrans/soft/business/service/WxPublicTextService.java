package cn.gzitrans.soft.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gzitrans.soft.business.dao.WxPublicTextDao;
import cn.gzitrans.soft.business.entity.WxPublicTextEntity;

@Service
public class WxPublicTextService {
	
	@Autowired
	private WxPublicTextDao wxPublicTextDao;
	
	/**
	 * 保存用户发往公众号的内容
	 * @param wxPublicTextEntity
	 */
	public void save(WxPublicTextEntity wxPublicTextEntity) {
		wxPublicTextDao.save(wxPublicTextEntity);
	}

}
