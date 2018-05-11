package cn.gzitrans.soft.wx;

import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.mxixm.fastboot.weixin.annotation.WxApplication;
import com.mxixm.fastboot.weixin.annotation.WxButton;
import com.mxixm.fastboot.weixin.annotation.WxController;
import com.mxixm.fastboot.weixin.annotation.WxEventMapping;
import com.mxixm.fastboot.weixin.module.event.WxEvent;
import com.mxixm.fastboot.weixin.module.user.WxUser;
import com.mxixm.fastboot.weixin.module.web.WxRequest;

import cn.gzitrans.soft.business.entity.UserLoginLogsEntity;
import cn.gzitrans.soft.business.entity.WxUserEntity;
import cn.gzitrans.soft.business.service.UserLoginLogsService;
import cn.gzitrans.soft.business.service.WxUserService;
import cn.gzitrans.soft.utils.EmojiFilter;


@WxApplication(menuAutoCreate = true)
@WxController
public class WeixinController {
	
	public static Logger logger = LogManager.getLogger(WeixinController.class);
	
	@Autowired
	private WxUserService wxUserService;
	
	@Autowired
	private UserLoginLogsService userLoginLogsService;
	
	/**
     * 接受微信事件(用户关注公众号)
     * @param wxRequest
     * @param wxUser
     */
    @WxEventMapping(type = WxEvent.Type.SUBSCRIBE)
    public void subscribe(WxRequest wxRequest, WxUser wxUser) {
    	String openid = wxUser.getOpenId();
    	WxUserEntity entity = wxUserService.getByOpenId(openid);
    	if(entity == null){
    		WxUserEntity userEntity = new WxUserEntity();
        	userEntity.setSubscribe(wxUser.getSubscribe());
        	userEntity.setOpenId(wxUser.getOpenId());
        	userEntity.setNickName(EmojiFilter.filterEmoji(wxUser.getNickName()));
        	userEntity.setSex(wxUser.getSex());
        	userEntity.setCity(wxUser.getCity());
        	userEntity.setCountry(wxUser.getCountry());
        	userEntity.setProvince(wxUser.getProvince());
        	userEntity.setLanguage(wxUser.getLanguage());
        	userEntity.setHeadImgUrl(wxUser.getHeadImgUrl());
        	userEntity.setSubscribeTime(wxUser.getSubscribeTime());
        	userEntity.setUnionId(wxUser.getUnionId());
        	userEntity.setRemark(wxUser.getRemark());
        	userEntity.setGroupId(wxUser.getGroupId());
        	wxUserService.save(userEntity);
    	}else{
    		entity.setSubscribe(1);
    		entity.setNickName(EmojiFilter.filterEmoji(wxUser.getNickName()));
    		entity.setSex(wxUser.getSex());
    		entity.setCity(wxUser.getCity());
    		entity.setCountry(wxUser.getCountry());
    		entity.setProvince(wxUser.getProvince());
    		entity.setLanguage(wxUser.getLanguage());
    		entity.setHeadImgUrl(wxUser.getHeadImgUrl());
    		entity.setSubscribeTime(wxUser.getSubscribeTime());
    		entity.setUnionId(wxUser.getUnionId());
    		entity.setRemark(wxUser.getRemark());
    		entity.setGroupId(wxUser.getGroupId());
    		wxUserService.update(entity);
    	}
    	
    	logger.info(wxUser.getNickName() + "订阅了公众号");
    }
    
    
    /**
     * 接受微信事件(用户取消关注)
     * @param wxRequest
     * @param wxUser
     */
    @WxEventMapping(type = WxEvent.Type.UNSUBSCRIBE)
    public void unsubscribe(WxRequest wxRequest, WxUser wxUser) {
    	String openid = wxUser.getOpenId();
    	WxUserEntity entity = wxUserService.getByOpenId(openid);
    	if(entity == null){
    		
    	}else{
    		entity.setSubscribe(0);
    		wxUserService.update(entity);
    		logger.info(entity.getNickName() + "退订了公众号");
    	}
    	
    }
    
	/**
     * 定义微信菜单
     */
    @WxButton(type = WxButton.Type.CLICK, group = WxButton.Group.LEFT, main = true, name = "简图-小程序")
    public String left(WxRequest wxRequest, WxUser wxUser) {
    	logger.info(wxUser.getNickName() + "点击了'小程序'按钮");
    	return "点击打开小程序";
    }

    /**
     * 定义微信菜单
     */
    @WxButton(type = WxButton.Type.VIEW, url = "http://simplepicwx.tunnel.qydev.com/wx/discover/getDiscoverList", group = WxButton.Group.RIGHT, main = true, name = "简图-公众号")
    public void right(WxUser wxUser) {
    	//保存用户登录记录
    	UserLoginLogsEntity userLoginLogsEntity = new UserLoginLogsEntity();
    	userLoginLogsEntity.setOpenId(wxUser.getOpenId());
    	userLoginLogsEntity.setLoginTime(new Timestamp(System.currentTimeMillis()));
    	userLoginLogsService.save(userLoginLogsEntity);
    	logger.info(wxUser.getNickName() + "点击了'公众号'按钮");
    }
    
    
}
