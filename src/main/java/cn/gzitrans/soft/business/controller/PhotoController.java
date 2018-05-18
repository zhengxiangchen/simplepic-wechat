package cn.gzitrans.soft.business.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;

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
import cn.gzitrans.soft.utils.HttpAccess;
import net.coobird.thumbnailator.Thumbnails;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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
		
		WxJsConfig config =  wxJsTicketManager.getWxJsConfig(url, WxJsApi.chooseImage, WxJsApi.uploadImage,WxJsApi.hideAllNonBaseMenuItem);
		
		modelMap.put("config", config);
		modelMap.put("staticPath", staticPath);
		
		return "qinzi-upload-photo";
	}
	
	
	
	/**
	 * 从微信服务器端获取图片保存本地并记录数据库
	 * 简化该图片
	 * @return
	 */
	@RequestMapping(value = "/downloadPicture")
	@ResponseBody
	public String downloadPicture(@RequestParam String img_serverId, @RequestParam String imgName){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		WxWebUser user = WxWebUtils.getWxWebUserFromSession();
		PictureUploadLogsEntity picUploadLog = new PictureUploadLogsEntity();
		picUploadLog.setOpenId(user.getOpenId());
		picUploadLog.setPictureName(imgName);
		//内部分装保存文件并返回文件名
		File file = null;
		try {
			file = wxMediaManager.getTempMedia(img_serverId).getFile();
		} catch (IOException e) {
			logger.error(e.getMessage());
			return e.getMessage();
		}
		String fileName = file.getName();
		
		
		logger.info("公众号openid = " + WxWebUtils.getWxWebUserFromSession().getOpenId() + " ,的用户上传了一张图片.");
        logger.info("图片上传时间 = " + format.format(System.currentTimeMillis()));
        logger.info("图片名称 = " + fileName);
		//调用接口简化图片
        //模拟接收到的简化图片为原图
        //保存简化后的图片到服务器本地
		
        /*String simplifyFileName = "simplify" + Math.random()*1000 + fileName;
        
        try {
			Thumbnails.of(filePath + fileName) .scale(1f) .outputQuality(0.1f) .toFile(filePath + simplifyFileName);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}*/
		
		
		String picture64 = imageToBase64(filePath + fileName);
		String url = "http://127.0.0.1:8082/api_v1/lab/picture/receive";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("picture64", picture64);
        
        String ret = HttpAccess.postNameValuePairRequest(url, map, "utf-8", "public");
        String simplifyFileName = base64ToImage(ret);
        if(simplifyFileName == null || simplifyFileName.length() <= 0){
        	logger.error("图片简化操作失败,base64转图片后返回为null");
        	return "error";
        }
        
        
        //调用google接口压缩图片
        String thumbnailFileName = "thumbnail" + Math.random()*1000 + fileName;
        try {
			Thumbnails.of(filePath + simplifyFileName) .scale(1f) .outputQuality(0.1f) .toFile(filePath + thumbnailFileName);
		} catch (IOException e) {
			logger.error(e.getMessage());
			return e.getMessage();
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
	
	
	
	//base64字符串转化成图片 
	public String base64ToImage(String imgStr) {
		//对字节数组字符串进行Base64解码并生成图片 
		if (imgStr == null) //图像数据为空 
		  return ""; 
		BASE64Decoder decoder = new BASE64Decoder(); 
		try {
		  //Base64解码 
		  byte[] b = decoder.decodeBuffer(imgStr); 
		  for(int i=0;i<b.length;++i){
		    if(b[i]<0){//调整异常数据 
		      b[i]+=256; 
		    } 
		  } 
		  //生成jpeg图片
		  String simplifyFileName = "simplify" + Math.random()*1000 + ".jpg";
		  String absPath = filePath + simplifyFileName;
		  
		  File targetFile = new File(filePath);  
		  if(!targetFile.exists()){    
            targetFile.mkdirs();    
		  }
		  
	      OutputStream out = new FileOutputStream(absPath);   
	      out.write(b); 
	      out.flush(); 
	      out.close();
	      return simplifyFileName;
	    }catch (Exception e) {
	      logger.error(e.getMessage());
	      return null; 
	    } 
	}
	
	
	public String imageToBase64(String picPath){
		//将图片文件转化为字节数组字符串，并对其进行Base64编码处理 
	    InputStream in = null; 
	    byte[] data = null; 
	    //读取图片字节数组 
	    try 
	    { 
	      in = new FileInputStream(picPath);     
	      data = new byte[in.available()]; 
	      in.read(data); 
	      in.close(); 
	    }  
	    catch (IOException e)  
	    { 
	    	logger.error(e.getMessage());
	    } 
	    //对字节数组Base64编码 
	    BASE64Encoder encoder = new BASE64Encoder(); 
	    return encoder.encode(data);//返回Base64编码过的字节数组字符串 
	  }

}
