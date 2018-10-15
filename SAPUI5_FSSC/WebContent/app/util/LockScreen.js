sap.ui.define([ "sap/ui/core/Control", ], function(Control) {
	"use strict";
	var LockScreen = Control.extend("harry.app.util.LockScreen", {

		metadata : {
			properties : {
				name : {
					type : "string",
					defaultValue : "Harry"
				},
				img :{
					type : "string"
				},
			},
			aggregations : {
				_html : {
					type : "sap.ui.core.HTML",
					multiple : false,
					visibility : "hidden"
				},
			},
			events : {
				pressLogin : {
					enablePreventDefault : true
				},
				changeLogin : {
					enablePreventDefault : true
				},
			},
		},
		/**
		 * @memberOf harry.app.util.LockScreen
		 */
		constructor : function(sId, mSettings) {
			Control.apply(this, arguments);
		},
		_sResizeHandlerId : null,

		_onReset : function(oEvent) {
			
		},

		/**
		 * Initialize hidden html aggregation
		 */
		init : function() {
			this._sContainerId = this.getId() + "--container";
			this.setAggregation("_html", new sap.ui.core.HTML(this._sContainerId, {
				content : this._div(this._sContainerId)
			}));
		},
		
		onBeforeRendering : function() {
			sap.ui.core.ResizeHandler.deregister(this._sResizeHandlerId);
			
			this.startTime();
		},

		onAfterRendering : function() {
			this._reRender();
			this._sResizeHandlerId = sap.ui.core.ResizeHandler.register(this, jQuery.proxy(this._onReset(), this));
		},
		
		_reRender : function(){
			$("#lockscreen-name").text(this.getName());
			$("#lockscreen-image").attr("src",this.getImg());
			
			var oControl=this;
			$("#input-group-btn").click(function(e) {
				oControl.firePressLogin({
					password : "12345"
				});
			}); 
			$("#lockscreen-link").click(function(e) {
				oControl.fireChangeLogin();
			}); 
		},
		
		_div : function(id) {
			this._html = $.ajax({
				url : "html/LockScreen.html",
				async : false
			}).responseText;
			return this._html;
		},
		
		startTime: function () {
	        var today = new Date();
	        var h = today.getHours();
	        var m = today.getMinutes();
	        var s = today.getSeconds();

	        // add a zero in front of numbers<10
	        m = this.checkTime(m);
	        s = this.checkTime(s);

	        //Check for PM and AM
	        var day_or_night = (h > 11) ? "PM" : "AM";

	        //Convert to 12 hours system
	        if (h > 12)
	            h -= 12;

	        //Add time to the headline and update every 500 milliseconds
	        $('#time').html(h + ":" + m + ":" + s + " " + day_or_night);
	        setTimeout(function() {
	            this.startTime()
	        }.bind(this), 500);
	    },

	    checkTime: function (i) {
	        if (i < 10) {
	            i = "0" + i;
	        }
	        return i;
	    },


		/**
		 * Renders the root div and the HTML aggregation
		 * 
		 * @param {sap.ui.core.RenderManger}
		 *            oRM the render manager
		 * @param {sap.ui.core.Control}
		 *            oControl the control to be rendered
		 */
		renderer : function(oRM, oControl) {
			if (!oControl.getVisible()) {
				return;
			}
			oRM.renderControl(oControl.getAggregation("_html"));
		}
	});
	return LockScreen;
});
