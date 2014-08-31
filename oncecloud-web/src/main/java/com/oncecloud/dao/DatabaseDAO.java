package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Database;
import com.oncecloud.helper.SessionHelper;

/**
 * @author hehai
 * @version 2014/08/23
 */
@Component
public class DatabaseDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public Database getDatabase(String dbUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "from Database where databaseUuid = :dbUuid";
			Query query = session.createQuery(queryString);
			query.setString("dbUuid", dbUuid);
			db = (Database) query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	public Database getAliveDatabase(String dbUuid) {
		Database db = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "from Database where databaseUuid = :dbUuid and databaseStatus = 1";
			Query query = session.createQuery(queryString);
			query.setString("dbUuid", dbUuid);
			db = (Database) query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public String getDBName(String dbUuid) {
		Session session = null;
		String result = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "select databaseName from Database where databaseUuid='"
					+ dbUuid + "'";
			Query query = session.createQuery(queryString);
			List<String> list = query.list();
			result = list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	public boolean preCreateDatabase(String databaseUuid, String databaseName,
			Integer databaseUID, String databaseUser, String databasePwd,
			String databaseType, Integer databaseThroughout,
			String databaseMac, String databaseIp, Integer databasePort,
			Integer databasePower, Integer databaseStatus, Date createDate) {
		Transaction tx = null;
		Session session = null;
		boolean result = false;
		try {
			session = this.getSessionHelper().getMainSession();
			Database db = new Database(databaseUuid, databaseName, databaseUID,
					databaseUser, databasePwd, databaseType,
					databaseThroughout, databaseMac, databaseIp, databasePort,
					databasePower, databaseStatus, createDate);
			tx = session.beginTransaction();
			session.save(db);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	public void updateDatabase(String databaseUuid, int power, String hostUuid) {
		Session session = null;
		Transaction tx = null;
		try {
			Database db = this.getDatabase(databaseUuid);
			db.setDatabasePower(power);
			db.setHostUuid(hostUuid);
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			session.update(db);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	public void removeDatabase(String databaseUuid) {
		Session session = null;
		Transaction tx = null;
		try {
			Database db = this.getDatabase(databaseUuid);
			db.setDatabaseStatus(0);
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			session.update(db);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Database> getOnePageDatabaseList(int page, int limit,
			String search, int uid) {
		List<Database> dbList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from Database where databaseUID = " + uid
					+ " and databaseName like '%" + search
					+ "%' and databaseStatus = 1 order by createDate desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			dbList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	public int countAllDatabaseList(String search, int uid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "select count(*) from Database where databaseUID = "
					+ uid
					+ " and databaseName like '%"
					+ search
					+ "%' and databaseStatus = 1";
			Query query = session.createQuery(queryString);
			count = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	public boolean setDBPowerStatus(String uuid, int powerStatus) {
		boolean result = false;
		Database db = this.getDatabase(uuid);
		Session session = null;
		Transaction tx = null;
		try {
			db.setDatabasePower(powerStatus);
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			session.update(db);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	public boolean setDBStatus(String uuid, int state) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().getMainSession();
			tx = session.beginTransaction();
			String queryString = "update Database set databaseStatus=" + state
					+ " where databaseUuid ='" + uuid + "'";
			Query query = session.createQuery(queryString);
			query.executeUpdate();
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Database> getOnePageDatabasesWithoutEip(int page, int limit,
			String searchStr, int uid) {
		List<Database> databaseList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from Database where databaseUID = "
					+ uid
					+ " and databaseName like '%"
					+ searchStr
					+ "%' and databaseStatus <>0 and databaseUuid not in "
					+ "(select eip.eipDependency from EIP eip where eip.eipUID="
					+ uid + " and eip.eipDependency is not null)";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			databaseList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	public int countDatabasesWithoutEIP(String search, int uid) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			String queryString = "select count(*) from Database where databaseUID= "
					+ uid
					+ " and databaseName like '%"
					+ search
					+ "%' and databaseStatus <> 0 and databaseUuid not in "
					+ "(select eip.eipDependency from EIP eip where eip.eipUID="
					+ uid + " and eip.eipDependency is not null)";
			Query query = session.createQuery(queryString);
			count = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}
}
