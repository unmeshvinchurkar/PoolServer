PROJECT.namespace("PROJECT.pool");

/*
 * @class PROJECT.pool.PoolConstants
 */
PROJECT.pool.PoolConstants = (function() {
	var objRef = {};
	objRef.REQUEST_MIME_TYPE = "text/xml";
	objRef.GLOBAL_HEADER_DIV = 'headerDiv';
	objRef.GLOBAL_CONTAINER_DIV = 'containerDiv';
	objRef.GLOBAL_LOADING_DIV = "loadingDiv";

	objRef.MAP_CONTENT = "rightMapContent"
	objRef.MODULE_PATH = "framework/pool/module/";
	objRef.APPLICATION_PATH = "framework/pool/";
	objRef.RIGHT_CONTENT = "rightContent";

	// layouts
	objRef.LAYOUT_SECTION_HEADER = "header";
	objRef.LAYOUT_SECTION_CONTENT = "content";

	// images
	objRef.COMMON_IMAGE_PATH = "framework/pool/images/";

	objRef.APPLICATION_TYPE_POOL = "POOL";

	// Delay time for showing slide up messages
	objRef.MESSAGE_DELAY_TIME = 4000;

	// Session Constants

	objRef.USER_SESSION_ATTR = "USER";

	// Screen Ids
	objRef.LOGIN_SCREEN = "loginScreen";
	objRef.MAIN_SCREEN = "mainScreen";
	objRef.MY_POOLS_SCREEN = "myPoolsScreen";
	objRef.CREATE_UPDATE_POOL_SCREEN = "createUpdatePoolScreen";
	objRef.SEARCH_POOL_SCREEN = "searchPoolScreen";
	objRef.SEARCH_POOL_RESULT_SCREEN = "searchPoolResultScreen";
	objRef.USER_REGISTRATION_SCREEN = "userRegistrationScreen";
	objRef.MANAGE_VEHICLE_SCREEN = "manageVehicleScreen";
	objRef.POOL_CALENDAR_SCREEN = "poolCalendarScreen";
	objRef.POOL_MY_REQUESTS_SCREEN = "myRequestsScreen";
	objRef.POOL_MY_NOTIFICATIONS_SCREEN = "myNotifications";
	objRef.MY_PROFILE_SCREEN = "myProfileScreen";
	objRef.CHANGE_PASSWORD_SCREEN = "changePassword";

	// Pool Commands Constants
	objRef.LOGIN_COMMAND = "login";
	objRef.LOGOUT_COMMAND = "logout";
	objRef.CREATE_POOL_COMMAND = "createPool";
	objRef.SEARCH_POOL_COMMAND = "searchPools";
	objRef.CHANGE_PASSWORD_COMMAND = "changePassword";
	objRef.MY_POOL_COMMAND = "myPools";
	objRef.SIGN_UP_COMMAND = "signup";
	objRef.EDIT_USER_COMMAND = "edituser";
	objRef.GET_CALENDAR_COMMAND = "getCalendar";
	objRef.REMOVE_TRAVELLER_COMMAND = "removeTraveller";
	objRef.DELETE_POOL_COMMAND = "deletepool";
	objRef.GET_VEHICLE_COMMAND = "getVehicle";
	objRef.ADD_VEHICLE_COMMAND = "addVehicle";

	objRef.GET_USER_DETAILS_COMMAND = "getUserDetails";
	objRef.GET_CURRENT_USER_DETAILS_COMMAND = "getCurrentUserDetails";
	objRef.UPLOAD_PROFILE_IMAGE = "upload";
	objRef.GET_USER_PROFILE_STATUS_COMMAND = "getUserProfileStatus";

	objRef.MARK_HOLIDAY_COMMAND = "markHoliday";
	objRef.UNMARK_HOLIDAY_COMMAND = "unMarkHoliday";
	objRef.GET_NOTIFICATIONS_COMMAND = "getNotifications";
	objRef.RAISE_JOIN_REQUEST_COMMAND = "raiseJoinRequest";
	objRef.REJECT_JOIN_REQUEST_COMMAND = "rejectJoinRequest";
	objRef.ACCEPT_JOIN_REQUEST_COMMAND = "acceptJoinRequest";
	objRef.LEAVE_POOL_COMMAND = "leavePool";
	objRef.GET_SENT_REQUESTS_COMMAND = "getSentRequests";
	objRef.GET_RECEIVED_REQUESTS_COMMAND = "getReceivedRequests";

	return objRef;
})();
