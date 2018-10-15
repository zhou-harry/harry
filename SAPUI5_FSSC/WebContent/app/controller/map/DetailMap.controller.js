sap.ui.define([ 'harry/app/controller/BaseController', 'jquery.sap.global',
		'sap/ui/model/json/JSONModel' ], function(BaseController, jQuery,
		JSONModel) {
	"use strict";
	return BaseController.extend("harry.app.controller.map.DetailMap", {

	/**
	 * Called when a controller is instantiated and its View controls (if
	 * available) are already created. Can be used to modify the View before it
	 * is displayed, to bind event handlers and do other one-time
	 * initialization.
	 * 
	 * @memberOf app.view.map.MasterMap
	 */
	 onInit: function() {
		var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
		var oTarget = oRouter.getTarget("detailMap");
		oTarget.attachDisplay(this.handleAttachDisplay, this);
	 },
	/**
	 * Similar to onAfterRendering, but this hook is invoked before the
	 * controller's View is re-rendered (NOT before the first rendering!
	 * onInit() is used for that one!).
	 * 
	 * @memberOf app.view.map.MasterMap
	 */
	// onBeforeRendering: function() {
	//
	// },
	/**
	 * Called when the View has been rendered (so its HTML is part of the
	 * document). Post-rendering manipulations of the HTML could be done here.
	 * This hook is the same one that SAPUI5 controls get after being rendered.
	 * 
	 * @memberOf app.view.map.MasterMap
	 */
	// onAfterRendering: function() {
	//
	// },
	/**
	 * Called when the Controller is destroyed. Use this one to free resources
	 * and finalize activities.
	 * 
	 * @memberOf app.view.map.MasterMap
	 */
	// onExit: function() {
	//
	// }
	 handleAttachDisplay : function(oEvent) {
		this._oData = oEvent.getParameter("data"); // store the data
		if (this._oData.data !== undefined) {
			
			this.getView().setModel(new JSONModel(this._oData.data), "map");
			
			var bar = this.getView().byId("mapid");
			bar._onResetMap();
		}
	},
	navPress : function(oEvent) {
		var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
		oRouter.getTargets().display("masterMap", {
			fromTarget : "detailMap"
		});
	},
})
});