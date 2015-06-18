package com.pool.esapi;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.EncoderConstants;
import org.owasp.esapi.Logger;
import org.owasp.esapi.errors.AuthenticationException;
import org.owasp.esapi.errors.AuthenticationHostException;
import org.owasp.esapi.errors.EncryptionException;

import com.pool.spring.model.User;

public class UserEsapi implements org.owasp.esapi.User, Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The logger used by the class. */
	private transient final Logger logger = ESAPI.getLogger("UserEsapi");
	
	/** This user's account name. */
	private String accountName = "";

	/** This user's screen name (account name alias). */
	private String screenName = "";

	/** This user's CSRF token. */
	private String csrfToken = null;

	private User user = null;

	/** Whether this user's account is locked. */
	private boolean locked = false;

	/** Whether this user is logged in. */
	private boolean loggedIn = true;

	/** Whether this user's account is enabled. */
	private boolean enabled = false;

	/** The failed login count for this user's account. */
	private int failedLoginCount = 0;

	/** This user's Locale. */
	private Locale locale;

	/** The last password change time for this user. */
	private Date lastPasswordChangeTime = new Date(0);

	/** The last login time for this user. */
	private Date lastLoginTime = new Date(0);

	/** The last failed login time for this user. */
	private Date lastFailedLoginTime = new Date(0);

	/** The expiration date/time for this user's account. */
	private Date expirationTime = new Date(Long.MAX_VALUE);

	public UserEsapi(User user) {
		this.user = user;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addRole(String arg0) throws AuthenticationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRoles(Set<String> arg0) throws AuthenticationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSession(HttpSession arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changePassword(String oldPassword, String newPassword1,
			String newPassword2) throws AuthenticationException,
			EncryptionException {
		ESAPI.authenticator().changePassword(this, oldPassword, newPassword1,
				newPassword2);
	}

	@Override
	public void disable() {
		enabled = false;
		logger.info(Logger.SECURITY_SUCCESS, "Account disabled: "
				+ getAccountName());

	}

	@Override
	public void enable() {
		this.enabled = true;
		logger.info(Logger.SECURITY_SUCCESS, "Account enabled: "
				+ getAccountName());

	}

	@Override
	public long getAccountId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getAccountName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCSRFToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap getEventMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getExpirationTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFailedLoginCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Date getLastFailedLoginTime() throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLastHostAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getLastLoginTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getLastPasswordChangeTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScreenName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getSessions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void incrementFailedLoginCount() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAnonymous() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInRole(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSessionAbsoluteTimeout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSessionTimeout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void lock() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loginWithPassword(String arg0) throws AuthenticationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeRole(String arg0) throws AuthenticationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeSession(HttpSession arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public String resetCSRFToken() throws AuthenticationException {
		csrfToken = ESAPI.randomizer().getRandomString(8, EncoderConstants.CHAR_ALPHANUMERICS);
		return csrfToken;
	}

	@Override
	public void setAccountName(String arg0) {
		String old = getAccountName();
		this.accountName = accountName.toLowerCase();
		if (old != null) {
			if ( old.equals( "" ) ) {
				old = "[nothing]";
			}
			logger.info(Logger.SECURITY_SUCCESS, "Account name changed from " + old + " to " + getAccountName() );
		}

	}

	@Override
	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = new Date(expirationTime.getTime());
		logger.info(Logger.SECURITY_SUCCESS, "Account expiration time set to "
				+ expirationTime + " for " + getAccountName());

	}

	@Override
	public void setLastFailedLoginTime(Date lastFailedLoginTime) {
		this.lastFailedLoginTime = lastFailedLoginTime;
		logger.info(Logger.SECURITY_SUCCESS, "Set last failed login time to "
				+ lastFailedLoginTime + " for " + getAccountName());

	}

	@Override
	public void setLastHostAddress(String arg0)
			throws AuthenticationHostException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
		logger.info(Logger.SECURITY_SUCCESS,
				"Set last successful login time to " + lastLoginTime + " for "
						+ getAccountName());

	}

	@Override
	public void setLastPasswordChangeTime(Date lastPasswordChangeTime) {
		this.lastPasswordChangeTime = lastPasswordChangeTime;
		logger.info(Logger.SECURITY_SUCCESS,
				"Set last password change time to " + lastPasswordChangeTime
						+ " for " + getAccountName());

	}

	/**
	 * Override clone and make final to prevent duplicate user objects.
	 * 
	 * @return
	 * @throws java.lang.CloneNotSupportedException
	 */
	public final Object clone() throws java.lang.CloneNotSupportedException {
		throw new java.lang.CloneNotSupportedException();
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	@Override
	public void setRoles(Set<String> arg0) throws AuthenticationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setScreenName(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unlock() {
		this.locked = false;
		this.failedLoginCount = 0;
		logger.info(Logger.SECURITY_SUCCESS, "Account unlocked: "
				+ getAccountName());

	}

	@Override
	public boolean verifyPassword(String password) throws EncryptionException {
		return ESAPI.authenticator().verifyPassword(this, password);
	}

}
