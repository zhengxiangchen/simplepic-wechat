package cn.gzitrans.soft.business.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wx_public_text")
public class WxPublicTextEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	
	@Column(name = "openid")
	private String openId;//用户openid
	
	
	@Column(name = "send_content")
	private String sendContent;//发往公众号的内容
	
	
	@Column(name = "send_time")
	private Timestamp sendTime;//发送时间


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


	public String getSendContent() {
		return sendContent;
	}


	public void setSendContent(String sendContent) {
		this.sendContent = sendContent;
	}


	public Timestamp getSendTime() {
		return sendTime;
	}


	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
}
