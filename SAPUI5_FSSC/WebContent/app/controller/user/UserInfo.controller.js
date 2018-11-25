sap.ui.define([
		"harry/app/controller/BaseController", "harry/app/controller/BaseType", "sap/ui/model/json/JSONModel", "sap/ui/commons/MessageBox", "sap/m/MessageToast", "sap/ui/Device",
		'sap/ui/model/Sorter', 'sap/m/GroupHeaderListItem', 'harry/app/format/formatter'
], function(Controller, BaseType, JSONModel, MessageBox, MessageToast, Device, Sorter, GroupHeaderListItem, formatter) {
	"use strict";
	var UserInfo = Controller.extend("harry.app.controller.user.UserInfo", {
		formatter : formatter,
		/**
		 * Called when a controller is instantiated and its View controls (if
		 * available) are already created. Can be used to modify the View before
		 * it is displayed, to bind event handlers and do other one-time
		 * initialization.
		 * 
		 * @memberOf app.controller.user.UserInfo
		 */
		onInit : function() {
			this.getRouter().getTarget("userInfo").attachDisplay(this.handleAttachDisplay, this);
		},

		/**
		 * Similar to onAfterRendering, but this hook is invoked before the
		 * controller's View is re-rendered (NOT before the first rendering!
		 * onInit() is used for that one!).
		 * 
		 * @memberOf app.controller.user.UserInfo
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
		 * @memberOf app.controller.user.UserInfo
		 */
		// onAfterRendering: function() {
		//
		// },
		/**
		 * Called when the Controller is destroyed. Use this one to free
		 * resources and finalize activities.
		 * 
		 * @memberOf app.controller.user.UserInfo
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
				if (this._oData.fromTarget) {
					this._oData.edit = ("userList" == this._oData.fromTarget) ? true : false;
					this._oData.data.preview = "../../../fssc/file/preview?id=" + this._oData.data.photo;
					this.getView().setModel(new JSONModel(this._oData), "target");

					this.initRoles();
					this.initRecent();
				}
			}
		},
		initRoles : function(oEvent) {
			var userId = this.getView().getModel("target").getData().data.userId;

			this.JsonModel().loadData(this, "role/listByUser?userid=" + userId, function(oData, model) {
				if (oData.statusCode == "000000") {
					$.each(oData.data, function(index, ele) {
						ele.created = this.convertDate(ele.created);
					}.bind(this))
					this.getView().setModel(new JSONModel(oData.data), "roles");
				}
			}.bind(this), function(oData) {
				MessageToast.show("系统异常!");
			}.bind(this));
		},
		initRecent : function(oEvent) {
			var userId = this.getView().getModel("target").getData().data.userId;
			this.JsonModel().loadData(this, "behavior/recentActivities?id=" + userId, function(oData, model) {
				if (oData.statusCode == "000000") {
					$.each(oData.data, function(index, ele) {
						ele.date = this.convertDate(ele.date);
						ele.type = "Recent Activities";
					}.bind(this))
					this.getView().setModel(new JSONModel(oData.data), "recent");
					this.handleInfoPress();
				}
			}.bind(this), function(oData) {
				MessageToast.show("系统异常!");
			}.bind(this));
		},
		initAll : function(oEvent) {
			var userId = this.getView().getModel("target").getData().data.userId;
			this.JsonModel().loadData(this, "behavior/allActivities?id=" + userId, function(oData, model) {
				if (oData.statusCode == "000000") {
					$.each(oData.data, function(index, ele) {
						ele.date = this.convertDate(ele.date);
					}.bind(this))
					this.getView().setModel(new JSONModel(oData.data), "recent");
					this.handleInfoPress();
				}
			}.bind(this), function(oData) {
				MessageToast.show("系统异常!");
			}.bind(this));
		},
		onDisplayBack : function(oEvent) {
			var fromTarget = this.getView().getModel("target").oData.fromTarget;
			this.getRouter().getTargets().display(fromTarget);
		},
		onEditUser : function(oEvent) {
			this.getRouter().getTargets().display("userAdd", {
				fromTarget : "userInfo",
				data : this.getView().getModel("target").getData().data,
				roles : this.getView().getModel("roles").getData()
			});
		},

		handleInfoPress : function(oEvent) {
			var list = this.getView().byId("activities");
			var binding = list.getBinding("items");
			var aSorters = [];
			aSorters.push(new Sorter("sessionId", false, function(oContext) {
				var name = oContext.getProperty("sessionId");
				return {
					key : name,
					text : "会话Id：" + name
				};
			}));
			aSorters.push(new Sorter("date", false));
			binding.sort(aSorters);
		},
	})
	return UserInfo;
});