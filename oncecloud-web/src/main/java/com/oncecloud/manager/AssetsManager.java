package com.oncecloud.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.AssetsDAO;
import com.oncecloud.dao.QuotaDAO;
import com.oncecloud.dao.UserDAO;
import com.oncecloud.entity.Assets;
import com.oncecloud.entity.Quota;
import com.oncecloud.entity.User;
import com.oncecloud.ui.model.UserMoneyModel;
import com.oncecloud.ui.model.UserQuotaModel;

/**
 * @author cyh
 * @version 2014/09/12
 */
@Component
public class AssetsManager {
	private AssetsDAO assetsDAO;
	private QuotaDAO quotaDAO;
	private UserDAO userDAO;
	
	
	
	public UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	public void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
	}

	
	public AssetsDAO getAssetsDAO() {
		return assetsDAO;
	}

	@Autowired
	public void setAssetsDAO(AssetsDAO assetsDAO) {
		this.assetsDAO = assetsDAO;
	}

	///总资产
	public List<Assets> getAssets(int cid) {
	    return this.getAssetsDAO().getAssets(cid);
	}
	
	///用户配额列表
	public List<UserQuotaModel> getQuotaList(int cid,int page, int limit,String search) {
	   List<UserQuotaModel> models =new ArrayList<UserQuotaModel>();
	   List<User> listuser = this.getUserDAO().getOnePageUserList(page, limit, search);
	   for(User user : listuser)
	   {
		   UserQuotaModel usermodel=new UserQuotaModel();
		   Quota quota = this.getQuotaDAO().getQuotaTotal(user.getUserId());
		   usermodel.setUser(user);
		   usermodel.setQuota(quota);
		   models.add(usermodel);
	   }
	   return models;
	}
	
	///用户配额列表
	public List<UserMoneyModel> getAssetsMonthList(int cid,int page, int limit,String search) {
	   List<UserMoneyModel> models =new ArrayList<UserMoneyModel>();
	   List<Assets> assetslist = this.getAssetsDAO().getAssets(cid);
	   List<User> listuser = this.getUserDAO().getOnePageUserList(page, limit, search);
	
	   for(User user : listuser)
	   {
		   UserMoneyModel usermodel=new UserMoneyModel();
		   usermodel.setHostFee(2500);
		   usermodel.setNetEquipmentFee(2500);
		   usermodel.setNetworkFee(2500);
		   usermodel.setPowerFee(2500);
		   usermodel.setStorageFee(2500);
		   usermodel.setUserId(user.getUserId());
		   usermodel.setUserName(user.getUserName());
		   models.add(usermodel);
	   }
	   return models;
	}
	
}
