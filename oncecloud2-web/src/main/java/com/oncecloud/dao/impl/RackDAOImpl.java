package com.oncecloud.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.oncecloud.dao.RackDAO;
import com.oncecloud.entity.Rack;

@Component("RackDAO")
public class RackDAOImpl implements RackDAO {

	public Rack getRack(String rackUuid) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Rack> getOnePageRackList(int page, int limit, String search) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Rack> getRackList() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Rack> getRackListOfDC(String dcUuid) {
		// TODO Auto-generated method stub
		return null;
	}

	public int countAllRackList(String search) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Rack createRack(String rackName, String dcId, String rackDesc) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteRack(String rackId) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean bindDatacenter(String rackId, String dcId) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean unbindDatacenter(String rackId) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean updateRack(String rackUuid, String rackName,
			String rackDesc, String dcUuid) {
		// TODO Auto-generated method stub
		return false;
	}

}
