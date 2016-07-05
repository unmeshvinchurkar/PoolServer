PROJECT.namespace("PROJECT.pool.poolScreens");

(function() {

	/* Class Declaration */
	PROJECT.pool.poolScreens.ManageVehiclesScreen = ManageVehiclesScreen;

	/* extends */
	PROJECT.pool.util.Lang.extend(ManageVehiclesScreen,
			PROJECT.pool.poolScreens.AbstractScreen);

	/**
	 * @class PROJECT.pool.map.ManageVehiclesScreen
	 */
	function ManageVehiclesScreen(containerElemId, readOnly) {

		ManageVehiclesScreen.superclass.constructor.call(this);

		var objRef = this;

		var SegmentLoader = PROJECT.pool.util.SegmentLoader;
		var PoolConstants = PROJECT.pool.PoolConstants;
		var PoolCommands = PROJECT.pool.PoolCommands;

		var _containerElemId = containerElemId

		var _container = null;
		var _validator = null;
		var _doesVehicleExist = false;
		var _noEdit = readOnly ? true : false;
		var _editedFields = null;

		/* Public Properties */
		objRef.render = render;

		function render() {
			SegmentLoader.getInstance().getSegment("manageVehiclesSeg.xml",
					null, _init);
		}

		function _init(data) {
			_container = $('#' + _containerElemId);
			_container.html(data);
			_loadVehicleData();

			if (_noEdit) {
				$("#editDetails").remove();
			} else {
				$("#editDetails").click(_handleVehicleDetails);

			}
		}

		function _loadVehicleData() {

			objRef.fetch("getVehicle", fillData);

			function fillData(data) {
				if (data) {
					_editedFields = $("#manufacturer, #modelname, #fueltype, #color, #registrationNumber, #drivingLicense");

					_editedFields.removeAttr("readonly");
					_doesVehicleExist = true;
					$("#manufacturer").val(data.manufacturer);
					$("#modelname").val(data.model);
					$("#fueltype").val(data.fuelType);
					$("#color").val(data.color);
					$("#registrationNumber").val(data.registrationNo);
					$("#drivingLicense").val(data.drivingLicense);
					_editedFields.attr("readonly", true);
				} else {
					_handleVehicleDetails();
				}
			}
		}

		function _handleVehicleDetails() {

			var readonly = $("#manufacturer").attr("readonly");
			 _editedFields = $("#manufacturer, #modelname, #fueltype, #color, #registrationNumber, #drivingLicense");
			$("small[id$='_error']").remove();

			if (_noEdit || readonly == "readonly") {

				_editedFields.removeAttr("readonly");

				_validator = new FormValidator('vehicleForm', [ {
					name : 'manufacturer',
					display : 'Manufacturer',
					rules : 'required|max_length[20]|alpha'
				}, {
					name : 'modelname',
					display : 'Model Name',
					rules : 'required|max_length[20]'
				}, {
					name : 'fueltype',
					display : 'Fuel Type',
					rules : 'required|alpha|max_length[10]'
				}, {
					name : 'color',
					rules : 'required|alpha|max_length[20]'
				}, {
					name : 'registrationNumber',
					rules : 'required|alpha_numeric|max_length[10]'
				}, {
					name : 'drivingLicense',
					rules : 'required|alpha_numeric|max_length[30]'
				} ], function(errors, event) {

					$("small[id$='_error']").remove();

					if (errors.length > 0) {
						for (var i = 0; i < errors.length; i++) {
							var $span = $('<small/>').attr("id",
									errors[i].id + "_error").addClass(
									'help-block errorMessage').insertAfter(
									$(errors[i].element)).html(
									errors[i].message);
						}

					} else {
						_saveVehicle();
					}
				});

				$("#editDetails").html("Cancel Edit");

				$("#vehicleForm").append(
						'<input type="button" id="save" class="btn btn-custom btn-lg btn-block"'
								+ 'value="Save" />');

				$("#save").click(function(e) {
					_validator.form.onsubmit();
				});

			} else {
				_editedFields.attr("readonly", true);
				$("#editDetails").html("Edit Details");
				$("#save").remove();
			}
		}

		function _saveVehicle() {
			var params = {};
			params["manufacturer"] = $("#manufacturer").val();
			params["model"] = $("#modelname").val();
			params["fuelType"] = $("#fueltype").val();
			params["color"] = $("#color").val();
			params["registrationNumber"] = $("#registrationNumber").val();
			params["drivingLicense"] = $("#drivingLicense").val();

			objRef.fireCommand("addVehicle",
					[ params, _saveSuccess, _saveError ]);

		}

		function _saveSuccess(data) {
			objRef.successMsg("msg_div",
					"Successfully saved vehicle details !!", 3000);
			_editedFields.attr("readonly", true);
			$("#editDetails").html("Edit Details");
		}

		function _saveError(data) {
			objRef.errorMsg("msg_div", "Failed to save vehicle details !!");
		}
	}
})();