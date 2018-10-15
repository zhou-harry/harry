sap.ui.define([
		'harry/app/controller/BaseController',
		'sap/ui/core/Component',
		'sap/ui/model/json/JSONModel',
		'sap/ui/Device',
		'sap/m/MessageToast',
		'sap/ui/core/mvc/ViewType',
		'harry/app/format/formatter'
], function (BaseController, Component, JSONModel, Device,MessageToast,ViewType, formatter) {
		"use strict";
		return BaseController.extend("harry.app.controller.process.ProcessInfo", {
			formatter: formatter,
/**
* Called when a controller is instantiated and its View controls (if available) are already created.
* Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
* @memberOf app.controller.process.ProcessInfo
*/
	onInit: function() {
		this.eventBus = sap.ui.getCore().getEventBus();
		this.getRouter().getTarget("processInfo").attachDisplay(this.handleAttachDisplay, this);
	},

/**
* Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
* (NOT before the first rendering! onInit() is used for that one!).
* @memberOf app.controller.process.ProcessInfo
*/
//	onBeforeRendering: function() {
//
//	},

/**
* Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
* This hook is the same one that SAPUI5 controls get after being rendered.
* @memberOf app.controller.process.ProcessInfo
*/
//	onAfterRendering: function() {
//
//	},

/**
* Called when the Controller is destroyed. Use this one to free resources and finalize activities.
* @memberOf app.controller.process.ProcessInfo
*/
	onExit: function() {
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
			this._initForm(this._oData.data.formKey);
		}
		this._openFormView();
	},
	/**
	 * 加载表单
	 */
	_openFormView: function(oEvent){
		var code="harry.app.view.fssc."+this._oData.data.formKey;
		var that=this.byId("formPage");
		var oDetailView,oData=this._oData.data;
		
		Component.getOwnerComponentFor(this.getView()).runAsOwner(function () {
			oDetailView = sap.ui.view({
				preprocessors : {
					xml : {
						models : {
							meta : new JSONModel(oData)
						},
					}
				},
				type : sap.ui.core.mvc.ViewType.XML,
				viewName : code
			});
		});
		that.destroyContent();
		that.addContent(oDetailView);
	},
	/**
	 * 审批清单(所有任务)
	 */
	_initForm: function (key) {
		this.JsonModel().loadData(this,"menu/form?key="+key,
			function (oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data),"form");
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this)
		);
	},
	/**
	 * 审批历史信息
	 */
	_historyTask: function(piid,_oPopover) {
		var oView=this.getView(),
		_oPopover=_oPopover;
		this.JsonModel().loadData(this,
			"bpm/historyTask?piid="+piid,
			function (oData, model) {
				if (oData.statusCode == "000000") {
					$.each(oData.data, function(index, ele) {
						ele.startTime = this.convertDate(ele.startTime);
						ele.claimTime = this.convertDate(ele.claimTime);
						ele.endTime = this.convertDate(ele.endTime);
						if (undefined!=ele.comment) {
							ele.comment="<strong>"+ele.comment+"</strong>";
						}
						if (null!=ele.deleteReason) {
							ele.reasonText="【"+ele.reasonText+"】耗时："+ele.durationInMillis;
						}
					}.bind(this))
					_oPopover.setModel(new JSONModel(oData.data),"tasks");
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), 
			function (oData) {
			}.bind(this)
		);
	},
	
	/**
	 * 返回流程清单
	 */
	navBack : function(oEvent) {
		this.getRouter().getTargets().display("processList", {
			fromTarget : "processInfo",
			data : this._oData.data.type
		});
	},
	/**
	 * 打开审批信息
	 */
	openTaskView : function(oEvent) {
		if (!this._oPopover) {
			this._oPopover = sap.ui.xmlfragment("harry.app.fragment.process.TaskPopover", this);
			this.getView().addDependent(this._oPopover);
			this._oPopover.addStyleClass(this.getOwnerComponent().getPopoverDensityClass());
		}
		this._oPopover.setModel(new JSONModel(this._oData.data),"comment");
		this._historyTask(this._oData.data.instanceId,this._oPopover);
		this._oPopover.openBy(oEvent.getSource());
	},
	/**
	 * 关闭审批信息
	 */
	handleCloseButton: function (oEvent) {
		this._oPopover.close();
	},
	/**
	 * 执行审批
	 */
	onTaskPost: function(oEvent){
		var cData=this._oPopover.getModel("comment").getData(),
		action=1==cData.status?"REJECT":"COMPLETE",
		instanceId=this._oData.data.instanceId,
		taskId=this._oData.data.taskId;
		
		this.JsonModel().loadData(this,
			"bpm/commitTask?piid="+instanceId+"&taskId="+taskId+"&action="+action+"&comment="+cData.value,
			function (oData, model) {
				if (oData.statusCode == "000000") {
					this.handleCloseButton();
					this.navBack();
					//调用表单事件
					this.eventBus.publish(this._oData.data.formKey, "commitForm",{
						instanceId : this._oData.data.instanceId,
						action : action
					});
				} else {
					MessageToast.show(oData.message+"/"+oData.data);
				}
			}.bind(this), 
			function (oData) {
				MessageToast.show(oData.responseText);
			}.bind(this)
		);
	},
	
	
})
});