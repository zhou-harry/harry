sap.ui.define([ 
	'harry/app/controller/BaseController', 
	"sap/ui/Device",
	"sap/m/MessageToast"
	], function(BaseController, Device,MessageToast) {
	"use strict";
	return BaseController.extend("harry.app.controller.process.AppProcInstance", {

		/**
		 * Called when a controller is instantiated and its View controls (if
		 * available) are already created. Can be used to modify the View before
		 * it is displayed, to bind event handlers and do other one-time
		 * initialization.
		 * 
		 * @memberOf app.controller.process.ProcAttachment
		 */
		onInit : function() {
			this.bus = sap.ui.getCore().getEventBus();
			this.bus.subscribe("flexible", "setDetailPage", this.setDetailPage,this);
		},

		/**
		 * Similar to onAfterRendering, but this hook is invoked before the
		 * controller's View is re-rendered (NOT before the first rendering!
		 * onInit() is used for that one!).
		 * 
		 * @memberOf app.controller.process.ProcAttachment
		 */
		// onBeforeRendering: function() {
		//
		// },
		/**
		 * Called when the View has been rendered (so its HTML is part of the
		 * document). Post-rendering manipulations of the HTML could be done
		 * here. This hook is the same one that SAPUI5 controls get after being
		 * rendered.
		 * 
		 * @memberOf app.controller.process.ProcAttachment
		 */
		// onAfterRendering: function() {
		//
		// },
		/**
		 * Called when the Controller is destroyed. Use this one to free
		 * resources and finalize activities.
		 * 
		 * @memberOf app.controller.process.ProcAttachment
		 */
		onExit : function() {
			this.bus.unsubscribe("flexible", "setDetailPage",this.setDetailPage, this);
		},
		setDetailPage: function () {
			var app = this.getView().byId("appProcInstance");
			var mode=app.getMode();
			if ("HideMode"===mode) {
				app.setMode("ShowHideMode");
			}else {
				app.setMode("HideMode");
			}
		},
	})
});