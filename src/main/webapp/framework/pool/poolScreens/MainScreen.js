PROJECT.namespace("PROJECT.pool.poolScreens");

(function() {

	/* Class Declaration */
	PROJECT.pool.poolScreens.MainScreen = MainScreen;

	/* extends */
	PROJECT.pool.util.Lang.extend(MainScreen,
			PROJECT.pool.poolScreens.AbstractScreen);

	/**
	 * @class PROJECT.pool.poolScreens.MainScreen
	 */
	function MainScreen() {

		MainScreen.superclass.constructor.call(this);

		var objRef = this;

		var SegmentLoader = PROJECT.pool.util.SegmentLoader;
		var PoolConstants = PROJECT.pool.PoolConstants;
		var PoolCommands = PROJECT.pool.PoolCommands;
		var _container = null;

		/* Public Properties */
		objRef.render = render;

		function render() {
			SegmentLoader.getInstance().getSegment("mainSeg.xml", null, _init);
		}

		function _init(data) {
			_container = $('#' + PoolConstants.GLOBAL_CONTAINER_DIV);
			_container.html(data);
			$("#createPool").click(_createPool);
			$("#myPools").click(_showMyPools);
			$("#searchPool").click(_searchPools);
			$("#manageVehicles").click(_manageVehicles);
			$("#poolCalendar").click(_poolCalendar);
			$("#logOut").click(_logout);
			$("#myRequests").click(_showMyRequests);
			$("#notifications").click(_showNotifications);
			$("#myProfile").click(_showMyProfile);
			$("#changePassword").click(_changePassword);

			$('.mainTabs > .tabs .tab-links a').on(
					'click',
					function(e) {
						// Change/remove current tab to active
						$(this).parent('li').addClass('active').siblings()
								.removeClass('active');

						e.preventDefault();
					});

			_showMyPools();
		}

		function _changePassword(e) {
			objRef.navigateTo(PoolConstants.CHANGE_PASSWORD_SCREEN);
			e.preventDefault();
		}

		function _showNotifications(e) {
			objRef.navigateTo(PoolConstants.POOL_MY_NOTIFICATIONS_SCREEN);
			e.preventDefault();
		}

		function _showMyRequests(e) {
			objRef.navigateTo(PoolConstants.POOL_MY_REQUESTS_SCREEN);
			e.preventDefault();
		}

		function _poolCalendar(e) {
			objRef.navigateTo(PoolConstants.POOL_CALENDAR_SCREEN);
			e.preventDefault();
		}

		function _manageVehicles(e) {
			objRef.navigateTo(PoolConstants.MANAGE_VEHICLE_SCREEN);
			e.preventDefault();
		}

		function _logout(e) {
			objRef.fireCommand("logout");
			objRef.navigateTo(PoolConstants.LOGIN_SCREEN);
			e.preventDefault();
		}

		function _createPool(e) {
			objRef.navigateTo(PoolConstants.CREATE_UPDATE_POOL_SCREEN);
			e.preventDefault();
		}

		function _showMyPools(e) {
			objRef.navigateTo(PoolConstants.MY_POOLS_SCREEN);
			if (e)
				e.preventDefault();
		}

		function _searchPools(e) {
			objRef.navigateTo(PoolConstants.SEARCH_POOL_SCREEN);
			e.preventDefault();
		}
		
		function _showMyProfile(e){
			objRef.navigateTo(PoolConstants.MY_PROFILE_SCREEN);
			e.preventDefault();
		}

	}
})();