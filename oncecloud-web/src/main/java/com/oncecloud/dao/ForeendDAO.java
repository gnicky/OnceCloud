package com.oncecloud.dao;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.Backend;
import com.oncecloud.entity.Foreend;
import com.oncecloud.helper.SessionHelper;

@Component
public class ForeendDAO {
	private SessionHelper sessionHelper;
	private BackendDAO backendDAO;

	private SessionHelper getSessionHelper() {
		return sessionHelper;
	}

	@Autowired
	private void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	private BackendDAO getBackendDAO() {
		return backendDAO;
	}

	@Autowired
	private void setBackendDAO(BackendDAO backendDAO) {
		this.backendDAO = backendDAO;
	}

	/**
	 * 通过id获取前端监听
	 * 
	 * @param foreUuid
	 * @return
	 * @author xpx 2014-7-11
	 */
	@SuppressWarnings("unchecked")
	public Foreend getForeend(String foreUuid) {
		Foreend fore = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Foreend where foreUuid = :foreUuid";
			Query query = session.createQuery(queryString);
			query.setString("foreUuid", foreUuid);
			List<Foreend> foreList = query.list();
			if (foreList.size() == 1) {
				fore = foreList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return fore;
	}

	/**
	 * 创建前端监听
	 * 
	 * @param foreName
	 *            前端监听名称
	 * @param foreProtocol
	 *            监听协议"TCP"/"HTTP"
	 * @param forePort
	 *            监听端口
	 * @param forePolicy
	 *            均衡策略
	 * @param lbUuid
	 *            对应负载均衡对象id
	 * @return 更新后的Foreend前端对象
	 * @author xpx 2014-7-11
	 */
	public Foreend createForeend(String foreUuid, String foreName,
			String foreProtocol, Integer forePort, Integer forePolicy,
			String lbUuid) {
		Foreend fore = null;
		Session session = null;
		Transaction tx = null;
		try {
			fore = new Foreend();
			fore.setForeUuid(foreUuid);
			fore.setForeName(foreName);
			fore.setForeProtocol(foreProtocol);
			fore.setForePort(forePort);
			fore.setForePolicy(forePolicy);
			fore.setLbUuid(lbUuid);
			fore.setForeStatus(1);// 设置正常运行0/禁用,1/正常
			fore.setCreateDate(new Date());
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			session.save(fore);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			fore = null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return fore;
	}

	/**
	 * 检查端口的重复性
	 * 
	 * @param lbUuid
	 *            负载均衡id
	 * @param forePort
	 *            新建端口
	 * @return true 端口可用,false 端口不可用
	 * @author xpx 2014-7-11
	 */
	public boolean checkRepeat(String lbUuid, Integer forePort) {
		boolean result = false;
		Session session = null;
		int total = 1;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from Foreend where lbUuid=:lbuuid and forePort=:port";
			Query query = session.createQuery(queryString);
			query.setString("lbuuid", lbUuid);
			query.setInteger("port", forePort);
			total = ((Number) query.iterate().next()).intValue();
			if (0 == total) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	/**
	 * 更新前端监听
	 * 
	 * @param foreUuid
	 * @param foreName
	 * @param forePolicy
	 * @return
	 * @author xpx 2014-7-11
	 */
	public boolean updateForeend(String foreUuid, String foreName,
			Integer forePolicy) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update Foreend set foreName = :name, forePolicy = :policy where foreUuid=:uuid";
			Query query = session.createQuery(queryString);
			query.setString("name", foreName);
			query.setString("uuid", foreUuid);
			query.setInteger("policy", forePolicy);
			query.executeUpdate();
			tx.commit();
			result = true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Foreend> getOnePageFEList(int page, int limit, String search) {
		List<Foreend> foreList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			int startPos = (page - 1) * limit;
			String queryString = "from Foreend where foreName like '%" + search
					+ "%' order by foreStatus desc";
			Query query = session.createQuery(queryString);
			query.setFirstResult(startPos);
			query.setMaxResults(limit);
			foreList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return foreList;
	}

	/**
	 * 根据负载均衡的id获取前端监听器的列表
	 * 
	 * @param lbUuid
	 *            负载均衡id
	 * @return 监听器列表
	 * @author xpx 2014-7-11
	 */
	@SuppressWarnings("unchecked")
	public JSONArray getFEListByLB(String lbUuid) {
		JSONArray feArray = new JSONArray();
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Foreend where lbUuid ='" + lbUuid
					+ "' order by createDate desc";
			Query query = session.createQuery(queryString);
			List<Foreend> foreList = null;
			foreList = query.list();
			if (null != foreList && foreList.size() > 0) {
				for (Foreend foreend : foreList) {
					JSONObject feJo = new JSONObject();
					feJo.put("foreUuid", foreend.getForeUuid());
					feJo.put("foreName",
							URLEncoder.encode(foreend.getForeName(), "utf-8")
									.replace("+", "%20"));
					feJo.put("foreProtocol", foreend.getForeProtocol());
					feJo.put("forePort", foreend.getForePort());
					feJo.put("forePolicy", foreend.getForePolicy());
					feJo.put("foreStatus", foreend.getForeStatus());
					List<Backend> backList = this.getBackendDAO()
							.getBEListByFE(session, foreend.getForeUuid(), 0);
					JSONArray beArray = new JSONArray();
					if (null != backList && backList.size() > 0) {
						for (Backend backend : backList) {
							JSONObject beJo = new JSONObject();
							beJo.put("backUuid", backend.getBackUuid());
							beJo.put(
									"backName",
									URLEncoder.encode(backend.getBackName(),
											"utf-8").replace("+", "%20"));
							beJo.put("vmUuid", backend.getVmUuid());
							beJo.put("vmIp", backend.getVmIp());
							beJo.put("vmPort", backend.getVmPort());
							beJo.put("backWeight", backend.getBackWeight());
							beJo.put("backStatus", backend.getBackStatus());
							beArray.put(beJo);
						}
					}
					feJo.put("beArray", beArray);
					feArray.put(feJo);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return feArray;
	}

	/**
	 * 根据负载均衡的id 组成修改应用的json串
	 * 
	 * @param lbUuid
	 *            负载均衡id
	 * @return 监听器列表
	 * @author xpx 2014-7-11
	 */
	@SuppressWarnings("unchecked")
	public JSONArray getSimpleFEListByLB(String lbUuid) {
		JSONArray feArray = new JSONArray();
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Foreend where lbUuid ='" + lbUuid
					+ "' and foreStatus >=1 order by createDate desc";
			Query query = session.createQuery(queryString);
			List<Foreend> foreList = null;
			foreList = query.list();
			if (null != foreList && foreList.size() > 0) {
				for (Foreend foreend : foreList) {
					JSONObject feJo = new JSONObject();
					feJo.put("protocol", foreend.getForeProtocol()
							.toLowerCase());
					feJo.put("port", foreend.getForePort());
					feJo.put("policy", foreend.getForePolicy());
					List<Backend> backList = this.getBackendDAO()
							.getBEListByFE(session, foreend.getForeUuid(), 1);
					JSONArray beArray = new JSONArray();
					if (null != backList && backList.size() > 0) {
						for (Backend backend : backList) {
							JSONObject beJo = new JSONObject();
							beJo.put("ip", backend.getVmIp());
							beJo.put("port", backend.getVmPort());
							beArray.put(beJo);
						}
					}
					feJo.put("rules", beArray);
					feArray.put(feJo);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return feArray;
	}

	public int countAllForeend(String search) {
		int total = 0;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "select count(*) from Foreend where foreName like '%"
					+ search + "%'";
			Query query = session.createQuery(queryString);
			total = ((Number) query.iterate().next()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return total;
	}

	public boolean deleteForeend(String foreUuid) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			this.getBackendDAO().deleteBackendByFE(session, foreUuid);
			String queryString = "delete from Foreend where foreUuid = :foreUuid";
			Query query = session.createQuery(queryString);
			query.setString("foreUuid", foreUuid);
			query.executeUpdate();
			tx.commit();
			result = true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	/**
	 * @param foreUuid
	 * @param state
	 *            1可用,0 禁用
	 * @return true 成功,false 失败
	 * @author xpx 2014-7-11
	 */
	public boolean changeForeendStatus(String foreUuid, int state) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSessionHelper().openMainSession();
			tx = session.beginTransaction();
			String queryString = "update Foreend set foreStatus = :state where foreUuid = :foreUuid";
			Query query = session.createQuery(queryString);
			query.setInteger("state", state);
			query.setString("foreUuid", foreUuid);
			query.executeUpdate();
			tx.commit();
			result = true;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Foreend> getAllPageFEList() {
		List<Foreend> foreList = null;
		Session session = null;
		try {
			session = this.getSessionHelper().openMainSession();
			String queryString = "from Foreend order by createDate desc";
			Query query = session.createQuery(queryString);
			foreList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return foreList;
	}
}
