sap.ui.define([
		'harry/app/controller/BaseController', 'sap/m/MessageToast', 'sap/ui/model/json/JSONModel', 'harry/app/format/formatter'
], function(BaseController, MessageToast, JSONModel, formatter) {
	"use strict";
	var ProcType = BaseController.extend("harry.app.controller.processConfig.ProcType", {
		formatter : formatter,
		/**
		 * Called when a controller is instantiated and its View controls (if
		 * available) are already created. Can be used to modify the View before
		 * it is displayed, to bind event handlers and do other one-time
		 * initialization.
		 * 
		 * @memberOf app.controller.processConfig.ProcType
		 */
		onInit : function() {
			this.getRouter().getTarget("procTypeMaster").attachDisplay(this.handleAttachDisplayMaster, this);
			this.getRouter().getTarget("procTypeDetail").attachDisplay(this.handleAttachDisplayDetail, this);
		},

		/**
		 * Similar to onAfterRendering, but this hook is invoked before the
		 * controller's View is re-rendered (NOT before the first rendering!
		 * onInit() is used for that one!).
		 * 
		 * @memberOf app.controller.processConfig.ProcType
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
		 * @memberOf app.controller.processConfig.ProcType
		 */
		// onAfterRendering: function() {
		//
		// },
		/**
		 * Called when the Controller is destroyed. Use this one to free
		 * resources and finalize activities.
		 * 
		 * @memberOf app.controller.processConfig.ProcType
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
			if (undefined != _oData && "procTypeDetail" == _oData.fromTarget) {
				return;
			}
			this._initTypeTree();
		},
		handleAttachDisplayDetail : function(oEvent) {
			var _oData = oEvent.getParameter("data");
			this.getView().setModel(new JSONModel(_oData.data), "type");

			this._initDimension(_oData.data.type);
			this._initForm(_oData.data.form);
			this._initMatcher(_oData.data.type);
		},
		/**
		 * 加载弹出框
		 */
		_getResponsivePopover : function() {
			if (!this._oPopover) {
				this._oPopover = sap.ui.xmlfragment("harry.app.fragment.processConfig.DeploymentLastVersion", this);
			}
			oDialog.addStyleClass(this.getOwnerComponent().getPopoverDensityClass());
			return this._oPopover;
		},
		_getDimensionPropover : function() {
			var oView = this.getView();
			var oDialog = oView.byId("dimensionsId");
			if (!oDialog) {
				oDialog = sap.ui.xmlfragment(oView.getId(), "harry.app.fragment.processConfig.DimensionList", this);
				oDialog.addStyleClass(this.getOwnerComponent().getPopoverDensityClass());
			}
			return oDialog;
		},
		_getFormPropover : function() {
			var oView = this.getView();
			var oDialog = oView.byId("formId");
			if (!oDialog) {
				oDialog = sap.ui.xmlfragment(oView.getId(), "harry.app.fragment.processConfig.FormEdit", this);
				oDialog.addStyleClass(this.getOwnerComponent().getPopoverDensityClass());
			}
			return oDialog;
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
		_initTypeTree : function() {
			this.JsonModel().loadData(this, "bpm/procTypeTree", function(oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data), "tree");
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), function(oData) {
				if ("timeout" == oData.message) {
					MessageToast.show("访问超时,请检查网络或稍后再试.");
				}
			}.bind(this));
		},
		_initDimension : function(type, oDialog, sync) {
			var obj = this.getView();
			if (undefined != oDialog) {
				obj = oDialog;
			}
			if (undefined == sync) {
				sync = true;
			}
			this.JsonModel().loadData(this, "bpmConfig/findProcDms?procType=" + type, function(oData, model) {
				if (oData.statusCode == "000000") {
					obj.setModel(new JSONModel(oData.data), "pdms");
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), function(oData) {
				if ("timeout" == oData.message) {
					MessageToast.show("访问超时,请检查网络或稍后再试.");
				}
			}.bind(this), null, sync);
		},
		_initFormSection : function(oDialog) {
			var oDialog = oDialog;
			this.JsonModel().loadData(this, "menu/forms", function(oData, model) {
				if (oData.statusCode == "000000") {
					oDialog.setModel(new JSONModel(oData.data), "fSection");
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), function(oData) {
				if ("timeout" == oData.message) {
					MessageToast.show("访问超时,请检查网络或稍后再试.");
				}
			}.bind(this));
		},
		_initForm : function(key) {
			this.JsonModel().loadData(this, "menu/form?key=" + key, function(oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data), "f");
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), function(oData) {
				if ("timeout" == oData.message) {
					MessageToast.show("访问超时,请检查网络或稍后再试.");
				}
			}.bind(this));
		},
		_initMatcher : function(type) {
			this.JsonModel().loadData(this, "bpmConfig/findPMatcher?type=" + type, function(oData, model) {
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
		/**
		 * 事件
		 */
		onItemPress : function(oEvent) {
			var item = oEvent.getParameters().listItem;
			var context = item.getItemNodeContext().context;
			var cData = context.getModel().getProperty(context.getPath());

			this.getRouter().getTargets().display("procTypeDetail", {
				fromTarget : "procTypeMaster",
				data : cData
			});
		},
		handleEditForm : function(oEvent) {
			var oView = this.getView();
			var oDialog = this._getFormPropover();

			this._initFormSection(oDialog);
			oDialog.setModel(oView.getModel("type"), "type");
			oView.addDependent(oDialog);
			oDialog.open();

		},
		onChangeSection : function(oEvent) {
			var section = oEvent.getParameters().section;
			var id = section.getId();
			var key = id.substr(id.lastIndexOf("--") + 2);
			var type = this.getView().getModel("type").getData().type;
			if ("dimension" === key) {
				this._initDimension(type);
			} else {
				this._initMatcher(type);
			}
		},
		handleAddPdms : function(oEvent) {
			var oView = this.getView();
			this.JsonModel().loadData(this, "bpmConfig/findDimension", function(oData, model) {
				if (oData.statusCode == "000000") {
					var oDialog = this._getDimensionPropover();
					oDialog.setModel(new JSONModel(oData.data), "dimensions");
					oView.addDependent(oDialog);
					oDialog.open();
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), function(oData) {
				if ("timeout" == oData.message) {
					MessageToast.show("访问超时,请检查网络或稍后再试.");
				}
			}.bind(this));
		},
		confirmPdmsSave : function() {
			var oView = this.getView();
			var oDialog = this._getDimensionPropover();
			var olist = this.byId("dimensionListId");
			var aContexts = olist.getSelectedContexts(true);

			// 准备数据
			oView.setModel(new JSONModel(new Array()), "npdms");
			var iModel = oView.getModel("npdms");
			var iEntries = iModel.getData();
			var type = this.getView().getModel("type").getData().type;
			$.each(aContexts, function(index, context) {
				var oData = context.getModel().getProperty(context.getPath());
				iEntries.push({
					typeName : type,
					dmsDesc : oData.key
				});
			}.bind(this))
			// 保存
			this.JsonModel().loadData(this, "bpmConfig/saveProcDms", function(oData, model) {
				this._initDimension(type);
				oDialog.close();
			}.bind(this), function(oData) {
				MessageToast.show("保存失败!");
			}.bind(this), iModel.getJSON());
		},
		cancelPdmsSave : function() {
			this._getDimensionPropover().destroy();
		},
		handleDeleteDms : function(oEvent) {
			var oList = oEvent.getSource(), oItem = oEvent.getParameter("listItem"), context = oItem.getBindingContext("pdms");

			var oData = context.getModel().getProperty(context.getPath());
			var type = this.getView().getModel("type").getData().type;
			var dData = {
				typeName : type,
				dmsDesc : oData.key
			}
			oList.setBusy(true);
			this.JsonModel().loadData(this, "bpmConfig/deleteProcDms", function(oData, model) {
				this._initDimension(type);
				oList.setBusy(false);
			}.bind(this), function(oData) {
				MessageToast.show("删除失败!");
				oList.setBusy(false);
			}.bind(this), new JSONModel(dData).getJSON());
		},
		confirmFormSave : function() {
			var oDialog = this._getFormPropover();
			var oModel = oDialog.getModel("type");
			var form = oModel.getData().form;
			// 保存
			this.JsonModel().loadData(this, "bpmConfig/saveProctype", function(oData, model) {
				this._initForm(form);
				oDialog.close();
			}.bind(this), function(oData) {
				MessageToast.show("保存失败!");
			}.bind(this), oModel.getJSON());
		},
		cancelFormSave : function() {
			this._getFormPropover().destroy();
		},
		handleMatcherDelete : function(oEvent) {
			var oList = oEvent.getSource(), oItem = oEvent.getParameter("listItem"), context = oItem.getBindingContext("matchers");

			var oData = context.getModel().getProperty(context.getPath());
			var dData = {
				procType : oData.PROC_TYPE,
				procKey : oData.PROC_KEY
			}
			oList.setBusy(true);
			this.JsonModel().loadData(this, "bpmConfig/deletePMatcher", function(oData, model) {
				this._initMatcher(dData.procType);
				oList.setBusy(false);
			}.bind(this), function(oData) {
				MessageToast.show("删除失败!");
				oList.setBusy(false);
			}.bind(this), new JSONModel(dData).getJSON());
		},
		handleMatcherAdd : function(oEvent) {
			var tData = this.getView().getModel("type").getData();
			var type = tData.type;
			if (null == type) {
				MessageToast.show("流程类型无效!");
				return;
			}

			var oDialog = this._getMatcherPopover();

			this._initDimension(type, oDialog, false);
			this._initProcdef(oDialog);
			var oData = oDialog.getModel("pdms").getData();
			if (oData.length > 0) {
				this.getView().addDependent(oDialog);
				oDialog.open();
			} else {
				MessageToast.show("当前类型下未配置维度!");
			}
		},
		confirmMatcherSave : function(oEvent) {
			var oView = this.getView();
			var oDialog = this._getMatcherPopover();
			var oData = oDialog.getModel("pdms").getData();
			var tData = this.getView().getModel("type").getData();
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
					procType : tData.type,
					procKey : oData.procKey,
					dmsKey : ele.key,
					matcher : ele.value
				});
			}.bind(this))
			// 保存
			this.JsonModel().loadData(this, "bpmConfig/savePMatcher", function(oData, model) {
				this._initMatcher(tData.type);
				oDialog.close();
			}.bind(this), function(oData) {
				MessageToast.show("保存失败!");
			}.bind(this), iModel.getJSON());
		},
		cancelMatcherSave : function() {
			this._getMatcherPopover().destroy();
		},
		navBack : function(oEvent) {
			this.getRouter().getTargets().display("procTypeMaster", {
				fromTarget : "procTypeDetail",
				data : null
			});
		},
	})
	return ProcType;
});