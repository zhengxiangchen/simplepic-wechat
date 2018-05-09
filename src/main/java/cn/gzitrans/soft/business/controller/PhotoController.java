package cn.gzitrans.soft.business.controller;

import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mxixm.fastboot.weixin.module.js.WxJsApi;
import com.mxixm.fastboot.weixin.module.js.WxJsConfig;
import com.mxixm.fastboot.weixin.module.media.WxMediaManager;
import com.mxixm.fastboot.weixin.support.WxJsTicketManager;

@Controller
@RequestMapping(value = "/wx/photo")
public class PhotoController {
	
	public static Logger logger = LogManager.getLogger(DiscoverController.class);
	
	@Autowired
    private WxJsTicketManager wxJsTicketManager;
	
	@Autowired
	private WxMediaManager wxMediaManager;
	
	@Value("${wx.appid}")
	private String appId;
	
	@Value("${wx.callback-domain}")
	private String staticPath;
	
	
	
	/**
	 * 跳转到‘上传图片页面’
	 * @return
	 */
	@RequestMapping(value = "/toUpload")
	public String toUpload(HttpServletRequest request, ModelMap modelMap){
		String url = staticPath + request.getRequestURI();
		
		WxJsConfig config =  wxJsTicketManager.getWxJsConfig(url, WxJsApi.chooseImage, WxJsApi.uploadImage);
		
		modelMap.put("config", config);
		
		return "qinzi-upload-photo";
	}
	
	
	
	/**
	 * 跳转到‘上传图片页面’
	 * @return
	 */
	@RequestMapping(value = "/downloadPicture")
	@ResponseBody
	public String downloadPicture(@RequestParam String img_serverId){
		System.out.println("前端页面传递过来的serverid = " + img_serverId);
		System.out.println(wxMediaManager.getTempMedia(img_serverId));
		
		return "success";
	}

}
