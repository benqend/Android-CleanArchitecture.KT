/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};

/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {

/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;

/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false
/******/ 		};

/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);

/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;

/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}


/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;

/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;

/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";

/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ function(module, exports) {

	;__weex_define__("@weex-component/66c38a85b14a3fa696ba4a588bd3830d", [], function(__weex_require__, __weex_exports__, __weex_module__){

	;
	    __weex_module__.exports = {
	        methods: {
	            redirect: function () {
	                __weex_require__('@weex-module/event').openURL(this.baseURL + "main-entry.js");
	            }
	        },
	        created: function () {
	            var bundleUrl = this.$getConfig().bundleUrl;
	            bundleUrl = new String(bundleUrl);
	            console.log('hit', bundleUrl);
	            var nativeBase;
	            var isAndroidAssets = bundleUrl.indexOf('file://assets/') >= 0;

	            var isiOSAssets = bundleUrl.indexOf('file:///') >= 0 && bundleUrl.indexOf('WeexDemo.app') > 0;
	            if (isAndroidAssets) {
	                nativeBase = 'file://assets/';
	            }
	            else if (isiOSAssets) {
	                // file:///var/mobile/Containers/Bundle/Application/{id}/WeexDemo.app/
	                // file:///Users/{user}/Library/Developer/CoreSimulator/Devices/{id}/data/Containers/Bundle/Application/{id}/WeexDemo.app/
	                nativeBase = bundleUrl.substring(0, bundleUrl.lastIndexOf('/') + 1);
	            }
	            else {
	                var host = 'localhost:12580';
	                var matches = /\/\/([^\/]+?)\//.exec(this.$getConfig().bundleUrl);
	                if (matches && matches.length >= 2) {
	                    host = matches[1];
	                }
	                nativeBase = 'http://' + host + '/' + this.dir + '/build/';
	            }
	            var h5Base = './index.html?page=./' + this.dir + '/build/';
	            // in Native
	            var base = nativeBase;
	            if (typeof window === 'object') {
	                // in Browser or WebView
	                base = h5Base;
	            }
	            base = 'file://assets/';
	            this.baseURL = base;
	        }
	    }


	;__weex_module__.exports.template = __weex_module__.exports.template || {}
	;Object.assign(__weex_module__.exports.template, {
	  "type": "div",
	  "children": [
	    {
	      "type": "text",
	      "style": {
	        "fontSize": 100
	      },
	      "events": {
	        "click": "redirect"
	      },
	      "attr": {
	        "value": "Hello World."
	      }
	    }
	  ]
	})
	})
	;__weex_bootstrap__("@weex-component/66c38a85b14a3fa696ba4a588bd3830d", {
	  "transformerVersion": "0.3.1"
	},undefined)

/***/ }
/******/ ]);