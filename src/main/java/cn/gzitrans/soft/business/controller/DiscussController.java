package cn.gzitrans.soft.business.controller;

import java.sql.Timestamp;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.mxixm.fastboot.weixin.util.WxWebUtils;
import com.mxixm.fastboot.weixin.web.WxWebUser;
import cn.gzitrans.soft.business.entity.DiscussEntity;
import cn.gzitrans.soft.business.entity.DiscussInfoEntity;
import cn.gzitrans.soft.business.entity.WxUserEntity;
import cn.gzitrans.soft.business.service.DiscussService;
import cn.gzitrans.soft.business.service.WxUserService;


@Controller
@RequestMapping(value = "/wx/discuss")
public class DiscussController {
	
	public static Logger logger = LogManager.getLogger(DiscussController.class);
	
	
	@Autowired
	private WxUserService wxUserService;
	
	@Autowired
	private DiscussService discussService;
	
	
	@Value("${static_path}")
	private String staticPath;
	
	/**
	 * 跳转到评论页面
	 * @return
	 */
	@RequestMapping(value = "/toDiscuss")
	public String toDiscuss(ModelMap modelMap, @RequestParam Integer id, @RequestParam String openId){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		WxWebUser webUser = WxWebUtils.getWxWebUserFromSession();
		
		WxUserEntity user = wxUserService.getByOpenId(webUser.getOpenId());
		
		modelMap.put("user", user);
		modelMap.put("date", format.format(System.currentTimeMillis()));
		modelMap.put("id", id);
		modelMap.put("openId", openId);
		modelMap.put("staticPath", staticPath);
		return "write-photo-comment";
		
	}
	
	
	/**
	 * 小程序发送评论信息，后台进行保存
	 * @param request
	 * @param pictureUploadLogsId
	 * @param openId
	 * @param discussContent
	 * @return
	 */
	@RequestMapping(value = "/receiveDiscuss", method = RequestMethod.GET)
	@ResponseBody
	public String receiveDiscuss(@RequestParam Integer pictureUploadLogsId, @RequestParam String openId, @RequestParam String discussContent){
		
		DiscussEntity discussEntity = new DiscussEntity();
		discussEntity.setPictureUploadLogsId(Long.valueOf(pictureUploadLogsId));
		discussEntity.setFromOpenId(openId);
		discussEntity.setDiscussContent(discussContent);
		discussEntity.setDiscussTime(new Timestamp(System.currentTimeMillis()));
		discussService.save(discussEntity);
		return "success";
	}
	
	
	/**
	 * 跳转到评论页面
	 * @return
	 */
	@RequestMapping(value = "/allDiscuss")
	public String allDiscuss(ModelMap modelMap, @RequestParam Integer id){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
			discussInfoList.add(discussInfo);
		}
		
		modelMap.put("discussInfoList", discussInfoList);
		
		return "photo-comment-reply";
		
	}

}
