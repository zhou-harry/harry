sap.ui.define([
		"harry/app/controller/BaseController", "sap/ui/model/json/JSONModel", "sap/ui/commons/MessageBox", "sap/m/MessageToast", "sap/ui/Device", 'harry/app/format/formatter'
], function(Controller, JSONModel, MessageBox, MessageToast, Device, formatter) {
	"use strict";
	var Login = Controller.extend("harry.app.controller.Login", {
		formatter : formatter,

		onInit : function() {
			this.getRouter().getRoute("index").attachMatched(this.onRouteMatched, this);

			var oViewModel = new JSONModel({
				isPhone : Device.system.phone
			});
			this.setModel(oViewModel, "view");
			Device.media.attachHandler(function(oDevice) {
				this.getModel("view").setProperty("/isPhone", oDevice.name === "Phone");
			}.bind(this));

			this.initData();

			// attach handlers for validation errors
			sap.ui.getCore().attachValidationError(this.handleValidationError);
			sap.ui.getCore().attachValidationSuccess(this.handleValidationSuccess);

		},

		/**
		 * Similar to onAfterRendering, but this hook is invoked before the
		 * controller's View is re-rendered (NOT before the first rendering!
		 * onInit() is used for that one!).
		 * 
		 * @memberOf app.login.login
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
		 * @memberOf app.login.login
		 */
		// onAfterRendering: function() {
		//
		// },
		/**
		 * Called when the Controller is destroyed. Use this one to free
		 * resources and finalize activities.
		 * 
		 * @memberOf app.login.login
		 */
		// onExit: function() {
		//
		// }
		onRouteMatched : function(oEvent) {
			var oArgs, oView, oChild;
			oArgs = oEvent.getParameter("arguments");
			oView = this.getView();
			this.JsonModel().loadData(this, "session/currentCookie", function(oData, model) {
				if (oData.statusCode == "000000") {
					this.getRouter().getTargets().display("main", {
						fromTarget : "login",
						data : oData.data
					});
				} else {
					this.showMessage(oData.message + "[" + oData.data + "]");
				}
			}.bind(this), function(oData) {
				MessageToast.show(oData.message);
			}.bind(this));
		},
		handleValidationError : function(evt) {
			var control = evt.getParameter("element");
			if (control && control.setValueState) {
				control.setValueState("Error");
			}
		},
		handleValidationSuccess : function(evt) {
			var control = evt.getParameter("element");
			if (control && control.setValueState) {
				control.setValueState("None");
			}
		},
		handleSubmit : function(evt) {
			// collect input controls
			var view = this.getView();
			var inputs = [
					view.byId("nameInput"), view.byId("passwordInput")
			];
			// check that inputs are not empty
			// this does not happen during data binding as this is only
			// triggered by changes
			jQuery.each(inputs, function(i, input) {
				if (!input.getValue()) {
					input.setValueState("Error");
				}
			});
			var valid = true;
			// check states of inputs
			jQuery.each(inputs, function(i, input) {
				if ("Error" === input.getValueState()) {
					valid = false;
					return false;
				}
			});
			// submit
			if (valid) {
				var btn = evt.getSource();
				this.JsonModel().loadData(this, "home/login", function(oData, model) {
					btn.setBusy(false);
					if (oData.statusCode == "000000") {
						this.destroyMessage();
						this.getRouter().getTargets().display("main", {
							fromTarget : "login",
							data : oData.data
						});
						this.initData();
					} else {
						this.showMessage(oData.message + "[" + oData.data + "]");
					}
				}.bind(this), function(oData) {
					btn.setBusy(false);
					if ("timeout" == oData.message) {
						MessageToast.show("登录超时,请检查网络或稍后再试.");
					} else {
						MessageToast.show("登录失败: [" + oData.message + "]");
					}
				}.bind(this), view.getModel("user").getJSON());
				btn.setBusy(true);
			}
		},
		initData : function() {
			this.getView().setModel(new JSONModel({
				userId : "",
				password : ""
			}), "user");
		},
		destroyMessage : function() {
			var oMs = sap.ui.getCore().byId("msgStrip");
			if (oMs) {
				oMs.destroy();
			}
		},
		showMessage : function(msg) {
			this.destroyMessage();
			var oVC = this.getView().byId("contentBox"), oMsgStrip = new sap.m.MessageStrip("msgStrip", {
				text : msg,
				showCloseButton : true,
				showIcon : true,
				type : "Error"
			});
			var curWwwPath = window.document.location.href + "#Help";
			var oMsgLink = new sap.m.Link("msgLink", {
				text : "忘记密码",
				target : "_self",
				href : curWwwPath
			});
			oMsgStrip.setLink(oMsgLink);
			oVC.insertItem(oMsgStrip, 1);
		},
	})
	return Login;
});