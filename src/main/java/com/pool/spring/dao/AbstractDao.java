package com.pool.spring.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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

	public Object load(Class c, Long id) {

		Session session = null;
		Object o = null;
		try {
			session = this.openSession();

			o = session.load(c, id);
		} finally {
			session.close();
		}
		return o;
	}

	public Object get(Class c, Long id) {

		Session session = null;
		Object o = null;
		try {
			session = this.openSession();
			o = session.get(c, id);
		} finally {
			session.close();
		}
		return o;
	}

	public void save(Object obj) {

		Session session = null;
		try {
			session = this.openSession();
			Transaction tx = session.beginTransaction();
			session.save(obj);
			session.flush();
			tx.commit();
		} finally {
			session.close();
		}
	}

	public void saveOrUpdate(Object obj) {

		Session session = null;
		try {
			session = this.openSession();
			Transaction tx = session.beginTransaction();
			session = this.openSession();
			session.saveOrUpdate(obj);
			session.flush();
			tx.commit();

		} finally {
			session.close();
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}