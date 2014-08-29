package com.oncecloud.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

/**
 * @author hehai
 * @version 2014/08/24
 */
@Component
public class ModifyAction extends HttpServlet {
	private static final long serialVersionUID = 1037909976587522851L;
	private VMDAO vmDAO;
	private FeeDAO feeDAO;
	private VolumeDAO volumeDAO;
	private EIPDAO eipDAO;
	private ImageDAO imageDAO;
	private LBDAO lbDAO;
	private RouterDAO routerDAO;
	private AlarmDAO alarmDAO;

	private VMDAO getVmDAO() {
		return vmDAO;
	}

	@Autowired
	private void setVmDAO(VMDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	private FeeDAO getFeeDAO() {
		return feeDAO;
	}

	@Autowired
	private void setFeeDAO(FeeDAO feeDAO) {
		this.feeDAO = feeDAO;
	}

	private VolumeDAO getVolumeDAO() {
		return volumeDAO;
	}

	@Autowired
	private void setVolumeDAO(VolumeDAO volumeDAO) {
		this.volumeDAO = volumeDAO;
	}

	private EIPDAO getEipDAO() {
		return eipDAO;
	}

	@Autowired
	private void setEipDAO(EIPDAO eipDAO) {
		this.eipDAO = eipDAO;
	}

	private ImageDAO getImageDAO() {
		return imageDAO;
	}

	@Autowired
	private void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}

	private LBDAO getLbDAO() {
		return lbDAO;
	}

	@Autowired
	private void setLbDAO(LBDAO lbDAO) {
		this.lbDAO = lbDAO;
	}

	private RouterDAO getRouterDAO() {
		return routerDAO;
	}

	@Autowired
	private void setRouterDAO(RouterDAO routerDAO) {
		this.routerDAO = routerDAO;
	}

	private AlarmDAO getAlarmDAO() {
		return alarmDAO;
	}

	@Autowired
	private void setAlarmDAO(AlarmDAO alarmDAO) {
		this.alarmDAO = alarmDAO;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("basicinformation")) {
				String modifyType = request.getParameter("modifytype");
				String modifyUuid = request.getParameter("modifyuuid");
				String modifyName = request.getParameter("modifyname");
				String modifyDesc = request.getParameter("modifydesc");
				modifyDesc = modifyDesc.trim();
				if (modifyDesc.equals("")) {
					modifyDesc = null;
				} else if (modifyDesc.length() > 80) {
					modifyDesc = modifyDesc.substring(0, 79);
				}
				if (modifyType.equals("instance")) {
					this.getVmDAO().updateName(modifyUuid, modifyName,
							modifyDesc);
					this.getFeeDAO().updateVmName(modifyUuid, modifyName);
					this.getFeeDAO().updateSnapshotVMName(modifyUuid,
							modifyName);
					JSONObject jo = new JSONObject();
					jo.put("isSuccess", true);
					out.print(jo.toString());
				} else if (modifyType.equals("volume")) {
					this.getVolumeDAO().updateName(modifyUuid, modifyName,
							modifyDesc);
					this.getFeeDAO().updateVolumeName(modifyUuid, modifyName);
					JSONObject jo = new JSONObject();
					jo.put("isSuccess", true);
					out.print(jo.toString());
				} else if (modifyType.equals("eip")) {
					this.getEipDAO().updateName(modifyUuid, modifyName,
							modifyDesc);
					this.getFeeDAO().updateEipName(modifyUuid, modifyName);
					JSONObject jo = new JSONObject();
					jo.put("isSuccess", true);
					out.print(jo.toString());
				} else if (modifyType.equals("image")) {
					this.getImageDAO().updateName(modifyUuid, modifyName,
							modifyDesc);
					this.getFeeDAO().updateImageName(modifyUuid, modifyName);
					JSONObject jo = new JSONObject();
					jo.put("isSuccess", true);
					out.print(jo.toString());
				} else if (modifyType.equals("lb")) {
					this.getLbDAO().updateName(modifyUuid, modifyName,
							modifyDesc);
					JSONObject jo = new JSONObject();
					jo.put("isSuccess", true);
					out.print(jo.toString());
				} else if (modifyType.equals("rt")) {
					this.getRouterDAO().updateName(modifyUuid, modifyName,
							modifyDesc);
					JSONObject jo = new JSONObject();
					jo.put("isSuccess", true);
					out.print(jo.toString());
				} else if (modifyType.equals("al")) {
					this.getAlarmDAO().updateName(modifyUuid, modifyName,
							modifyDesc);
					JSONObject jo = new JSONObject();
					jo.put("isSuccess", true);
					out.print(jo.toString());
				}
			}
		}
	}
}
