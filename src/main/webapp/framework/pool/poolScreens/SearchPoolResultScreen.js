PROJECT.namespace("PROJECT.pool.poolScreens");

(function() {

	/* Class Declaration */
	PROJECT.pool.poolScreens.SearchPoolResultScreen = SearchPoolResultScreen;

	/* extends */
	PROJECT.pool.util.Lang.extend(SearchPoolResultScreen,
			PROJECT.pool.poolScreens.AbstractScreen);

	/**
	 * 
	 * 
	 * @class PROJECT.pool.poolScreens.SearchPoolResultScreen
	 */
	function SearchPoolResultScreen(containerElemId, searchResult) {

		SearchPoolResultScreen.superclass.constructor.call(this);

		var objRef = this;

		var SegmentLoader = PROJECT.pool.util.SegmentLoader;
		var PoolConstants = PROJECT.pool.PoolConstants;
		var PoolCommands = PROJECT.pool.PoolCommands;

		var _containerElemId = containerElemId

		var _container = null;
		var _geocoder = null;
		var _searchResult = searchResult;
		var _poolTable = null;

		/* Public Properties */
		objRef.render = render;

		function render() {
			SegmentLoader.getInstance().getSegment("searchPoolResultSeg.xml",
					null, _init);
		}

		function _init(data) {
			_container = $('#' + _containerElemId);
			_container.html(data);
			_poolTable = new CarPoolTable("poolTable");
			_poolTable.onRowClick(_onClick);
			_poolTable.addRows(_searchResult);
		}

		function _openDialog(carpoolId, pickupLattitude, pickupLongitude) {
			SegmentLoader.getInstance().getSegment("mapDialog.xml", null,
					initDialog);

			function initDialog(data) {
				$("body").append(data);
				var dialogId = "dialogId";
				$("#dialogId").dialog({
					height : 700,
					width : 800,
					draggable : false,
					modal : true,
					open : function() {
						$('.ui-widget-overlay').addClass('custom-overlay');
					},
					close : function() {
						$('.ui-widget-overlay').removeClass('custom-overlay');
					}
				});
				var params = {};
				params["poolId"] = carpoolId;
				params["readOnly"] = true;
				var screen = new PROJECT.pool.poolScreens.CreateUpdatePoolScreen(
						dialogId, params);
				screen.render();
				screen.markPoint(pickupLattitude, pickupLongitude);
			}
		}

		function _onClick(elementId, poolTable, rowData) {

			if (elementId.startsWith("_join")) {
				var poolId = elementId.split(":")[1];

				objRef.get(PoolConstants.GET_USER_PROFILE_STATUS_COMMAND, [ {},
						handleProfileSuccess, function() {
						} ]);

				function handleProfileSuccess(data) {
					var status = data;
					if (status == "incomplete") {
						$("#msg_div").css("display", "block");
						$("#msg_div")
								.html(
										"You can't join pool.Please complete your profile details.");

					} else {
						_raiseJoinRequest(poolId, rowData);
					}
				}
			} else if (elementId.startsWith("_open")) {
				var poolId = elementId.split(":")[1];
				_openDialog(poolId, rowData["pickupLattitude"],
						rowData["pickupLongitude"]);
			}
		}

		function _raiseJoinRequest(poolId, rowData) {

			var poolId = elementId.split(":")[1];

			var params = {};
			params["carPoolId"] = poolId;
			params["srcLattitude"] = rowData["pickupLattitude"];
			params["srcLongitude"] = rowData["pickupLongitude"];
			params["destLattitude"] = rowData["destLattitude"];
			params["destLongitude"] = rowData["destLongitude"];
			params["tripCost"] = rowData["tripCost"];
			params["pickupDistance"] = rowData["pickupDistance"];
			params["pickupTime"] = rowData["pickupTime"];

			objRef.fireCommand(PoolConstants.RAISE_JOIN_REQUEST_COMMAND, [
					params, _joinReqSuccess, function() {
					} ]);

			function _joinReqSuccess() {
				$(document.getElementById(elementId)).attr("disabled",
						"disabled");
				$(document.getElementById(elementId)).parent().html(
						"Join Request Sent");
			}
		}

		function CarPoolTable(id) {
			var that = this;
			var _jTable = null;
			var _id = id;
			var _callBackFun = null;

			that.addRow = addRow;
			that.addRows = addRows;
			that.destroy = destroy;
			that.clear = clear;
			that.onRowClick = onRowClick;
			that.deleteRow = deleteRow;

			_initialize();

			function onRowClick(callBack) {
				_callBackFun = callBack;
			}

			function deleteRow(row) {
				_jTable.fnDeleteRow(row);
			}

			function destroy() {
				try {
					if (_jTable) {
						_jTable.fnDestroy();
					}
				} catch (e) {
				}
				_jTable = null;
				$('#' + _id).html("");
			}

			function _initialize() {

				_jTable = $('#' + _id)
						.dataTable(
								{

									"aoColumns" : [
											{
												'sTitle' : "SN",
												'sWidth' : '5%',
												'mDataProp' : "srcArea" // dummy
											// property
											},
											{
												'sTitle' : "Start Address",
												'sWidth' : '30%',
												'sType' : 'string-case',
												'mDataProp' : 'srcArea',
												"bUseRendered" : true,
												'fnRender' : function(o) {
													return "<span style='display:block;overflow:hidden;width:100%;' title='"
															+ o.aData["srcArea"]
															+ "'>"
															+ o.aData["srcArea"]
															+ "</span>";
												}
											},
											{
												'sTitle' : "End Address",
												'sWidth' : '30%',
												'sType' : 'string-case',
												'mDataProp' : 'destArea',
												"bUseRendered" : true,
												'fnRender' : function(o) {
													return "<span style='display:block;overflow:hidden;width:100%;' title='"
															+ o.aData["destArea"]
															+ "'>"
															+ o.aData["destArea"]
															+ "</span>";
												}
											},

											{
												'sTitle' : "Owner Name",
												'sWidth' : '20%',
												'sType' : 'string-case',
												'mDataProp' : 'ownerName',
												"bUseRendered" : true,
												'fnRender' : function(o) {
													return "<span style='display:block;overflow:hidden;width:100%' title='"
															+ o.aData["ownerName"]
															+ "'>"
															+ o.aData["ownerName"]
															+ "</span>";
												}
											},
											{
												'sTitle' : "Pickup Time",
												'sWidth' : '7%',
												'sType' : 'string-case',
												'data' : 'pickupTime',
												'render' : function(cellData,
														type, rowData) {

													var unit = "AM";
													var timeInSeconds = cellData;
													var hrs = parseInt(timeInSeconds / (3600));

													if (hrs >= 12) {
														unit = "PM";

														if (hrs >= 13) {
															hrs = hrs - 12;
														}
													}

													var remSeconds = parseInt(timeInSeconds % (3600));
													var secs = parseInt(remSeconds / 60);

													secs = secs > 9 ? secs
															: secs + "0";

													return hrs + ":" + secs
															+ unit;
												}
											},
											{
												'sTitle' : "Send Join Request",
												'sWidth' : '8%',
												'sType' : 'string-case',
												'mDataProp' : 'carPoolId',
												"mRender" : function(data,
														type, full) {
													
													if(!full["requestReceived"]){
													
													return '<a href="javascript:void(0)" id="_join:'
															+ data
															+ '">Send Join Request</a>';
													}
													else{														
														return "Request Received";
													}
												}
											} ],
									"bInfo" : false,
									"bFilter" : false,
									"pagingType" : "full_numbers",
									"iDisplayLength" : 25,
									"bLengthChange" : false,
									"aaSorting" : [ [ 1, "asc" ] ],
									"fnRowCallback" : function(nRow, aData,
											iDisplayIndex, iDisplayIndexFull) {

										// Populate index column
										var index = iDisplayIndex + 1;

										$("td:first", nRow).html(
												"<a href ='javascript:void(0)' id='_open:"
														+ aData["carPoolId"]
														+ "' >" + index
														+ "</a>");					

										$(nRow)
												.find(
														"td:first > a, td:last > a")
												.click(
														function() {
															
															var api  =_jTable.api();
															var rowData = api.row($(this).closest("tr")).data();
															_callBackFun($(this).attr("id"), that, rowData);
														});

										return nRow;
									}
								});
			}

			function addRow(data) {
				_jTable.fnAddData([ data ]);
			}

			function addRows(dataArray) {
				if (dataArray && dataArray.length > 0) {
					_jTable.fnAddData(dataArray);
				}
			}

			function clear() {
				_jTable.fnClearTable();
			}
		}
	}

})();