package com.oncecloud.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.AssetsDAO;
import com.oncecloud.entity.Assets;

/**
 * @author cyh
 * @version 2014/09/12
 */
@Component
public class AssetsManager {
	private AssetsDAO assetsDAO;

	public AssetsDAO getAssetsDAO() {
		return assetsDAO;
	}

	@Autowired
	public void setAssetsDAO(AssetsDAO assetsDAO) {
		this.assetsDAO = assetsDAO;
	}

	public List<Assets> getAssets(int cid) {
	    return this.getAssetsDAO().getAssets(cid);
	}
	
}
