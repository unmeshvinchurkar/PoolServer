PROJECT.namespace("PROJECT.pool");

(function() {
	/* class declaration */
	PROJECT.pool.ScreenNavigator = ScreenNavigator;

	/**
	 * @class PROJECT.pool.PoolCommands
	 */
	function ScreenNavigator() {

		if (ScreenNavigator.caller != ScreenNavigator.getInstance) {
			throw new Error(
					"There is no public constructor for PoolCommands. Use PoolCommands.getInstance().");
		}

		var objRef = this;

		/* imports */
		var PoolConstants = PROJECT.pool.PoolConstants;
		var CreateUpdatePoolScreen = PROJECT.pool.poolScreens.CreateUpdatePoolScreen;
		var MyPoolScreen = PROJECT.pool.poolScreens.MyPoolScreen;
		var SearchPoolScreen = PROJECT.pool.poolScreens.SearchPoolScreen;
		var UserRegistrationScreen = PROJECT.pool.poolScreens.UserRegistrationScreen;
		var ManageVehiclesScreen = PROJECT.pool.poolScreens.ManageVehiclesScreen;
		var PoolCalendarScreen = PROJECT.pool.poolScreens.PoolCalendarScreen;
		var SearchPoolResultScreen = PROJECT.pool.poolScreens.SearchPoolResultScreen;
		var MyRequestsScreen = PROJECT.pool.poolScreens.MyRequestsScreen;
		var MyNotificationsScreen = PROJECT.pool.poolScreens.MyNotificationsScreen;
		var UserProfileScreen = PROJECT.pool.poolScreens.UserProfileScreen;
		var ChangePasswordScreen = PROJECT.pool.poolScreens.ChangePasswordScreen;

		// var _componentId;
		objRef.navigateTo = navigateTo;

		function navigateTo(screenId, params) {

			if (screenId == PoolConstants.MAIN_SCREEN) {
				var mainScreen = new PROJECT.pool.poolScreens.MainScreen(params);
				mainScreen.render();
			} else if (screenId == PoolConstants.LOGIN_SCREEN) {
				var login = new PROJECT.pool.poolScreens.LoginScreen();
				login.render();
			} else if (screenId == PoolConstants.MY_POOLS_SCREEN) {
				var myPoolScreen = new MyPoolScreen(PoolConstants.RIGHT_CONTENT);
				myPoolScreen.render();
			} else if (screenId == PoolConstants.CREATE_UPDATE_POOL_SCREEN) {
				var screen = new CreateUpdatePoolScreen(
						PoolConstants.RIGHT_CONTENT, params);
				screen.render();
			} else if (screenId == PoolConstants.SEARCH_POOL_SCREEN) {
				var screen = new SearchPoolScreen(PoolConstants.RIGHT_CONTENT,
						params);
				screen.render();
			} else if (screenId == PoolConstants.USER_REGISTRATION_SCREEN) {
				var screen = new UserRegistrationScreen(
						PoolConstants.RIGHT_CONTENT);
				screen.render();
			} else if (screenId == PoolConstants.MANAGE_VEHICLE_SCREEN) {
				var screen = new ManageVehiclesScreen(
						PoolConstants.RIGHT_CONTENT);
				screen.render();
			} else if (screenId == PoolConstants.POOL_CALENDAR_SCREEN) {
				var screen = new PoolCalendarScreen(PoolConstants.RIGHT_CONTENT);
				screen.render();
			} else if (screenId == PoolConstants.SEARCH_POOL_RESULT_SCREEN) {
				var screen = new SearchPoolResultScreen(
						PoolConstants.RIGHT_CONTENT, params);
				screen.render();
			} else if (screenId == PoolConstants.POOL_MY_REQUESTS_SCREEN) {
				var screen = new MyRequestsScreen(PoolConstants.RIGHT_CONTENT,
						params);
				screen.render();
			} else if (screenId == PoolConstants.POOL_MY_NOTIFICATIONS_SCREEN) {
				var screen = new MyNotificationsScreen(PoolConstants.RIGHT_CONTENT,
						params);
				screen.render();
			}			
			else if (screenId == PoolConstants.MY_PROFILE_SCREEN) {
				var screen = new UserProfileScreen(PoolConstants.RIGHT_CONTENT,
						params);
				screen.render();
			}
			else if(screenId == PoolConstants.CHANGE_PASSWORD_SCREEN){
				var screen = new ChangePasswordScreen(PoolConstants.RIGHT_CONTENT, params);
				screen.render();
			}
		}
	}

	/**
	 * This method creates the singleton object of PoolCommands.
	 * 
	 * @function {public PoolCommands} getInstance
	 * @return Returns object of PoolCommands class
	 */
	ScreenNavigator.getInstance = function() {
		if (!ScreenNavigator.instance) {
			ScreenNavigator.instance = new ScreenNavigator();
		}
		return ScreenNavigator.instance;
	};

})();