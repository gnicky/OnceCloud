package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.AlarmLog;
import com.oncecloud.entity.performance.Cpu;
import com.oncecloud.entity.performance.Cpu30min;
import com.oncecloud.entity.performance.Memory;
import com.oncecloud.entity.performance.Memory30min;
import com.oncecloud.entity.performance.Vbd;
import com.oncecloud.entity.performance.Vbd30min;
import com.oncecloud.helper.SessionHelper;

@Component
public class AlarmLogDAO {
	private SessionHelper sessionHelper;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}
	
	//插入告警日志
	public boolean addAlarmLog(String uuid,String alarmObject,String alarmType,String alarmDesc, int userId) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Date alarmDate = new Date();
			AlarmLog alarmlog = new AlarmLog(uuid,alarmType,alarmObject, 0,alarmDesc,userId,alarmDate);
			session.save(alarmlog);
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

	
	public int countAlarmLogList(int userId, String keyword) {
		int count = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(AlarmLog.class);
			criteria.add(Restrictions.eq("alarmUid", userId));
			criteria.add(Restrictions.like("alarmDesc", keyword,
					MatchMode.ANYWHERE));
			criteria.setProjection(Projections.rowCount());
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
	
	@SuppressWarnings("unchecked")
	public List<AlarmLog> getOnePageList(int userId, int pageIndex,
			int itemPerPage, String keyword) {
		List<AlarmLog> alarmList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			int startPosition = (pageIndex - 1) * itemPerPage;
			Criteria criteria = session.createCriteria(AlarmLog.class);
			criteria.add(Restrictions.eq("alarmUid", userId));
			criteria.add(Restrictions.like("alarmDesc", keyword,
					MatchMode.ANYWHERE));
			criteria.addOrder(Order.desc("alarmDate"));
			criteria.setFirstResult(startPosition);
			criteria.setMaxResults(itemPerPage);
			alarmList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return alarmList;
	}

	
	private AlarmLog doGetAlarmLog(Session session, String alarmUuid) {
		Criteria criteria = session.createCriteria(AlarmLog.class);
		criteria.add(Restrictions.eq("uuid", alarmUuid));
		AlarmLog alarm = (AlarmLog) criteria.uniqueResult();
		return alarm;
	}
	
	public AlarmLog getAlarmLog(String alarmlogUuid) {
		AlarmLog alarm = null;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			alarm = this.doGetAlarmLog(session, alarmlogUuid);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
		return alarm;
	}
	
	public boolean removeAlarmLog(String alarmlogUuid) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			AlarmLog alarm = this.doGetAlarmLog(session, alarmlogUuid);
			session.delete(alarm);
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

	public boolean updateAlarm(AlarmLog alarm) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			session.update(alarm);
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
	
	public boolean updateStatus(String alarmUuid, int alarmStatus) {
		boolean result = false;
		Session session = null;
		try {
			session = this.getSessionHelper().getMainSession();
			session.beginTransaction();
			AlarmLog alarm = this.doGetAlarmLog(session, alarmUuid);
			if (alarm != null) {
				alarm.setAlarmStatus(alarmStatus);
				session.update(alarm);
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
	}
	
	@SuppressWarnings("unchecked")
	public List<Cpu> getCheckCPUList() {
		  Session session=null;  
	      Transaction tx=null;  
	      try{  
	         session=this.getSessionHelper().getPerformaceSession();  
	         tx=session.beginTransaction();           
	         String sql="select * from cpu_30min GROUP BY id,cpu_id";  
	         List<Cpu> list = (List<Cpu>) session.createSQLQuery(sql).addEntity(Cpu30min.class).list(); 
	         tx.commit();
	         return list;
	      }catch(Exception e){  
	         tx.rollback();  
	         e.printStackTrace();  
	         return null;
	      }
	}

	public boolean checkOneCPU(String uuid, Integer cpuId, double d) {
		 Session session=null;  
	      Transaction tx=null;  
	      boolean result =false;
	      try{  
	         session=this.getSessionHelper().getPerformaceSession();  
	         tx=session.beginTransaction();           
	         String sql="select COUNT(*) from (SELECT * from cpu_30min where id= ? and cpu_id =? ORDER BY t DESC limit 3)AS t where t.usage > ?";  
	        
	         Query query = session.createSQLQuery(sql).setString(0, uuid).setInteger(1, cpuId).setDouble(2, d);
	         Object obj = query.uniqueResult(); 
	         int count= Integer.parseInt(obj.toString());
	         tx.commit();
	         if(count==3)
	         {
	            result = true;
	         }
	      }catch(Exception e){  
	         tx.rollback();  
	         e.printStackTrace();  
	      }
	      return result;
	}

	@SuppressWarnings("unchecked")
	public List<Vbd> getCheckVbdList() {
		  Session session=null;  
	      Transaction tx=null;  
	      try{  
	         session=this.getSessionHelper().getPerformaceSession();  
	         tx=session.beginTransaction();           
	         String sql="select * from vbd_30min GROUP BY id,vbd_id";  
	         List<Vbd> list = (List<Vbd>) session.createSQLQuery(sql).addEntity(Vbd30min.class).list(); 
	         tx.commit();
	         return list;
	      }catch(Exception e){  
	         tx.rollback();  
	         e.printStackTrace();  
	         return null;
	      }
	}

	//// d是kbps 数值
	public boolean checkOneVbd(String uuid, String vbdId, double d,
			String isread) {
		// TODO Auto-generated method stub
		 Session session=null;  
	      Transaction tx=null;  
	      boolean result =false;
	      try{  
	         session=this.getSessionHelper().getPerformaceSession();  
	         tx=session.beginTransaction();    
	         String sql ="";
	         if(isread=="read")
	         {
	              sql="select COUNT(*) from (SELECT * from vbd_30min where id= ? and vbd_id =? ORDER BY t DESC limit 3)AS t where t.read  > ?";  
	         }
	         else
	         {
	        	 sql="select COUNT(*) from (SELECT * from vbd_30min where id= ? and vbd_id =? ORDER BY t DESC limit 3)AS t where t.write  > ?";  
	         }
	         
	         Object obj = session.createSQLQuery(sql).setString(0, uuid).setString(1, vbdId).setDouble(2, d).uniqueResult(); 
	         int count= Integer.parseInt(obj.toString());
	         tx.commit();
	         if(count==3)
	         {
	            result = true;
	         }
	      }catch(Exception e){  
	         tx.rollback();  
	         e.printStackTrace();  
	      }
	      return result;
	}

	@SuppressWarnings("unchecked")
	public List<Memory> getCheckMemoryList() {
		 Session session=null;  
	      Transaction tx=null;  
	      try{  
	         session=this.getSessionHelper().getPerformaceSession();  
	         tx=session.beginTransaction();           
	         String sql="select * from mem_30min GROUP BY id";  
	         List<Memory> list = (List<Memory>) session.createSQLQuery(sql).addEntity(Memory30min.class).list(); 
	         tx.commit();
	         return list;
	      }catch(Exception e){  
	         tx.rollback();  
	         e.printStackTrace();  
	         return null;
	      }
	}

	///d 空闲内存 小于多少 数值
	public boolean checkOneMem(String uuid, double d) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
			 Session session=null;  
		      Transaction tx=null;  
		      boolean result =false;
		      try{  
		         session=this.getSessionHelper().getPerformaceSession();  
		         tx=session.beginTransaction();    
		         String sql="select COUNT(*) from (SELECT * from mem_30min where id= ? ORDER BY t DESC limit 3)AS t where t.free < ?";  
		         
		         Object obj =session.createSQLQuery(sql).setString(0, uuid).setDouble(1, d).uniqueResult();
		         int count= Integer.parseInt(obj.toString());
		         tx.commit();
		         if(count==3)
		         {
		            result = true;
		         }
		      }catch(Exception e){  
		         tx.rollback();  
		         e.printStackTrace();  
		      }
		      return result;
	}
}
