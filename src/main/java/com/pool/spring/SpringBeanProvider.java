
package com.pool.spring;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

/**
 * Application Context provider initializes Spring context. It is a singleton
 * class.
 *
 */
public class SpringBeanProvider {
	private static ApplicationContext context = null;

	private static boolean initialized = false;

	/**
	 * Returns Application Context which is initialized
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return context;
	}

	/**
	 * Gets bean from context.
	 * 
	 * @param beanId
	 * @return
	 */
	public synchronized static Object getBean(String beanId) {
		if (context == null) {
			initializeContext();
		}
		return context != null ? context.getBean(beanId) : null;
	}

	public static SessionFactory getSessionFactory() {
		if (context == null) {
			initializeContext();
		}
		return (SessionFactory) SpringBeanProvider.getBean("sessionFactory");
	}

	/**
	 * Initialize Spring Context from the classpath.
	 */
	public static void initializeContext() {
		// Initialize Spring context from the RMA classpath
		initializeContext("applicationContext.xml");
	}

	/**
	 * Takes application context file path and initialize Spring Context.
	 * 
	 * @param applContextFilePath
	 */
	public static void initializeContext(String applContextFilePath) {

		if (!initialized) {
			context = new ClassPathXmlApplicationContext(applContextFilePath);

		}
		initialized = true;
	}

}
