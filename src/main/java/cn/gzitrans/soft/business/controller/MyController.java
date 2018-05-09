package cn.gzitrans.soft.business.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.mxixm.fastboot.weixin.util.WxWebUtils;
import com.mxixm.fastboot.weixin.web.WxWebUser;
import cn.gzitrans.soft.business.entity.DiscoverEntity;
import cn.gzitrans.soft.business.entity.PictureUploadLogsEntity;
import cn.gzitrans.soft.business.entity.UserLikePictureLogsEntity;
import cn.gzitrans.soft.business.entity.WxUserEntity;
import cn.gzitrans.soft.business.service.PictureUploadLogsService;
import cn.gzitrans.soft.business.service.UserLikePictureLogsService;
import cn.gzitrans.soft.business.service.WxUserService;

@Controller
@RequestMapping(value = "/wx/my")
public class MyController {
	
	public static Logger logger = LogManager.getLogger(DiscoverController.class);
	
	@Autowired
	private WxUserService wxUserService;
	
	@Autowired
	private PictureUploadLogsService pictureUploadLogsService;
	
	@Autowired
	private UserLikePictureLogsService userLikePictureLogsService;
	
	@Value("${static_path}")
	private String staticPath;
	
	/**
	 * 跳转到‘我的’
	 * @return
	 */
	@RequestMapping(value = "/my")
	public String my(ModelMap modelMap){
		WxWebUser webUser = WxWebUtils.getWxWebUserFromSession();
		
		WxUserEntity user = wxUserService.getByOpenId(webUser.getOpenId());
		
		modelMap.put("user", user);
		return "qinzi-user-center";
	}
	
	
	
	/**
	 * 跳转到‘我的历史上传记录’
	 * @return
	 */
	@RequestMapping(value = "/getMyHistory")
	public String toHistory(ModelMap modelMap){
		WxWebUser webUser = WxWebUtils.getWxWebUserFromSession();
		String openId = webUser.getOpenId();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DiscoverEntity discover;
		ArrayList<DiscoverEntity> returnList = new ArrayList<DiscoverEntity>();
		
		ArrayList<PictureUploadLogsEntity> list = pictureUploadLogsService.getMyUploadLogsList(openId);
		for(int i = 0; i < list.size(); i++){
			discover = new DiscoverEntity();
			PictureUploadLogsEntity entity = list.get(i);
			discover.setId(entity.getId());
			discover.setPictureUrl(staticPath + entity.getThumbnailPictureUrl());
			discover.setUploadTime(format.format(entity.getUploadTime()));
			discover.setLikeNumber(String.valueOf(entity.getLikeNumber()));
			discover.setShareNumber(String.valueOf(entity.getShareNumber()));
			String openid = entity.getOpenId();
			WxUserEntity user = wxUserService.getByOpenId(openid);
			discover.setNickName(user.getNickName());
			discover.setAvatarUrl(user.getHeadImgUrl());
			returnList.add(discover);
		}
		
		modelMap.put("historyList", returnList);
		
		return "qinzi-photo-record";
	}
	
	
	/**
	 * 跳转到‘我的点赞记录’
	 * @param request
	 * @param openId
	 * @return
	 */
	@RequestMapping(value = "/getMyLikeHistory", method = RequestMethod.GET)
	public String getMyLikeHistory(ModelMap modelMap){
		WxWebUser webUser = WxWebUtils.getWxWebUserFromSession();
		String openId = webUser.getOpenId();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ArrayList<DiscoverEntity> returnList = new ArrayList<DiscoverEntity>();
		DiscoverEntity discover;
		ArrayList<UserLikePictureLogsEntity> likeLogsList = userLikePictureLogsService.getMyLikes(openId);
		for(int i = 0; i < likeLogsList.size(); i++){
			discover = new DiscoverEntity();
			UserLikePictureLogsEntity likeEntity = likeLogsList.get(i);
			Long uploadId = likeEntity.getPictureUploadLogsId();
			PictureUploadLogsEntity uploadLog = pictureUploadLogsService.findOne(uploadId.intValue());
			discover.setId(uploadLog.getId());
			discover.setPictureUrl(staticPath + uploadLog.getThumbnailPictureUrl());
			discover.setUploadTime(format.format(uploadLog.getUploadTime()));
			discover.setLikeNumber(String.valueOf(uploadLog.getLikeNumber()));
			discover.setShareNumber(String.valueOf(uploadLog.getShareNumber()));
			
			WxUserEntity uploadUser = wxUserService.getByOpenId(uploadLog.getOpenId());
			discover.setNickName(uploadUser.getNickName());
			discover.setAvatarUrl(uploadUser.getHeadImgUrl());
			returnList.add(discover);
		}
		
		modelMap.put("myLikeList", returnList);
		
		return "mylike-history";
	}

}
