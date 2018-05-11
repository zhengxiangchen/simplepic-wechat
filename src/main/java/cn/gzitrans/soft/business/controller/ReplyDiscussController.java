package cn.gzitrans.soft.business.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mxixm.fastboot.weixin.util.WxWebUtils;

import cn.gzitrans.soft.business.entity.DiscussEntity;
import cn.gzitrans.soft.business.entity.ReplyDiscussEntity;
import cn.gzitrans.soft.business.entity.ReplyInfoEntity;
import cn.gzitrans.soft.business.entity.WxUserEntity;
import cn.gzitrans.soft.business.service.DiscussService;
import cn.gzitrans.soft.business.service.ReplyDiscussService;
import cn.gzitrans.soft.business.service.WxUserService;

@Controller
@RequestMapping(value = "/wx/replyDiscuss")
public class ReplyDiscussController {
	
	public static Logger logger = LogManager.getLogger(ReplyDiscussController.class);
	
	@Autowired
	private WxUserService wxUserService;
	
	@Autowired
	private ReplyDiscussService replyDiscussService;
	
	@Autowired
	private DiscussService discussService;
	
	
	/**
	 * 跳转到回复页面
	 * @return
	 */
	@RequestMapping(value = "/toReplyDiscuss")
	public String toReplyDiscuss(ModelMap modelMap, @RequestParam Integer discussId){
		
		DiscussEntity discuss = discussService.getDiscussById(discussId);
		//之前评论的用户
		WxUserEntity fromUser = wxUserService.getByOpenId(discuss.getFromOpenId());
		
		modelMap.put("discuss", discuss);
		modelMap.put("fromUser", fromUser);
		
		return "write-reply-discuss";
	}
	
	
	
	/**
	 * 接收评论回复，存数据库
	 * @param discussId
	 * @param replyContent
	 * @return
	 */
	@RequestMapping(value = "/receiveReply", method = RequestMethod.GET)
	@ResponseBody
	public String receiveReply(@RequestParam Integer discussId, @RequestParam String replyContent){
		String openId = WxWebUtils.getWxWebUserFromSession().getOpenId();
		
		ReplyDiscussEntity replyDiscuss = new ReplyDiscussEntity();
		replyDiscuss.setDiscussId(discussId);
		replyDiscuss.setOpenId(openId);
		replyDiscuss.setReplyContent(replyContent);
		replyDiscuss.setReplyTime(new Timestamp(System.currentTimeMillis()));
		replyDiscussService.save(replyDiscuss);
		return "success";
	}
	
	
	/**
	 * 跳转到某条评论下所有回复的页面
	 * @return
	 */
	@RequestMapping(value = "/getAllReply")
	public String getAllReply(ModelMap modelMap, @RequestParam Integer discussId){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		DiscussEntity discuss = discussService.getDiscussById(discussId);
		//之前评论的用户
		WxUserEntity fromUser = wxUserService.getByOpenId(discuss.getFromOpenId());
		
		ArrayList<ReplyInfoEntity> replyInfoList = new ArrayList<ReplyInfoEntity>();
		ReplyInfoEntity replyInfoEntity;
		ArrayList<ReplyDiscussEntity> replyDiscussList = replyDiscussService.getListByDiscussId(discussId);
		for(int i = 0; i < replyDiscussList.size(); i++){
			replyInfoEntity = new ReplyInfoEntity();
			
			ReplyDiscussEntity replyDiscussEntity = replyDiscussList.get(i);
			
			replyInfoEntity.setReplyContent(replyDiscussEntity.getReplyContent());
			replyInfoEntity.setReplyTime(format.format(replyDiscussEntity.getReplyTime()));
			
			WxUserEntity user = wxUserService.getByOpenId(replyDiscussEntity.getOpenId());
			replyInfoEntity.setReplyUserName(user.getNickName());
			replyInfoEntity.setReplyUserImg(user.getHeadImgUrl());
			replyInfoList.add(replyInfoEntity);
		}
		
		modelMap.put("discuss", discuss);
		modelMap.put("fromUser", fromUser);
		modelMap.put("replyInfoList", replyInfoList);
		
		return "photo-comment-reply";
	}

}
