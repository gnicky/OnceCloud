package com.oncecloud.helper;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.stereotype.Component;

@Component
public final class SessionHelper {
	private SessionFactory mainSessionFactory;
	private SessionFactory performanceSessionFactory;
	private SessionFactory testSessionFactory;

	private SessionFactory getMainSessionFactory() {
		return mainSessionFactory;
	}

	private void setMainSessionFactory(SessionFactory mainSessionFactory) {
		this.mainSessionFactory = mainSessionFactory;
	}

	private SessionFactory getPerformanceSessionFactory() {
		return performanceSessionFactory;
	}

	private void setPerformanceSessionFactory(
			SessionFactory performanceSessionFactory) {
		this.performanceSessionFactory = performanceSessionFactory;
	}

	private SessionFactory getTestSessionFactory() {
		return testSessionFactory;
	}

	private void setTestSessionFactory(SessionFactory testSessionFactory) {
		this.testSessionFactory = testSessionFactory;
	}

	public SessionHelper() {
		try {
			this.configureMainDatabase();
			this.configurePerformanceDatabase();
			this.configureTestDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void configurePerformanceDatabase() {
		Configuration performanceConfiguration = new Configuration()
				.configure("com/oncecloud/config/hibernate-performance.cfg.xml");
		ServiceRegistry performanceServiceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(performanceConfiguration.getProperties())
				.build();
		this.setPerformanceSessionFactory(performanceConfiguration
				.buildSessionFactory(performanceServiceRegistry));
	}

	private void configureMainDatabase() {
		Configuration mainConfiguration = new Configuration()
				.configure("com/oncecloud/config/hibernate-main.cfg.xml");
		ServiceRegistry mainServiceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(mainConfiguration.getProperties()).build();
		this.setMainSessionFactory(mainConfiguration
				.buildSessionFactory(mainServiceRegistry));
	}

	private void configureTestDatabase() {
		Configuration testConfiguration = new Configuration()
				.configure("com/oncecloud/config/hibernate-main-test.cfg.xml");
		ServiceRegistry mainServiceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(testConfiguration.getProperties()).build();
		this.setTestSessionFactory(testConfiguration
				.buildSessionFactory(mainServiceRegistry));
	}

	public Session getMainSession() {
		return this.getMainSessionFactory().getCurrentSession();
	}

	public Session getPerformaceSession() {
		return this.getPerformanceSessionFactory().getCurrentSession();
	}

	public Session getTestSession() {
		return this.getTestSessionFactory().getCurrentSession();
	}
}
