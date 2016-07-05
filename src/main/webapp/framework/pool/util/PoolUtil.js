PROJECT.namespace("PROJECT.pool.util");

(function() {

	/* Singleton Class Declaration */
	PROJECT.pool.util.PoolUtil = PoolUtil;

	/**
	 * Utility class that can be used to load the view segment files
	 * 
	 * @class PROJECT.util.SegmentLoader
	 */
	function PoolUtil() {
		if (PoolUtil.caller != PoolUtil.getInstance) {
			throw new Error("This is a singleton class.");
		}

		var objRef = this;

		/* public properties */
		objRef.convertSecondsToTime = convertSecondsToTime;
		objRef.convertDateToString = convertDateToString;

		function convertDateToString(date, format) {
			
			format = format ? format : 'dd-mm-yy';
			return $.datepicker.formatDate(format, date);
		}

		function convertSecondsToTime(timeInSeconds) {

			var unit = "AM";
			var hrs = parseInt(timeInSeconds / (3600));

			if (hrs >= 12) {
				unit = "PM";

				if (hrs >= 13) {
					hrs = hrs - 12;
				}
			}

			var remSeconds = parseInt(timeInSeconds % (3600));
			var secs = parseInt(remSeconds / 60);
			secs = secs > 9 ? secs : secs + "0";
			return hrs + ":" + secs + unit;
		}
	}

	/**
	 * returns the singleton instance of the function
	 * 
	 * @function {public static} getInstance
	 */
	PoolUtil.getInstance = function() {
		if (!PoolUtil.instance) {
			PoolUtil.instance = new PoolUtil();
		}
		return PoolUtil.instance;
	}
}());