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
	 * 获取用户硬盘列表
	 * 
	 * @param userId
	 * @param page
	 * @param limit
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Volume> getOnePageVolumeList(int userId, int page, int limit,
			String search) {
		List<Volume> volumeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			String queryString = "from Volume where volumeUID = :userId and volumeName like :search and volumeStatus != 0  order by createDate desc";
			Query query = session.createQuery(queryString);
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
	 * 获取用户硬盘总数
	 * 
	 * @param userId
	 * @param search
	 * @return
	 */
	public int countAllVolumeList(int userId, String search) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select count(*) from Volume where volumeUID = :userId and volumeName like :search and volumeStatus != 0 ";
			Query query = session.createQuery(queryString);
			query.setInteger("userId", userId);
			query.setString("search", "%" + search + "%");
			count = ((Number) query.iterate().next()).intValue();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}

	/**
	 * @param vmUuid
	 * @return volumeList(id)
	 * @author xpx 2014-7-8
	 */
	@SuppressWarnings("unchecked")
	public List<String> getVolumeListByVM(String vmUuid) {
		List<String> volumeList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			String queryString = "select volumeUuid from Volume where volumeDependency = :vmUuid and volumeStatus = 4 order by  createDate desc";
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
			this.getQuotaDAO().updateQuotaField(session, volumeUID,
					"quotaDiskN", 1, true);
			this.getQuotaDAO().updateQuotaField(session, volumeUID,
					"quotaDiskS", volumeSize, true);
			result = true;
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public Volume getVolume(String volumeUuid) {
		Volume volume = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from Volume where volumeUuid= :volumeUuid");
			query.setString("volumeUuid", volumeUuid);
			List<Volume> volList = query.list();
			if (volList.size() == 1) {
				volume = volList.get(0);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return volume;
	}

	public boolean isExist(String volumeUuid) {
		boolean result = false;
		Volume volume = this.getVolume(volumeUuid);
		if (volume.getVolumeStatus() > 0) {
			result = true;
		}
		return result;
	}

	public void deleteVolume(int userId, String volumeUuid) {
		Session session = null;
		try {
			Volume volume = this.getVolume(volumeUuid);
			volume.setVolumeStatus(0);
			int size = volume.getVolumeSize();
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.update(volume);
			this.getQuotaDAO().updateQuotaField(session, userId, "quotaDiskN",
					1, false);
			this.getQuotaDAO().updateQuotaField(session, userId, "quotaDiskS",
					size, false);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	public void addDependency(String volumeUuid, String vmUuid) {
		Session session = null;
		try {
			Volume volume = this.getVolume(volumeUuid);
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

	public void emptyDependency(String volumeUuid) {
		Session session = null;
		try {
			Volume volume = this.getVolume(volumeUuid);
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

	public void updateBackupDate(String volumeUuid, Date backupDate) {
		Session session = null;
		try {
			Volume volume = this.getVolume(volumeUuid);
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

	public void updateName(String volumeUuid, String newName, String description) {
		Session session = null;
		try {
			Volume volume = this.getVolume(volumeUuid);
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

	public boolean setVolumeStatus(String volUuid, int status) {
		boolean result = false;
		Session session = null;
		try {
			Volume volume = this.getVolume(volUuid);
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
		return result;
	}
}
