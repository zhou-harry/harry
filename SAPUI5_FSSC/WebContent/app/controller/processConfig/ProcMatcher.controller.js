sap.ui.define([
		'harry/app/controller/BaseController', 'sap/m/MessageToast', 'sap/m/MessageBox', 'sap/ui/model/json/JSONModel', 'harry/app/format/formatter'
], function(BaseController, MessageToast, MessageBox, JSONModel, formatter) {
	"use strict";
	var ProcMatcher = BaseController.extend("harry.app.controller.processConfig.ProcMatcher", {
		formatter : formatter,
		/**
		 * Called when a controller is instantiated and its View controls (if
		 * available) are already created. Can be used to modify the View before
		 * it is displayed, to bind event handlers and do other one-time
		 * initialization.
		 * 
		 * @memberOf app.controller.processConfig.ProcMatcher
		 */
		onInit : function() {
			this.getRouter().getTarget("procMatcherMaster").attachDisplay(this.handleAttachDisplayMaster, this);
			this.getRouter().getTarget("procMatcherDetail").attachDisplay(this.handleAttachDisplayDetail, this);
		},

		/**
		 * Similar to onAfterRendering, but this hook is invoked before the
		 * controller's View is re-rendered (NOT before the first rendering!
		 * onInit() is used for that one!).
		 * 
		 * @memberOf app.controller.processConfig.ProcMatcher
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
		 * @memberOf app.controller.processConfig.ProcMatcher
		 */
		// onAfterRendering: function() {
		//
		// },
		/**
		 * Called when the Controller is destroyed. Use this one to free
		 * resources and finalize activities.
		 * 
		 * @memberOf app.controller.processConfig.ProcMatcher
		 */
		onExit : function() {
			if (this._oPopover) {
				this._oPopover.destroy();
			}
		},

		/**
		 * AttachDisplay
		 */
		handleAttachDisplayMaster : function(oEvent) {
			var _oData = oEvent.getParameter("data");
			if (undefined != _oData && "procMatcherDetail" == _oData.fromTarget) {
				return;
			}
			this._initForm();
		},
		handleAttachDisplayDetail : function(oEvent) {
			var _oData = oEvent.getParameter("data");
			if (undefined != _oData && "procMatcherMaster" == _oData.fromTarget) {
				this.getView().setModel(new JSONModel(_oData.data), "form");
				this._initFormInfo(_oData.data.key, _oData.data.title);
				this._initMatcher(_oData.data.key);
			}
		},
		/**
		 * 弹出框加载
		 */
		_errorMessageBoxPress : function(oEvent, message) {
			var bCompact = !!this.getView().$().closest(".sapUiSizeCompact").length;
			MessageBox.error(message, {
				styleClass : bCompact ? "sapUiSizeCompact" : ""
			});
		},
		_getMatcherPopover : function() {
			var oView = this.getView();
			var oDialog = oView.byId("matcherEditId");
			if (!oDialog) {
				oDialog = sap.ui.xmlfragment(oView.getId(), "harry.app.fragment.processConfig.MatcherEdit", this);
			}
			oDialog.addStyleClass(this.getOwnerComponent().getPopoverDensityClass());
			return oDialog;
		},
		/**
		 * 初始化数据
		 */
		_initForm : function() {
			this.JsonModel().loadData(this, "menu/forms", function(oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data), "forms");
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), function(oData) {
				if ("timeout" == oData.message) {
					MessageToast.show("访问超时,请检查网络或稍后再试.");
				}
			}.bind(this));
		},
		_initProcdef : function(oDialog) {
			var oDialog = oDialog;
			this.JsonModel().loadData(this, "bpmConfig/findProcdef", function(oData, model) {
				if (oData.statusCode == "000000") {
					oDialog.setModel(new JSONModel(oData.data), "pdefs");
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), function(oData) {
				if ("timeout" == oData.message) {
					MessageToast.show("访问超时,请检查网络或稍后再试.");
				}
			}.bind(this));
		},
		_initFormInfo : function(key, name) {
			this.JsonModel().loadData(this, "bpmConfig/findPFormsByKey?key=" + key, function(oData, model) {
				if (oData.statusCode == "000000") {
					if (oData.data) {
						oData.data.formName = name;
					}
					this.getView().setModel(new JSONModel(oData.data), "form");
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), function(oData) {
				if ("timeout" == oData.message) {
					MessageToast.show("访问超时,请检查网络或稍后再试.");
				}
			}.bind(this));
		},
		_initMatcher : function(fkey) {
			var fkey = fkey;
			this.JsonModel().loadData(this, "bpmConfig/findFMatcher?fKey=" + fkey, function(oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data), "matchers");
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), function(oData) {
				if ("timeout" == oData.message) {
					MessageToast.show("访问超时,请检查网络或稍后再试.");
				}
			}.bind(this));
		},
		_initDimension : function(type, oDialog) {
			var type = type, oDialog = oDialog;
			this.JsonModel().loadData(this, "bpmConfig/findProcDms?procType=" + type, function(oData, model) {
				if (oData.statusCode == "000000") {
					oDialog.setModel(new JSONModel(oData.data), "pdms");
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), function(oData) {
				if ("timeout" == oData.message) {
					MessageToast.show("访问超时,请检查网络或稍后再试.");
				}
			}.bind(this), null, false);
		},
		/**
		 * 事件
		 */
		handleFormPress : function(oEvent) {
			var cData = this.currentData(oEvent, "forms");

			this.getRouter().getTargets().display("procMatcherDetail", {
				fromTarget : "procMatcherMaster",
				data : cData
			});
		},
		navBack : function(oEvent) {
			this.getRouter().getTargets().display("procMatcherMaster", {
				fromTarget : "procMatcherDetail"
			});
		},
		handleMatcherDelete : function(oEvent) {
			var oList = oEvent.getSource(), oItem = oEvent.getParameter("listItem"), context = oItem.getBindingContext("matchers");

			var oData = context.getModel().getProperty(context.getPath());
			var dData = {
				formKey : oData.FORM_KEY,
				procKey : oData.PROC_KEY
			}
			oList.setBusy(true);
			this.JsonModel().loadData(this, "bpmConfig/deleteFMatcher", function(oData, model) {
				this._initMatcher(dData.formKey);
				oList.setBusy(false);
			}.bind(this), function(oData) {
				MessageToast.show("删除失败!");
				oList.setBusy(false);
			}.bind(this), new JSONModel(dData).getJSON());
		},
		handleMatcherAdd : function(oEvent) {
			var fData = this.getView().getModel("form").getData();
			var type = fData.procType;
			if (null == type) {
				MessageToast.show("未关联流程类型!");
				return;
			}
			var dkey = fData.deploymentKey;
			if (null == dkey) {
				MessageToast.show("未挂载流程模板!");
				return;
			}
			var oDialog = this._getMatcherPopover();

			this._initDimension(type, oDialog);
			this._initProcdef(oDialog);
			var oData = oDialog.getModel("pdms").getData();
			if (oData.length > 0) {
				this.getView().addDependent(oDialog);
				oDialog.open();
			} else {
				MessageToast.show("当前表单下未配置维度!");
			}
		},
		confirmMatcherSave : function(oEvent) {
			var oView = this.getView();
			var oDialog = this._getMatcherPopover();
			var oData = oDialog.getModel("pdms").getData();
			var fData = this.getView().getModel("form").getData();
			if (!oData.procKey) {
				this._errorMessageBoxPress(oEvent, "请选择流程定义!");
				return;
			}
			// 准备数据
			oView.setModel(new JSONModel(new Array()), "input");
			var iModel = oView.getModel("input");
			var iEntries = iModel.getData();
			$.each(oData, function(index, ele) {
				iEntries.push({
					formKey : fData.key,
					procKey : oData.procKey,
					dmsKey : ele.key,
					matcher : ele.value
				});
			}.bind(this))
			// 保存
			this.JsonModel().loadData(this, "bpmConfig/saveFMatcher", function(oData, model) {
				this._initMatcher(fData.key);
				oDialog.close();
			}.bind(this), function(oData) {
				MessageToast.show("保存失败!");
			}.bind(this), iModel.getJSON());
		},
		cancelMatcherSave : function() {
			this._getMatcherPopover().destroy();
		},
	})
	return ProcMatcher;
});