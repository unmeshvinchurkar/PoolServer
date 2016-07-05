PROJECT.namespace("PROJECT.pool.model");

(function() {

	/* Class Declaration */
	PROJECT.pool.model.LoginModel = LoginModel;

	/**
	 * @class PROJECT.pool.map.GooglePoolMap
	 */
	function LoginModel() {
		var objRef = this;

		objRef.userName = ko.observable("");
		objRef.password = ko.observable("");

		var SegmentLoader = PROJECT.pool.util.SegmentLoader;
		var PoolConstants = PROJECT.pool.PoolConstants;
		var PoolCommands = PROJECT.pool.PoolCommands;
		var PoolConstants = PROJECT.pool.PoolConstants;
		var _container = null;

		/* Public Properties */
		objRef.render = render;

		function render() {
			SegmentLoader.getInstance().getSegment("loginSeg.xml", null, _init);
		}

		function _init(data) {
			_container = $('#' + PoolConstants.GLOBAL_CONTAINER_DIV);
			_container.html(data);
			// $("#loginId").click(_handleClick);
		}

		function _handleClick(e) {
			var username = $("#usernameId").val();
			var passwd = $("#passwordId").val();
			var params = {};
			params["username"] = username;
			params["password"] = passwd;

			PoolCommands.getInstance().execute(PoolConstants.LOGIN_COMMAND,
					[ params, _login, _loginFailed ]);
		}

		function _login() {
			var gMap = new PROJECT.pool.map.GooglePoolMap();
			gMap.render();
		}

		function _loginFailed() {
			alert("login failed");
		}
	}

})();