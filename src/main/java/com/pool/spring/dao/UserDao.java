package com.pool.spring.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.stereotype.Repository;

import com.pool.spring.model.User;

@Repository("userDao")
public class UserDao extends AbstractDao {

	public void removeUser(User usr) {
		Session session = null;
		try {
			session = this.openSession();
			session.delete(usr);

		} finally {
			session.close();
		}

	}

	public void saveOrUpdate(User usr) {

		Session session = null;
		try {
			session = this.openSession();
			session.saveOrUpdate(usr);

		} finally {
			session.close();
		}

	}

	public User findUserById(String userId) {
		Session session = null;
		User usr = null;
		try {
			session = this.openSession();
			usr = (User) session.get(User.class, userId);

		} finally {
			session.close();
		}
		return usr;
	}

	public User findUserByUserName(String userNname) {
		Session session = null;
		User usr = null;
		try {
			usr = new User();
			usr.setUsername(userNname);

			session = this.openSession();

			Example usrExample = Example.create(usr);
			Criteria criteria = session.createCriteria(User.class).add(
					usrExample);
			List userList = criteria.list();

			if (userList != null && userList.size() > 0) {
				return (User) userList.get(0);
			}

		} finally {
			session.close();
		}
		return null;
	}

	public User findUserByEmail(String email) {
		Session session = null;
		User usr = null;
		try {
			usr = new User();
			usr.setEmail(email);

			session = this.openSession();

			Example usrExample = Example.create(usr);
			Criteria criteria = session.createCriteria(User.class).add(
					usrExample);
			List userList = criteria.list();

			if (userList != null && userList.size() > 0) {
				return (User) userList.get(0);
			}

		} finally {
			session.close();
		}
		return null;
	}

}
