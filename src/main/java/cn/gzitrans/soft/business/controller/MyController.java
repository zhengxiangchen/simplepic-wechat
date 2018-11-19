package cn.gzitrans.soft.business.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mxixm.fastboot.weixin.module.credential.WxJsTicketManager;
import com.mxixm.fastboot.weixin.module.js.WxJsApi;
import com.mxixm.fastboot.weixin.module.js.WxJsConfig;
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
	
	@Autowired
    private WxJsTicketManager wxJsTicketManager;
	
	@Value("${static_path}")
	private String staticPath;
	
	/**
	 * 跳转到‘我的’
	 * @return
	 */
	@RequestMapping(value = "/my")
	public String my(HttpServletRequest request, ModelMap modelMap){
		
		String url = request.getRequestURL().toString();
		WxWebUser webUser = WxWebUtils.getWxWebUserFromSession();
		
		WxUserEntity user = wxUserService.getByOpenId(webUser.getOpenId());
		WxJsConfig config =  wxJsTicketManager.getWxJsConfig(url,WxJsApi.hideAllNonBaseMenuItem);
		
		modelMap.put("config", config);
		modelMap.put("user", user);
		return "qinzi-user-center";
	}
	
	
	
	/**
	 * 跳转到‘我的历史上传记录’
	 * @return
	 */
	@RequestMapping(value = "/getMyHistory")
	public String toHistory(HttpServletRequest request, ModelMap modelMap){
		String url = request.getRequestURL().toString();
		WxJsConfig config =  wxJsTicketManager.getWxJsConfig(url,WxJsApi.hideAllNonBaseMenuItem);
		
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
		
		modelMap.put("config", config);
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
	public String getMyLikeHistory(HttpServletRequest request, ModelMap modelMap){
		String url = request.getRequestURL().toString();
		WxJsConfig config =  wxJsTicketManager.getWxJsConfig(url,WxJsApi.hideAllNonBaseMenuItem);
		
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
		
		modelMap.put("config", config);
		modelMap.put("myLikeList", returnList);
		
		return "mylike-history";
	}
	
	
	
	/**
	 * 删除上传记录
	 * @param request
	 * @param pictureUploadLogsId
	 */
	@RequestMapping(value = "/deleteOneHistory", method = RequestMethod.GET)
	@ResponseBody
	public void deleteOneHistory(@RequestParam Integer pictureUploadLogsId){
		PictureUploadLogsEntity pictureUploadLogs = pictureUploadLogsService.findOne(pictureUploadLogsId);
		pictureUploadLogs.setIsDelete(1);
		pictureUploadLogsService.updateIsDelete(1,pictureUploadLogsId);
	}

}
