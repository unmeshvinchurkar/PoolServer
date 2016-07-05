PROJECT.namespace("PROJECT.pool.util");
/**
 * 
 * Provides the language utilites and extensions used by the library
 * 
 * @class PROJECT.pool.util.Lang
 */
(function() {
	
	PROJECT.pool.util.Lang = {};
	 var OP = Object.prototype;

	var HTML_CHARS = {
		'&' : '&amp;',
		'<' : '&lt;',
		'>' : '&gt;',
		'"' : '&quot;',
		"'" : '&#x27;',
		'/' : '&#x2F;',
		'`' : '&#x60;'
	};

	/**
	 * <p>
	 * Returns a copy of the specified string with special HTML characters
	 * escaped. The following characters will be converted to their
	 * corresponding character entities:
	 * <code>&amp; &lt; &gt; &quot; &#x27; &#x2F; &#x60;</code>
	 * </p>
	 * 
	 * <p>
	 * This implementation is based on the <a
	 * href="http://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet">OWASP
	 * HTML escaping recommendations</a>. In addition to the characters in the
	 * OWASP recommendation, we also escape the <code>&#x60;</code> character,
	 * since IE interprets it as an attribute delimiter when used in innerHTML.
	 * </p>
	 * 
	 * @method escapeHTML
	 * @param {String}
	 *            html String to escape.
	 * @return {String} Escaped string.
	 * @static
	 */
	PROJECT.pool.util.Lang.escapeHTML = escapeHTML;
	function escapeHTML(html) {
		return html.replace(/[&<>"'\/`]/g, function(match) {
			return HTML_CHARS[match];
		});
	}

	/**
	 * Utility to set up the prototype, constructor and superclass properties to
	 * support an inheritance strategy that can chain constructors and methods.
	 * Static members will not be inherited.
	 * 
	 * @method extend
	 * @static
	 * @param {Function}
	 *            subc the object to modify
	 * @param {Function}
	 *            superc the object to inherit
	 * @param {Object}
	 *            overrides additional properties/methods to add to the subclass
	 *            prototype. These will override the matching items obtained
	 *            from the superclass if present.
	 */
	PROJECT.pool.util.Lang.extend = extend;
	function extend(subc, superc, overrides) {
		if (!superc || !subc) {
			throw new Error("extend failed, please check that "
					+ "all dependencies are included.");
		}
		var F = function() {
		}, i;
		F.prototype = superc.prototype;
		subc.prototype = new F();
		subc.prototype.constructor = subc;
		subc.superclass = superc.prototype;
		if (superc.prototype.constructor == OP.constructor) {
			superc.prototype.constructor = superc;
		}

		if (overrides) {
			for (i in overrides) {
				if (L.hasOwnProperty(overrides, i)) {
					subc.prototype[i] = overrides[i];
				}
			}
		}
	}

	/**
	 * Applies all properties in the supplier to the receiver if the
	 * receiver does not have these properties yet.  Optionally, one or
	 * more methods/properties can be specified (as additional
	 * parameters).  This option will overwrite the property if receiver
	 * has it already.  If true is passed as the third parameter, all
	 * properties will be applied and _will_ overwrite properties in
	 * the receiver.
	 *
	 * @method augmentObject
	 * @static
	 * @param {Function} r  the object to receive the augmentation
	 * @param {Function} s  the object that supplies the properties to augment
	 * @param {String*|boolean}  arguments zero or more properties methods
	 *        to augment the receiver with.  If none specified, everything
	 *        in the supplier will be used unless it would
	 *        overwrite an existing property in the receiver. If true
	 *        is specified as the third parameter, all properties will
	 *        be applied and will overwrite an existing property in
	 *        the receiver
	 */
	PROJECT.pool.util.Lang.augmentObject = augmentObject;
	function augmentObject(r, s) {
		if (!s || !r) {
			throw new Error("Absorb failed, verify dependencies.");
		}
		var a = arguments, i, p, overrideList = a[2];
		if (overrideList && overrideList !== true) { // only absorb the specified properties
			for (i = 2; i < a.length; i = i + 1) {
				r[a[i]] = s[a[i]];
			}
		} else { // take everything, overwriting only if the third parameter is true
			for (p in s) {
				if (overrideList || !(p in r)) {
					r[p] = s[p];
				}
			}

			//  L._IEEnumFix(r, s);
		}

		return r;
	}

	/**
	 * Same as augmentObject, except it only applies prototype properties
	 * @see YaugmentObject
	 * @method augmentProto
	 * @static
	 * @param {Function} r  the object to receive the augmentation
	 * @param {Function} s  the object that supplies the properties to augment
	 * @param {String*|boolean}  arguments zero or more properties methods
	 *        to augment the receiver with.  If none specified, everything
	 *        in the supplier will be used unless it would overwrite an existing
	 *        property in the receiver.  if true is specified as the third
	 *        parameter, all properties will be applied and will overwrite an
	 *        existing property in the receiver
	 */
	PROJECT.pool.util.Lang.augmentProto = augmentProto;
	function augmentProto(r, s) {
		if (!s || !r) {
			throw new Error("Augment failed, verify dependencies.");
		}
		//var a=[].concat(arguments);
		var a = [ r.prototype, s.prototype ], i;
		for (i = 2; i < arguments.length; i = i + 1) {
			a.push(arguments[i]);
		}
		PROJECT.pool.util.Lang.augmentObject.apply(this, a);

		return r;
	}

	/**
	 * returns a new object based upon the supplied object.
	 * 
	 * @function {public Object} object
	 * @param {Object}
	 *            obj
	 */
	PROJECT.pool.util.Lang.object = object;
	function object(o) {
		function F() {
		}
		F.prototype = o;
		return new F();
	}

	/**
	 * this function allows check whether an item exist in the arrays or not
	 * 
	 * @function {public Boolean} arrayContains
	 * @param {Array}
	 *            arr - array to use for checking
	 * @param {Object}
	 *            obj - the object to check presence for
	 */
	PROJECT.pool.util.Lang.arrayContains = function(arr, obj) {
		var i = arr.length;
		while (i--) {
			if (arr[i] === obj) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This function returns the index of first occurrence of an object in a
	 * javascript array. Returns -1 if the object is not found in the Array.
	 * 
	 * @function {public Number} indexOf
	 * @param {Array}
	 *            arr - array
	 * @param {Object}
	 *            obj - Object
	 */
	PROJECT.pool.util.Lang.indexOf = function(arr, obj) {
		var index = -1;
		if (arr) {
			for (var i = 0; i < arr.length; i++) {
				if (arr[i] === obj) {
					index = i;
					break;
				}
			}
		}
		return index;
	}

	/**
	 * This function removes an item from an array.
	 * 
	 * @function {public void} removeFromArray
	 * @param {Array}
	 *            arr - array
	 * @param {Object}
	 *            obj - Object to be removed from array
	 */
	PROJECT.pool.util.Lang.removeFromArray = function(arr, obj) {
		if (arr) {
			var index = ROJECT.pool.util.Lang.indexOf(arr, obj);
			if (index >= 0) {
				arr.splice(index, 1);
			}
		}
	};

	/**
	 * Return the boolean value for actual string value
	 * 
	 * @function {public Boolean} assertBoolean
	 * @param {String}
	 *            actualValue - string to be checked for boolean value
	 */
	PROJECT.pool.util.Lang.assertBoolean = function(actualValue) {
		return (actualValue != undefined && (actualValue === 'true' || actualValue === true));
	};

	/**
	 * Normalizes the data so that it can be used in XML
	 * 
	 * @function {public String} normalizeDataForXML
	 * @param {String}
	 *            data - data to be normalized
	 * @returns Normalized data
	 */
	PROJECT.pool.util.Lang.normalizeDataForXML = function(data) {
		if (data) {
			data = data.replace(/&/g, '&amp;');
			data = data.replace(/</g, '&lt;');
			data = data.replace(/>/g, '&gt;');
			// replacing &apos; as it is not working with IE 7
			data = data.replace(/'/g, "&#39;");
			data = data.replace(/"/g, '&quot;');
		}
		return data;
	};

	/**
	 * Escape the string so it can rendered properly
	 * 
	 * @function {public String} escapeString
	 * @param {String}
	 *            str - String to be escaped
	 * @returns string
	 */
	PROJECT.pool.util.Lang.escapeString = function(str) {
		if (str === null || str === 'undefined')
			return null;
		var retStr = PROJECT.pool.util.Lang.normalizeDataForXML(str);
		retStr = escape(retStr);
		return retStr;
	};

	/**
	 * Unescape the string so it can rendered properly
	 * 
	 * 
	 * @function {public String} unEscapeString
	 * @param {String}
	 *            str - String to be unescaped
	 * @returns string
	 */
	PROJECT.pool.util.Lang.unEscapeString = function(str) {
		if (str === null || str === 'undefined')
			return null;
		return unescape(str);
	};

	/**
	 * trims string
	 * 
	 * @function {public String} trimString
	 * @param {String}
	 *            s - String to be trimmed
	 * @returns trimmed string
	 */
	PROJECT.pool.util.Lang.trimString = function(s) {
		try {
			return s.replace(/^\s+|\s+$/g, "");
		} catch (e) {
			return s;
		}
	};

	/**
	 * Deep Clone an Object
	 * 
	 * @function {public Object} cloneObject
	 * @param {Object}
	 *            obj - Object to be cloned
	 * @returns object
	 */
	PROJECT.pool.util.Lang.cloneObject = function(obj) {
		var cloned = (obj instanceof Array) ? [] : {};
		/* Iterate through all properties */
		for (prop in obj) {
			if (typeof (obj[prop]) === 'object' && obj[prop] !== null) { /* Recurse */
				cloned[prop] = arguments.callee(obj[prop]);
			} else {
				cloned[prop] = obj[prop];
			}
		}
		return cloned;
	};

})();