package com.pool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pool.spring.SpringBeanProvider;
import com.pool.spring.dao.PoolCacheDao;
import com.pool.spring.model.NotificationType;
import com.pool.spring.model.RequestType;

public class PoolCache {

	private static Map<String, Long> _poolNotiTypeCache = new HashMap<String, Long>();
	private static Map<String, Long> _poolRequestTypeCache = new HashMap<String, Long>();

	private static boolean _initialized = false;

	private static void _init() {

		if (!_initialized) {
			synchronized (PoolCache.class) {

				if (!_initialized) {

					PoolCacheDao poolDao = (PoolCacheDao) SpringBeanProvider
							.getBean("poolCacheDao");

					List notis = poolDao.fetchNotificationTypes();

					for (int i = 0; i < notis.size(); i++) {
						NotificationType nt = (NotificationType) notis.get(i);
						_poolNotiTypeCache.put(nt.getNotificationType(),
								nt.getNotificationTypeId());

					}

					List requestTypes = poolDao.fetchRequestTypes();

					for (int i = 0; i < requestTypes.size(); i++) {
						RequestType rt = (RequestType) notis.get(i);
						_poolNotiTypeCache.put(rt.getRequestType(),
								rt.getRequestTypeId());
					}
					_initialized = true;
				}
			}
		}
	}

	public static Long getNotificationTypeId(String type) {
		_init();
		return _poolNotiTypeCache.get(type);
	}

	public static Long getRequestTypeId(String type) {
		_init();
		return _poolRequestTypeCache.get(type);
	}
}
