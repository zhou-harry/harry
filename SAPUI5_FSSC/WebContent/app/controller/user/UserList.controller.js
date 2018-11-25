sap.ui.define([
		"harry/app/controller/BaseController", "harry/app/controller/BaseType", "sap/ui/model/json/JSONModel", "sap/ui/commons/MessageBox", "sap/m/MessageToast", "sap/ui/Device",
		'harry/app/format/formatter'
], function(Controller, BaseType, JSONModel, MessageBox, MessageToast, Device, formatter) {
	"use strict";
	var UserList = Controller.extend("harry.app.controller.user.UserList", {
		formatter : formatter,
		/**
		 * Called when a controller is instantiated and its View controls (if
		 * available) are already created. Can be used to modify the View before
		 * it is displayed, to bind event handlers and do other one-time
		 * initialization.
		 * 
		 * @memberOf app.controller.user.userList
		 */
		onInit : function() {
			this.getRouter().getTarget("userList").attachDisplay(this.handleAttachDisplay, this);
		},

		/**
		 * Similar to onAfterRendering, but this hook is invoked before the
		 * controller's View is re-rendered (NOT before the first rendering!
		 * onInit() is used for that one!).
		 * 
		 * @memberOf app.controller.user.userList
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
		 * @memberOf app.controller.user.userList
		 */
		// onAfterRendering: function() {
		//
		// },
		/**
		 * Called when the Controller is destroyed. Use this one to free
		 * resources and finalize activities.
		 * 
		 * @memberOf app.controller.user.userList
		 */
		// onExit: function() {
		//
		// },
		/**
		 * AttachDisplay
		 */
		handleAttachDisplay : function(oEvent) {
			this._oData = oEvent.getParameter("data");

			this.initList();
		},
		initList : function(oEvent) {
			this.JsonModel().loadData(this, "user/userList", function(oData, model) {
				if (oData.statusCode == "000000") {
					$.each(oData.data, function(index, ele) {
						ele.createdDate = this.convertDate(ele.createdDate);
						ele.preview = "../../../fssc/file/preview?id=" + ele.photo;
					}.bind(this))
					this.getView().setModel(new JSONModel(oData.data), "usrs");
				}
			}.bind(this), function(oData) {
				MessageToast.show("系统异常!");
			}.bind(this));
		},
		onListItemPress : function(oEvent) {
			var context = oEvent.getSource().getBindingContext("usrs");
			var path = context.getPath();
			var index = path.substr(path.lastIndexOf("/") + 1);
			var oData = context.getModel().oData[index];
			this.getRouter().getTargets().display("userInfo", {
				fromTarget : "userList",
				data : oData
			});
		}
	})
	return UserList;
});