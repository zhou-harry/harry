sap.ui.define([
	'harry/app/controller/BaseController',
	'sap/m/MessageToast',
	'sap/m/MessageBox',
	'sap/ui/model/json/JSONModel',
	'harry/app/format/formatter'
], function (BaseController, MessageToast, MessageBox, JSONModel, formatter) {
	"use strict";
	var ProcDefinition= BaseController.extend("harry.app.controller.processConfig.ProcDefinition", {
		formatter: formatter,
/**
* Called when a controller is instantiated and its View controls (if available) are already created.
* Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
* @memberOf app.controller.processConfig.ProcDefinition
*/
	onInit: function() {
		this.getRouter().getTarget("procDefinitionMaster").attachDisplay(this.handleAttachDisplayMaster, this);
		this.getRouter().getTarget("procDefinitionDetail").attachDisplay(this.handleAttachDisplayDetail, this);
	},

/**
* Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
* (NOT before the first rendering! onInit() is used for that one!).
* @memberOf app.controller.processConfig.ProcDefinition
*/
//	onBeforeRendering: function() {
//
//	},

/**
* Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
* This hook is the same one that SAPUI5 controls get after being rendered.
* @memberOf app.controller.processConfig.ProcDefinition
*/
//	onAfterRendering: function() {
//
//	},

/**
* Called when the Controller is destroyed. Use this one to free resources and finalize activities.
* @memberOf app.controller.processConfig.ProcDefinition
*/
//	onExit: function() {
//
//	},
	/**
	 * AttachDisplay
	 */
	handleAttachDisplayMaster : function(oEvent) {
		var _oData = oEvent.getParameter("data");
		if (undefined!=_oData&&"procDefinitionDetail"==_oData.fromTarget) {
			return;
		}
		this._initProcdef();
	},
	handleAttachDisplayDetail : function(oEvent) {
		var _oData = oEvent.getParameter("data");
		if (undefined!=_oData) {
			if ("procDefinitionMaster"==_oData.fromTarget) {
				this.getView().setModel(new JSONModel(_oData.data), "pdef");
				this._initTasks(_oData.data.key);
			}else if ("procTaskEdit"==_oData.fromTarget) {
				if (_oData.data) {
					this._initTasks(_oData.data.definitionId);
				}
			}
		}
	},
	/**
	 * 加载弹出框
	 */
	_getPdefPropover: function(){
		var oView=this.getView();
		var oDialog = oView.byId("definitionEditId");
		if (!oDialog) {
			oDialog = sap.ui.xmlfragment(oView.getId(), "harry.app.fragment.processConfig.DefinitionEdit",this);
		}
		oDialog.addStyleClass(this.getOwnerComponent().getPopoverDensityClass());
		return oDialog;
	},
	/**
	 * 初始化数据
	 */
	_initProcdef: function(){
		this.JsonModel().loadData(this,
			"bpmConfig/findProcdef",
			function(oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data), "pdefs");
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
	_initDeployment : function(oDialog) {
		var oDialog = oDialog;
		this.JsonModel().loadData(this,
			"bpm/allLastVersion",
			function(oData, model) {
				if (oData.statusCode == "000000") {
					oDialog.setModel(new JSONModel(oData.data), "deploy");
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
	_initTasks: function(key){
		var key=key;
		this.JsonModel().loadData(this,
			"bpmConfig/findTasks?key="+key,
			function(oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data), "tasks");
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
	 * 流程定义事件
	 */
	handlePdefPress : function(oEvent) {
		var cData=this.currentData(oEvent,"pdefs");
		
		this.getRouter().getTargets().display("procDefinitionDetail", {
			fromTarget : "procDefinitionMaster",
			data : cData
		});
	},
	navBack: function(oEvent) {
		this.getRouter().getTargets().display("procDefinitionMaster", {
			fromTarget : "procDefinitionDetail"
		});
	},
	handlePdefAdd: function(oEvent){
		var oDialog = this._getPdefPropover();
		oDialog.setModel(new JSONModel({vis:true}), "pdef");
		this._initDeployment(oDialog);
		this.getView().addDependent(oDialog);
		oDialog.open();
	},
	handlePdefEdit: function(oEvent){
		var oList = oEvent.getSource(),
		context = oList.getBindingContext("pdefs");
		var oData=context.getModel().getProperty(context.getPath());
		oData.vis=false;
		var oDialog = this._getPdefPropover();
		this._initDeployment(oDialog);
		oDialog.setModel(new JSONModel(oData), "pdef");
		this.getView().addDependent(oDialog);
		oDialog.open();
	},
	handlePdefDelete: function(oEvent){
		var oList = oEvent.getSource(),
		oItem = oEvent.getParameter("listItem"),
		context = oItem.getBindingContext("pdefs");
		
		var oData=context.getModel().getProperty(context.getPath());
		oList.setBusy(true);
		this.JsonModel().loadData(this,
			"bpmConfig/deleteProcdef",
			function (oData, model) {
				this._initProcdef();
				MessageToast.show("删除成功!");
				oList.setBusy(false);
			}.bind(this), 
			function (oData) {
				MessageToast.show("删除失败!");
				oList.setBusy(false);
			}.bind(this),
			new JSONModel(oData).getJSON()
		);
	},
	confirmDefinitionSave : function () {
		var oView=this.getView();
		var oDialog = oView.byId("definitionEditId");
		oDialog.setBusy(true);
		this.JsonModel().loadData(this,
			"bpmConfig/saveProcdef",
			function (oData, model) {
				this._initProcdef();
				oDialog.setBusy(false);
				oDialog.close();
			}.bind(this), 
			function (oData) {
				MessageToast.show("编辑失败!");
				oDialog.setBusy(false);
			}.bind(this),
			oDialog.getModel("pdef").getJSON()
		);
	},
	cancelDefinitionSave: function() {
		this.getView().byId("definitionEditId").destroy();
	},
	/**
	 * 任务节点事件
	 */
	handleTaskAdd: function(oEvent){
		var key=this.getView().getModel("pdef").getData().key;
		var oData={
				definitionId : key,
				status : 1,
				step : 0,
				pending : false,
				isSelect : false
		};
		
		this.getRouter().getTargets().display("procTaskEdit", {
			fromTarget : "procDefinitionDetail",
			data : oData
		});
	},
	handleTaskEdit: function(oEvent){
		var oList = oEvent.getSource(),
		context = oList.getBindingContext("tasks");
		var oData=context.getModel().getProperty(context.getPath());
		
		this.getRouter().getTargets().display("procTaskEdit", {
			fromTarget : "procDefinitionDetail",
			data : oData
		});
	},
	handleTaskDelete: function(oEvent){
		var oList = oEvent.getSource(),
		oItem = oEvent.getParameter("listItem"),
		context = oItem.getBindingContext("tasks");
		
		var oData=context.getModel().getProperty(context.getPath());
		oList.setBusy(true);
		this.JsonModel().loadData(this,
			"bpmConfig/deleteTask",
			function (oData, model) {
				this._initTasks(oData.data.definitionId);
				MessageToast.show("删除成功!");
				oList.setBusy(false);
			}.bind(this), 
			function (oData) {
				MessageToast.show("删除失败!");
				oList.setBusy(false);
			}.bind(this),
			new JSONModel(oData).getJSON()
		);
	},
})
return ProcDefinition;
});