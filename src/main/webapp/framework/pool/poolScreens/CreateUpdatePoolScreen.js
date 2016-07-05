PROJECT.namespace("PROJECT.pool.poolScreens");

(function() {

	/* Class Declaration */
	PROJECT.pool.poolScreens.CreateUpdatePoolScreen = CreateUpdatePoolScreen;

	/* extends */
	PROJECT.pool.util.Lang.extend(CreateUpdatePoolScreen,
			PROJECT.pool.poolScreens.AbstractScreen);

	/**
	 * 
	 * 
	 * @class PROJECT.pool.map.GooglePoolMap
	 */
	function CreateUpdatePoolScreen(containerElemId, params) {

		CreateUpdatePoolScreen.superclass.constructor.call(this);

		var objRef = this;

		var SegmentLoader = PROJECT.pool.util.SegmentLoader;
		var PoolConstants = PROJECT.pool.PoolConstants;
		var PoolCommands = PROJECT.pool.PoolCommands;
		var PoolUtil = PROJECT.pool.util.PoolUtil.getInstance();

		var _USER_ROW = '<tr "><td><img id="{imageId}" class="" src="{ImageSrc}" alt="" /></td> <td class="text-center"><strong>{name}</<strong></td><td class="text-center"><strong>{email}</<strong></td><td class="text-center">{phoneNo}</td><td class="text-center"><strong>{tripCost}</<strong></td><td class="text-center"><strong>{pickupDistance}</<strong></td><td class="text-center"><strong>{remove}</<strong></td></tr>';

		var _containerElemId = containerElemId;
		var _container = null;
		var _carPoolId = params ? params["poolId"] : null;
		var _isReadOnly = params ? params["readOnly"] : false;

		if (_carPoolId) {
			_isReadOnly = true;
		}

		var _geocoder = null;

		/* Public Properties */
		objRef.render = render;
		objRef.markPoint = markPoint;
		objRef.destroy = destroy;

		var _map;
		var _directionsService;
		var _directionRenderer;
		var _markers = [];

		var _autocomplete = null;
		var _autocomplete1 = null;
		var _srcMarker = null;
		var _destMarker = null;
		var _infowindow = null;

		var _fromDateElem = null;
		var _toDateElem = null;
		var _startTimeElem = null;
		var _route = null;
		var _srcAddress = null;
		var _destAddress = null;
		var _poolPath = null;

		function destroy() {
			$('#' + _containerElemId).html(" ");

		}

		function render() {
			SegmentLoader.getInstance().getSegment("createPoolSeg.xml", null,
					_initAndLoadVehicle);
		}

		function _initAndLoadVehicle(htmlData) {
			_container = $('#' + _containerElemId);
			_container.html(htmlData);

			objRef.get(PoolConstants.GET_USER_PROFILE_STATUS_COMMAND, [ {},
					handleProfileSuccess, function() {
					} ]);

			function handleProfileSuccess(data1) {
				var status = data1;
				if (status == "complete") {
					// Fetch Vehicle details
					objRef.get(PoolConstants.GET_VEHICLE_COMMAND, [ {},
							handleVehicleSuccess, handleVehicleFailure ]);
				} else {
					$("#msg_div").css("display", "block");
					$("#msg_div")
							.html(
									"Please complete your profile details before creating pool.");
					_isReadOnly = true;
					_init() ;
				}
			}

			function handleVehicleSuccess(data) {
				_init();
				$("#vehicleId").html(data.registrationNo);
				$("#vehicleId").click(function() {
					_showVehicleDetails(data)
				});
			}

			function handleVehicleFailure() {
				_isReadOnly = true;
				_init();
				$("#vehicleId").hide();

				$("#msg_div").css("display", "block");
				$("#msg_div")
						.html(
								"Please go to <strong>Register Vehicle</strong> tab and register vehicle for creating pool");

			}
		}		

		function _showVehicleDetails(data) {

			SegmentLoader.getInstance().getSegment("mapDialog.xml", null,
					initDialog);

			function initDialog(data) {
				$("body").append(data);
				var dialogId = "dialogId";

				var screen = new PROJECT.pool.poolScreens.ManageVehiclesScreen(
						dialogId, true);

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

		function _init() {

			if (!_isReadOnly) {
				$("#savePoolButton").click(_handleSave);
			} else {
				$("#savePoolButton").html("Update");
				$("#savePoolButton").click(_handleSave);
			}

			_fromDateElem = $("#fromDate");
			_toDateElem = $("#toDate");
			_startTimeElem = $('#startTime');

			$('#excludeDays').multiselect();

			$(_fromDateElem).datepicker(
					{
						dateFormat : 'dd-mm-yy',
						showOtherMonths : true,
						selectOtherMonths : true,
						changeYear : true,
						defaultDate : new Date(),
						minDate : new Date(),
						onClose : function(selectedDate) {
							$(_toDateElem).datepicker("option", "minDate",
									selectedDate);
						}
					});

			$(_toDateElem).datepicker(
					{
						dateFormat : 'dd-mm-yy',
						showOtherMonths : true,
						selectOtherMonths : true,
						changeYear : true,
						'defaultDate' : new Date(),
						minDate : new Date(),
						onClose : function(selectedDate) {
							$(_fromDateElem).datepicker("option", "maxDate",
									selectedDate);
						}
					});

			$(_startTimeElem).timepicker({
				'step' : 15,
				'disableTextInput' : true,
				'forceRoundTime' : true
			});

			$(_fromDateElem).datepicker("setDate", new Date());
			$(_toDateElem).datepicker("setDate", new Date());

			$(_startTimeElem).timepicker('setTime', new Date());
			_directionRenderer = new google.maps.DirectionsRenderer({
				suppressMarkers : true,
				draggable : true
			});

			if (!_isReadOnly) {
				_directionRenderer
						.addListener('directions_changed',
								function() {
									var directions = _directionRenderer
											.getDirections();
									if (directions) {
										_drawRoute(directions.routes[0]);
									}
								});
			}

			_geocoder = new google.maps.Geocoder();
			_directionsService = new google.maps.DirectionsService();

			var mapOptions = {
				zoom : 13,
				mapTypeId : google.maps.MapTypeId.ROADMAP
			};
			_map = new google.maps.Map(document.getElementById('map-canvas'),
					mapOptions);
			_directionRenderer.setMap(_map);

			if (!_isReadOnly) {

				// Create the search box and link it to the UI element.
				var input = (document.getElementById('pac-input'));
				_map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

				var input1 = (document.getElementById('pac-input1'));
				_map.controls[google.maps.ControlPosition.TOP_CENTER]
						.push(input1);

				

				_autocomplete = new google.maps.places.Autocomplete(input);
				_autocomplete.bindTo('bounds', _map);

				_autocomplete1 = new google.maps.places.Autocomplete(input1);
				_autocomplete1.bindTo('bounds', _map);

				_infowindow = new google.maps.InfoWindow();

				google.maps.event.addListener(_map, 'click', function(event) {
					_placeMarker(event.latLng);
				});

				google.maps.event.addListener(_autocomplete, 'place_changed',
						function() {
							_navToPlace(_autocomplete, true)
						});
				google.maps.event.addListener(_autocomplete1, 'place_changed',
						function() {
							_navToPlace(_autocomplete1, false)
						});

				var resetMap = (document.getElementById('resetMap'));
				_map.controls[google.maps.ControlPosition.TOP_RIGHT]
						.push(resetMap);
				$(resetMap).click(_clearMap);
			} 

			if (_carPoolId) {
				_loadPoolData(_carPoolId);
			}

			if (_isReadOnly) {
				_makeReadOnly();
			}

			if (!_isReadOnly) {
				_getLocation();
			} else {
				var pos = {};
				pos.coords = {};
				pos.coords.latitude = 20.5937;
				pos.coords.longitude = 78.9629;
				_showPosition(pos);
			}
		}
		

		function _getLocation() {
		    if (navigator.geolocation) {
		        navigator.geolocation.getCurrentPosition(_showPosition);
		    } else {
		       //"Geolocation is not supported by this browser.";
		    }
		}

		function _showPosition(position) {
			_map.setCenter({
				lat : position.coords.latitude,
				lng : position.coords.longitude
			});
		}

		function _makeReadOnly() {

			$(_fromDateElem).attr("disabled", "disabled");
			$(_startTimeElem).attr("disabled", "disabled");
			$("#totalSeats").attr("disabled", "disabled");
			$("#bucksPerKm").attr("disabled", "disabled");

			if (_isReadOnly && !_carPoolId) {
				$(_toDateElem).attr("disabled", "disabled");
				$("#savePoolButton").remove();
			}	
			

			$(".multiselect-container input:checkbox").attr("disabled", "disabled");
			
			$("#pac-input").remove();
			$("#pac-input1").remove();

			if (_autocomplete) {
				_autocomplete.unbindAll();
				google.maps.event.clearInstanceListeners(_autocomplete);
			}
			if (_autocomplete1) {
				_autocomplete1.unbindAll();
				google.maps.event.clearInstanceListeners(_autocomplete1);
			}
			
			$("#savePoolButton").html("Update");
			$("#resetMap").remove();
			
			_isReadOnly = true;
		}

		function _loadPoolData(poolId) {

			objRef.fetch(poolId, drawPool);

			function drawPool(data) {

				var excludedDays = data.excludedDays;

				if (excludedDays && excludedDays.trim()) {
					
					$("#excludeDays").multiselect("enable");
					$(".multiselect-container input:checkbox").removeAttr("disabled");
					
					
					var days = excludedDays.split(",");					
					$("#excludeDays").val(days);
					$("#excludeDays").multiselect("refresh");
					
					$(".multiselect-container input:checkbox").attr("disabled", "disabled")
				}

				if (data.noOfRemainingSeats) {
					$("#totalSeats").val(data.noOfRemainingSeats);
				}

				if (data.bucksPerKm) {
					$("#bucksPerKm").val(data.bucksPerKm);
				}

				if (data.startDate) {
					$(_fromDateElem).datepicker("setDate",
							new Date(data.startDate * 1000));
				}

				if (data.endDate) {
					$(_toDateElem).datepicker("setDate",
							new Date(data.endDate * 1000));

					if (_carPoolId) {

						$(_toDateElem).datepicker("option", "minDate",
								$(_toDateElem).datepicker("getDate"));
					}

				}

				if (data.startTime) {
					var now = new Date();
					var d = new Date(now.getFullYear(), now.getMonth(), now
							.getDay(), 0, 0, 0, 0);

					$(_startTimeElem).timepicker('setTime',
							new Date(d.getTime() + data.startTime * 1000));
				}

				_srcAddress = data.srcArea;
				_destAddress = data.destArea;

				var srcLoc = new google.maps.LatLng(data.srcLattitude,
						data.srcLongitude);
				var destLoc = new google.maps.LatLng(data.destLattitude,
						data.destLongitude);

				_srcMarker = _createMarker(srcLoc, "START");

				_destMarker = _createMarker(destLoc, "END");  

				var bounds = new google.maps.LatLngBounds(srcLoc, destLoc);
				_map.fitBounds(bounds);

				var coordinates = [];
				var geoPoints = data.geoPoints;

				var bounds = new google.maps.LatLngBounds();
				for (var i = 0; i < geoPoints.length; i++) {
					var latLong = new google.maps.LatLng(geoPoints[i].latitude,
							geoPoints[i].longitude);

					coordinates.push(latLong);
					bounds.extend(latLong);
				}

				_poolPath = new google.maps.Polyline({
					path : coordinates,
					geodesic : true,
					strokeColor : '#FF0000',
					strokeOpacity : 1.0,
					strokeWeight : 2
				});

				_poolPath.setMap(_map);

				_map.fitBounds(bounds);

				var subscriptions = data.subscriptionDetails;

				if (subscriptions) {
					for (var i = 0; i < subscriptions.length; i++) {
						var sub = subscriptions[i];
						objRef
								.markPoint(
										sub["pickupLattitude"],
										sub["pickupLongitute"],
										sub["name"],
										PoolUtil
												.convertSecondsToTime(sub["pickupTime"]));
					}
					_drawSubUserDetails(subscriptions);
				}
			}
		}

		function _drawSubUserDetails(subDataArray) {

			if (subDataArray != null && subDataArray.length > 0) {

				var table = $("#users").show();
				var tableBody = table.find("tbody");
				for (var i = 0; i < subDataArray.length; i++) {
					var data = subDataArray[i];

					var rowHtml = null;

					if (!data["contactNo"]) {
						data["contactNo"] = "_";
					}

					if (data["profileImagePath"]) {

						rowHtml = _USER_ROW.replace("{ImageSrc}",
								objRef.SERVER_URL + "images/"
										+ data["profileImagePath"]);

					} else {
						rowHtml = _USER_ROW.replace("{ImageSrc}",
								"framework\style\images\no_image.jpg");
					}

					rowHtml = rowHtml.replace("{name}", data["name"]).replace(
							"{phoneNo}", data["contactNo"]).replace("{email}",
							data["email"]).replace("{imageId}", data["userId"])
							.replace("{tripCost}", data["tripCost"]);
					rowHtml = rowHtml.replace("{pickupDistance}",
							data["pickupDistance"] + " km");
					rowHtml = rowHtml.replace("{remove}", "<a id='"
							+ data["userId"]
							+ "'  href='javascript:void(0)'>Remove</a>");

					$(tableBody).append(rowHtml);
				}

				$(table).on("click", "img", function() {
					_showUserDetails(this.id)
				});

				$(table).on("click", "a", _removeUser);
			}
		}

		function _removeUser(userId) {

			var userId = this.id;
			var target = this;
			var param = {};
			params["carPoolId"] = _carPoolId;
			params["travellerId"] = userId;

			objRef.fireCommand(PoolConstants.REMOVE_TRAVELLER_COMMAND, [
					params, _tRemovedSuccess, function() {
					} ]);

			function _tRemovedSuccess() {
				$(target).closest("tr").remove();
				var rows = $("#users").children("tr");

				if (rows.length == 0) {
					$("#users").hide();
				}

				objRef.successMsg("msg_div", "Successfully Removed traveller",
						3000);
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

		function markPoint(lattitude, longitude, userName, pickupTime) {
			var latLng = {};

			var content = userName ? userName : "";
			content = pickupTime ? content + ", Pickup: " + pickupTime
					: content

			latLng["lat"] = lattitude;
			latLng["lng"] = longitude;
			var marker = new google.maps.Marker({
				position : latLng,
				label : content,
				title : content,
				map : _map,
				icon : 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'
			});

			var infowindow = new google.maps.InfoWindow({
				content : content
			});
			infowindow.open(_map, marker);

			if (content) {

				google.maps.event.addListener(marker, 'click', function() {
					var infowindow = new google.maps.InfoWindow({
						content : content
					});
					infowindow.open(_map, marker);
				});
			}

		}

		function _placeMarker(location, labelStr) {

			var marker = _createMarker(location,"")

			if (!_srcMarker) {
				_srcMarker = marker;
				marker.setLabel("START");
			} else if (!_destMarker) {
				_destMarker = marker;
				marker.setLabel("END");
				_calcRoute(_srcMarker.getPosition(), _destMarker.getPosition());
			} else if (!_srcMarker && !_destMarker) {
				_srcMarker = marker;
				marker.setLabel("START");
				_destMarker = null;
			} else if (_srcMarker && _destMarker) {
				//_clearMap();
				marker.setMap(null);
			}
		}

		function _clearMap() {

			if (_srcMarker) {
				_srcMarker.setMap(null);
				_srcMarker = null;
			}

			if (_destMarker) {
				_destMarker.setMap(null);
				_destMarker = null;
			}

			_clearPath();

			if (_autocomplete) {
				_autocomplete.set('place', void (0));
				$("#pac-input").val('');
			}
			if (_autocomplete1) {
				_autocomplete1.set('place', void (0));
				$("#pac-input1").val('');
			}
		}
		
		function _clearPath(){
			
			_directionRenderer.set('directions', null);
			if (_poolPath) {
				_poolPath.setMap(null);
			}
		}

		function _calcRoute(startPos, destpos) {

			var startDate = $(_fromDateElem).datepicker("getDate");
			var timeinSeconds = $(_startTimeElem).timepicker(
					'getSecondsFromMidnight');

			var departureTime = startDate.getTime() + timeinSeconds * 1000;

			var request = {
				origin : startPos,
				destination : destpos,
				travelMode : google.maps.TravelMode.DRIVING,
				unitSystem : google.maps.UnitSystem.METRIC,
				drivingOptions : {
					departureTime : new Date(departureTime),
					trafficModel : google.maps.TrafficModel.BEST_GUESS
				}
			// ,key:"AIzaSyDM9TTxSkYXKz6F1XtOod-Nr8Q_wlRaNs4"
			};

			_directionsService.route(request, function(response, status) {
				if (status == google.maps.DirectionsStatus.OK) {
					_route = response.routes[0];
					_drawRoute(_route);
					_directionRenderer.setDirections(response);

				} else {
					_route = null;
				}
			});

		}

		function _drawRoute(route) {

			// Place new markers on the MAP
			var legs = route.legs;
			var startLeg = legs[0];
			var endLeg = legs[legs.length - 1];

			var startStep = startLeg.steps[0];
			var endStep = endLeg.steps[endLeg.steps.length - 1];

			var startLoc = startStep["start_location"];
			var endLoc = endStep["end_location"];

			// Re-create src and dest markers
			_srcMarker.setMap(null);
			_destMarker.setMap(null);

			_srcMarker = _createMarker(startLoc,"START");

			_destMarker = _createMarker(endLoc,"END"); 

			_srcAddress = _route.legs[0].start_address;
			_destAddress = _route.legs[_route.legs.length - 1].end_address;
		}

		function _handleSave(e) {

			if (_srcMarker == null || _destMarker == null) {
				objRef.errorMsg("msg_div",
						"Please mark both source and destination points.");
				return;
			}
			var timeinSeconds = $(_startTimeElem).timepicker(
			'getSecondsFromMidnight');

			if (timeinSeconds == null) {
				objRef.errorMsg("msg_div",
						"Please mention your start time.");
				return;
			}

			var params = {};
			var endDate = $(_toDateElem).datepicker("getDate");
			params["endDate"] = endDate.getTime();

			if (_carPoolId) {
				params["carPoolId"] = _carPoolId;
			} else {

				var startDate = $(_fromDateElem).datepicker("getDate");
				var timeinSeconds = $(_startTimeElem).timepicker(
						'getSecondsFromMidnight');
				var route = _route;
				var totalNoOfSeats = $("#totalSeats").val();
				var bucksPerKm = $("#bucksPerKm").val();

				var excludeDays = [];
				$('#excludeDays :selected').each(function() {
					excludeDays = excludeDays.concat($(this).text());
					excludeDays = excludeDays.concat(",");
				});
				excludeDays.pop();

				params["startDate"] = startDate.getTime();

				params["startTime"] = timeinSeconds;
				params["bucksPerKm"] = bucksPerKm;
				params["excludeDays"] = excludeDays;

				// Remove instructions which contain special characters

				if (route) {
					var totalDistance = _getTotalDistance(route);
					params["totalDistance"] = totalDistance;

					var legs = route["legs"];
					for (var i = 0; i < legs.length; i++) {
						var leg = legs[i];
						var steps = leg["steps"];
						for (var j = 0; j < steps.length; j++) {

							steps[j].instructions = '';
							var obj = {};
							obj["lat"] = steps[j]["start_point"].lat();
							obj["lng"] = steps[j]["start_point"].lng();
							steps[j]["start_point"] = obj;

							obj = {};
							obj["lat"] = steps[j]["end_point"].lat();
							obj["lng"] = steps[j]["end_point"].lng();
							steps[j]["end_point"] = obj;

							var path = steps[j].path;

							for (var k = 0; k < path.length; k++) {
								var obj = {};
								obj["lat"] = path[k].lat();
								obj["lng"] = path[k].lng();
								path[k] = obj;
							}
						}
					}
				}

				params["route"] = route ? _escape(JSON.stringify(route))
						: undefined;
				params["totalSeats"] = totalNoOfSeats;
				params["srcArea"] = _srcAddress;
				params["destArea"] = _destAddress;
			}

			_disableSave();
			objRef.fireCommand(PoolConstants.CREATE_POOL_COMMAND, [ params,
					_saveSuccess, _saveError ]);
		}

		function _escape(text) {
			return text.replace(/(\r\n|\n|\r|(\n)+)/gm, "").replace(/[\b]/g,
					' ').replace(/\\n/g, ' ').replace(/[\f]/g, ' ').replace(
					/[\n]/g, ' ').replace(/[\r]/g, ' ').replace(/nbsp;/g, ' ');

		}

		function _saveSuccess(data) {

			if (!_carPoolId) {
				objRef.successMsg("msg_div", "Sucessfully created pool!");
			} else {
				objRef.successMsg("msg_div", "Sucessfully updated pool!");
			}

			_carPoolId = data;

			$(_toDateElem).datepicker("option", "minDate",
					$(_toDateElem).datepicker("getDate"));

			_makeReadOnly();
			_enableSave();
		}

		function _saveError(data) {
			if (!_carPoolId) {
				objRef.errorMsg("msg_div", "Sorry!! couldn't create pool");
			} else {
				objRef.errorMsg("msg_div", "Sorry!! couldn't update pool");
			}
			_enableSave();
		}

		function _disableSave() {
			$("#savePoolButton").attr("disabled", "disabled");
			$("#savePoolButton").removeClass("btn-success");
			$("#savePoolButton").addClass("btn-default");
		}
		function _enableSave() {
			$("#savePoolButton").removeAttr("disabled");
			$("#savePoolButton").removeClass("btn-default");
			$("#savePoolButton").addClass("btn-success");
		}

		function _navToPlace(autocomplete, isSrc) {

			var place = autocomplete.getPlace();

			if (!place) {
				return;
			}
			
			if (!place.geometry) {
				window
						.alert("Autocomplete's returned place contains no geometry");
				return;
			}

			// If the place has a geometry, then present it
			// on a map.
			if (place.geometry.viewport) {
				_map.fitBounds(place.geometry.viewport);
			} else {
				_map.setCenter(place.geometry.location);
				_map.setZoom(17); // Why 17? Because it
				// looks good.
			}

			var placeLoc = place.geometry.location;

			if (isSrc) {
				if(_srcMarker){
					_srcMarker.setMap(null);
				}
				
				_srcMarker = _createMarker(placeLoc, "START");
			} else {
				
				if(_destMarker){
					_destMarker.setMap(null);
				}
				_destMarker = _createMarker(placeLoc, "END");
			}

			if (_srcMarker && _destMarker) {
				_clearPath();
				_calcRoute(_srcMarker.getPosition(), _destMarker.getPosition());
			}

		}

		function _createMarker(placeLoc, name) {
			var marker = new google.maps.Marker({
				position : placeLoc,
				map : _map,
				label : name,
				draggable : true
			});

			google.maps.event.addListener(marker, 'dragend',
					_markerDraggedEventHandler);

			return marker;
		}

		function _markerDraggedEventHandler(mouseMoveEvent) {

			if (_srcMarker && _destMarker) {
				_clearPath();
				_calcRoute(_srcMarker.getPosition(), _destMarker.getPosition());
			}
		}

		function _getTotalDistance(route) {
			var total = 0;
			var myroute = route;
			for (var i = 0; i < myroute.legs.length; i++) {
				total += myroute.legs[i].distance.value;
			}
			total = total / 1000.0;

			return total;
		}

	}

})();