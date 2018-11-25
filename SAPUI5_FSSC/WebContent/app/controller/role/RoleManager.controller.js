sap.ui.define([
		"harry/app/controller/BaseController", "sap/ui/model/json/JSONModel", "sap/ui/commons/MessageBox", "sap/m/MessageToast", "sap/ui/Device", 'sap/ui/model/Sorter',
		'harry/app/format/formatter'
], function(Controller, JSONModel, MessageBox, MessageToast, Device, Sorter, formatter) {
	"use strict";
	var RoleManager = Controller.extend("harry.app.controller.role.RoleManager", {

		/**
		 * Called when a controller is instantiated and its View controls (if
		 * available) are already created. Can be used to modify the View before
		 * it is displayed, to bind event handlers and do other one-time
		 * initialization.
		 * 
		 * @memberOf app.controller.role.RoleManager
		 */
		onInit : function() {
			this.getRouter().getTarget("roleManager").attachDisplay(this.handleAttachDisplay, this);
		},

		/**
		 * Similar to onAfterRendering, but this hook is invoked before the
		 * controller's View is re-rendered (NOT before the first rendering!
		 * onInit() is used for that one!).
		 * 
		 * @memberOf app.controller.role.RoleManager
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
		 * @memberOf app.controller.role.RoleManager
		 */
		// onAfterRendering: function() {
		//
		// },
		/**
		 * Called when the Controller is destroyed. Use this one to free
		 * resources and finalize activities.
		 * 
		 * @memberOf app.controller.role.RoleManager
		 */
		// onExit: function() {
		//
		// }，
		/**
		 * AttachDisplay
		 */
		handleAttachDisplay : function(oEvent) {
			this._oData = oEvent.getParameter("data");
			this.getView().setModel(new JSONModel({
				height : $(window).height()
			}), "v");
			this.initList();
		},
		/**
		 * 重置菜单树
		 */
		refreshTree : function(oEvent) {
			var oView = this.getView();

			oView.byId("tree").destroyItems();

			var tModel = oView.getModel("tree");
			if (tModel) {
				tModel.destroy();
			}
			var cModel = oView.getModel("cr");
			if (cModel) {
				cModel.destroy();
			}
		},

		initList : function(oEvent) {
			var oView = this.getView();
			oView.byId("role").setBusy(true);

			this.JsonModel().loadData(this, "role/list", function(oData, model) {
				if (oData.statusCode == "000000") {
					$.each(oData.data, function(index, ele) {
						ele.created = this.convertDate(ele.created);
					}.bind(this))
					this.getView().setModel(new JSONModel(oData.data), "roles");
					oView.byId("role").setBusy(false);
					// 重置菜单树
					this.refreshTree();
				}
			}.bind(this), function(oData) {
				MessageToast.show("系统异常!");
				oView.byId("role").setBusy(false);
			}.bind(this));
		},
		onRoleAdd : function(oEvent) {
			var oView = this.getView();
			var oDialog = oView.byId("roleEditorId");
			if (!oDialog) {
				oDialog = sap.ui.xmlfragment(oView.getId(), "harry.app.fragment.role.RoleEditor", this);
				oDialog.setModel(new JSONModel({
					isSys : false,
					status : true
				}), "role");
				oView.addDependent(oDialog);
			}
			oDialog.open();
		},
		cancelRoleEditor : function(oEvent) {
			this.getView().byId("roleEditorId").destroy();
		},
		confirmRoleEditor : function(oEvent) {
			var oView = this.getView();
			var oModel = oView.getModel("roles");
			var oDialog = oView.byId("roleEditorId");

			oView.byId("roleEditorId").setBusy(true);
			this.JsonModel().loadData(this, "role/save", function(oData, model) {
				// oData.data.created = this.convertDate(oData.data.created);
				// oModel.getData().unshift(oData.data);
				// oModel.refresh();
				this.initList();
				oView.byId("roleEditorId").setBusy(false);
				oView.byId("roleEditorId").close();
			}.bind(this), function(oData) {
				MessageToast.show("角色编辑失败!");
				oView.byId("roleEditorId").setBusy(false);
			}.bind(this), oDialog.getModel("role").getJSON());
		},
		onRoleDelete : function(oEvent) {
			var oView = this.getView();
			var oModel = oView.getModel("roles");
			var _data = oModel.getData();

			var context = oEvent.getSource().getBindingContext("roles");
			var sPath = context.getPath();

			var index = sPath.substr(sPath.lastIndexOf("/") + 1);
			oView.byId("role").setBusy(true);
			this.JsonModel().loadData(this, "role/remove?roleid=" + _data[index].roleId, function(oData, model) {
				_data.splice(index, 1);
				oModel.refresh();
				oView.byId("role").setBusy(false);
				// 重置菜单树
				this.refreshTree();
			}.bind(this), function(oData) {
				MessageToast.show("角色删除失败!");
				oView.byId("role").setBusy(false);
			}.bind(this));
		},
		onRoleEdit : function(oEvent) {
			var oView = this.getView();
			var oModel = oView.getModel("roles");
			var _data = oModel.getData();

			var context = oEvent.getSource().getBindingContext("roles");
			var sPath = context.getPath();

			var oDialog = oView.byId("roleEditorId");
			if (!oDialog) {
				oDialog = sap.ui.xmlfragment(oView.getId(), "harry.app.fragment.role.RoleEditor", this);
				oDialog.setModel(new JSONModel(_data[sPath.substr(sPath.lastIndexOf("/") + 1)]), "role");
				oView.addDependent(oDialog);
			}
			oDialog.open();
		},
		onRoleSelected : function(oEvent) {
			var oView = this.getView();

			var context = oEvent.getParameters().rowContext;
			var index = oEvent.getParameters().rowIndex
			var sPath = context.getPath();
			var oModel = context.getModel();
			var cData = oModel.getData()[sPath.substr(sPath.lastIndexOf("/") + 1)]

			var oTree = oView.byId("tree");
			oTree.setBusy(true);
			this.JsonModel().loadData(this, "menu/treeSide?roleId=" + cData.roleId + "&parentKey=SIDE", function(oData, model) {
				oTree.destroyItems();
				oView.setModel(new JSONModel(oData.data), "tree");
				oView.setModel(new JSONModel(cData), "cr");
				oTree.setBusy(false);
			}.bind(this), function(oData) {
				MessageToast.show("菜单加载失败!");
				oTree.setBusy(false);
			}.bind(this));
		},
		onSelectionChange : function(oEvent) {
			var oView = this.getView();
			oView.setModel(new JSONModel(new Array()), "rm");
			var iModel = oView.getModel("rm");
			var iEntries = iModel.getData();

			var item = oEvent.getParameters().listItem;

			var context = item.getItemNodeContext().context;
			var cData = context.getModel().getProperty(context.getPath());

			var oTree = item.getTree();
			var isLeaf = item.isLeaf();
			var selected = oEvent.getParameters().selected;

			// 准备数据
			var rId = oView.getModel("cr").getData().roleId;
			iEntries.push({
				roleId : rId,
				menuId : cData.KEY,
				valid : selected
			});
			if (isLeaf) {
				var pItem = item.getParentNode();
				// 子节点选中，父节点必选中
				if (pItem) {
					context = item.getParentNodeContext().context;
					cData = context.getModel().getProperty(context.getPath());
					if (selected) {
						if (!pItem.getSelected()) {
							pItem.setSelected(selected);
							iEntries.push({
								roleId : rId,
								menuId : cData.KEY,
								valid : cData.VALID
							});
						}
					} else {
						var change = true;
						// 子节点取消选中，判断其兄弟节点是否都已取消选中（是：取消父节点选中）
						var cNode = this.getChildNode(pItem);
						$.each(cNode, function(index, ele) {
							if (ele.getSelected()) {
								change = false;
								return;
							}
						}.bind(this))
						if (change) {
							pItem.setSelected(false);
							iEntries.push({
								roleId : rId,
								menuId : cData.KEY,
								valid : cData.VALID
							});
						}
					}
				}
			} else {
				// 自动展开下级子节点
				oTree.onItemExpanderPressed(item, true);
				// 全选中/取消子节点
				var cItem = this.getChildNode(item);
				$.each(cItem, function(index, ele) {
					ele.setSelected(selected);

					context = ele.getItemNodeContext().context;
					cData = context.getModel().getProperty(context.getPath());
					iEntries.push({
						roleId : rId,
						menuId : cData.KEY,
						valid : selected
					});
				}.bind(this))
			}
			iModel.refresh();
			// 实时保存
			this.onSaveTree(iModel, oTree);

		},
		getChildNode : function(item) {
			if (item.isLeaf()) {
				return;
			}
			var oTree = item.getTree(), iNodeLevel = item.getLevel(), i = oTree.indexOfItem(item) + 1, aItems = oTree.getItems(), iLevel;
			var oChildNode = new Array();
			var c = 0;
			while (i >= 0) {
				iLevel = aItems[i].getLevel();
				if (iLevel === iNodeLevel + 1) {
					oChildNode[c++] = aItems[i++];
				} else {
					break;
				}
			}
			return oChildNode;
		},
		onSaveTree : function(iModel, oTree) {
			var oView = this.getView();
			var that = oTree;

			that.setBusy(true);
			this.JsonModel().loadData(this, "role/saveRM", function(oData, model) {
				MessageToast.show("操作成功!");
				that.setBusy(false);
			}.bind(this), function(oData) {
				MessageToast.show("操作失败!");
				that.setBusy(false);
			}.bind(this), iModel.getJSON());
		}
	})
	return RoleManager;
});