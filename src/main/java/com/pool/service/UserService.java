package com.pool.service;

import com.pool.spring.SpringBeanProvider;
import com.pool.spring.dao.UserDao;
import com.pool.spring.dao.VehicleDao;
import com.pool.spring.model.User;
import com.pool.spring.model.Vehicle;

public class UserService {

	public Vehicle getVehicle(Long userId) {
		VehicleDao vehicleDao = (VehicleDao) SpringBeanProvider
				.getBean("vehicleDao");
		return vehicleDao.getVehicleByOwnerId(userId);
	}

	public Vehicle getVehicleByCarPoolId(Long carPoolId) {
		VehicleDao vehicleDao = (VehicleDao) SpringBeanProvider
				.getBean("vehicleDao");
		return vehicleDao.getVehicleByCarpoolId(carPoolId);
	}
	
	public User getUserById(String userId) {
		
		UserDao usrDao = (UserDao) SpringBeanProvider.getBean("userDao");

		User user = usrDao.findUserById(userId);
		
		return user;
	}

	public User getUser(String username, String password) {

		UserDao usrDao = (UserDao) SpringBeanProvider.getBean("userDao");

		User user = usrDao.findUserByUserName(username);

//		if (user == null || !user.getPasswd().equals(password)) {
//			return null;
//		}
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
