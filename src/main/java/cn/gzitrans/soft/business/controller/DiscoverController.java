package cn.gzitrans.soft.business.controller;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map.Entry;

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

import com.mxixm.fastboot.weixin.module.js.WxJsApi;
import com.mxixm.fastboot.weixin.module.js.WxJsConfig;
import com.mxixm.fastboot.weixin.support.WxJsTicketManager;
import com.mxixm.fastboot.weixin.util.WxWebUtils;
import com.mxixm.fastboot.weixin.web.WxWebUser;

import cn.gzitrans.soft.business.entity.DiscoverEntity;
import cn.gzitrans.soft.business.entity.DiscoverInfoEntity;
import cn.gzitrans.soft.business.entity.DiscussEntity;
import cn.gzitrans.soft.business.entity.DiscussInfoEntity;
import cn.gzitrans.soft.business.entity.PictureUploadLogsEntity;
import cn.gzitrans.soft.business.entity.ReplyDiscussEntity;
import cn.gzitrans.soft.business.entity.ReplyMessageEntity;
import cn.gzitrans.soft.business.entity.UserLikePictureLogsEntity;
import cn.gzitrans.soft.business.entity.WxUserEntity;
import cn.gzitrans.soft.business.service.DiscussService;
import cn.gzitrans.soft.business.service.PictureUploadLogsService;
import cn.gzitrans.soft.business.service.ReplyDiscussService;
import cn.gzitrans.soft.business.service.UserLikePictureLogsService;
import cn.gzitrans.soft.business.service.WxUserService;

@Controller
@RequestMapping(value = "/wx/discover")
public class DiscoverController {
	
	public static Logger logger = LogManager.getLogger(DiscoverController.class);
	
	@Autowired
	private WxUserService wxUserService;
	
	@Autowired
	private PictureUploadLogsService pictureUploadLogsService;
	
	@Autowired
	private DiscussService discussService;
	
	@Autowired
	private UserLikePictureLogsService userLikePictureLogsService;
	
	@Autowired
    private WxJsTicketManager wxJsTicketManager;
	
	@Autowired
	private ReplyDiscussService replyDiscussService;
	
	//第一次加载评论的数量
	@Value("${discuss_index_count}")
	private int discuss_index_count;
	
	//第一次加载发现的数量
	@Value("${discover_index_count}")
	private int discover_index_count;
	
	@Value("${discover_more_count}")
	private int discover_more_count;
	
	@Value("${static_path}")
	private String staticPath;
	
	@Value("${wx.callback-domain}")
	private String urlPath;
	
	@Value("${wx.appid}")
	private String appId;
	
	/**
	 * 跳转到首页
	 * @return
	 */
	@RequestMapping(value = "/getDiscoverList")
	public String getDiscoverList(HttpServletRequest request, ModelMap modelMap){
		WxWebUser webUser = WxWebUtils.getWxWebUserFromSession();
		String userOpenId = webUser.getOpenId();
		WxUserEntity userEntity = wxUserService.getByOpenId(userOpenId);
		if(userEntity == null){
			logger.info("未关注用户访问首页,请先关注");
			return "redirect:https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzI3NTE3MDQzNw==&scene=123#wechat_redirect";
		}else if(userEntity.getSubscribe() == 0){
			logger.info("已经取消关注的用户访问首页,请先关注");
			return "redirect:https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzI3NTE3MDQzNw==&scene=123#wechat_redirect";
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		DiscoverEntity discover;
		ArrayList<DiscoverEntity> returnList = new ArrayList<DiscoverEntity>();
		ArrayList<PictureUploadLogsEntity> list = pictureUploadLogsService.getPictureUploadLogsList(discover_index_count);
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
		
		
		//去获取对接微信js的配置信息
		//获得调用js的url地址：http://simplepicwx.tunnel.qydev.com/wx/discover/getDiscoverInfo?id=12
		StringBuffer url = new StringBuffer();
		url.append(request.getRequestURL());
		if(!request.getParameterMap().isEmpty()){
			url.append("?");
		}
		StringBuffer endUrl = new StringBuffer();
		for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
			if(endUrl.length() > 0){
				endUrl.append("&");
			}
			endUrl.append(entry.getKey());
			endUrl.append("=");
			endUrl.append(entry.getValue()[0]);
		}
		url.append(endUrl.toString());
		WxJsConfig config =  wxJsTicketManager.getWxJsConfig(url.toString(), WxJsApi.onMenuShareAppMessage, WxJsApi.onMenuShareTimeline);
		
		modelMap.put("discoverList", returnList);
		modelMap.put("config", config);
		modelMap.put("staticPath", staticPath);
		
		return "qinzi-activity-index";
	}
	
	
	
