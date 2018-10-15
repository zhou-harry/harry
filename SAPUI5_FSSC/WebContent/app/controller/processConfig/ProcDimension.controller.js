sap.ui.define([ 
	'harry/app/controller/BaseController', 
	'jquery.sap.global',
	'sap/ui/model/json/JSONModel' ,
	"sap/m/MessageToast",
	'harry/app/format/formatter'
], function(BaseController, jQuery,JSONModel,MessageToast,formatter) {
	"use strict";
	return BaseController.extend("harry.app.controller.processConfig.ProcDimension", {
		formatter: formatter,
/**
* Called when a controller is instantiated and its View controls (if available) are already created.
* Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
* @memberOf app.controller.processConfig.ProcDimension
*/
	onInit: function() {
		this.getRouter().getTarget("procDimension").attachDisplay(this.handleAttachDisplay, this);
	},

/**
* Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
* (NOT before the first rendering! onInit() is used for that one!).
* @memberOf app.controller.processConfig.ProcDimension
*/
//	onBeforeRendering: function() {
//
//	},

/**
* Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
* This hook is the same one that SAPUI5 controls get after being rendered.
* @memberOf app.controller.processConfig.ProcDimension
*/
//	onAfterRendering: function() {
//
//	},

/**
* Called when the Controller is destroyed. Use this one to free resources and finalize activities.
* @memberOf app.controller.processConfig.ProcDimension
*/
//	onExit: function() {
//
//	},
	/**
	 * AttachDisplay
	 */
	handleAttachDisplay : function(oEvent) {
		var _oData = oEvent.getParameter("data");
		//加载数据
		this._initList();
	},
	
	_initList : function(){
		var list=this.getView().byId("list");
		list.setBusy(true);
		this.JsonModel().loadData(this,
			"bpmConfig/findDimension",
			function(oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data), "dimension");
				} else {
					MessageToast.show(oData.message);
				}
				list.setBusy(false);
			}.bind(this), 
			function(oData) {
				if ("timeout"==oData.message) {
					MessageToast.show("访问超时,请检查网络或稍后再试.");
				}
				list.setBusy(false);
			}.bind(this)
		);
	},
	_getDimensionPropover: function(){
		var oView=this.getView();
		var oDialog = oView.byId("newDimension");
		if (!oDialog) {
			oDialog = sap.ui.xmlfragment(oView.getId(), "harry.app.fragment.processConfig.DimensionEdit",this);
		}
		oDialog.addStyleClass(this.getOwnerComponent().getPopoverDensityClass());
		return oDialog;
	},
	handleAdd: function(oEvent){
		var oView=this.getView();
		var oData={
				key:null,
				name:null,
				type:"1"
		};
		var oDialog = this._getDimensionPropover();
		oDialog.setModel(new JSONModel(oData), "nDms");
		oView.addDependent(oDialog);
		oDialog.open();
	},
	handleEditPress: function(oEvent){
		var oView=this.getView();
		var oList = oEvent.getSource(),
		context = oList.getBindingContext("dimension");
		
		var oData=context.getModel().getProperty(context.getPath());
		
		var oDialog = this._getDimensionPropover();
		oDialog.setModel(new JSONModel(oData), "nDms");
		oView.addDependent(oDialog);
		oDialog.open();
	},
	handleDelete: function(oEvent){
		var oList = oEvent.getSource(),
		oItem = oEvent.getParameter("listItem"),
		context = oItem.getBindingContext("dimension");
		
		var oData=context.getModel().getProperty(context.getPath());
		oList.setBusy(true);
		this.JsonModel().loadData(this,
			"bpmConfig/delDimension",
			function (oData, model) {
				this._initList();
				oList.setBusy(false);
			}.bind(this), 
			function (oData) {
				MessageToast.show("删除失败!");
				oList.setBusy(false);
			}.bind(this),
			new JSONModel(oData).getJSON()
		);
	},
	confirmSave : function () {
		var oView=this.getView();
		var oDialog = oView.byId("newDimension");
		oDialog.setBusy(true);
		this.JsonModel().loadData(this,
			"bpmConfig/saveDimension",
			function (oData, model) {
				this._initList();
				oDialog.setBusy(false);
				oDialog.close();
			}.bind(this), 
			function (oData) {
				MessageToast.show("编辑失败!");
				oDialog.setBusy(false);
			}.bind(this),
			oDialog.getModel("nDms").getJSON()
		);
	},
	cancelSave: function() {
		this.getView().byId("newDimension").destroy();
	},
})
});