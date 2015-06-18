package com.pool.service;

import com.pool.spring.SpringBeanProvider;
import com.pool.spring.dao.UserDao;
import com.pool.spring.model.User;

public class UserService {

	public User getUser(String username, String password) {

		UserDao usrDao = (UserDao) SpringBeanProvider.getBean("userDao");

		User user = usrDao.findUserByUserName(username);

		if (user == null || !user.getPasswd().equals(password)) {
			return null;
		}
		return user;
	}

	public void createUser(User usr) {
		UserDao usrDao = (UserDao) SpringBeanProvider.getBean("userDao");
		usrDao.saveOrUpdate(usr);
	}

	public void removeUser(User usr) {
		UserDao usrDao = (UserDao) SpringBeanProvider.getBean("userDao");
		usrDao.removeUser(usr);
	}

}
