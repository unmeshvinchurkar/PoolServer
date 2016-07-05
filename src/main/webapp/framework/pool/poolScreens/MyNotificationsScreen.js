PROJECT.namespace("PROJECT.pool.poolScreens");

(function() {

	/* Class Declaration */
	PROJECT.pool.poolScreens.MyNotificationsScreen = MyNotificationsScreen;

	/* extends */
	PROJECT.pool.util.Lang.extend(MyNotificationsScreen,
			PROJECT.pool.poolScreens.AbstractScreen);

	/**
	 * @class PROJECT.pool.map.MyNotificationsScreen
	 */
	function MyNotificationsScreen(containerElemId) {

		MyNotificationsScreen.superclass.constructor.call(this);

		var objRef = this;

		var SegmentLoader = PROJECT.pool.util.SegmentLoader;
		var PoolConstants = PROJECT.pool.PoolConstants;
		var PoolCommands = PROJECT.pool.PoolCommands;
		var PoolUtil = PROJECT.pool.util.PoolUtil.getInstance();

		var _containerElemId = containerElemId;
		var _poolTable = null;
		var _carPoolId = null;
		var _isOwner = false;
		var _data = {};

		/* Public Properties */
		objRef.render = render;

		function render() {
			_container = $('#' + _containerElemId);
			objRef.get(PoolConstants.GET_NOTIFICATIONS_COMMAND, [ {},
					_renderNotifications, _fetchingFailed ]);
		}

		function _renderNotifications(data) {

			var html = "";
			var valArray = data;

			if (valArray != null) {

				html = "<table class='requestTable'><thead><tr><th>SNo.</th><th>Related User</th><th>Notification Date</th><th>Notification Type</th><th>Holiday Date</th></tr></thead><tbody>";

				for (var i = 0; i < valArray.length; i++) {
					var row = valArray[i];

					var date = new Date(1970, 0, 1);
					date.setSeconds(row["createDate"]);

					var holidayDate = null;

					if (row["holidayDate"]) {
						holidayDate = new Date(1970, 0, 1);
						holidayDate.setSeconds(row["createDate"]);
					}

					html = html + "<tr>";
					html = html + "<td><a  id='" + row["carPoolId"]
							+ "' href='javascript:void(0)' >" + i + "</a></td>";
					html = html + "<td>" + row["fromUser"] + "</td>";
					html = html + "<td>" + PoolUtil.convertDateToString(date) + "</td>";
					html = html + "<td>" + row["notificationType"] + "</td>";
					html = html + "<td>"
							+ (holidayDate ? PoolUtil.convertDateToString(holidayDate) : "")
							+ "</td>";
					html = html + "</tr>";
				}
			}
			html = html + "</tbody></table>";
			_container.html(html);
			$(".requestTable a").click(_showCarPoolMap);
		}

		function _fetchingFailed() {
		}

		function _showCarPoolMap(e) {
			var target = $(e.target);
			var carPoolId = $(target).attr("id");
			_openDialog(carPoolId);
		}

		function _openDialog(carpoolId) {
			SegmentLoader.getInstance().getSegment("mapDialog.xml", null,
					initDialog);

			function initDialog(data) {
				$("body").append(data);
				var dialogId = "dialogId";

				var params = {};
				params["poolId"] = carpoolId;
				params["readOnly"] = true;
				var screen = new PROJECT.pool.poolScreens.CreateUpdatePoolScreen(
						dialogId, params);

				$("#dialogId").dialog({
					height : 700,
					width : 800,
					draggable : false,
					modal : false,
					open : function() {
						$('.ui-widget-overlay').addClass('custom-overlay');
					},
					close : function() {
						$('.ui-widget-overlay').removeClass('custom-overlay');
						screen.destroy();
						$(this).dialog('close');
						$(this).remove();
						$("#dialogId").remove();
					}
				});
				screen.render();
			}
		}
	}
})();