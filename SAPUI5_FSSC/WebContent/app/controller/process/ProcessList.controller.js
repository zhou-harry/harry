sap.ui.define([
		'harry/app/controller/BaseController', 'sap/ui/model/json/JSONModel', 'sap/ui/Device', 'sap/m/MessageToast', 'sap/ui/model/Filter', "sap/ui/model/FilterOperator",
		'harry/app/format/formatter'
], function(BaseController, JSONModel, Device, MessageToast, Filter, FilterOperator, formatter) {
	"use strict";
	var ProcessList = BaseController.extend("harry.app.controller.process.ProcessList", {
		formatter : formatter,
		/**
		 * Called when a controller is instantiated and its View controls (if
		 * available) are already created. Can be used to modify the View before
		 * it is displayed, to bind event handlers and do other one-time
		 * initialization.
		 * 
		 * @memberOf app.controller.process.ProcessList
		 */
		onInit : function() {
			this.getRouter().getTarget("processList").attachDisplay(this.handleAttachDisplay, this);
		},

		/**
		 * Similar to onAfterRendering, but this hook is invoked before the
		 * controller's View is re-rendered (NOT before the first rendering!
		 * onInit() is used for that one!).
		 * 
		 * @memberOf app.controller.process.ProcessList
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
		 * @memberOf app.controller.process.ProcessList
		 */
		// onAfterRendering: function() {
		//
		// },
		/**
		 * Called when the Controller is destroyed. Use this one to free
		 * resources and finalize activities.
		 * 
		 * @memberOf app.controller.process.ProcessList
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
				this.getView().setModel(new JSONModel(this._oData.data), "type");
				this._initTaskProcess(this._oData.data.type);
				this._initTaskHistory(this._oData.data.type);
				this._initApplyHistory(this._oData.data.type);
			}
		},
		/**
		 * 审批清单
		 */
		_initTaskProcess : function(type) {
			var oView = this.getView();
			var list = oView.byId("processList");
			list.setBusy(true)
			this.JsonModel().loadData(this, "bpm/findTaskProcess?type=" + type, function(oData, model) {
				if (oData.statusCode == "000000") {
					$.each(oData.data, function(index, ele) {
						ele.startTime = this.convertDate(ele.startTime);
						ele.taskCreateTime = this.convertDate(ele.taskCreateTime);
					}.bind(this))
					this.getView().setModel(new JSONModel(oData.data), "process");
				} else {
					MessageToast.show(oData.message);
				}
				list.setBusy(false);
			}.bind(this), function(oData) {
				list.setBusy(false);
			}.bind(this));
		},
		/**
		 * 我的申请
		 */
		_initApplyHistory : function(type) {
			var oView = this.getView();
			var list = oView.byId("applyList");
			list.setBusy(true)
			this.JsonModel().loadData(this, "bpm/findStarterProcess?type=" + type, function(oData, model) {
				if (oData.statusCode == "000000") {
					$.each(oData.data, function(index, ele) {
						ele.startTime = this.convertDate(ele.startTime);
						ele.taskCreateTime = this.convertDate(ele.taskCreateTime);

						$.each(ele.tasks, function(index, t) {
							t.endTime = this.convertDate(t.endTime);
						}.bind(this))
					}.bind(this))
					this.getView().setModel(new JSONModel(oData.data), "apply");
				} else {
					MessageToast.show(oData.message);
				}
				list.setBusy(false);
			}.bind(this), function(oData) {
				list.setBusy(false);
			}.bind(this));
		},
		/**
		 * 我的已办
		 */
		_initTaskHistory : function(type) {
			var oView = this.getView();
			var list = oView.byId("historyList");
			list.setBusy(true)
			this.JsonModel().loadData(this, "bpm/findHistoryProcess?type=" + type, function(oData, model) {
				if (oData.statusCode == "000000") {
					$.each(oData.data, function(index, ele) {
						ele.startTime = this.convertDate(ele.startTime);
						ele.taskCreateTime = this.convertDate(ele.taskCreateTime);

						$.each(ele.tasks, function(index, t) {
							t.endTime = this.convertDate(t.endTime);
						}.bind(this))
					}.bind(this))
					this.getView().setModel(new JSONModel(oData.data), "history");
				} else {
					MessageToast.show(oData.message);
				}
				list.setBusy(false);
			}.bind(this), function(oData) {
				list.setBusy(false);
			}.bind(this));
		},
		/**
		 * 任务历史
		 */
		_historyTask : function(piid, notificationGroup) {
			var oView = this.getView();
			this.JsonModel().loadData(this, "bpm/historyTask?piid=" + piid, function(oData, model) {
				if (oData.statusCode == "000000") {
					$.each(oData.data, function(index, ele) {
						ele.startTime = this.convertDate(ele.startTime);
						ele.claimTime = this.convertDate(ele.claimTime);
						ele.endTime = this.convertDate(ele.endTime);
						if (null != ele.deleteReason) {
							ele.reasonText = "【" + ele.reasonText + "】耗时：" + ele.durationInMillis;
						}
						// 懒加载审批节点信息
						notificationGroup.addItem(new sap.m.NotificationListItem({
							title : ele.name + ele.description + " ▪ " + ele.reasonText,
							description : ele.comment,
							authorName : "审批人：" + ele.assignee,
							datetime : ele.endTime,
							authorPicture : ele.preview,
							priority : formatter.taskPriority(ele.deleteReason),
							showCloseButton : false,
							unread : true,
						}));
					}.bind(this))
					oView.setModel(new JSONModel(oData.data), "tasks");
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), function(oData) {
			}.bind(this));
		},
		/**
		 * search filter
		 */
		_filter : function(listId, oEvent) {
			// add filter for search
			var _oGlobalFilter;
			var sQuery = oEvent.getSource().getValue();
			if (sQuery && sQuery.length > 0) {
				_oGlobalFilter = new Filter([
						new Filter("businessKey", FilterOperator.Contains, sQuery), new Filter("description", FilterOperator.Contains, sQuery),
						new Filter("startUserId", FilterOperator.Contains, sQuery), new Filter("processName", FilterOperator.Contains, sQuery),
						new Filter("instanceId", FilterOperator.Contains, sQuery),
				], false);
			}
			// update list binding
			var list = this.byId(listId);
			var obinding = list.getBinding();
			var binding = list.getBinding("items");
			binding.filter(_oGlobalFilter, "Application");
		},
		_showInfo : function(context, active) {
			var oData = context.getModel().getProperty(context.getPath());
			var formKey = oData.formKey;
			if (undefined == formKey) {
				MessageToast.show("流程数据缺失.");
				return;
			}
			oData.type = this._oData.data;
			oData.active = active;
			this.getRouter().getTargets().display("processInfo", {
				fromTarget : "processList",
				data : oData
			});
		},
		/**
		 * 事件
		 */
		navBack : function(oEvent) {
			this.getRouter().getTargets().display("home", {
				fromTarget : "processList"
			});
		},
		onProcessPress : function(oEvent) {
			var oList = oEvent.getSource(), context = oList.getBindingContext("process");
			// 查看明细
			this._showInfo(context, true);
		},
		onHistoryPress : function(oEvent) {
			var oList = oEvent.getSource(), context = oList.getBindingContext("history");

			// 查看明细
			this._showInfo(context, false);
		},
		onApplyPress : function(oEvent) {
			var oList = oEvent.getSource(), context = oList.getBindingContext("apply");

			// 查看明细
			this._showInfo(context, false);
		},
		handleSelect : function(oEvent) {
			var sKey = oEvent.getParameter("key");
			if ("instance" == sKey) {
				this._initTaskProcess(this._oData.data.type);
			} else if ("history" == sKey) {
				this._initTaskHistory(this._oData.data.type);
			} else if ("apply" == sKey) {
				this._initApplyHistory(this._oData.data.type);
			}
		},
		loadTasks : function(oEvent) {
			/** @type {sap.m.NotificationListGroup} */
			var notificationGroup = oEvent.getSource();
			/** @type [sap.m.NotificationListItem] */
			var notifications = notificationGroup.getItems();

			var collapsed = oEvent.getParameters().collapsed;
			if (collapsed) {
				notificationGroup.destroyItems();
			} else {
				var oList = oEvent.getSource(), context = oList.getBindingContext("history");

				var oData = context.getModel().getProperty(context.getPath());
				this._historyTask(oData.instanceId, notificationGroup);
			}
		},
		onHistorySearch : function(oEvent) {
			this._filter("historyList", oEvent);
		},
		onApplySearch : function(oEvent) {
			this._filter("applyList", oEvent);
		},

	})
	return ProcessList;
});