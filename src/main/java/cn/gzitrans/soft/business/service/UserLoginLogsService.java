package cn.gzitrans.soft.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gzitrans.soft.business.dao.UserLoginLogsDao;
import cn.gzitrans.soft.business.entity.UserLoginLogsEntity;

@Service
public class UserLoginLogsService {
	
	@Autowired
	private UserLoginLogsDao userLoginLogsDao;
	
	/**
	 * 保存登录记录
	 * @param user
	 */
	public void save(UserLoginLogsEntity userLoginLogs){
		userLoginLogsDao.save(userLoginLogs);
    }

}
