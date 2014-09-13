package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Volume;
import com.oncecloud.entity.VolumeStatus;
import com.oncecloud.helper.SessionHelper;

/**
 * @author yly hehai
 * @version 2014/08/23
 */
@Component
public class VolumeDAO {
	private SessionHelper sessionHelper;
	private QuotaDAO quotaDAO;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	private QuotaDAO getQuotaDAO() {
		return quotaDAO;
	}

	@Autowired
	private void setQuotaDAO(QuotaDAO quotaDAO) {
		this.quotaDAO = quotaDAO;
	}

	/**
	 * 获取硬盘
	 * 
	 * @param volumeUuid
	 * @return
	 */
	public Volume getVolume(String volumeUuid) {
		Volume volume = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from Volume where volumeUuid = :volumeUuid");
			query.setString("volumeUuid", volumeUuid);
			volume = (Volume) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return volume;
	}

	/**
	 * 获取一页用户硬盘列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Volume> getOnePageVolumes(int userId, int page, int limit,
			String search) {
		List<Volume> volumeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			StringBuilder builder = new StringBuilder(
					"from Volume where volumeUID = :userId and ");
			builder.append("volumeName like :search and volumeStatus != 0 ");
			builder.append("order by createDate desc");
			Query query = session.createQuery(builder.toString());
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			volumeList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return volumeList;
	}

	/**
	 * 获取主机硬盘列表
	 * 
	 * @param vmUuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getVolumesOfVM(String vmUuid) {
		List<String> volumeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select volumeUuid from Volume where volumeDependency = :vmUuid and volumeStatus = 4 order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setString("vmUuid", vmUuid);
			volumeList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return volumeList;
	}

	@SuppressWarnings("unchecked")
	public List<Volume> getVolListByVM(String vmUuid) {
		List<Volume> volumeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Volume where volumeDependency=:vmUuid and volumeStatus=4";
			Query query = session.createQuery(queryString);
			query.setString("vmUuid", vmUuid);
			volumeList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return volumeList;
	}

	/**
	 * 获取用户硬盘总数
	 * 
	 * @param userId
	 * @param search
	 * @return
	 */
	public int countVolumes(int userId, String search) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			StringBuilder builder = new StringBuilder(
					"select count(*) from Volume where volumeUID = :userId and ");
			builder.append("volumeName like :search and volumeStatus != 0");
			Query query = session.createQuery(builder.toString());
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
			count = ((Number) query.uniqueResult()).intValue();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}

	/**
	 * 预创建硬盘
	 * 
	 * @param volumeUuid
	 * @param volumeName
	 * @param volumeUID
	 * @param volumeSize
	 * @param createDate
	 * @param status
	 * @return
	 */
	public boolean preCreateVolume(String volumeUuid, String volumeName,
			Integer volumeUID, Integer volumeSize, Date createDate,
			Integer status) {
		boolean result = false;
		Session session = null;
		try {
			Volume volume = new Volume(volumeUuid, volumeName, volumeUID,
					volumeSize, createDate, status);
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.save(volume);
			this.getQuotaDAO().updateQuotaFieldNoTransaction(volumeUID,
					"quotaDiskN", 1, true);
			this.getQuotaDAO().updateQuotaFieldNoTransaction(volumeUID,
					"quotaDiskS", volumeSize, true);
			session.getTransaction().commit();
			result = true;
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	/**
	 * 删除硬盘
	 * 
	 * @param userId
	 * @param volumeUuid
	 */
	public void deleteVolume(int userId, String volumeUuid) {
		Volume volume = this.getVolume(volumeUuid);
		if (volume != null) {
			Session session = null;
			try {
				volume.setVolumeStatus(0);
				int size = volume.getVolumeSize();
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(volume);
				this.getQuotaDAO().updateQuotaFieldNoTransaction(userId,
						"quotaDiskN", 1, false);
				this.getQuotaDAO().updateQuotaFieldNoTransaction(userId,
						"quotaDiskS", size, false);
				session.getTransaction().commit();
			} catch (Exception e) {
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
	}

	/**
	 * 硬盘是否存在
	 * 
	 * @param volumeUuid
	 * @return
	 */
	public boolean isExist(String volumeUuid) {
		boolean result = false;
		Volume volume = this.getVolume(volumeUuid);
		if (volume != null && volume.getVolumeStatus() > 0) {
			result = true;
		}
		return result;
	}

	public void addDependency(String volumeUuid, String vmUuid) {
		Volume volume = this.getVolume(volumeUuid);
		if (volume != null) {
			Session session = null;
			try {
				volume.setVolumeDependency(vmUuid);
				volume.setVolumeStatus(VolumeStatus.STATUS_MOUNTED);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(volume);
				session.getTransaction().commit();
			} catch (Exception e) {
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
	}

	public void emptyDependency(String volumeUuid) {
		Volume volume = this.getVolume(volumeUuid);
		if (volume != null) {
			Session session = null;
			try {
				volume.setVolumeDependency(null);
				volume.setVolumeStatus(VolumeStatus.STATUS_FREE);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(volume);
				session.getTransaction().commit();
			} catch (Exception e) {
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
	}

	public void updateBackupDate(String volumeUuid, Date backupDate) {
		Volume volume = this.getVolume(volumeUuid);
		if (volume != null) {
			Session session = null;
			try {
				volume.setBackupDate(backupDate);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(volume);
				session.getTransaction().commit();
			} catch (Exception e) {
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
	}

	public void updateName(String volumeUuid, String newName, String description) {
		Volume volume = this.getVolume(volumeUuid);
		if (volume != null) {
			Session session = null;
			try {
				volume.setVolumeName(newName);
				volume.setVolumeDescription(description);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(volume);
				session.getTransaction().commit();
			} catch (Exception e) {
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
	}

	public boolean updateVolumeStatus(String volUuid, int status) {
		boolean result = false;
		Volume volume = this.getVolume(volUuid);
		if (volume != null) {
			Session session = null;
			try {
				volume.setVolumeStatus(status);
				session = this.getSessionHelper().getMainSession();
				session.beginTransaction();
				session.update(volume);
				session.getTransaction().commit();
				result = true;
			} catch (Exception e) {
				if (session != null) {
					session.getTransaction().rollback();
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Volume> getAbledVolumes(int userId) {
		List<Volume> volumeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "from Volume where volumeUID=:userId and volumeDependency=null and volumeStatus=1";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			volumeList = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return volumeList;
	}
}
