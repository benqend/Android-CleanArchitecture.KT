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

	;__weex_define__("@weex-component/f6500aaea26a476a004434f256598154", [], function(__weex_require__, __weex_exports__, __weex_module__){

	;
	    __weex_module__.exports = {
	        data: function () {return {
	            url:"http://www.android10.org/",
	            loadTip:"Load Sample Data",
	            show: true,
	            pictureUrl:"http://fernandocejas.com/wp-content/uploads/2014/08/android10_coder_logo.png"
	        }},
	        methods: {
	            clicked: function() {
	                console.log("clicked");
	            }
	        }
	    }


	;__weex_module__.exports.template = __weex_module__.exports.template || {}
	;Object.assign(__weex_module__.exports.template, {
	  "type": "div",
	  "style": {
	    "flexDirection": "column"
	  },
	  "children": [
	    {
	      "type": "text",
	      "style": {
	        "textAlign": "center"
	      },
	      "attr": {
	        "value": function () {return this.url}
	      }
	    },
	    {
	      "type": "image",
	      "classList": [
	        "thumb"
	      ],
          "events": {
            "click": "clicked"
          },
	      "attr": {
	        "src": function () {return this.pictureUrl}
	      }
	    },
	    {
	      "type": "text",
	      "style": {
	        "textAlign": "center"
	      },
	      "events": {
	        "click": "clicked"
	      },
	      "attr": {
	        "value": function () {return this.loadTip}
	      }
	    }
	  ]
	})
	;__weex_module__.exports.style = __weex_module__.exports.style || {}
	;Object.assign(__weex_module__.exports.style, {
	  "thumb": {
	    "width": 529,
	    "height": 259
	  }
	})
	})
	;__weex_bootstrap__("@weex-component/f6500aaea26a476a004434f256598154", {
	  "transformerVersion": "0.3.1"
	},undefined)

/***/ }
/******/ ]);