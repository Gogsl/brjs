if((navigator.appName == 'Microsoft Internet Explorer') && (navigator.userAgent.match(/MSIE ([0-9]+)/)[1] <= 8))
{
	// see <http://tatiyants.com/how-to-get-ie8-to-support-html5-tags-and-web-fonts/> for info
	 var html5tags = ["article", "aside", "bdi", "details", "dialog", "figcaption", "figure", "footer", "header", "main", "mark", "menuitem", "meter", "nav", "progress", "rp", "rt", "ruby",
	                  "section", "summary", "time", "wbr", "datalist", "keygen", "output", "canvas", "svg", "audio", "embed", "source", "track", "video"];
	 for(var i = 0; i < html5tags.length; i++)
	 {	 	
	 	document.createElement(html5tags[i]);
	 }

	// see <http://www.adequatelygood.com/2011/4/Replacing-setTimeout-Globally> for info
	var pProperties = ["alert", "setTimeout", "clearTimeout", "setInterval", "clearInterval"];
	for(var i = 0, l = pProperties.length; i < l; ++i)
	{
		var sProperty = pProperties[i];
		var fNativePropertyAccessor = window[sProperty];
		eval("var " + sProperty + ";");
		window[sProperty] = fNativePropertyAccessor;
	}
}

if (!Function.prototype['bind']) {
	// 24/01/2011 - AmirH - changed for better performance when there are no arguments to be curried.
	// Function.prototype.bind is a standard part of ECMAScript 5th Edition (December 2009, http://www.ecma-international.org/publications/files/ECMA-ST/ECMA-262.pdf)
	// In case the browser doesn't implement it natively, provide a JavaScript implementation. This implementation is based on the one in prototype.js
	Function.prototype['bind'] = function (object) {
		var originalFunction = this;
		
		if (arguments.length === 1) {
			return function () {
					return originalFunction.apply(object, arguments);
				};
		} else {
			var args = Array.prototype.slice.call(arguments, 1);
			return function () {
				return originalFunction.apply(object, args.concat(Array.prototype.slice.call(arguments)));
			};
		}
	};
}

if (Function.prototype.bind && typeof console == "object" && typeof console.log == "object") {
	var logFns = ["log", "info", "warn", "error", "assert", "dir", "clear", "profile", "profileEnd"];
	for(var i = 0, l = logFns.length; i < l; ++i)
	{
		var method = logFns[i];
		console[method] = Function.prototype.call.bind(console[method], console);
	}
}

if(!Number.isNaN) {
	Number.isNaN = Number.isNaN || function(value) {
	    return typeof value === "number" && value !== value;
	}
}
