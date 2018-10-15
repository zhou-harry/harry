sap.ui.define([
	'harry/app/controller/BaseController',
	'sap/m/MessageToast',
	'sap/ui/model/json/JSONModel',
	'harry/app/format/formatter'
], function (BaseController, MessageToast, JSONModel, formatter) {
	"use strict";
	return BaseController.extend("harry.app.controller.settings.MasterSettings", {

		formatter: formatter,
		
		onInit: function () {
			var oViewModel = new JSONModel({
					currentUser: "Administrator",
					lastLogin: new Date(Date.now() - 86400000)
				});

			this.setModel(oViewModel, "view");
			
			this.getRouter().getTargets().display("detailSettings");
		},

		onMasterPressed: function (oEvent) {
			var oContext = oEvent.getParameter("listItem").getBindingContext("side");
			var sPath = oContext.getPath() + "/selected";
			oContext.getModel().setProperty(sPath, true);
			var sSelectedMasterElement = oContext.getProperty("title");
			var sKey = oContext.getProperty("key");
			switch (sSelectedMasterElement) {
				case "System Settings": {
//					this.getRouter().navTo(sKey);
					this.getRouter().getTargets().display("detailSettings");
					break;
				}
				default: {
					MessageToast.show(sSelectedMasterElement + " was pressed");
					break;
				}
			}
		},

		onSavePressed: function () {
			MessageToast.show("Save was pressed");
		},

		onCancelPressed: function () {
			MessageToast.show("Cancel was pressed");
		},
		onNavButtonPress: function  () {
			this.getOwnerComponent().myNavBack();
		}
	});
});