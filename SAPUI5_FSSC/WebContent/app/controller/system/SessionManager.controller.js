sap.ui.define([ 
	'harry/app/controller/BaseController', 
	"sap/ui/Device",
	'sap/ui/model/json/JSONModel',
	"sap/m/MessageToast"
	], function(BaseController, Device, JSONModel, MessageToast) {
	"use strict";
	return BaseController.extend("harry.app.controller.system.SessionManager", {

/**
* Called when a controller is instantiated and its View controls (if available) are already created.
* Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
* @memberOf app.controller.session.SessionManager
*/
	onInit: function() {
		this.getRouter().getTarget("sessionManager").attachDisplay(this.handleAttachDisplay, this);
	},

/**
* Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
* (NOT before the first rendering! onInit() is used for that one!).
* @memberOf app.controller.session.SessionManager
*/
//	onBeforeRendering: function() {
//
//	},

/**
* Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
* This hook is the same one that SAPUI5 controls get after being rendered.
* @memberOf app.controller.session.SessionManager
*/
//	onAfterRendering: function() {
//
//	},

/**
* Called when the Controller is destroyed. Use this one to free resources and finalize activities.
* @memberOf app.controller.session.SessionManager
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
			}
		}
		this.refresh(oEvent);
	},
	
	refresh: function(oEvent) {
		var oView=this.getView();
		oView.byId("sessionsid").setBusy(true);
		this.JsonModel().loadData(this,"session/sessionList",
			function (oData, model) {
				if (oData.statusCode == "000000") {
					$.each(oData.data, function(index, ele) {
						ele.start = this.convertDate(ele.start);
						ele.lastAccess = this.convertDate(ele.lastAccess);
					}.bind(this))
					this.getView().setModel(new JSONModel(oData.data),"sessions");
				} else {
					MessageToast.show(oData.message);
				}
				oView.byId("sessionsid").setBusy(false);
			}.bind(this), 
			function (oData) {
				MessageToast.show("系统会话清单查询失败!");
				oView.byId("sessionsid").setBusy(false);
			}.bind(this)
		);
	},
	sessionOutPress: function(oEvent) {
		var oModel=this.getView().getModel("sessions");
		var oRow = oEvent.getParameter("row");
		var path=oRow.getBindingContext("sessions");
		this.JsonModel().loadData(this,"session/forceLogout?sessionId="+oModel.getProperty("id", path),
			function (oData, model) {
				if (oData.statusCode == "000000") {
					this.refresh(oEvent);
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), 
			function (oData) {
				MessageToast.show("系统会话清单查询失败!");
			}.bind(this)
		);
	},
	onDisplayBack:function(oEvent) {
		var fromTarget=this.getView().getModel("target").oData.fromTarget;
		this.getRouter().getTargets().display(fromTarget);
	}
})
});