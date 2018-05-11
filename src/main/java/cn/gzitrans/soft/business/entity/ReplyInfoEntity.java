package cn.gzitrans.soft.business.entity;

/**
 * 用于存放查看某条评论展示全部回复所需要的信息
 * 创建人：Jarvan   
 * 创建时间：2018年5月11日 下午3:47:28
 */
public class ReplyInfoEntity {
	
	private String replyUserName;//回复者微信名
	
	private String replyUserImg;//回复者头像
	
	private String replyContent;//回复内容
	
	private String replyTime;//回复时间

	public String getReplyUserName() {
		return replyUserName;
	}

	public void setReplyUserName(String replyUserName) {
		this.replyUserName = replyUserName;
	}

	public String getReplyUserImg() {
		return replyUserImg;
	}

	public void setReplyUserImg(String replyUserImg) {
		this.replyUserImg = replyUserImg;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public String getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}

}
