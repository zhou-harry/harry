sap.ui.define([
		'harry/app/controller/BaseController', "sap/ui/Device", 'sap/ui/model/json/JSONModel', "sap/m/MessageToast"
], function(BaseController, Device, JSONModel, MessageToast) {
	"use strict";
	var SystemManager = BaseController.extend("harry.app.controller.system.SystemManager", {

		/**
		 * Called when a controller is instantiated and its View controls (if
		 * available) are already created. Can be used to modify the View before
		 * it is displayed, to bind event handlers and do other one-time
		 * initialization.
		 * 
		 * @memberOf app.controller.session.SystemManager
		 */
		onInit : function() {
			this.getRouter().getTarget("systemManager").attachDisplay(this.handleAttachDisplay, this);
		},

		/**
		 * Similar to onAfterRendering, but this hook is invoked before the
		 * controller's View is re-rendered (NOT before the first rendering!
		 * onInit() is used for that one!).
		 * 
		 * @memberOf app.controller.session.SystemManager
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
		 * @memberOf app.controller.session.SystemManager
		 */
		// onAfterRendering: function() {
		//
		// },
		/**
		 * Called when the Controller is destroyed. Use this one to free
		 * resources and finalize activities.
		 * 
		 * @memberOf app.controller.session.SystemManager
		 */
		// onExit: function() {
		//
		// },
		/**
		 * AttachDisplay
		 */
		handleAttachDisplay : function(oEvent) {
			this.refresh(oEvent);
		},

		refresh : function(oEvent) {
			var oView = this.getView();
			oView.byId("systemContainer").setBusy(true);
			this.JsonModel().loadData(this, "menu/monitor", function(oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data), "monitor");
				} else {
					MessageToast.show(oData.message);
				}
				oView.byId("systemContainer").setBusy(false);
			}.bind(this), function(oData) {
				MessageToast.show("系统监控清单查询失败!");
				oView.byId("systemContainer").setBusy(false);
			}.bind(this));
		},
		handleTileAdded : function(oEvent) {
			MessageToast.show("加载监测模块...");
		},
		showMonitor : function(oEvent) {
			var oModel = this.getView().getModel("monitor");
			var path = oEvent.getSource().getBindingContext("monitor");
			this.getRouter().getTargets().display(oModel.getProperty("key", path), {
				fromTarget : "systemManager"
			});
		},
	})
	return SystemManager;
});