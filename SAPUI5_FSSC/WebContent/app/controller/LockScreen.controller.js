sap.ui.define([
		"harry/app/controller/BaseController", "sap/ui/model/json/JSONModel", "sap/ui/commons/MessageBox", "sap/m/MessageToast", "sap/ui/Device", 'harry/app/format/formatter'
], function(Controller, JSONModel, MessageBox, MessageToast, Device, formatter) {
	"use strict";
	var LockScreen = Controller.extend("harry.app.controller.LockScreen", {
		formatter : formatter,
		/**
		 * Called when a controller is instantiated and its View controls (if
		 * available) are already created. Can be used to modify the View before
		 * it is displayed, to bind event handlers and do other one-time
		 * initialization.
		 * 
		 * @memberOf app.controller.LockScreen
		 */
		onInit : function() {
			this.getRouter().getTarget("lockScreen").attachDisplay(this.handleAttachDisplay, this);
		},

		/**
		 * Similar to onAfterRendering, but this hook is invoked before the
		 * controller's View is re-rendered (NOT before the first rendering!
		 * onInit() is used for that one!).
		 * 
		 * @memberOf app.controller.LockScreen
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
		 * @memberOf app.controller.LockScreen
		 */
		// onAfterRendering: function() {
		//
		// },
		/**
		 * Called when the Controller is destroyed. Use this one to free
		 * resources and finalize activities.
		 * 
		 * @memberOf app.controller.LockScreen
		 */
		// onExit: function() {
		//
		// },
		/**
		 * AttachDisplay
		 */
		handleAttachDisplay : function(oEvent) {
			this._oData = oEvent.getParameter("data");
			if (this._oData !== undefined) {
				if (this._oData.fromTarget !== undefined) {
					this.getView().setModel(new JSONModel(this._oData.data), "luser");
				}
			}
		},
		onPressLogin : function(oEvent) {
			var btn = this.getView().byId("loginbtn");
			btn.setBusy(true);
			this.JsonModel().loadData(this, "home/login", function(oData, model) {
				btn.setBusy(false);
				if (oData.statusCode == "000000") {
					this.getRouter().getTargets().display("main", {
						fromTarget : "login",
						data : oData.data
					});
				} else {
					MessageToast.show(oData.message + "[" + oData.data + "]");
				}
			}.bind(this), function(oData) {
				btn.setBusy(false);
				if ("timeout" == oData.message) {
					MessageToast.show("登录超时,请检查网络或稍后再试.");
				} else {
					MessageToast.show("登录失败: [" + oData.message + "]");
				}
			}.bind(this), this.getView().getModel("luser").getJSON());
		},
		onChangeLogin : function(oEvent) {
			MessageToast.show("切换用户");
		},
	})
	return LockScreen;
});