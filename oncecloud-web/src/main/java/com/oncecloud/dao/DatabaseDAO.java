package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Database;
import com.oncecloud.entity.EIP;
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
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Database.class).add(
					Restrictions.eq("databaseUuid", dbUuid));
			Database database = (Database) criteria.uniqueResult();
			session.getTransaction().commit();
			return database;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public Database getAliveDatabase(String dbUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Database.class)
					.add(Restrictions.eq("databaseUuid", dbUuid))
					.add(Restrictions.eq("databaseStatus", 1));
			Database database = (Database) criteria.uniqueResult();
			session.getTransaction().commit();
			return database;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public String getDBName(String dbUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Database.class).add(
					Restrictions.eq("databaseUuid", dbUuid));
			Database database = (Database) criteria.uniqueResult();
			session.getTransaction().commit();
			return (database == null) ? null : database.getDatabaseName();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public boolean preCreateDatabase(String databaseUuid, String databaseName,
			Integer databaseUID, String databaseUser, String databasePwd,
			String databaseType, Integer databaseThroughout,
			String databaseMac, String databaseIp, Integer databasePort,
			Integer databasePower, Integer databaseStatus, Date createDate) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Database database = new Database(databaseUuid, databaseName,
					databaseUID, databaseUser, databasePwd, databaseType,
					databaseThroughout, databaseMac, databaseIp, databasePort,
					databasePower, databaseStatus, createDate);
			session.save(database);
			session.getTransaction().commit();
			return true;
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
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Database database = this.getDatabase(databaseUuid);
			database.setDatabasePower(power);
			database.setHostUuid(hostUuid);
			session.update(database);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	public void removeDatabase(String databaseUuid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Database database = this.getDatabase(databaseUuid);
			database.setDatabaseStatus(0);
			session.update(database);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<Database> getOnePageDatabaseList(int page, int limit,
			String search, int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			Criteria criteria = session
					.createCriteria(Database.class)
					.add(Restrictions.eq("databaseUID", uid))
					.add(Restrictions.like("databaseName", search,
							MatchMode.ANYWHERE))
					.add(Restrictions.eq("databaseStatus", 1))
					.addOrder(Order.desc("createDate"))
					.setFirstResult(startPos).setMaxResults(limit);
			List<Database> list = criteria.list();
			session.getTransaction().commit();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public int countAllDatabaseList(String search, int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session
					.createCriteria(Database.class)
					.add(Restrictions.eq("databaseUID", uid))
					.add(Restrictions.like("databaseName", search,
							MatchMode.ANYWHERE))
					.add(Restrictions.eq("databaseStatus", 1));
			int total = ((Number) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
			return total;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}

	public boolean setDBPowerStatus(String uuid, int powerStatus) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Database database = this.getDatabase(uuid);
			database.setDatabasePower(powerStatus);
			session.update(database);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return false;
		}
	}

	public boolean setDBStatus(String uuid, int state) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Database.class).add(
					Restrictions.eq("databaseUuid", uuid));
			Database database = (Database) criteria.uniqueResult();
			database.setDatabaseStatus(state);
			session.update(database);
			session.getTransaction().commit();
			return true;
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
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			Criteria criteria = session
					.createCriteria(Database.class)
					.add(Restrictions.eq("databaseUID", uid))
					.add(Restrictions.like("databaseName", searchStr,
							MatchMode.ANYWHERE))
					.add(Restrictions.ne("databaseStatus", 0))
					.add(Restrictions.not(Restrictions.in(
							"databaseUuid",
							session.createCriteria(EIP.class)
									.add(Restrictions.eq("eipUID", uid))
									.add(Restrictions
											.isNotNull("eipDependency"))
									.setProjection(
											Projections
													.property("eipDependency"))
									.list()))).setFirstResult(startPos)
					.setMaxResults(limit);
			List<Database> list = criteria.list();
			session.getTransaction().commit();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return null;
		}
	}

	public int countDatabasesWithoutEIP(String search, int uid) {
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session
					.createCriteria(Database.class)
					.add(Restrictions.eq("databaseUID", uid))
					.add(Restrictions.like("databaseName", search,
							MatchMode.ANYWHERE))
					.add(Restrictions.ne("databaseStatus", 0))
					.add(Restrictions.not(Restrictions.in(
							"databaseUuid",
							session.createCriteria(EIP.class)
									.add(Restrictions.eq("eipUID", uid))
									.add(Restrictions
											.isNotNull("eipDependency"))
									.setProjection(
											Projections
													.property("eipDependency"))
									.list())))
					.setProjection(Projections.rowCount());
			int total = ((Number) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
			return total;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
			return 0;
		}
	}
}
