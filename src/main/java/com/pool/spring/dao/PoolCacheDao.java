package com.pool.spring.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository("poolCacheDao")
public class PoolCacheDao extends AbstractDao {

	public List fetchRequestTypes() {

		Session session = null;
		List result = null;
		try {
			session = this.openSession();

			Query q = session.createQuery("from RequestType");
			result = q.list();

		} finally {
			session.close();
		}

		return result;
	}

	public List fetchNotificationTypes() {

		Session session = null;
		List result = null;
		try {
			session = this.openSession();

			Query q = session.createQuery("from NotificationType");
			result = q.list();

		} finally {
			session.close();
		}

		return result;
	}

}
