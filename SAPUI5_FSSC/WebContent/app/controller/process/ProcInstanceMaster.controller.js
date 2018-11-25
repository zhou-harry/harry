sap.ui.define([ 
	'harry/app/controller/BaseController', 
	'jquery.sap.global',
	'sap/ui/model/json/JSONModel' ,
	"sap/m/MessageToast",
	"sap/m/SplitContainer",
	"sap/ui/Device",
	'sap/ui/model/Filter',
	'sap/ui/model/Sorter',
	"sap/ui/model/FilterOperator",
	'harry/app/format/formatter'
], function(BaseController, 
		jQuery,
		JSONModel,
		MessageToast,
		SplitContainer, 
		Device,
		Filter,
		Sorter,
		FilterOperator,
		formatter) {
	"use strict";
	var ProcInstanceMaster = BaseController.extend("harry.app.controller.process.ProcInstanceMaster", {
		formatter: formatter,
		/**
		* Called when a controller is instantiated and its View controls (if available) are already created.
		* Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
		* @memberOf app.view.process.ProcInstanceManager
		*/
		onInit: function() {
			this.bus = sap.ui.getCore().getEventBus();
			this.getRouter().getTarget("masterProcess").attachDisplay(this.handleAttachDisplay, this);
		},
		
		/**
		* Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
		* (NOT before the first rendering! onInit() is used for that one!).
		* @memberOf app.view.process.ProcInstanceManager
		*/
		//	onBeforeRendering: function() {
		//
		//	},
		
		/**
		* Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
		* This hook is the same one that SAPUI5 controls get after being rendered.
		* @memberOf app.view.process.ProcInstanceManager
		*/
		//	onAfterRendering: function() {
		//
		//	},
		
		/**
		* Called when the Controller is destroyed. Use this one to free resources and finalize activities.
		* @memberOf app.view.process.ProcInstanceManager
		*/
		onExit: function() {
			if (this.processViewSettingsDialog) {
				this.processViewSettingsDialog.destroy();
			}
		},
		handleAttachDisplay : function(oEvent) {
			
			var _oData = oEvent.getParameter("data");
			var _procKey=null;
			if (_oData) {
				this._procInstance(null,_oData.data,null,null);
			}
		},
		handleProcInstance: function(oEvent) {
			if (!this.processViewSettingsDialog) {
				this.processViewSettingsDialog = sap.ui.xmlfragment("harry.app.fragment.process.ProcessViewSettingsDialog", this);
				this.getView().addDependent(this.processViewSettingsDialog);
				this.processViewSettingsDialog.addStyleClass(this.getOwnerComponent().getPopoverDensityClass());
			}
			this.JsonModel().loadData(this,"bpm/allVersion",
					function(oData, model) {
						if (oData.statusCode == "000000") {
							this.processViewSettingsDialog.setModel(new JSONModel(oData.data), "deployment");
							this.processViewSettingsDialog.setModel(new JSONModel(), "processForm");
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
			this.processViewSettingsDialog.open();
		},
		handleProcessQuery: function (oEvent) {
			var oView = this.getView();
			var mParams = oEvent.getParameters();
			var pdid;
			var fData=this.processViewSettingsDialog.getModel("processForm").getData();
			jQuery.each(mParams.filterItems, function (i, oItem) {
				pdid=oItem.getKey();
			});
			this._procInstance(pdid,fData.instanceId,fData.businessKey,fData.name);
			// apply sorter to binding
			// (grouping comes before sorting)
			var sPath,bDescending,vGroup,aSorters = [];
			if (mParams.groupItem) {
				sPath = mParams.groupItem.getKey();
				aSorters.push(new Sorter(sPath, mParams.groupDescending, function(oContext) {
					var name = oContext.getProperty(sPath),text=name;
					if ("isEnded"==sPath) {
						text= (name?"[已结束]":"[进行中]")
					}
					return {
						key: name,
						text: text
					};
				}));
			}
			aSorters.push(new Sorter(mParams.sortItem.getKey(), mParams.sortDescending));
			
			var list = this.getView().byId("procList");
			var binding = list.getBinding("items");
			binding.sort(aSorters);
		},
		_procInstance: function(pdid,piid,bsid,name) {
			var oView=this.getView();
			oView.byId("procList").setBusy(true);
			this.JsonModel().loadData(this,"bpm/procInstances?pdid="+pdid+"&piid="+piid+"&bsid="+bsid+"&name="+name,
				function (oData, model) {
					if (oData.statusCode == "000000") {
						$.each(oData.data, function(index, ele) {
							ele.startTime = this.convertDate(ele.startTime);
						}.bind(this))
						this.getView().setModel(new JSONModel(oData.data),"process");
						if (oData.data.length===1) {
							this.getRouter().getTargets().display("detailProcess", {
								fromTarget : "masterProcess",
								data : oData.data[0]
							});
						}else if (oData.data.length===0) {
							this.getRouter().getTargets().display("detailProcess", {
								fromTarget : "masterProcess"
							});
						}
					} else {
						MessageToast.show(oData.message);
					}
					oView.byId("procList").setBusy(false);
				}.bind(this), 
				function (oData) {
					MessageToast.show("流程实例查询失败!");
					oView.byId("procList").setBusy(false);
				}.bind(this),null,false
			);
			
		},
		showProcInfo: function(oEvent) {
			var context=oEvent.getSource().getBindingContext("process");
			var path = context.getPath();
			var index = path.substr(path.lastIndexOf("/")+1);
			var oData=context.getModel().oData[index];
			
			this.getRouter().getTargets().display("detailProcess", {
				fromTarget : "masterProcess",
				data : oData
			});
		},
		searchProc: function(oEvent) {
			// add filter for search
			var _oGlobalFilter;
			var sQuery = oEvent.getSource().getValue();
			if (sQuery && sQuery.length > 0) {
				_oGlobalFilter = new Filter([
					new Filter("instanceId", FilterOperator.Contains, sQuery),
					new Filter("description", FilterOperator.Contains, sQuery),
					new Filter("processName", FilterOperator.Contains, sQuery)
				], false);
			}
			// update list binding
			var list = this.byId("procList");
			var obinding = list.getBinding();
			var binding = list.getBinding("items");
			binding.filter(_oGlobalFilter, "Application");
		},
	})
	return ProcInstanceMaster;
});