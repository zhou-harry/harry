sap.ui.define([
		'harry/app/controller/BaseController',
		'sap/ui/model/json/JSONModel',
		'sap/ui/Device',
		'sap/m/MessageToast',
		'harry/app/format/formatter'
], function (BaseController, JSONModel, Device,MessageToast, formatter) {
		"use strict";
		return BaseController.extend("harry.app.controller.fssc.F001", {
			formatter: formatter,
/**
* Called when a controller is instantiated and its View controls (if available) are already created.
* Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
* @memberOf app.controller.fssc.F001
*/
	onInit: function() {
		var xml=this.getView().mPreprocessors.xml;
		
		this.bus = sap.ui.getCore().getEventBus();
		this.bus.subscribe("F001", "commitForm", this.commitForm,this);
	},

/**
* Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
* (NOT before the first rendering! onInit() is used for that one!).
* @memberOf app.controller.fssc.F001
*/
//	onBeforeRendering: function() {
//
//	},

/**
* Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
* This hook is the same one that SAPUI5 controls get after being rendered.
* @memberOf app.controller.fssc.F001
*/
//	onAfterRendering: function() {
//
//	},

/**
* Called when the Controller is destroyed. Use this one to free resources and finalize activities.
* @memberOf app.controller.fssc.F001
*/
	onExit: function() {
		MessageToast.show("F001退出.");
	},
	
	commitForm: function (channelId,eventId,oData) {
		MessageToast.show("表单事件触发..."+oData.instanceId);
	},
})
});