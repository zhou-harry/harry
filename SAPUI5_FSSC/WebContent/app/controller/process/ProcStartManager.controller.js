sap.ui.define([ 
	'harry/app/controller/BaseController', 
	'jquery.sap.global',
	'sap/ui/model/json/JSONModel' ,
	"sap/m/MessageToast",
	'harry/app/format/formatter'
], function(BaseController, jQuery,JSONModel,MessageToast,formatter) {
	"use strict";
	return BaseController.extend("harry.app.controller.process.ProcStartManager", {
		formatter: formatter,
		/**
		* Called when a controller is instantiated and its View controls (if available) are already created.
		* Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
		* @memberOf app.view.process.ProcStartManager
		*/
		onInit: function() {
			this.getRouter().getTarget("procStartManager").attachDisplay(this.handleAttachDisplay, this);
		},
		
		/**
		* Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
		* (NOT before the first rendering! onInit() is used for that one!).
		* @memberOf app.view.process.ProcStartManager
		*/
		//	onBeforeRendering: function() {
		//
		//	},
		
		/**
		* Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
		* This hook is the same one that SAPUI5 controls get after being rendered.
		* @memberOf app.view.process.ProcStartManager
		*/
		//	onAfterRendering: function() {
		//
		//	},
		
		/**
		* Called when the Controller is destroyed. Use this one to free resources and finalize activities.
		* @memberOf app.view.process.ProcStartManager
		*/
		//	onExit: function() {
		//
		//	}
		/**
		 * AttachDisplay
		 */
		handleAttachDisplay : function(oEvent) {
			var _oData = oEvent.getParameter("data");
			var _procKey=null;
			if (_oData) {
				_procKey=_oData.data.key;
			}
			this.getView().setModel(new JSONModel({
				procType : null,
				businessKey : null,
				description : null,
				variables :{}
			}), "deploy");
			this.getView().setModel(new JSONModel(new Array()), "variables");
		},
		/**
		 * 弹出框
		 */
		_getTypePopover: function () {
			var oView=this.getView();
			var oDialog = oView.byId("typeTreeId");
			if (!oDialog) {
				oDialog = sap.ui.xmlfragment(oView.getId(), "harry.app.fragment.processConfig.TypeTree",this);
			}
			oDialog.addStyleClass(this.getOwnerComponent().getPopoverDensityClass());
			return oDialog;
		},
		/**
		 * 初始化数据
		 */
		_initTypeDms: function(type){
			var oModel=this.getView().getModel("variables");
			this.JsonModel().loadData(this,
				"bpmConfig/findProcDms?procType="+type,
				function(oData, model) {
					if (oData.statusCode == "000000") {
						oModel.setData(new Array());
						// add data
						var aEntries = oModel.getData();
						$.each(oData.data, function(index, ele) {
							aEntries.unshift({
								_key : ele.key,
								_value : null,
								_enabled : false
							});
						}.bind(this))
						oModel.refresh();
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
		_initTypeTree : function(oDialog){
			if (undefined==oDialog) {
				obj=this.getView();
			}
			this.JsonModel().loadData(this,
				"bpm/procTypeTree",
				function(oData, model) {
					if (oData.statusCode == "000000") {
						oDialog.setModel(new JSONModel(oData.data), "tree");
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
		handleTypeHelp: function(oEvent) {
			var oView=this.getView();
			var oDialog = this._getTypePopover();
			
			this._initTypeTree(oDialog);
			
			oView.addDependent(oDialog);
			oDialog.open();
		},
		onChangeSelect: function(oEvent) {
			var item=oEvent.getParameters().listItem;
			var context=item.getItemNodeContext().context;
			var cData=context.getModel().getProperty(context.getPath());
			
			var dModel=this.getView().getModel("deploy");
			dModel.getData().procType=cData.type;
			dModel.getData().typeName=cData.name;
			dModel.refresh();
			
			this._initTypeDms(cData.type);
			
			var oDialog = this._getTypePopover();
			oDialog.close();
		},
		cancelSelect: function(oEvent) {
			this._getTypePopover().destroy();
		},
		onPressStart : function(oEvent) {
			var oView=this.getView();
			var _data=oView.getModel("deploy");
			var _data2=oView.getModel("variables");
			_data.getData().variables=_data2.getData();
			oView.byId("startId").setBusy(true);
			this.JsonModel().loadData(this,
					"bpm/startProcess",
					function (oData, model) {
						if (oData.statusCode == "000000") {
							this.getRouter().getTargets().display("masterProcess", {
								fromTarget : "procStartManager",
								data : oData.data
							});
						} else {
							MessageToast.show(oData.message);
						}
						oView.byId("startId").setBusy(false);
					}.bind(this), 
					function (oData) {
						oView.byId("startId").setBusy(false);
					}.bind(this),
					_data.getJSON()
			);
		},
		onAddVar: function(oEvent){
			var oModel=this.getView().getModel("variables");
			// add data
			var aEntries = oModel.getData();
			aEntries.unshift({
				_key : null,
				_value : null,
				_enabled : true
			});
			oModel.refresh();
		},
		onSelectedDelete: function(oEvent){
			var oModel=this.getView().getModel("variables");
			
			var context=oEvent.getSource().getBindingContext("variables");
			var sPath = context.getPath();
			var _data = oModel.getData();
			
			var index = sPath.substr(sPath.lastIndexOf("/")+1);
			_data.splice(index,1);
			oModel.refresh();
		},
	})
});