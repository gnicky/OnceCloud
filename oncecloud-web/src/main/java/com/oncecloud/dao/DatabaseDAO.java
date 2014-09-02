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

	public int countAllDatabaseList(String search, int uid) {
		int count = 0;
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
			count = ((Number) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}

	public int countDatabasesWithoutEIP(String search, int uid) {
		int count = 0;
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
			count = ((Number) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return count;
	}

	private Database doGetDatabase(Session session, String dbUuid) {
		Database database;
		Criteria criteria = session.createCriteria(Database.class).add(
				Restrictions.eq("databaseUuid", dbUuid));
		database = (Database) criteria.uniqueResult();
		return database;
	}

	public Database getAliveDatabase(String dbUuid) {
		Database database = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Database.class)
					.add(Restrictions.eq("databaseUuid", dbUuid))
					.add(Restrictions.eq("databaseStatus", 1));
			database = (Database) criteria.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return database;
	}

	public Database getDatabase(String dbUuid) {
		Database database = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			database = this.doGetDatabase(session, dbUuid);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return database;
	}

	public String getDBName(String dbUuid) {
		String result = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Database.class).add(
					Restrictions.eq("databaseUuid", dbUuid));
			Database database = (Database) criteria.uniqueResult();
			session.getTransaction().commit();
			result = (database == null) ? null : database.getDatabaseName();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Database> getOnePageDatabaseList(int page, int limit,
			String search, int uid) {
		List<Database> databaseList = null;
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
			databaseList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return databaseList;
	}

	@SuppressWarnings("unchecked")
	public List<Database> getOnePageDatabasesWithoutEip(int page, int limit,
			String searchStr, int uid) {
		List<Database> databaseList = null;
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
			databaseList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return databaseList;
	}

	public boolean preCreateDatabase(String databaseUuid, String databaseName,
			Integer databaseUID, String databaseUser, String databasePwd,
			String databaseType, Integer databaseThroughout,
			String databaseMac, String databaseIp, Integer databasePort,
			Integer databasePower, Integer databaseStatus, Date createDate) {
		boolean result = false;
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
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean removeDatabase(String databaseUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Database database = this.doGetDatabase(session, databaseUuid);
			database.setDatabaseStatus(0);
			session.update(database);
			session.getTransaction().commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean setDBPowerStatus(String uuid, int powerStatus) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Database database = this.doGetDatabase(session, uuid);
			database.setDatabasePower(powerStatus);
			session.update(database);
			session.getTransaction().commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	public boolean updateDatabase(String databaseUuid, int power,
			String hostUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Database database = this.doGetDatabase(session, databaseUuid);
			database.setDatabasePower(power);
			database.setHostUuid(hostUuid);
			session.update(database);
			session.getTransaction().commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}
}
