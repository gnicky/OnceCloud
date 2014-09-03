package com.oncecloud.manager;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.AlarmDAO;
import com.oncecloud.dao.EIPDAO;
import com.oncecloud.dao.FeeDAO;
import com.oncecloud.dao.ImageDAO;
import com.oncecloud.dao.LBDAO;
import com.oncecloud.dao.RouterDAO;
import com.oncecloud.dao.VMDAO;
import com.oncecloud.dao.VolumeDAO;

@Component
public class ModifyManager {
	private VMDAO vmDAO;
	private FeeDAO feeDAO;
	private VolumeDAO volumeDAO;
	private EIPDAO eipDAO;
	private ImageDAO imageDAO;
	private LBDAO lbDAO;
	private RouterDAO routerDAO;
	private AlarmDAO alarmDAO;
	
	public VMDAO getVmDAO() {
		return vmDAO;
	}
	
	@Autowired
	public void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	public FeeDAO getFeeDAO() {
		return feeDAO;
	}

	@Autowired
	public void setFeeDAO(FeeDAO feeDAO) {
		this.feeDAO = feeDAO;
	}

	public VolumeDAO getVolumeDAO() {
		return volumeDAO;
	}

	@Autowired
	public void setVolumeDAO(VolumeDAO volumeDAO) {
		this.volumeDAO = volumeDAO;
	}

	public EIPDAO getEipDAO() {
		return eipDAO;
	}

	@Autowired
	public void setEipDAO(EIPDAO eipDAO) {
		this.eipDAO = eipDAO;
	}

	public ImageDAO getImageDAO() {
		return imageDAO;
	}

	@Autowired
	public void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}

	public LBDAO getLbDAO() {
		return lbDAO;
	}

	@Autowired
	public void setLbDAO(LBDAO lbDAO) {
		this.lbDAO = lbDAO;
	}

	public RouterDAO getRouterDAO() {
		return routerDAO;
	}

	@Autowired
	public void setRouterDAO(RouterDAO routerDAO) {
		this.routerDAO = routerDAO;
	}

	public AlarmDAO getAlarmDAO() {
		return alarmDAO;
	}

	@Autowired
	public void setAlarmDAO(AlarmDAO alarmDAO) {
		this.alarmDAO = alarmDAO;
	}

	public JSONObject modifyBasicInfo(String modifyUuid, String modifyName, String modifyDesc,String modifyType) {
		JSONObject jo = new JSONObject();
		if (modifyType.equals("instance")) {
			this.getVmDAO().updateName(modifyUuid, modifyName,
					modifyDesc);
			this.getFeeDAO().updateVmName(modifyUuid, modifyName);
			this.getFeeDAO().updateSnapshotVMName(modifyUuid,
					modifyName);
			jo.put("isSuccess", true);
		} else if (modifyType.equals("volume")) {
			this.getVolumeDAO().updateName(modifyUuid, modifyName,
					modifyDesc);
			this.getFeeDAO().updateVolumeName(modifyUuid, modifyName);
			jo.put("isSuccess", true);
		} else if (modifyType.equals("eip")) {
			this.getEipDAO().updateName(modifyUuid, modifyName,
					modifyDesc);
			this.getFeeDAO().updateEipName(modifyUuid, modifyName);
			jo.put("isSuccess", true);
		} else if (modifyType.equals("image")) {
			this.getImageDAO().updateName(modifyUuid, modifyName,
					modifyDesc);
			this.getFeeDAO().updateImageName(modifyUuid, modifyName);
			jo.put("isSuccess", true);
		} else if (modifyType.equals("lb")) {
			this.getLbDAO().updateName(modifyUuid, modifyName,
					modifyDesc);
			jo.put("isSuccess", true);
		} else if (modifyType.equals("rt")) {
			this.getRouterDAO().updateName(modifyUuid, modifyName,
					modifyDesc);
			jo.put("isSuccess", true);
		} else if (modifyType.equals("al")) {
			this.getAlarmDAO().updateName(modifyUuid, modifyName,
					modifyDesc);
			jo.put("isSuccess", true);
		}
		return jo;
	}
}
