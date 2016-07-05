PROJECT.namespace("PROJECT.pool.poolScreens");

(function() {

	/* Class Declaration */
	PROJECT.pool.poolScreens.ChangePasswordScreen = ChangePasswordScreen;

	/* extends */
	PROJECT.pool.util.Lang.extend(ChangePasswordScreen,
			PROJECT.pool.poolScreens.AbstractScreen);

	/**
	 * @class PROJECT.pool.poolScreens.ChangePasswordScreen
	 */
	function ChangePasswordScreen() {

		ChangePasswordScreen.superclass.constructor.call(this);

		var objRef = this;

		var SegmentLoader = PROJECT.pool.util.SegmentLoader;
		var PoolConstants = PROJECT.pool.PoolConstants;
		var PoolCommands = PROJECT.pool.PoolCommands;
		var _container = null;
		var _dialog = null;
		var _validator = null;

		objRef.render = render;

		function render() {
			SegmentLoader.getInstance().getSegment("changePassword.xml", null,
					_init);
		}

		function _init(data) {

			$("body").append(data);
			var dialogId = "dialogId";

			_dialog = $("#dialogId").dialog({
				height : 550,
				width : 700,
				draggable : false,
				modal : true,
				open : function() {
					$('.ui-widget-overlay').addClass('custom-overlay');
				},
				close : function() {
					_closeDialog($(this));
				}
			});

			$("#change").click(_handleSave);

			_validator = new FormValidator('changePasswordForm', [ {
				name : 'oldPassword',
				display : 'Old Password',
				rules : 'required|min_length[6]|max_length[20]'
			}, {
				name : 'newPassword',
				display : 'New Password',
				rules : 'required|min_length[6]|max_length[20]'
			}, {
				name : 'reNewPassword',
				display : 'password confirmation',
				rules : 'required|matches[newPassword]'
			} ], function(errors, event) {

				$("small[id$='_error']").remove();

				if (errors.length > 0) {
					for (var i = 0; i < errors.length; i++) {
						var $span = $('<small/>').attr("id",
								errors[i].id + "_error").addClass(
								'help-block errorMessage').insertAfter(
								$(errors[i].element)).html(errors[i].message);
					}

				} else {
					_saveForm()
				}
			});

		}

		function _handleSave(e) {
			_validator.form.onsubmit();
		}

		function _saveForm() {
			var params = {};

			if ($("#newPassword").val() == $("#reNewPassword").val()) {

				params["oldPassword"] = $("#oldpassword").val();
				params["newPassword"] = $("#newPassword").val();

				objRef.fireCommand(PoolConstants.CHANGE_PASSWORD_COMMAND, [
						params, _saveSuccess, _saveError ]);
			}
		}

		function _saveSuccess(e) {
			_closeDialog(_dialog);
		}

		function _saveError(e) {

		}

		function _closeDialog(dialog) {
			$('.ui-widget-overlay').removeClass('custom-overlay');
			$(dialog).dialog('close');
			$(dialog).remove();
			$("#dialogId").remove();
		}
	}
})();