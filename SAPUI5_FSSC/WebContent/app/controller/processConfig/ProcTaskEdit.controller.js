sap.ui.define([
		'harry/app/controller/BaseController', 'sap/m/MessageToast', 'sap/m/MessageBox', 'sap/ui/model/json/JSONModel', 'harry/app/format/formatter'
], function(BaseController, MessageToast, MessageBox, JSONModel, formatter) {
	"use strict";
	var ProcTaskEdit = BaseController.extend("harry.app.controller.processConfig.ProcTaskEdit", {

		/**
		 * Called when a controller is instantiated and its View controls (if
		 * available) are already created. Can be used to modify the View before
		 * it is displayed, to bind event handlers and do other one-time
		 * initialization.
		 * 
		 * @memberOf app.controller.processConfig.ProcTaskEdit
		 */
		onInit : function() {
			this.getRouter().getTarget("procTaskEdit").attachDisplay(this.handleAttachDisplay, this);
		},

		/**
		 * Similar to onAfterRendering, but this hook is invoked before the
		 * controller's View is re-rendered (NOT before the first rendering!
		 * onInit() is used for that one!).
		 * 
		 * @memberOf app.controller.processConfig.ProcTaskEdit
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
		 * @memberOf app.controller.processConfig.ProcTaskEdit
		 */
		// onAfterRendering: function() {
		//
		// },
		/**
		 * Called when the Controller is destroyed. Use this one to free
		 * resources and finalize activities.
		 * 
		 * @memberOf app.controller.processConfig.ProcTaskEdit
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
				if ("procDefinitionDetail" === this._oData.fromTarget) {
					this.getView().setModel(new JSONModel(this._oData.data), "task");

					this._initRoles(this._oData.data.taskId);
				}
			}
			this._initFilter();
		},
		/**
		 * 弹出框加载
		 */
		_getTaskRolePopover : function() {
			var oView = this.getView();
			var oDialog = oView.byId("tRoleEditId");
			if (!oDialog) {
				oDialog = sap.ui.xmlfragment(oView.getId(), "harry.app.fragment.processConfig.TaskRoleEdit", this);
			}
			oDialog.addStyleClass(this.getOwnerComponent().getPopoverDensityClass());
			return oDialog;
		},
		_onSuccessDialog : function(oData) {
			MessageBox.success("是否返回流程定义界面?", {
				icon : sap.m.MessageBox.Icon.SUCCESS,
				title : "保存成功",
				actions : [
						sap.m.MessageBox.Action.YES, sap.m.MessageBox.Action.NO
				],
				styleClass : this.getOwnerComponent().getPopoverDensityClass(),
				onClose : function(sAction) {
					if (sap.m.MessageBox.Action.YES == sAction) {
						this.getRouter().getTargets().display("procDefinitionDetail", {
							fromTarget : "procTaskEdit",
							data : oData
						});
					} else if (sap.m.MessageBox.Action.NO == sAction) {
						var tModel = this.getView().getModel("task");
						var iEntry = tModel.getData();
						if (undefined === iEntry.taskId) {
							iEntry.taskId = oData.taskId;
							tModel.refresh();
						}
					}
				}.bind(this)
			});
		},
		/**
		 * 初始化数据
		 */
		_initFilter : function() {
			this.JsonModel().loadData(this, "bpmConfig/findFilter", function(oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data), "fSection");
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), function(oData) {
				if ("timeout" == oData.message) {
					MessageToast.show("访问超时,请检查网络或稍后再试.");
				}
			}.bind(this));
		},
		_initRoles : function(taskId) {
			this.JsonModel().loadData(this, "bpmConfig/findRoleByTask?key=" + taskId, function(oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data), "roles");
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), function(oData) {
				if ("timeout" == oData.message) {
					MessageToast.show("访问超时,请检查网络或稍后再试.");
				}
			}.bind(this));
		},
		_initAllRole : function(oDialog) {
			var oDialog = oDialog;
			this.JsonModel().loadData(this, "bpmConfig/findRoles", function(oData, model) {
				if (oData.statusCode == "000000") {
					oDialog.setModel(new JSONModel(oData.data), "rSection");
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), function(oData) {
				if ("timeout" == oData.message) {
					MessageToast.show("访问超时,请检查网络或稍后再试.");
				}
			}.bind(this));
		},
		/**
		 * 事件
		 */
		navBack : function(oEvent) {
			this.getRouter().getTargets().display("procDefinitionDetail", {
				fromTarget : "procTaskEdit"
			});
		},
		handleSavePress : function(oEvent) {
			var btn = oEvent.getSource();
			btn.setBusy(true);
			this.JsonModel().loadData(this, "bpmConfig/saveTask", function(oData, model) {
				btn.setBusy(false);
				this._onSuccessDialog(oData.data);
			}.bind(this), function(oData) {
				btn.setBusy(false);
				MessageToast.show("编辑失败!");
				oDialog.setBusy(false);
			}.bind(this), this.getView().getModel("task").getJSON());
		},
		handleCancelPress : function(oEvent) {
			this.navBack();
		},
		handleRoleAdd : function(oEvent) {
			var t = this.getView().getModel("task").getData();
			var oDialog = this._getTaskRolePopover();
			oDialog.setModel(new JSONModel({
				definitionId : t.definitionId,
				taskId : t.taskId
			}), "rEdit");
			oDialog.setModel(this.getView().getModel("fSection"), "fSection");
			this._initAllRole(oDialog);
			this.getView().addDependent(oDialog);
			oDialog.open();
		},
		handleRoleEdit : function(oEvent) {
			var oList = oEvent.getSource(), context = oList.getBindingContext("roles");
			var oData = context.getModel().getProperty(context.getPath());

			var oDialog = this._getTaskRolePopover();
			oDialog.setModel(this.getView().getModel("fSection"), "fSection");
			this._initAllRole(oDialog);
			oDialog.setModel(new JSONModel(oData), "rEdit");

			this.getView().addDependent(oDialog);
			oDialog.open();
		},
		handleRoleDelete : function(oEvent) {
			var oList = oEvent.getSource(), oItem = oEvent.getParameter("listItem"), context = oItem.getBindingContext("roles");

			var oData = context.getModel().getProperty(context.getPath());
			oList.setBusy(true);
			this.JsonModel().loadData(this, "bpmConfig/deleteTaskRole", function(oData, model) {
				this._initRoles(oData.data.taskId);
				oList.setBusy(false);
				MessageToast.show("删除成功!");
			}.bind(this), function(oData) {
				MessageToast.show("删除失败!");
				oList.setBusy(false);
			}.bind(this), new JSONModel(oData).getJSON());
		},
		confirmRoleSave : function(oEvent) {
			var oView = this.getView();
			var oDialog = this._getTaskRolePopover();
			var fModel = oDialog.getModel("rEdit");
			oDialog.setBusy(true);
			this.JsonModel().loadData(this, "bpmConfig/saveTaskRole", function(oData, model) {
				oDialog.setBusy(false);
				oDialog.close();
				this._initRoles(oData.data.taskId);
			}.bind(this), function(oData) {
				MessageToast.show("编辑失败!");
				oDialog.setBusy(false);
			}.bind(this), fModel.getJSON());
		},
		cancelRoleSave : function(oEvent) {
			this._getTaskRolePopover().destroy();
		},

	})
	return ProcTaskEdit;
});