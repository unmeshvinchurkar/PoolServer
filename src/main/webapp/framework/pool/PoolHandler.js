PROJECT.namespace("PROJECT.pool");

(function() {
	/* class declaration */
	PROJECT.pool.PoolHandler = PoolHandler;

	/* extends */

	/**
	 * @class PROJECT.pool.PoolHandler
	 */
	function PoolHandler() {

		var objRef = this;

		/* Aliases */
		var ErrorUtil = FICO.base.error.ErrorUtil.getInstance();

		/* Private Properties */
		var _targetComponent;

		/* Public Properties */
		objRef.onEvent = onEvent

		/**
		 * Overrides IRmaEventHandler method onEvent
		 */
		function onEvent(eventObj) {
			var rmaAction = eventObj.getData();
			var data = rmaAction.serializeToString();

			var requestHandler = new XMLRequestHandler(RMAConstants.AJAX_URL,
					_handleResponse);
			requestHandler.post(RMAConstants.REQUEST_MIME_TYPE, data);
		}

		/**
		 * Call back method to relay the server call response to the View.
		 */
		function _handleResponse(url, requestObj) {
			var rmaAction = ActionUtil.getActionResponse(requestObj);
			// to indicate that the server has finished processing the request
			_editorRequestHandler = null;

			if (typeof (rmaAction) === 'undefined' || rmaAction === null) {
				ErrorUtil.showApplicationError(LocalizedStringsProvider
						.getInstance().getString("SERVER_RESPONSE_EMPTY"));
				return;
			}

			var launchMode = rmaAction
					.getRequestParameter(NdInstanceEditCommandConstants.LAUNCH_MODE_PARAM);

			if (launchMode === NdInstanceEditCommandConstants.INSTANCE_EDIT_LAUNCH_SPLIT_VIEW_MODE) {
				var rmaView = RmaViewManager
						.getView(InstanceEditorConstants.LAYOUT_SECTION_INSTANCE_EDIT_MAIN_SPLIT_VIEW);
				if (rmaView.getViewId() === InstanceEditorConstants.EDITOR_INIT_VIEWID
						|| rmaView.getViewId() == null) {
					rmaView.setViewId(rmaAction.getViewId());
				}
			}

			var dataAvailableEvent = new CustomEvent(
					InstanceEditorFrameworkConstants.EVENT_INSTANCE_EDITOR_DATA_AVAILABLE,
					null);
			dataAvailableEvent.setData(rmaAction);
			RmaEventManagerFactory.getEventManager().fireEvent(
					dataAvailableEvent);
		}
	}
})();