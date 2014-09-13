package com.oncecloud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author cyh
 * @version 2014/03/28 拓扑图 位置表
 */
@Entity
@Table(name = "oc_position")
public class Position {
	private String objectUuid;
	private Integer positionX;
	private Integer positionY;

	public Position() {
	}

	@Override
	public String toString() {
		return "Position [objectUuid=" + objectUuid + ", positionX="
				+ positionX + ", positionY=" + positionY + "]";
	}

	@Id
	@Column(name = "object_uuid")
	public String getObjectUuid() {
		return objectUuid;
	}

	public void setObjectUuid(String objectUuid) {
		this.objectUuid = objectUuid;
	}

	@Column(name = "positionx")
	public Integer getPositionX() {
		return positionX;
	}

	public void setPositionX(Integer positionX) {
		this.positionX = positionX;
	}

	@Column(name = "positiony")
	public Integer getPositionY() {
		return positionY;
	}

	public void setPositionY(Integer positionY) {
		this.positionY = positionY;
	}

	public Position(String objectUuid, Integer positionX, Integer positionY) {
		super();
		this.objectUuid = objectUuid;
		this.positionX = positionX;
		this.positionY = positionY;
	}

	public String toJosnString() {
		return "{'objectUuid'='" + objectUuid + "', 'positionX'='" + positionX
				+ "', 'positionY'='" + positionY + "'}";
	}
}
