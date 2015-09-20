package com.pool.spring.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.stereotype.Repository;

import com.pool.spring.model.User;
import com.pool.spring.model.Vehicle;

@Repository("vehicleDao")
public class VehicleDao extends AbstractDao {

	public void addvehicle(Vehicle vh) {
		Session session = null;
		try {
			session = this.openSession();
			session.save(vh);

		} finally {
			session.close();
		}
	}

	public Vehicle getVehicleByOwnerId(Long ownerId) {
		Session session = null;
		Vehicle vehicle = null;
		try {
			vehicle = new Vehicle();
			vehicle.setOwnerId(ownerId);

			session = this.openSession();
			Example vehicleExample = Example.create(vehicle);
			Criteria criteria = session.createCriteria(Vehicle.class).add(
					vehicleExample);
			List vehicleList = criteria.list();
			if (vehicleList != null && vehicleList.size() > 0) {
				return (Vehicle) vehicleList.get(0);
			}

		} finally {
			session.close();
		}
		return null;

	}

}
