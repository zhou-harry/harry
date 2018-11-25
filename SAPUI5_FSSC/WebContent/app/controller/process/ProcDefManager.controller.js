sap.ui.define([
		'harry/app/controller/BaseController', 'jquery.sap.global', 'sap/ui/model/json/JSONModel', "sap/m/MessageToast", 'sap/ui/model/Sorter', 'harry/app/format/formatter'
], function(BaseController, jQuery, JSONModel, MessageToast, Sorter, formatter) {
	"use strict";
	var ProcDefManager_ = BaseController.extend("harry.app.controller.process.ProcDefManager", {
		formatter : formatter,
		/**
		 * Called when a controller is instantiated and its View controls (if
		 * available) are already created. Can be used to modify the View before
		 * it is displayed, to bind event handlers and do other one-time
		 * initialization.
		 * 
		 * @memberOf app.view.map.MasterMap
		 */
		onInit : function() {
			this.getRouter().getTarget("procDefManager").attachDisplay(this.handleAttachDisplay, this);
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
		 * document). Post-rendering manipulations of the HTML could be done
		 * here. This hook is the same one that SAPUI5 controls get after being
		 * rendered.
		 * 
		 * @memberOf app.view.map.MasterMap
		 */
		// onAfterRendering: function() {
		//
		// },
		/**
		 * Called when the Controller is destroyed. Use this one to free
		 * resources and finalize activities.
		 * 
		 * @memberOf app.view.map.MasterMap
		 */
		onExit : function() {
			if (this.oDiagramPopover) {
				this.oDiagramPopover.destroy();
			}
		},
		/**
		 * AttachDisplay
		 */
		handleAttachDisplay : function(oEvent) {
			this._oData = oEvent.getParameter("data");
			var uData = this.getView().getModel("user");

			this._initTypeTree();
		},
		_initTypeTree : function() {
			var tree = this.getView().byId("tree");
			tree.setBusy(true);
			this.JsonModel().loadData(this, "bpm/procTypeTree", function(oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data), "tree");
				} else {
					MessageToast.show(oData.message);
				}
				tree.setBusy(false);
			}.bind(this), function(oData) {
				if ("timeout" == oData.message) {
					MessageToast.show("访问超时,请检查网络或稍后再试.");
				}
				tree.setBusy(false);
			}.bind(this));
		},
		_initDetailDeployment : function(type) {
			var detail = this.getView().byId("DeploymentDetail");
			detail.setBusy(true);
			this.JsonModel().loadData(this, "bpm/allProcessDef?type=" + type, function(oData, model) {
				this.getView().byId("DynamicSideContent").setShowSideContent(true);

				$.each(oData.data, function(index, ele) {
					var arr1 = ele.deployTime.split(" ");
					var sdate = arr1[0].split('-');
					var stime = arr1[1].split(':');
					var date = new Date(sdate[0], sdate[1] - 1, sdate[2], stime[0], stime[1], stime[2]);
					ele.deployTime = date;
					this.JsonModel().loadData(this, "bpm/sizeInstance?pdid=" + ele.id, function(oData, model) {
						ele.sizeInstance = oData.data[0];
						ele.sizeHistory = oData.data[1];
					}.bind(this), function(oData) {
						MessageToast.show("流程实例数/历史数查询失败");
					}.bind(this), null, false);
				}.bind(this))
				this.getView().setModel(new JSONModel(oData.data), "detail");
				// 分组排序
				this._detailSorter();
				detail.setBusy(false);
			}.bind(this), function(oData) {
				MessageToast.show("查询失败");
				detail.setBusy(false);
			}.bind(this));
		},
		_detailSorter : function() {
			var list = this.getView().byId("DeploymentDetail");
			var binding = list.getBinding("items");
			var aSorters = [];
			aSorters.push(new Sorter("key", false, function(oContext) {
				return {
					key : oContext.getProperty("key"),
					text : oContext.getProperty("name")
				};
			}));
			aSorters.push(new Sorter("deploymentId", true));
			binding.sort(aSorters);
		},
		onItemPress : function(oEvent) {
			var item = oEvent.getParameters().listItem;
			var context = item.getItemNodeContext().context;
			var cData = context.getModel().getProperty(context.getPath());

			this._initDetailDeployment(cData.type);
		},
		onPressDeploy : function(oEvent) {
			var oView = this.getView();
			var context = oEvent.getSource().getBindingContext("tree");
			var oData = context.getModel().getProperty(context.getPath());

			var oDialog = oView.byId("newDeployment");
			if (!oDialog) {
				oDialog = sap.ui.xmlfragment(oView.getId(), "harry.app.fragment.process.DeploymentPopover", this);
				oDialog.setModel(new JSONModel(oData), "nDeploy");
				oView.addDependent(oDialog);
			}
			oDialog.open();
		},
		onPressStart : function(oEvent) {
			var oData = this.currentData(oEvent, "tree");

			this.getRouter().getTargets().display("procStartManager", {
				fromTarget : "procDefManager",
				data : oData
			});
		},
		onPressDiagram : function(oEvent) {
			var oData = this.currentData(oEvent, "detail");
			var url = this.JsonModel().getPath() + "bpm/deployDiagram?deployId=" + oData.deploymentId;

			this.getView().setModel(new JSONModel({
				diagram : url
			}));
			if (!this.oDiagramPopover) {
				this.oDiagramPopover = sap.ui.xmlfragment("harry.app.fragment.process.DeployDiagramPopover", this);
				this.getView().addDependent(this.oDiagramPopover);
			}
			this.oDiagramPopover.openBy(oEvent.getSource());
		},
		confirmDeploy : function() {
			var oView = this.getView();
			oView.byId("newDeployment").setBusy(true);
			var sType = oView.byId('deployType').getText();
			var sText = oView.byId('deployDesc').getValue();
			this.JsonModel().loadData(this, "bpm/deploy?name=" + sText + "&type=" + sType, function(oData, model) {
				this._initTypeTree();
				this._initDetailDeployment(sType);
				oView.byId("newDeployment").setBusy(false);
				oView.byId("newDeployment").close();
			}.bind(this), function(oData) {
				MessageToast.show("新版本部署失败!");
				oView.byId("newDeployment").setBusy(false);
			}.bind(this));
		},
		cancelDeploy : function() {
			this.getView().byId("newDeployment").destroy();
		},
		handleDelete : function(oEvent) {
			var oView = this.getView();
			var oList = oEvent.getSource(), oItem = oEvent.getParameter("listItem"), context = oItem.getBindingContext("detail"), sPath = context.getPath();
			var sizeInstance = context.getProperty("sizeInstance");
			var sizeHistory = context.getProperty("sizeHistory");
			if (sizeHistory + sizeInstance > 0) {
				MessageToast.show("当前版本下存在有效的审批信息!");
				return;
			}
			var deploymentId = context.getProperty("deploymentId");
			var key = context.getProperty("key");

			oList.attachEventOnce("updateFinished", oList.focus, oList);
			oView.byId("DeploymentDetail").setBusy(true);
			this.JsonModel().loadData(this, "bpm/deleteDeploy?deployId=" + deploymentId, function(oData, model) {
				if (oData.statusCode == "000000") {
					this._initTypeTree();
					this._initDetailDeployment(key);
					oView.byId("DeploymentDetail").setBusy(false);
					MessageToast.show("成功删除此版本:" + deploymentId);
				} else {
					oView.byId("DeploymentDetail").setBusy(false);
					MessageToast.show(oData.data.message);
				}
			}.bind(this), function(oData) {
				oView.byId("DeploymentDetail").setBusy(false);
				MessageToast.show("操作失败!");
			}.bind(this));
		},

	})
	return ProcDefManager_;
});