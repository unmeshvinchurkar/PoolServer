package com.pool.service;

import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pool.spring.SpringBeanProvider;
import com.pool.spring.dao.CarPoolDao;
import com.pool.spring.model.Notification;
import com.pool.spring.model.Request;

public class NotificationService {

	public List fetchNotifications(Long userId) {

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");

		Calendar cal = Calendar.getInstance();

		int currentMonth = cal.get(Calendar.MONTH);
		int currentYear = cal.get(Calendar.YEAR);
		int prevMonth = currentMonth > 0 ? currentMonth - 1 : 11;
		int prevYear = currentMonth == 0 ? currentYear - 1 : currentYear;

		cal.set(Calendar.MONTH, prevMonth);
		cal.set(Calendar.YEAR, prevYear);
		long dateInSec = cal.getTimeInMillis() / 1000;

		return poolDao.fetchNotifications(userId, dateInSec);

	}

	public void removeTraveller(Long travellerId, Long carPoolId) {

		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		poolDao.removeTraveller(travellerId, carPoolId);
	}

	public void saveOrUpdate(Object obj) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		poolDao.saveOrUpdate(obj);

	}

	public Request fetchRequestById(Long requestId) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		return (Request) poolDao.get(Request.class, requestId);
	}

	public void saveObjects(Object obj1, Object obj2) {
		CarPoolDao poolDao = (CarPoolDao) SpringBeanProvider
				.getBean("carPoolDao");
		Session session = null;
		Transaction tx = null;
		try {
			session = poolDao.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(obj1);
			session.saveOrUpdate(obj2);
			session.flush();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
	}

}
