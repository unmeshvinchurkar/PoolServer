//
// $Id: Browser.js 288583 2015-05-21 07:37:14Z advbuild $
//
// Copyright (1997-2015),Fair Isaac Corporation. All Rights Reserved.
//
PROJECT.namespace("PROJECT.pool.util");

/**
 * Class to provide browser related API, depends upon jQuery
 * <code>$.browser</code>
 * 
 * @class PROJECT.util.Browser
 */
// using jquery
PROJECT.pool.util.Browser = {

	/**
	 * true if current browser is Internet Explorer
	 * 
	 * !! - Coerces oObject to boolean. If it was falsey (e.g. 0, null, undefined, etc.), it will be false, otherwise, true.
	 * !oObject  //Inverted boolean
	 * !!oObject //Non inverted boolean so true boolean representation
	 * 
	 * @property {read boolean} isIE
	 */
	isIE : $.browser.msie || !!navigator.userAgent.match(/Trident/),
	/**
	 * true if current browser is Mozilla Firefox
	 * 
	 * @property {read boolean} isFirefox
	 */
	isFirefox : $.browser.mozilla && !($.browser.msie || !!navigator.userAgent.match(/Trident/)),
	/**
	 * true if current browser is Webkit Chrome/Safari
	 * 
	 * @property {read boolean} isWebkit
	 */
	isWebkit : $.browser.webkit,

	/**
	 * the current version of the browser in use
	 * 
	 * @property {read boolean} version
	 */
	version : $.browser.version,

	/**
	 * true if the current browser is IE 64 bit version.
	 *
	 * @property {read boolean} version
	 */
	isIE64Bit : (($.browser.msie || navigator.userAgent.match(/Trident/)) && navigator.userAgent.indexOf("Win64") != -1 && navigator.userAgent.indexOf("x64"))
};
