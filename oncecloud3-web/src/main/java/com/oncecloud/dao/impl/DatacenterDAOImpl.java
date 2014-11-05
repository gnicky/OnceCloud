package com.oncecloud.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.DatacenterDAO;
import com.oncecloud.entity.Datacenter;
import com.oncecloud.entity.OCPool;
import com.oncecloud.entity.Rack;
import com.oncecloud.helper.SessionHelper;

@Component("DatacenterDAO")
public class DatacenterDAOImpl implements DatacenterDAO {

	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

/*	public int countAllDatacenter(String search) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session
					.createCriteria(Datacenter.class)
					.add(Restrictions
							.like("dcName", search, MatchMode.ANYWHERE))
					.add(Restrictions.eq("dcStatus", 1))
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

	public Datacenter createDatacenter(String dcName, String dcLocation,
			String dcDesc) {
		Datacenter datacenter = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			datacenter = new Datacenter();
			datacenter.setDcUuid(UUID.randomUUID().toString());
			datacenter.setDcName(dcName);
			datacenter.setDcLocation(dcLocation);
			datacenter.setDcDesc(dcDesc);
			datacenter.setDcStatus(1);
			datacenter.setCreateDate(new Date());
			session.save(datacenter);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return datacenter;
	}

	@SuppressWarnings("unchecked")
	public boolean deleteDatacenter(String dcUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Datacenter datacenter = this.doGetDatacenter(session, dcUuid);
			if (datacenter != null) {
				datacenter.setDcStatus(0);
				session.update(datacenter);
				List<Rack> racks = session.createCriteria(Rack.class)
						.add(Restrictions.eq("dcUuid", dcUuid)).list();
				for (Rack rack : racks) {
					rack.setDcUuid(null);
					session.update(rack);
				}
				List<OCPool> pools = session.createCriteria(OCPool.class)
						.add(Restrictions.eq("dcUuid", dcUuid)).list();
				for (OCPool pool : pools) {
					pool.setDcUuid(null);
					session.update(pool);
				}
				session.getTransaction().commit();
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}

	private Datacenter doGetDatacenter(Session session, String dcUuid) {
		Datacenter datacenter;
		Criteria criteria = session.createCriteria(Datacenter.class).add(
				Restrictions.eq("dcUuid", dcUuid));
		datacenter = (Datacenter) criteria.uniqueResult();
		return datacenter;
	}

	@SuppressWarnings("unchecked")
	public List<Datacenter> getAllPageDCList() {
		List<Datacenter> datacenterList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Datacenter.class)
					.add(Restrictions.eq("dcStatus", 1))
					.addOrder(Order.desc("createDate"));
			datacenterList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return datacenterList;
	}

	public Datacenter getDatacenter(String dcUuid) {
		Datacenter datacenter = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			datacenter = this.doGetDatacenter(session, dcUuid);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return datacenter;
	}

	@SuppressWarnings("unchecked")
	public List<Datacenter> getOnePageDCList(int page, int limit, String search) {
		List<Datacenter> datacenterList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPos = (page - 1) * limit;
			Criteria criteria = session
					.createCriteria(Datacenter.class)
					.add(Restrictions
							.like("dcName", search, MatchMode.ANYWHERE))
					.add(Restrictions.eq("dcStatus", 1))
					.addOrder(Order.desc("createDate"))
					.setFirstResult(startPos).setMaxResults(limit);
			datacenterList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return datacenterList;
	}

	public boolean updateDatacenter(String dcUuid, String dcName,
			String dcLocation, String dcDesc) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Datacenter.class).add(
					Restrictions.eq("dcUuid", dcUuid));
			Datacenter datacenter = (Datacenter) criteria.uniqueResult();
			if (datacenter != null) {
				datacenter.setDcName(dcName);
				datacenter.setDcLocation(dcLocation);
				datacenter.setDcDesc(dcDesc);
				session.update(datacenter);
				result = true;
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return result;
	}*/
}
