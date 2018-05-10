package cn.gzitrans.soft.business.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

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
import com.mxixm.fastboot.weixin.util.WxWebUtils;
import com.mxixm.fastboot.weixin.web.WxWebUser;

import cn.gzitrans.soft.business.entity.PictureUploadLogsEntity;
import cn.gzitrans.soft.business.service.PictureUploadLogsService;
import cn.gzitrans.soft.utils.FileUtils;
import net.coobird.thumbnailator.Thumbnails;

@Controller
@RequestMapping(value = "/wx/photo")
public class PhotoController {
	
	public static Logger logger = LogManager.getLogger(DiscoverController.class);
	
	@Autowired
    private WxJsTicketManager wxJsTicketManager;
	
	@Autowired
	private WxMediaManager wxMediaManager;
	
	@Autowired
	private PictureUploadLogsService pictureUploadLogsService;
	
	@Value("${wx.appid}")
	private String appId;
	
	@Value("${wx.callback-domain}")
	private String staticPath;
	
	@Value("${wx_downimg_path}")
	private String filePath;
	
	
	
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
	 * 从微信服务器端获取图片保存本地并记录数据库
	 * 简化该图片
	 * @return
	 */
	@RequestMapping(value = "/downloadPicture")
	@ResponseBody
	public String downloadPicture(@RequestParam String img_serverId){
		WxWebUser user = WxWebUtils.getWxWebUserFromSession();
		PictureUploadLogsEntity picUploadLog = new PictureUploadLogsEntity();
		picUploadLog.setOpenId(user.getOpenId());
		//内部分装保存文件并返回文件名
		File file = null;
		try {
			file = wxMediaManager.getTempMedia(img_serverId).getFile();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		String fileName = file.getName();
		
		
		//调用接口简化图片
        //模拟接收到的简化图片为原图
        //保存简化后的图片到服务器本地
        String simplifyFileName = "simplify" + Math.random()*1000 + fileName;
        
        try {
			Thumbnails.of(filePath + fileName) .scale(1f) .outputQuality(0.1f) .toFile(filePath + simplifyFileName);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
        
        
        //调用google接口压缩图片
        String thumbnailFileName = "thumbnail" + Math.random()*1000 + fileName;
        try {
			Thumbnails.of(filePath + fileName) .scale(1f) .outputQuality(0.1f) .toFile(filePath + thumbnailFileName);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		
		picUploadLog.setUploadPictureUrl(fileName);
        picUploadLog.setSimplifyPictureUrl(simplifyFileName);
        picUploadLog.setThumbnailPictureUrl(thumbnailFileName);
        picUploadLog.setUploadTime(new Timestamp(System.currentTimeMillis()));
        picUploadLog.setLikeNumber(0);
        picUploadLog.setShareNumber(0);
        picUploadLog.setIsDelete(0);
        pictureUploadLogsService.save(picUploadLog);
        
		return "success";
	}

}