	/**
	 * 加载更多的发现
	 * @param request
	 * @param itemsLength
	 * @return
	 */
	@RequestMapping(value = "/getMoreDiscover", method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<DiscoverEntity> getMoreDiscover(HttpServletRequest request, @RequestParam String itemsLength){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DiscoverEntity discover;
		ArrayList<DiscoverEntity> returnList = new ArrayList<DiscoverEntity>();
		Long allCount = pictureUploadLogsService.getAllCount();
		if(Integer.parseInt(itemsLength) == allCount.intValue()){
			
		}else{
			int beginId = allCount.intValue() - Integer.parseInt(itemsLength);
			ArrayList<PictureUploadLogsEntity> list = pictureUploadLogsService.getMoreLogsList(beginId,discover_more_count);
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
		}
		return returnList;
	}
	
	
	
	/**
	 * 点击发现栏目下的每一个项目,获取对应id的发现的内容以及评价
	 * @param request
	 * @param discoverId
	 * @return
	 */
	@RequestMapping(value = "/getDiscoverInfo", method = RequestMethod.GET)
	public String getDiscoverInfo(HttpServletRequest request, ModelMap modelMap, @RequestParam Integer id){
		WxWebUser webUser = WxWebUtils.getWxWebUserFromSession();
		String userOpenId = webUser.getOpenId();
		WxUserEntity userEntity = wxUserService.getByOpenId(userOpenId);
		if(userEntity == null){
			logger.info("未关注用户访问图片详情页,请先关注");
			return "redirect:https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzI3NTE3MDQzNw==&scene=123#wechat_redirect";
		}else if(userEntity.getSubscribe() == 0){
			logger.info("已经取消关注的用户访问图片详情页,请先关注");
			return "redirect:https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzI3NTE3MDQzNw==&scene=123#wechat_redirect";
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		PictureUploadLogsEntity picLogs = pictureUploadLogsService.findOne(id);
		DiscoverInfoEntity discoverInfo = new DiscoverInfoEntity();
		discoverInfo.setId(picLogs.getId());
		discoverInfo.setOpenId(picLogs.getOpenId());
		discoverInfo.setPictureName(picLogs.getPictureName());
		discoverInfo.setPictureUrl(staticPath + picLogs.getUploadPictureUrl());
		discoverInfo.setSimplifyPictureUrl(staticPath + picLogs.getSimplifyPictureUrl());
		discoverInfo.setUploadTime(format.format(picLogs.getUploadTime()));
		discoverInfo.setLikeNumber(picLogs.getLikeNumber());
		discoverInfo.setShareNumber(picLogs.getShareNumber());
		discoverInfo.setIsDelete(picLogs.getIsDelete());
		
		WxUserEntity user = wxUserService.getByOpenId(picLogs.getOpenId());
		discoverInfo.setNickName(user.getNickName());
		discoverInfo.setUserHeadPicture(user.getHeadImgUrl());
		
		ArrayList<DiscussEntity> discussList = discussService.getAllListByPictureUploadLogsId(id);
		DiscussEntity discussEntity;
		DiscussInfoEntity discussInfo;
		ArrayList<DiscussInfoEntity> discussInfoList = new ArrayList<DiscussInfoEntity>();
		for(int i = 0; i < discussList.size(); i++){
			discussInfo = new DiscussInfoEntity();
			discussEntity = discussList.get(i);
			discussInfo.setId(discussEntity.getId());
			discussInfo.setDiscussContent(discussEntity.getDiscussContent());
			discussInfo.setDiscussTime(format.format(discussEntity.getDiscussTime()));
			
			String openid = discussEntity.getFromOpenId();
			
			WxUserEntity discussUser = wxUserService.getByOpenId(openid);
			
			discussInfo.setNickName(discussUser.getNickName());
			discussInfo.setUserHeadPicture(discussUser.getHeadImgUrl());
			
			//该评论回复内容的查找和设置
			Integer replyCount = replyDiscussService.getCountByDiscussId(discussEntity.getId());
			if(replyCount <= 0){
				//无评论回复
			}else{
				//有评论回复，无论多少条，只取最后一条
				ReplyDiscussEntity replyDiscuss = replyDiscussService.getEndReplyByDiscussId(discussEntity.getId());
				String replyUserOpenId = replyDiscuss.getOpenId();
				WxUserEntity replyUser = wxUserService.getByOpenId(replyUserOpenId);
				
				ReplyMessageEntity replyMessageEntity = new ReplyMessageEntity();
				replyMessageEntity.setReplyContent(replyDiscuss.getReplyContent());
				replyMessageEntity.setReplyCount(replyCount);
				replyMessageEntity.setReplyUserName(replyUser.getNickName());
				discussInfo.setReplyMessage(replyMessageEntity);
			}
			discussInfoList.add(discussInfo);
		}
		
		discoverInfo.setDiscussInfo(discussInfoList);
		
		//去获取对接微信js的配置信息
		//获得调用js的url地址：http://simplepicwx.tunnel.qydev.com/wx/discover/getDiscoverInfo?id=12
		StringBuffer url = new StringBuffer();
		url.append(request.getRequestURL());
		url.append("?id=");
		url.append(discoverInfo.getId());
		WxJsConfig config =  wxJsTicketManager.getWxJsConfig(url.toString(), WxJsApi.previewImage, WxJsApi.onMenuShareAppMessage, WxJsApi.onMenuShareTimeline);
		
		modelMap.put("discoverInfo", discoverInfo);
		modelMap.put("config", config);
		modelMap.put("staticPath", staticPath);
		
		return "qinzi-activity-detail";
	}
	
	
	
	/**
	 * 根据用户openid和上传记录id查找点赞记录表中是否有该条数据
	 * @param request
	 * @param openId
	 * @param pictureUploadLogsId
	 * @return
	 */
	@RequestMapping(value = "/toCheckLike", method = RequestMethod.GET)
	@ResponseBody
	public boolean toCheckLike(HttpServletRequest request, @RequestParam String openId, @RequestParam Integer pictureUploadLogsId){
		boolean ret;
		UserLikePictureLogsEntity userLike = userLikePictureLogsService.findOnEntity(openId,pictureUploadLogsId);
		if(userLike != null){
			ret = true;
		}else{
			ret = false;
		}
		return ret;
	}
	
	
	
	/**
	 * 改变点赞状态
	 * @param request
	 * @param openId
	 * @param pictureUploadLogsId
	 * @return
	 */
	@RequestMapping(value = "/toChangeLike", method = RequestMethod.GET)
	@ResponseBody
	public Integer toChangeLike(HttpServletRequest request, @RequestParam String openId, @RequestParam Integer pictureUploadLogsId){
		
		UserLikePictureLogsEntity userLike = userLikePictureLogsService.findOnEntity(openId,pictureUploadLogsId);
		PictureUploadLogsEntity pictureUploadLog = pictureUploadLogsService.findOne(pictureUploadLogsId);
		Integer likeNumber = pictureUploadLog.getLikeNumber();
		if(likeNumber == null){
			likeNumber = 0;
		}
		Integer nowLikeNumber;
		//如果存在则删除该条点赞记录,将点赞数减1
		//如果不存在则新建点赞记录,将点赞数加1
		if(userLike != null){
			userLikePictureLogsService.delete(userLike.getId());
			nowLikeNumber = likeNumber - 1;
			pictureUploadLogsService.updateLikeNumber(nowLikeNumber,pictureUploadLogsId);
		}else{
			userLike = new UserLikePictureLogsEntity();
			userLike.setOpenId(openId);
			userLike.setPictureUploadLogsId(Long.valueOf(pictureUploadLogsId));
			userLike.setLikeTime(new Timestamp(System.currentTimeMillis()));
			userLikePictureLogsService.save(userLike);
			
			nowLikeNumber = likeNumber + 1;
			pictureUploadLogsService.updateLikeNumber(nowLikeNumber,pictureUploadLogsId);
		}
		return nowLikeNumber;
	}
	
	
	
	/**
	 * 小程序在发现页面分享成功后记录的图片分享次数加1
	 * @param request
	 * @param pictureUploadLogsId
	 * @return
	 */
	@RequestMapping(value = "/share", method = RequestMethod.GET)
	@ResponseBody
	public Integer share(HttpServletRequest request, @RequestParam Integer pictureUploadLogsId){
		PictureUploadLogsEntity entity = pictureUploadLogsService.findOne(pictureUploadLogsId);
		Integer beforeNumber = entity.getShareNumber();
		Integer afterNumber = beforeNumber + 1;
		pictureUploadLogsService.updateShareNumber(afterNumber, pictureUploadLogsId);
		return afterNumber;
	}
	
	
}
