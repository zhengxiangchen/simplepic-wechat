package cn.gzitrans.soft.business.entity;

import java.util.ArrayList;

public class DiscoverInfoEntity {
	
	private Long id;
	
	private String openId;//上传者openid
	
	private String pictureName;//用户自定义的图片名称
	
	private String pictureUrl;//原图地址
	
	private String simplifyPictureUrl;//简化图地址
	
	private String nickName;
	
	private String userHeadPicture;//作者头像
	
	private String uploadTime;
	
	private Integer likeNumber;
	
	private Integer shareNumber;
	
	private ArrayList<DiscussInfoEntity> discussInfo;
	
	private Integer isDelete;//是否删除标记0正常,1已删除

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getSimplifyPictureUrl() {
		return simplifyPictureUrl;
	}

	public void setSimplifyPictureUrl(String simplifyPictureUrl) {
		this.simplifyPictureUrl = simplifyPictureUrl;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public Integer getLikeNumber() {
		return likeNumber;
	}

	public void setLikeNumber(Integer likeNumber) {
		this.likeNumber = likeNumber;
	}

	public ArrayList<DiscussInfoEntity> getDiscussInfo() {
		return discussInfo;
	}

	public void setDiscussInfo(ArrayList<DiscussInfoEntity> discussInfo) {
		this.discussInfo = discussInfo;
	}

	public String getUserHeadPicture() {
		return userHeadPicture;
	}

	public void setUserHeadPicture(String userHeadPicture) {
		this.userHeadPicture = userHeadPicture;
	}

	public Integer getShareNumber() {
		return shareNumber;
	}

	public void setShareNumber(Integer shareNumber) {
		this.shareNumber = shareNumber;
	}
	
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("[id] = ");
		sb.append(id);
		sb.append(",[pictureUrl] = ");
		sb.append(pictureUrl);
		sb.append(",[simplifyPictureUrl] = ");
		sb.append(simplifyPictureUrl);
		sb.append(",[nickName] = ");
		sb.append(nickName);
		sb.append(",[userHeadPicture] = ");
		sb.append(userHeadPicture);
		sb.append(",[uploadTime] = ");
		sb.append(uploadTime);
		sb.append(",[likeNumber] = ");
		sb.append(likeNumber);
		sb.append(",[shareNumber] = ");
		sb.append(shareNumber);
		sb.append(",[discussInfo] = ");
		sb.append(discussInfo);
		sb.append(",[isDelete] = ");
		if(isDelete == 0){
			sb.append("正常");
		}else if(isDelete == 1){
			sb.append("已删除");
		}else{
			sb.append(isDelete);
		}
		return sb.toString();
	}
	
}
