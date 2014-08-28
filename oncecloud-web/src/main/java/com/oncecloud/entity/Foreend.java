package com.oncecloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oc_fore_end")
public class Foreend {
	private String foreUuid;
	private String foreName;
	/**
	 * foreProtocol 前端监听协议 HTTP /TCP
	 */
	private String foreProtocol;
	private Integer forePort;
	/**
	 * 负载均衡策略 0轮询 1最小链接
	 */
	private Integer forePolicy;
	/**
	 * 对应负载均衡的id
	 */
	private String lbUuid;
	/**
	 * foreStatus 前端的状态 0/禁用 1/可用
	 */
	private Integer foreStatus;
	private Date createDate;

	public Foreend() {
	}

	// this method is used to precreate foreend
	public Foreend(String foreUuid, String foreName, String foreProtocol,
			Integer forePort, Integer forePolicy, String lbUuid,
			Integer foreStatus, Date createDate) {
		super();
		this.foreUuid = foreUuid;
		this.foreName = foreName;
		this.foreProtocol = foreProtocol;
		this.forePort = forePort;
		this.forePolicy = forePolicy;
		this.lbUuid = lbUuid;
		this.foreStatus = foreStatus;
		this.createDate = createDate;
	}

	@Id
	@Column(name = "fore_uuid")
	public String getForeUuid() {
		return foreUuid;
	}

	public void setForeUuid(String foreUuid) {
		this.foreUuid = foreUuid;
	}

	@Column(name = "fore_name")
	public String getForeName() {
		return foreName;
	}

	public void setForeName(String foreName) {
		this.foreName = foreName;
	}

	@Column(name = "fore_protocol")
	public String getForeProtocol() {
		return foreProtocol;
	}

	public void setForeProtocol(String foreProtocol) {
		this.foreProtocol = foreProtocol;
	}

	@Column(name = "fore_port")
	public Integer getForePort() {
		return forePort;
	}

	public void setForePort(Integer forePort) {
		this.forePort = forePort;
	}

	@Column(name = "fore_policy")
	public Integer getForePolicy() {
		return forePolicy;
	}

	public void setForePolicy(Integer forePolicy) {
		this.forePolicy = forePolicy;
	}

	@Column(name = "lb_uuid")
	public String getLbUuid() {
		return lbUuid;
	}

	public void setLbUuid(String lbUuid) {
		this.lbUuid = lbUuid;
	}

	@Column(name = "fore_status")
	public Integer getForeStatus() {
		return foreStatus;
	}

	public void setForeStatus(Integer foreStatus) {
		this.foreStatus = foreStatus;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "Foreend [foreUuid=" + foreUuid + ", foreName=" + foreName
				+ ", foreProtocol=" + foreProtocol + ", forePort=" + forePort
				+ ", forePolicy=" + forePolicy + ", lbUuid=" + lbUuid
				+ ", foreStatus=" + foreStatus + ", createDate=" + createDate
				+ "]";
	}

}
