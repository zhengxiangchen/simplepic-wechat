package cn.gzitrans.soft.business.entity;

/**
 * 显示在具体信息页的评论下方的内容
 * 创建人：Jarvan   
 * 创建时间：2018年5月11日 下午2:48:44
 */
public class ReplyMessageEntity {
	
	private String replyUserName;	//回复者的微信名
	
	private String replyContent;	//回复的内容
	
	private Integer replyCount;		//这条评论总回复数

	public String getReplyUserName() {
		return replyUserName;
	}

	public void setReplyUserName(String replyUserName) {
		this.replyUserName = replyUserName;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public Integer getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}
}
