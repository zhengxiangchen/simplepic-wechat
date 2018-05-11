package cn.gzitrans.soft.business.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reply_discuss")
public class ReplyDiscussEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name = "openid")
	private String openId;		//回复者openid
	
	@Column(name = "discuss_id")
	private Integer discussId;		//回复的是哪条评论
	
	
	@Column(name = "reply_content")
	private String replyContent;		//回复的内容
	
	
	@Column(name = "reply_time")
	private Timestamp replyTime;		//回复时间


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


	public Integer getDiscussId() {
		return discussId;
	}


	public void setDiscussId(Integer discussId) {
		this.discussId = discussId;
	}


	public String getReplyContent() {
		return replyContent;
	}


	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}


	public Timestamp getReplyTime() {
		return replyTime;
	}


	public void setReplyTime(Timestamp replyTime) {
		this.replyTime = replyTime;
	}
	
}
