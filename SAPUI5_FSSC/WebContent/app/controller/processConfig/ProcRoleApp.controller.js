sap.ui.define([ 
	'harry/app/controller/BaseController', 
	'sap/m/MessageToast',
	'sap/m/MessageBox', 
	'sap/ui/model/json/JSONModel',
	'harry/app/format/formatter' ], 
	function(BaseController, MessageToast,MessageBox, JSONModel, formatter) {
	"use strict";
	return BaseController.extend("harry.app.controller.processConfig.ProcRoleApp", {
		formatter: formatter,
			/**
			 * Called when a controller is instantiated and its View controls
			 * (if available) are already created. Can be used to modify the
			 * View before it is displayed, to bind event handlers and do other
			 * one-time initialization.
			 * 
			 * @memberOf app.controller.processConfig.ProcRoleApp
			 */
			 onInit: function() {
				 this.getRouter().getTarget("procRoleMaster").attachDisplay(this.handleAttachDisplayMaster, this);
				 this.getRouter().getTarget("procRoleDetail").attachDisplay(this.handleAttachDisplayDetail, this);
			 },
			/**
			 * Similar to onAfterRendering, but this hook is invoked before the
			 * controller's View is re-rendered (NOT before the first rendering!
			 * onInit() is used for that one!).
			 * 
			 * @memberOf app.controller.processConfig.ProcRoleApp
			 */
			// onBeforeRendering: function() {
			//
			// },
			/**
			 * Called when the View has been rendered (so its HTML is part of
			 * the document). Post-rendering manipulations of the HTML could be
			 * done here. This hook is the same one that SAPUI5 controls get
			 * after being rendered.
			 * 
			 * @memberOf app.controller.processConfig.ProcRoleApp
			 */
			// onAfterRendering: function() {
			//
			// },
			/**
			 * Called when the Controller is destroyed. Use this one to free
			 * resources and finalize activities.
			 * 
			 * @memberOf app.controller.processConfig.ProcRoleApp
			 */
			// onExit: function() {
			//
			// },
		 	/**
			 * AttachDisplay
			 */
			handleAttachDisplayMaster : function(oEvent) {
				var _oData = oEvent.getParameter("data");
				this._initRoles();
			},
			handleAttachDisplayDetail : function(oEvent) {
				var _oData = oEvent.getParameter("data");
				if (undefined!=_oData&&"procRoleMaster"==_oData.fromTarget) {
					this.getView().setModel(new JSONModel(_oData.data), "role");
					this._initOwners(_oData.data.roleId);
				}
			},
			/**
			 * 弹出框加载
			 */
			_getRolePopover: function () {
				var oView=this.getView();
				var oDialog = oView.byId("roleEditId");
				if (!oDialog) {
					oDialog = sap.ui.xmlfragment(oView.getId(), "harry.app.fragment.processConfig.RoleEdit",this);
				}
				oDialog.addStyleClass(this.getOwnerComponent().getPopoverDensityClass());
				return oDialog;
			},
			_getOwnerPopover: function () {
				var oView=this.getView();
				var oDialog = oView.byId("ownerEditId");
				if (!oDialog) {
					oDialog = sap.ui.xmlfragment(oView.getId(), "harry.app.fragment.processConfig.OwnerEdit",this);
				}
				oDialog.addStyleClass(this.getOwnerComponent().getPopoverDensityClass());
				return oDialog;
			},
			/**
			 * 数据初始化
			 */
			_initRoles: function(){
				this.JsonModel().loadData(this,
					"bpmConfig/findRoles",
					function(oData, model) {
						if (oData.statusCode == "000000") {
							this.getView().setModel(new JSONModel(oData.data), "roles");
						} else {
							MessageToast.show(oData.message);
						}
					}.bind(this), 
					function(oData) {
						if ("timeout"==oData.message) {
							MessageToast.show("访问超时,请检查网络或稍后再试.");
						}
					}.bind(this)
				);
			},
			_initOwners: function(key){
				this.JsonModel().loadData(this,
					"bpmConfig/findOwnersByRole?roleid="+key,
					function(oData, model) {
						if (oData.statusCode == "000000") {
							this.getView().setModel(new JSONModel(oData.data), "owners");
						} else {
							MessageToast.show(oData.message);
						}
					}.bind(this), 
					function(oData) {
						if ("timeout"==oData.message) {
							MessageToast.show("访问超时,请检查网络或稍后再试.");
						}
					}.bind(this)
				);
			},
			_initFilter: function(oDialog){
				var oDialog=oDialog;
				this.JsonModel().loadData(this,
					"bpmConfig/findFilter",
					function(oData, model) {
						if (oData.statusCode == "000000") {
							oDialog.setModel(new JSONModel(oData.data), "fSection");
						} else {
							MessageToast.show(oData.message);
						}
					}.bind(this), 
					function(oData) {
						if ("timeout"==oData.message) {
							MessageToast.show("访问超时,请检查网络或稍后再试.");
						}
					}.bind(this)
				);
			},
			_initUser: function(oDialog){
				var oDialog=oDialog;
				this.JsonModel().loadData(this,
					"user/userActive",
					function(oData, model) {
						if (oData.statusCode == "000000") {
							oDialog.setModel(new JSONModel(oData.data), "uSection");
						} else {
							MessageToast.show(oData.message);
						}
					}.bind(this), 
					function(oData) {
						if ("timeout"==oData.message) {
							MessageToast.show("访问超时,请检查网络或稍后再试.");
						}
					}.bind(this)
				);
			},
			/**
			 * 事件
			 */
			handleRoleAdd: function(oEvent){
				var oDialog=this._getRolePopover();
				oDialog.setModel(new JSONModel(), "rEdit");
				
				this.getView().addDependent(oDialog);
				oDialog.open();
			},
			handleRoleEdit: function(oEvent){
				var oList = oEvent.getSource(),
				context = oList.getBindingContext("roles");
				var oData=context.getModel().getProperty(context.getPath());
				
				var oDialog = this._getRolePopover();
				oDialog.setModel(new JSONModel(oData), "rEdit");
				
				this.getView().addDependent(oDialog);
				oDialog.open();
			},
			handleRoleDelete: function(oEvent){
				var oList = oEvent.getSource(),
				oItem = oEvent.getParameter("listItem"),
				context = oItem.getBindingContext("roles");
				
				var oData=context.getModel().getProperty(context.getPath());
				
				oList.setBusy(true);
				this.JsonModel().loadData(this,
					"bpmConfig/deleteRole",
					function (oData, model) {
						this._initRoles();
						oList.setBusy(false);
					}.bind(this), 
					function (oData) {
						MessageToast.show("删除失败!");
						oList.setBusy(false);
					}.bind(this),
					new JSONModel(oData).getJSON()
				);
			},
			handleRolePress: function(oEvent){
				var cData=this.currentData(oEvent,"roles");
				this.getRouter().getTargets().display("procRoleDetail", {
					fromTarget : "procRoleMaster",
					data : cData
				});
			},
			navBack: function(oEvent) {
				this.getRouter().getTargets().display("procRoleMaster", {
					fromTarget : "procRoleDetail"
				});
			},
			confirmRoleSave : function (oEvent) {
				var oView=this.getView();
				var oDialog = this._getRolePopover();
				var fModel=oDialog.getModel("rEdit");
				oDialog.setBusy(true);
				this.JsonModel().loadData(this,
					"bpmConfig/saveRole",
					function (oData, model) {
						oDialog.setBusy(false);
						oDialog.close();
						this._initRoles();
						this.getRouter().getTargets().display("procRoleDetail", {
							fromTarget : "procRoleMaster",
							data : oData.data
						});
					}.bind(this), 
					function (oData) {
						MessageToast.show("编辑失败!");
						oDialog.setBusy(false);
					}.bind(this),
					fModel.getJSON()
				);
			},
			cancelRoleSave: function() {
				this._getRolePopover().destroy();
			},
			handleOwnerAdd: function (oEvent) {
				var oDialog=this._getOwnerPopover();
				oDialog.setModel(new JSONModel({
					roleId:this.getView().getModel("role").getData().roleId
				}), "oEdit");
				this._initUser(oDialog);
				this._initFilter(oDialog);
				this.getView().addDependent(oDialog);
				oDialog.open();
			},
			handleOwnerEdit: function (oEvent) {
				var oList = oEvent.getSource(),
				context = oList.getBindingContext("owners");
				var oData=context.getModel().getProperty(context.getPath());
				var oDialog = this._getOwnerPopover();
				this._initUser(oDialog);
				this._initFilter(oDialog);
				oDialog.setModel(new JSONModel(oData), "oEdit");
				
				this.getView().addDependent(oDialog);
				oDialog.open();
			},
			handleOwnerDelete: function (oEvent) {
				var oList = oEvent.getSource(),
				oItem = oEvent.getParameter("listItem"),
				context = oItem.getBindingContext("owners");
				
				var oData=context.getModel().getProperty(context.getPath());
				
				oList.setBusy(true);
				this.JsonModel().loadData(this,
					"bpmConfig/deleteOwner",
					function (oData, model) {
						this._initOwners(oData.data.roleId);
						oList.setBusy(false);
					}.bind(this), 
					function (oData) {
						MessageToast.show("删除失败!");
						oList.setBusy(false);
					}.bind(this),
					new JSONModel(oData).getJSON()
				);
			},
			handleOwnerPress: function (oEvent) {
				var cData=this.currentData(oEvent,"owners");
				
				this.JsonModel().loadData(this,
					"user/info?userid="+cData.ownerId,
					function(oData, model) {
						if (oData.statusCode == "000000") {
							this.getRouter().getTargets().display("userInfo", {
								fromTarget : "procRoleDetail",
								data : oData.data
							});
						} else {
							MessageToast.show(oData.message);
						}
					}.bind(this), 
					function(oData) {
						if ("timeout"==oData.message) {
							MessageToast.show("访问超时,请检查网络或稍后再试.");
						}
					}.bind(this)
				);
			},
			confirmOwnerSave: function (oEvent) {
				var oView=this.getView();
				var oDialog = this._getOwnerPopover();
				var fModel=oDialog.getModel("oEdit");
				oDialog.setBusy(true);
				this.JsonModel().loadData(this,
					"bpmConfig/saveOwner",
					function (oData, model) {
						oDialog.setBusy(false);
						oDialog.close();
						this._initOwners(oData.data.roleId);
					}.bind(this), 
					function (oData) {
						MessageToast.show("编辑失败!");
						oDialog.setBusy(false);
					}.bind(this),
					fModel.getJSON()
				);
			},
			cancelOwnerSave: function() {
				this._getOwnerPopover().destroy();
			},
			
	})
});