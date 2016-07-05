PROJECT.namespace("PROJECT.pool.util");

(function() {

	/* Class Declaration */
	PROJECT.pool.util.Hashtable = Hashtable;

	/**
	 * This class implements a hashtable, which maps keys to values. Any non-null object can be used as a key or as a value.
	 *  
	 * @class PROJECT.pool.util.Hashtable
	 */
	function Hashtable() {
		var objRef = this;

		/* Public Properties */
		objRef.clear = clear;
		objRef.containsKey = containsKey;
		objRef.containsValue = containsValue;
		objRef.get = get;
		objRef.isEmpty = isEmpty;
		objRef.keys = keys;
		objRef.put = put;
		objRef.remove = remove;
		objRef.size = size;
		objRef.toString = toString;
		objRef.values = values;
		objRef.hashtable = {};

		/* =======Private methods for internal use only======== */

		/**
		 * Clears this hashtable so that it contains no keys.
		 * 
		 * @function {public void} clear
		 */
		function clear() {
			objRef.hashtable = {};
		}

		/**
		 * Tests if the specified object is a key in this hashtable.
		 * 
		 * @function {public Boolean} containsKey
		 * @param {Object} key
		 */
		function containsKey(key) {
			var exists = false;
			for ( var i in objRef.hashtable) {
				if (i == key) {
					exists = true;
					break;
				}
			}
			return exists;
		}

		/**
		 * Returns true if this Hashtable maps one or more keys to this value.
		 * 
		 * @function {public Boolean} containsValue
		 * @param {Object} value
		 */
		function containsValue(value) {
			var contains = false;
			if (value != null) {
				for ( var i in objRef.hashtable) {
					if (objRef.hashtable[i] == value) {
						contains = true;
						break;
					}
				}
			}
			return contains;
		}

		/**
		 * Returns the value to which the specified key is mapped in this hashtable.
		 * 
		 * @function {public Object} get
		 * @param {Object} key
		 */
		function get(key) {
			return objRef.hashtable[key];
		}

		/**
		 * Tests if this hashtable maps no keys to values.
		 * 
		 * @returns
		 */
		function isEmpty() {
			return (parseInt(objRef.size()) == 0) ? true : false;
		}

		/**
		 * Returns an array of the keys in this hashtable.
		 * 
		 * @function {public Array} keys
		 */
		function keys() {
			var keys = [];
			for ( var i in objRef.hashtable) {
				keys.push(i);
			}
			return keys;
		}

		/**
		 * Maps the specified key to the specified value in this hashtable.
		 * 
		 * @function {public void} put
		 * @param {Object} key
		 * @param {Object} value
		 */
		function put(key, value) {
			if (key == null || value == null) {
				throw "NullPointerException {" + key + "},{" + value + "}";
			} else {
				objRef.hashtable[key] = value;
			}
		}

		/**
		 * Removes the key (and its corresponding value) from this hashtable, and return
		 * the value.
		 * 
		 * @function {public Object} remove
		 * @param {Object} key
		 * 
		 */
		function remove(key) {
			var rtn = objRef.hashtable[key];
			delete objRef.hashtable[key];
			return rtn;
		}

		/**
		 * Returns the number of keys in this hashtable.
		 * 
		 * @function {public Number} size
		 * @returns Returns the number of keys in this hashtable.
		 */
		function size() {
			var size = 0;
			for ( var i in objRef.hashtable) {
				size++;
			}
			return size;
		}

		/**
		 * Returns a string representation of this Hashtable object in the form of a set
		 * of entries, enclosed in braces and separated by the ASCII characters ", "
		 * (comma and space).
		 * 
		 * @function {public String} toString
		 * @returns String representation of the hashtable
		 */
		function toString() {
			var result = "";
			for ( var i in objRef.hashtable) {
				result += "{" + i + "},{" + objRef.hashtable[i] + "}\n";
			}
			return result;
		}

		/**
		 * Returns a Collection view of the values contained in this Hashtable.
		 * 
		 * @function {public Array} values
		 * @returns Array of values
		 */
		function values() {
			var values = [];
			for ( var i in objRef.hashtable) {
				values.push(objRef.hashtable[i]);
			}
			return values;
		}
	}
})();