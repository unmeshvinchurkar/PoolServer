package com.pool.spring.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected Session openSession() {
		return sessionFactory.openSession();
	}

	protected Session getSession() {

		return sessionFactory.getCurrentSession();
	}

	public void persist(Object entity) {
		getSession().persist(entity);
	}

	public void delete(Object entity) {
		getSession().delete(entity);
	}

	public void save(Object obj) {

		Session session = null;
		try {
			session = this.openSession();
			session.save(obj);

		} finally {
			session.close();
		}
	}
	
	public void saveOrUpdate(Object obj) {

		Session session = null;
		try {
			session = this.openSession();
			session.saveOrUpdate(obj);

		} finally {
			session.close();
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}