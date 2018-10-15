sap.ui.controller("harry.app.controller.fssc.F004", {

/**
* Called when a controller is instantiated and its View controls (if available) are already created.
* Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
* @memberOf app.controller.fssc.F004
*/
	onInit: function() {
		this.bus = sap.ui.getCore().getEventBus();
		this.bus.subscribe("F004", "commitForm", this.commitForm,this);
	},

/**
* Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
* (NOT before the first rendering! onInit() is used for that one!).
* @memberOf app.controller.fssc.F004
*/
//	onBeforeRendering: function() {
//
//	},

/**
* Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
* This hook is the same one that SAPUI5 controls get after being rendered.
* @memberOf app.controller.fssc.F004
*/
//	onAfterRendering: function() {
//
//	},

/**
* Called when the Controller is destroyed. Use this one to free resources and finalize activities.
* @memberOf app.controller.fssc.F004
*/
//	onExit: function() {
//
//	},

	commitForm: function (channelId,eventId,oData) {
		MessageToast.show("表单事件触发..."+oData.instanceId);
	},
});