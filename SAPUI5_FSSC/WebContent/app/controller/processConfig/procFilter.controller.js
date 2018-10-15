sap.ui.define([
	'harry/app/controller/BaseController',
	'sap/m/MessageToast',
	'sap/m/MessageBox',
	'sap/ui/model/json/JSONModel',
	'harry/app/format/formatter'
], function (BaseController, MessageToast, MessageBox, JSONModel, formatter) {
	"use strict";
	return BaseController.extend("harry.app.controller.processConfig.procFilter", {
		formatter: formatter,
/**
* Called when a controller is instantiated and its View controls (if available) are already created.
* Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
* @memberOf app.controller.processConfig.procFilter
*/
	onInit: function() {
		this.getRouter().getTarget("procFilterMaster").attachDisplay(this.handleAttachDisplayMaster, this);
		this.getRouter().getTarget("procFilterDetail").attachDisplay(this.handleAttachDisplayDetail, this);
	},

/**
* Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
* (NOT before the first rendering! onInit() is used for that one!).
* @memberOf app.controller.processConfig.procFilter
*/
//	onBeforeRendering: function() {
//
//	},

/**
* Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
* This hook is the same one that SAPUI5 controls get after being rendered.
* @memberOf app.controller.processConfig.procFilter
*/
//	onAfterRendering: function() {
//
//	},

/**
* Called when the Controller is destroyed. Use this one to free resources and finalize activities.
* @memberOf app.controller.processConfig.procFilter
*/
//	onExit: function() {
//
//	},
	/**
	 * AttachDisplay
	 */
	handleAttachDisplayMaster : function(oEvent) {
		var _oData = oEvent.getParameter("data");
		if (undefined!=_oData&&"procFilterDetail"==_oData.fromTarget) {
			return;
		}
		this._initFilter();
	},
	handleAttachDisplayDetail : function(oEvent) {
		var _oData = oEvent.getParameter("data");
		if (undefined!=_oData&&"procFilterMaster"==_oData.fromTarget) {
			this.getView().setModel(new JSONModel(_oData.data), "filter");
			this._initFilterInfo(_oData.data.filterId);
			this._initFilterMatcher(_oData.data.filterId);
		}
	},
	/**
	 * 弹出框加载
	 */
	_getFilterPopover: function () {
		var oView=this.getView();
		var oDialog = oView.byId("filterEditId");
		if (!oDialog) {
			oDialog = sap.ui.xmlfragment(oView.getId(), "harry.app.fragment.processConfig.FilterEdit",this);
		}
		oDialog.addStyleClass(this.getOwnerComponent().getPopoverDensityClass());
		return oDialog;
	},
	_getFilterMatcherPopover: function () {
		var oView=this.getView();
		var oDialog = oView.byId("matcherEditId");
		if (!oDialog) {
			oDialog = sap.ui.xmlfragment(oView.getId(), "harry.app.fragment.processConfig.FilterMatcherEdit",this);
		}
		oDialog.addStyleClass(this.getOwnerComponent().getPopoverDensityClass());
		return oDialog;
	},
	/**
	 * 初始化数据
	 */
	_initFilter: function(){
		this.JsonModel().loadData(this,
			"bpmConfig/findFilter",
			function(oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data), "filters");
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
	_initFilterInfo: function(key){
		this.JsonModel().loadData(this,
			"bpmConfig/findFilterByKey?key="+key,
			function(oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data), "filter");
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
	_initFilterRef: function(key,oDialog){
		var oDialog=oDialog;
		this.JsonModel().loadData(this,
			"bpmConfig/findForRelation?key="+key,
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
	_initFilterMatcher: function(key){
		this.JsonModel().loadData(this,
			"bpmConfig/findFilterMatcherByKey?key="+key,
			function(oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data), "matchers");
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
	_initDimension : function(oDialog){
		var oDialog=oDialog;
		this.JsonModel().loadData(this,
			"bpmConfig/findDimension",
			function(oData, model) {
				if (oData.statusCode == "000000") {
					oDialog.setModel(new JSONModel(oData.data), "dmsSection");
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
	handleFilterPress: function(oEvent){
		var cData=this.currentData(oEvent,"filters");
		this.getRouter().getTargets().display("procFilterDetail", {
			fromTarget : "procFilterMaster",
			data : cData
		});
	},
	handleFilterAdd: function(oEvent){
		var oDialog=this._getFilterPopover();
		oDialog.setModel(new JSONModel(), "fEdit");
		this._initFilterRef("",oDialog);
		
		this.getView().addDependent(oDialog);
		oDialog.open();
	},
	handleFilterEdit: function(oEvent){
		var oList = oEvent.getSource(),
		context = oList.getBindingContext("filters");
		var oData=context.getModel().getProperty(context.getPath());
		
		var oDialog = this._getFilterPopover();
		oDialog.setModel(new JSONModel(oData), "fEdit");
		this._initFilterRef(oData.filterId,oDialog);
		
		this.getView().addDependent(oDialog);
		oDialog.open();
	},
	handleFilterDelete: function(oEvent){
		var oList = oEvent.getSource(),
		oItem = oEvent.getParameter("listItem"),
		context = oItem.getBindingContext("filters");
		
		var oData=context.getModel().getProperty(context.getPath());
		
		oList.setBusy(true);
		this.JsonModel().loadData(this,
			"bpmConfig/deleteFilter",
			function (oData, model) {
				this._initFilter();
				oList.setBusy(false);
			}.bind(this), 
			function (oData) {
				MessageToast.show("删除失败!");
				oList.setBusy(false);
			}.bind(this),
			new JSONModel(oData).getJSON()
		);
	},
	navBack: function(oEvent) {
		this.getRouter().getTargets().display("procFilterMaster", {
			fromTarget : "procFilterDetail"
		});
	},
	confirmFilterSave : function (oEvent) {
		var oView=this.getView();
		var oDialog = this._getFilterPopover();
		var fModel=oDialog.getModel("fEdit");
		oDialog.setBusy(true);
		this.JsonModel().loadData(this,
			"bpmConfig/saveFilter",
			function (oData, model) {
				oDialog.setBusy(false);
				oDialog.close();
				this._initFilter();
				this.getRouter().getTargets().display("procFilterDetail", {
					fromTarget : "procFilterMaster",
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
	cancelFilterSave: function() {
		this._getFilterPopover().destroy();
	},
	handleMatcherAdd: function(oEvent){
		var oDialog=this._getFilterMatcherPopover();
		var fModel=this.getView().getModel("filter");
		oDialog.setModel(fModel,"filter");
		var fData=fModel.getData();
		oDialog.setModel(new JSONModel({
			filterId: fData.filterId
		}), "mEdit");
		this._initDimension(oDialog);
		
		this.getView().addDependent(oDialog);
		oDialog.open();
	},
	handleMatcherEdit: function(oEvent){
		var oList = oEvent.getSource(),
		context = oList.getBindingContext("matchers");
		var oData=context.getModel().getProperty(context.getPath());
		
		var oDialog = this._getFilterMatcherPopover();
		oDialog.setModel(new JSONModel(oData), "mEdit");
		oDialog.setModel(this.getView().getModel("filter"),"filter");
		this._initDimension(oDialog);
		
		this.getView().addDependent(oDialog);
		oDialog.open();
	},
	handleMatcherDelete: function(oEvent){
		var oList = oEvent.getSource(),
		oItem = oEvent.getParameter("listItem"),
		context = oItem.getBindingContext("matchers");
		
		var _oData=context.getModel().getProperty(context.getPath());
		oList.setBusy(true);
		this.JsonModel().loadData(this,
			"bpmConfig/deleteFilterMatcher",
			function (oData, model) {
				this._initFilterMatcher(_oData.filterId);
				oList.setBusy(false);
			}.bind(this), 
			function (oData) {
				MessageToast.show("删除失败!");
				oList.setBusy(false);
			}.bind(this),
			new JSONModel(_oData).getJSON()
		);
	},
	confirmMatcherSave : function (oEvent) {
		var oView=this.getView();
		var oDialog = this._getFilterMatcherPopover();
		var filterId=this.getView().getModel("filter").getData().filterId;
		oDialog.setBusy(true);
		this.JsonModel().loadData(this,
			"bpmConfig/saveFilterMatcher",
			function (oData, model) {
				this._initFilterMatcher(filterId);
				oDialog.setBusy(false);
				oDialog.close();
			}.bind(this), 
			function (oData) {
				MessageToast.show("编辑失败!");
				oDialog.setBusy(false);
			}.bind(this),
			oDialog.getModel("mEdit").getJSON()
		);
	},
	cancelMatcherSave: function() {
		this._getFilterMatcherPopover().destroy();
	},
})
});