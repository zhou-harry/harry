sap.ui.define([
		'harry/app/controller/BaseController',
		'jquery.sap.global',
		'sap/ui/core/Fragment',
		'sap/ui/core/mvc/Controller',
		'sap/ui/model/json/JSONModel',
		'sap/ui/core/CustomData',
		'sap/m/MessageToast',
		'sap/ui/Device',
		'sap/m/Dialog'
	], function (BaseController,
		jQuery,
		Fragment,
		Controller,
		JSONModel,
		CustomData,
		MessageToast,
		Device,
		Dialog) {

		"use strict";

		return BaseController.extend("harry.app.controller.Main", {

			_bExpanded: true,

			onInit: function() {
				this.getRouter().getTarget("main").attachDisplay(this.handleAttachDisplay, this);
				
				this.getView().addStyleClass(this.getOwnerComponent().getContentDensityClass());

				// if the app starts on desktop devices with small or meduim screen size, collaps the sid navigation
				if (Device.resize.width <= 1024) {
					this.onSideNavButtonPress();
				}
				Device.media.attachHandler(function (oDevice) {
					if ((oDevice.name === "Tablet" && this._bExpanded) || oDevice.name === "Desktop") {
						this.onSideNavButtonPress();
						// set the _bExpanded to false on tablet devices
						// extending and collapsing of side navigation should be done when resizing from
						// desktop to tablet screen sizes)
						this._bExpanded = (oDevice.name === "Desktop");
					}
				}.bind(this));
			},
			
			onExit : function () {
				if (this._oPopover) {
					this._oPopover.destroy();
				}
			},
			
			/**
			 * AttachDisplay
			 */
			handleAttachDisplay : function(oEvent) {
				this._oData = oEvent.getParameter("data");
				if (this._oData !== undefined) {
					if (this._oData.fromTarget == undefined) {
						this.JsonModel().loadData(this,"user/userInfo",
							function(oData, model) {
								if (oData.statusCode == "000000") {
									jQuery.sap.clearIntervalCall(this._gInterval);
									this.getView().setModel(new JSONModel(oData.data), "user");
									this._initSideContent();
									this.checkSession(this.fnSession.bind(this));
								} else {
									MessageToast.show(oData.message);
								}
							}.bind(this), 
							function(oData) {
								if ("timeout"==oData.message) {
									MessageToast.show("访问超时,请检查网络或稍后再试.");
								}else{
									this.getRouter().navTo("login");
								}
							}.bind(this)
						);
					}
					if (this._oData.fromTarget == "login") {
						this.checkSession(this.fnSession.bind(this));
						this.getView().setModel(new JSONModel(this._oData.data), "user");
						
						this._initSideContent();
					}
				}
			},
			
			/**
			 * 动态菜单加载
			 */
			_initSideContent: function () {
				this.JsonModel().loadData(this,"menu/initSide?parentKey=SIDE",
					function (oData, model) {
						if (oData.statusCode == "000000") {
							this.getView().setModel(new JSONModel(oData.data),"side");
							//默认页面
							this.getRouter().getTargets().display("home");
						} else {
							MessageToast.show(oData.message);
						}
					}.bind(this), 
					function (oData) {
						MessageToast.show("获取菜单失败!");
					}.bind(this)
				);
			},

			/**
			 * Convenience method for accessing the router.
			 * @public
			 * @param {sap.ui.base.Event} oEvent The item select event
			 */
			onItemSelect: function(oEvent) {
				var oItem = oEvent.getParameter('item');
				var sKey = oItem.getKey();
				
				var arr = sKey.split("#");
				var key = arr[0],valid = "true"===arr[1],sideExpanded = "true"===arr[2];
				// if you click on home, settings or statistics button, call the navTo function
				if (valid) {
					// if the device is phone, collaps the navigation side of the app to give more space
					if (Device.system.phone) {
						this.onSideNavButtonPress();
					}
					var oToolPage = this.byId("app");
					this._setToggleButtonTooltip(true);
					oToolPage.setSideExpanded(sideExpanded);
					
					this.getRouter().getTargets().display(key);
				} else {
					MessageToast.show(sKey);
				}
			},

			onSideNavButtonPress: function() {
				var oToolPage = this.byId("app");
				var bSideExpanded = oToolPage.getSideExpanded();
				this._setToggleButtonTooltip(bSideExpanded);
				oToolPage.setSideExpanded(!bSideExpanded);
			},

			_setToggleButtonTooltip : function(bSideExpanded) {
				var oToggleButton = this.byId('sideNavigationToggleButton');
				if (bSideExpanded) {
					oToggleButton.setTooltip('Large Size Navigation');
				} else {
					oToggleButton.setTooltip('Small Size Navigation');
				}
			},
			/**
			 * User Press
			 */
			onUserNamePress: function(oEvent) {
				var oButton = oEvent.getSource();
				// create action sheet only once
				if (!this._actionSheet) {
					this._actionSheet = sap.ui.xmlfragment(
						"harry.app.fragment.UserActionSheet",
						this
					);
					this.getView().addDependent(this._actionSheet);
				}
				this._actionSheet.openBy(oButton);
			},
			
			onLockScreen : function(oEvent){
				var uData=this.getView().getModel("user").getData();
				this.JsonModel().loadData(this,"home/logout",
					function (oData, model) {
						if (oData.statusCode == "000000") {
							this.getRouter().getTargets().display("lockScreen", {
								fromTarget : "main",
								data : uData
							});
						} else {
							MessageToast.show(oData.message+"/"+oData.data);
						}
					}.bind(this), 
					function (oData) {
						MessageToast.show("操作失败!");
					}.bind(this)
				);
			},
			userActionSelected : function(oEvent){
				MessageToast.show("Selected action is " + oEvent.getSource().getId() + ","+oEvent.getSource().getText());
			},
			/**
			 * 注销
			 */
			onLogout: function(oEvent){
				this.JsonModel().loadData(this,"home/logout",
					function (oData, model) {
						if (oData.statusCode == "000000") {
							this.getRouter().getTargets().display("login", {
								fromTarget : "main"
							});
						} else {
							MessageToast.show(oData.message+"/"+oData.data);
						}
					}.bind(this), 
					function (oData) {
						MessageToast.show("注销失败!");
					}.bind(this)
				);
			},
			/**
			 * Errors Pressed
			 */
			onMessagePopoverPress: function (oEvent) {
				if (!this.oMessagePopover) {
					this.oMessagePopover = sap.ui.xmlfragment("harry.app.fragment.MessagePopover", this);
					this.getView().addDependent(this.oMessagePopover);
				}
				this.oMessagePopover.openBy(oEvent.getSource());
			},
			onMessageItemClose: function () {
				this.oMessagePopover.destroy();
			},
			onDetailPress: function() {
				MessageToast.show("More Details was pressed");
			},
			
			/**
			 * Notifacation Pressed
			 */
			onNotificationPress: function (oEvent) {
				if (!this._oPopover) {
					this._oPopover = sap.ui.xmlfragment("harry.app.fragment.NotifiPopover", this);
					this.getView().addDependent(this._oPopover);
				}
				this._oPopover.openBy(oEvent.getSource());
			},
			
			handleshowAllButton: function (oEvent) {
				MessageToast.show('show all notification.');
			},
			
			handleCloseButton: function (oEvent) {
				this._oPopover.close();
			},
			
			onNotifiClose: function (oEvent) {
				var oItem = oEvent.getSource(),
				oList = oItem.getParent();

				oList.removeItem(oItem);
	
				MessageToast.show('Item Closed: ' + oItem.getTitle());
			},
			
			onNotifiPress: function (oEvent) {
				var oItem = oEvent.getSource();
				MessageToast.show(oItem.getTitle());
			},
			/**
			 * session 过期后执行操作
			 */
			fnSession : function(interval) {
//				jQuery.sap.clearIntervalCall(interval);
//				this.sessionOutDialog(this.getView());
				this.onLockScreen();
			},
		});
	});

