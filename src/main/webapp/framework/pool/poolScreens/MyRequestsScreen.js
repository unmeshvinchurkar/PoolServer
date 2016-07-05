PROJECT.namespace("PROJECT.pool.poolScreens");

(function() {

	/* Class Declaration */
	PROJECT.pool.poolScreens.MyRequestsScreen = MyRequestsScreen;

	/* extends */
	PROJECT.pool.util.Lang.extend(MyRequestsScreen,
			PROJECT.pool.poolScreens.AbstractScreen);

	/**
	 * @class PROJECT.pool.map.PoolCalendarScreen
	 */
	function MyRequestsScreen(containerElemId) {

		MyRequestsScreen.superclass.constructor.call(this);

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
		var _dialog = null;

		/* Public Properties */
		objRef.render = render;

		function render() {
			SegmentLoader.getInstance().getSegment("myRequestsPage.xml", null,
					_init);
		}

		function _init(data) {
			_container = $('#' + _containerElemId);
			_container.html(data);

			objRef.get(PoolConstants.GET_SENT_REQUESTS_COMMAND, [ {},
					_renderRequests, _fetchingFailed ]);
		}

		function _renderRequests(data) {
			var sentReqdata = null;
			var recvReqdata = null;

			if (data) {
				sentReqdata = data["sentRequests"];
				recvReqdata = data["receivedRequests"];
			}
			_renderSentRequests(sentReqdata);
			_renderReceivedRequests(recvReqdata);
			$(".requestTable a").click(_handleClick);
		}
		
		function _renderReceivedRequests(data) {

			var html = "";
			var valArray = data;

			if (valArray != null) {

				html = "<table class='requestTable'><thead><tr><th>SNo.</th><th>From User</th><th>Pickup Time</th><th>Request Date</th><th>Accept/Reject</th></tr></thead><tbody>";

				for (var i = 0; i < valArray.length; i++) {
					var row = valArray[i];

					_data[row["requestId"]] = row;

					var date = new Date(1970, 0, 1);
					date.setSeconds(row["createDate"]);

					html = html + "<tr>";
					html = html + "<td><a requestId=" + row["requestId"] + "  id='" + row["carPoolId"]
							+ "' href='javascript:void(0)' >" + i + "</a></td>";

					html = html + "<td><a userId='" + row["userId"]+"' href='javascript:void(0)'> " + row["fullName"] + "</a></td>";
					html = html + "<td>"
							+ _convertSecondsToTime(row["startTime"]) + "</td>";
					html = html + "<td>" + PoolUtil.convertDateToString(date) + "</td>";
					html = html
							+ "<td>"
							+ '<button requestId="'	+ row["requestId"] 	+ '" id="acceptRequest" type="button">Accept</button>  <button requestId="'	+ row["requestId"] 	+ '" id="rejectRequest" type="button">Reject</button> '
							+ "</td>";
					html = html + "</tr>";
				}
			}
			html = html + "</tbody></table>";
			$("#receivedRequests").html(html);
			_applyEffect();
			
			$("button#acceptRequest").click(_acceptRequest);
			$("button#rejectRequest").click(_rejectRequest);
		}
		
		function _renderSentRequests(data) {

			var html = "";
			var valArray = data;

			if (valArray != null) {

				html = "<table class='requestTable'><thead><tr><th>SNo.</th><th>Car Pool Owner</th><th>Pickup Time</th><th>Request Date</th><th>Status</th></tr></thead><tbody>";

				for (var i = 0; i < valArray.length; i++) {
					var row = valArray[i];
					_data[row["requestId"]] = row;

					var date = new Date(1970, 0, 1);
					date.setSeconds(row["createDate"]);

					html = html + "<tr>";
					html = html + "<td><a requestId=" + row["requestId"] + " id='" + row["carPoolId"]
							+ "' href='javascript:void(0)' >" + i + "</a></td>";
					
					html = html + "<td><a userId='" + row["userId"]+"' href='javascript:void(0)'> " + row["ownerName"] + "</a></td>";
					
					html = html + "<td>"
							+ _convertSecondsToTime(row["startTime"]) + "</td>";
					html = html + "<td>" + date.toString() + "</td>";
					html = html + "<td>" + _getStatus(row["status"]) + "</td>";
					html = html + "</tr>";
				}
			}
			html = html + "</tbody></table>";
			$("#sentRequests").html(html);
			_applyEffect();
		}

		function _fetchingFailed() {
		}
		
		function _handleClick(e) {
			var target = $(e.target);

			if ($(target).attr("requestId")) {
				var reqData = _data[$(target).attr("requestId")];
				_openDialog(reqData["carPoolId"], reqData["pickupLattitude"],
						reqData["pickupLongitude"], reqData["destLattitude"],
						reqData["destLongitude"],
						_convertSecondsToTime(reqData["startTime"]));
			}
			else if($(target).attr("userId")){				
				_showUserDetails($(target).attr("userId"));				
			}
		}
		
		function _showUserDetails(userId) {

			SegmentLoader.getInstance().getSegment("mapDialog.xml", null,
					initDialog);

			function initDialog(data) {
				$("body").append(data);
				var dialogId = "dialogId";

				var screen = new PROJECT.pool.poolScreens.UserProfileScreen(
						dialogId, userId, true);

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
						// screen.destroy();
						$(this).dialog('close');
						$(this).remove();
						$("#dialogId").remove();
					}
				});
				screen.render();
			}

		}

		

		function _acceptRequest(e) {
			var target = $(e.target);
			var reqData = _data[$(target).attr("requestId")];

			if (reqData) {

				var params = {};
				params["requestId"] = reqData["requestId"];
				objRef.post(PoolConstants.ACCEPT_JOIN_REQUEST_COMMAND, [
						params, _acceptedSuccessful, _acceptedFailed ]);

				function _acceptedSuccessful() {
					$(".requestTable button[requestId='"+ reqData["requestId"] + "']").attr("disabled", "disabled");

					$(".requestTable button[requestId='"+ reqData["requestId"] + "']").parent().html("Accepted");
				}
				
				function _acceptedFailed() {
				}
			}
		}

		function _rejectRequest(e) {
			var target = $(e.target);
			var reqData = _data[$(target).attr("requestId")];

			if (reqData) {

				var params = {};
				params["requestId"] = reqData["requestId"];
				objRef.post(PoolConstants.REJECT_JOIN_REQUEST_COMMAND, [
						params, _rejectSuccessful, _rejectFailed ]);

				function _rejectSuccessful() {
					$(".requestTable button[requestId='"+ reqData["requestId"] + "']").attr("disabled", "disabled");
					$(".requestTable button[requestId='"+ reqData["requestId"] + "']").parent().html("Rejected");
				
				
				}
				
				function _rejectFailed() {
				}
			}
		}

	
		
		function _getStatus(status) {
			if (!status) {
				return "Pending"
			}
			return (row["status"] == 1 ? 'Accepted' : 'Rejected');
		}

		function _applyEffect() {

			$('.tabs .tab-links a').on(
					'click',
					function(e) {
						var currentAttrValue = jQuery(this).attr('href');
						// Show/Hide Tabs
						jQuery('.tabs ' + currentAttrValue).show().siblings()
								.hide();
						// Change/remove current tab to active
						jQuery(this).parent('li').addClass('active').siblings()
								.removeClass('active');
						e.preventDefault();
					});
		}

		function _openDialog(carpoolId, pickupLattitude, pickupLongitude, destLattitude,destLongitude, pickupTime) {
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

				if (_dialog && dialog.destroy) {
					_dialog.destroy();
				}
				
				_dialog = $("#dialogId").dialog({
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
						 $("#dialog").remove();
					}
				});

				screen.render();

				setTimeout(function() {
					screen.markPoint(pickupLattitude, pickupLongitude, "",pickupTime );
					screen.markPoint(destLattitude, destLongitude );
				}, 3000);

				
			}
		}		

		function _convertSecondsToTime(timeInSeconds) {
			return PoolUtil.convertSecondsToTime(timeInSeconds);
		}
	}
})();