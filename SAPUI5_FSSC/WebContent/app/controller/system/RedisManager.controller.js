sap.ui.define([ 
	'harry/app/controller/BaseController', 
	"sap/ui/Device",
	'sap/ui/model/json/JSONModel',
	"sap/m/MessageToast"
	], function(BaseController, Device, JSONModel, MessageToast) {
	"use strict";
	return BaseController.extend("harry.app.controller.system.RedisManager", {

/**
* Called when a controller is instantiated and its View controls (if available) are already created.
* Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
* @memberOf app.controller.system.RedisManager
*/
	onInit: function() {
		this.getRouter().getTarget("redisManager").attachDisplay(this.handleAttachDisplay, this);
	},

/**
* Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
* (NOT before the first rendering! onInit() is used for that one!).
* @memberOf app.controller.system.RedisManager
*/
//	onBeforeRendering: function() {
//
//	},

/**
* Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
* This hook is the same one that SAPUI5 controls get after being rendered.
* @memberOf app.controller.system.RedisManager
*/
//	onAfterRendering: function() {
//
//	},

/**
* Called when the Controller is destroyed. Use this one to free resources and finalize activities.
* @memberOf app.controller.system.RedisManager
*/
//	onExit: function() {
//
//	},
	/**
	 * AttachDisplay
	 */
	handleAttachDisplay : function(oEvent) {
		this._oData = oEvent.getParameter("data");
		if (this._oData !== undefined) {
			if (this._oData.fromTarget) {
				this.getView().setModel(new JSONModel(this._oData),"target");
				this.refresh();
			}
		}
	},
	onDisplayBack:function(oEvent) {
		var fromTarget=this.getView().getModel("target").oData.fromTarget;
		this.getRouter().getTargets().display(fromTarget);
	},
	refresh: function(oEvent) {
		var oView=this.getView();
		oView.byId("redisid").setBusy(true);
		this.JsonModel().loadData(this,"redis/info",
			function (oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data),"redis");
				} else {
					MessageToast.show(oData.message);
				}
				oView.byId("redisid").setBusy(false);
			}.bind(this), 
			function (oData) {
				MessageToast.show("缓存系统信息查询失败!");
				oView.byId("redisid").setBusy(false);
			}.bind(this)
		);
	},
})
});