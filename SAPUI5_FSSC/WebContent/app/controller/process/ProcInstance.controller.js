sap.ui.define([ 
	'harry/app/controller/BaseController', 
	'jquery.sap.global',
	'sap/ui/model/json/JSONModel',
	"sap/m/MessageToast",
	"sap/m/SplitContainer",
	"sap/ui/Device",
	"sap/m/UploadCollectionParameter",
	'harry/app/format/formatter'
], function(BaseController, jQuery,JSONModel,MessageToast,SplitContainer, Device,UploadCollectionParameter,formatter) {
	"use strict";
	return BaseController.extend("harry.app.controller.process.ProcInstance", {
		formatter: formatter,
		/**
		* Called when a controller is instantiated and its View controls (if available) are already created.
		* Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
		* @memberOf app.view.process.ProcInstanceManager
		*/
		onInit: function() {
			this.getView().setModel(new JSONModel(Device), "device");
			this.bus = sap.ui.getCore().getEventBus();
			this.getRouter().getTarget("detailProcess").attachDisplay(this.handleAttachDisplay, this);
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
		},
		/**
		 * AttachDisplay
		 */
		handleAttachDisplay : function(oEvent) {
			var _oData = oEvent.getParameter("data");
			var _procKey=null;
			if (_oData&&_oData.data) {
				this.getView().setModel(new JSONModel({visible:true,height:$(window).height()}),"v");
				_oData.data.uploadUrl="../../../fssc/bpm/uploadAttachment";
				this.getView().setModel(new JSONModel(_oData.data),"inst");
				this.initProc("ProcVariable");
				this.initProc("ProcHistory");
				this.initProc("ProcAttachment");
				this.initProc("ProcDiagram");
			}else {
				this.getView().setModel(new JSONModel({visible:false,height:$(window).height()}),"v");
			}
		},
		/**
		 * 选择流程实例
		 */
		handleSelect: function(oEvent) {
			this._currentKey=oEvent.getParameter("key");
			this.initProc(this._currentKey);
		},
		/**
		 * 流程实例Tab选项查看
		 */
		initProc: function(sKey) {
			var oData=this.getView().getModel("inst").getData();
			if ("ProcVariable"===sKey) {
				this._procVariables(oData.instanceId);
			}else if ("ProcHistory"===sKey) {
				this._procHistory(oData.instanceId);
			}else if ("ProcAttachment"===sKey) {
				this._procAttachment(oData.instanceId);
			}else if ("ProcDiagram"===sKey) {
				this._procDiagram(oData.instanceId,oData.deploymentId);
			}
		},
		/**
		 *  变量视图
		 */
		_procVariables: function(piid) {
			var oView=this.getView();
			oView.byId("procVariable").setBusy(true);
			this.JsonModel().loadData(this,
				"bpm/procVariables?piid="+piid,
				function (oData, model) {
					if (oData.statusCode == "000000") {
						this.getView().setModel(new JSONModel(oData.data),"vars");
					} else {
						MessageToast.show(oData.message);
					}
					oView.byId("procVariable").setBusy(false);
				}.bind(this), 
				function (oData) {
					MessageToast.show("流程变量查询失败!");
					oView.byId("procVariable").setBusy(false);
				}.bind(this)
			);
		},
		/**
		 * 流程图视图
		 */
		_procDiagram: function(piid,deploymentId) {
			var oView=this.getView();
			oView.byId("diagid").setBusy(true);
			this.JsonModel().loadData(this,
				"bpm/findCoording?piid="+piid,
				function (oData, model) {
					this.getView().setModel(new JSONModel({
						src: this.JsonModel().getPath()+"bpm/deployDiagram?deployId="+deploymentId,
						div: oData.data
					}),"diagData");
					this.getView().byId("diagid")._onReset();
					oView.byId("diagid").setBusy(false);
				}.bind(this), 
				function (oData) {
					MessageToast.show("流程图查询失败!");
					oView.byId("diagid").setBusy(false);
				}.bind(this)
			);
		},
		/**
		 * 审批历史视图
		 */
		_procHistory: function(piid) {
			var oView=this.getView();
			oView.byId("procHistory").setBusy(true);
			this.JsonModel().loadData(this,
				"bpm/historyTask?piid="+piid,
				function (oData, model) {
					if (oData.statusCode == "000000") {
						$.each(oData.data, function(index, ele) {
							ele.startTime = this.convertDate(ele.startTime);
							ele.claimTime = this.convertDate(ele.claimTime);
							ele.endTime = this.convertDate(ele.endTime);
							ele.duration = ele.durationInMillis/1000/60;
						}.bind(this))
						this.getView().setModel(new JSONModel(oData.data),"hist");
					} else {
						MessageToast.show(oData.message);
					}
					oView.byId("procHistory").setBusy(false);
				}.bind(this), 
				function (oData) {
					MessageToast.show("审批历史查询失败!");
					oView.byId("procHistory").setBusy(false);
				}.bind(this)
			);
		},
		/**
		 * 附件视图
		 */
		_procAttachment: function(piid) {
			var oView=this.getView();
			oView.byId("procAttachmentId").setBusy(true);
			this.JsonModel().loadData(this,
				"bpm/attachments?piid="+piid,
				function (oData, model) {
					$.each(oData.data, function(index, ele) {
						ele.time = this.convertDate(ele.time);
					}.bind(this))
					this.getView().setModel(new JSONModel(oData.data),"attachments");
					oView.byId("procAttachmentId").setBusy(false);
				}.bind(this), 
				function (oData) {
					MessageToast.show("流程附件查询失败!");
					oView.byId("procAttachmentId").setBusy(false);
				}.bind(this)
			);
		},
		/**
		 * 流程图中单击事件
		 */
		onPressDiagram :function(oEvent) {
			var oView=this.getView();
			var key=oEvent.getParameter('key');
			var _diagramDialog = oView.byId("diagramSignal");
			var piid=this.getView().getModel("inst").getData().instanceId;
			this.JsonModel().loadData(this,
				"bpm/findExecution?executionId="+key,
				function (oData, model) {
					if (oData.statusCode == "000000") {
						if (!_diagramDialog) {
							_diagramDialog = sap.ui.xmlfragment(oView.getId(), 
									"harry.app.fragment.process.DiagramDialog",this);
							oData.data.createTime = this.convertDate(oData.data.createTime);
							_diagramDialog.setModel(new JSONModel(oData.data), "acti");
							oView.addDependent(_diagramDialog);
						}
						_diagramDialog.open();
					} else {
						MessageToast.show(oData.message);
					}
				}.bind(this), 
				function (oData) {
					MessageToast.show("操作失败!");
				}.bind(this)
			);
		},
		/**
		 * 审批任务同意
		 */
		confirmTask: function(oEvent){
			this.confirm(oEvent,"COMPLETE");
		},
		/**
		 * 审批任务拒绝
		 */
		rejectTask: function(oEvent){
			this.confirm(oEvent,"REJECT");
		},
		/**
		 * 执行抄送任务
		 */
		confirmCopyTask: function(oEvent){
			this.confirm(oEvent,"COPY_READ");
		},
		/**
		 * 执行审批任务节点
		 */
		confirm: function(oEvent,action){
			var oDialog = this.getView().byId("diagramSignal");
			oDialog.setBusy(true);
			var iData=this.getView().getModel("inst").getData();
			var vModel=this.getView().getModel("vars");
			var oData=oDialog.getModel("acti").getData();
			this.JsonModel().loadData(this,
				"bpm/completeTask?piid="+iData.instanceId+"&taskId="+oData.id+"&action="+action+"&comment="+oData.comment,
				function (oData, model) {
					if (oData.statusCode == "000000") {
						this.getRouter().getTargets().display("masterProcess", {
							fromTarget : "detailProcess",
							data : iData.instanceId
						});
					} else {
						MessageToast.show(oData.message+"/"+oData.data);
					}
					oDialog.setBusy(false);
					this.cancelSignal();
				}.bind(this), 
				function (oData) {
					MessageToast.show("流程执行失败!");
					oDialog.setBusy(false);
				}.bind(this),
				vModel.getJSON()
			);
		},
		/**
		 * 执行非审批任务节点
		 */
		confirmSignal : function(oEvent){
			var oDialog = this.getView().byId("diagramSignal");
			oDialog.setBusy(true);
			var iData=this.getView().getModel("inst").getData();
			var oData=oDialog.getModel("acti").getData();
			var vModel=this.getView().getModel("vars");
			this.JsonModel().loadData(this,
				"bpm/completeSignal?executionId="+oData.key,
				function (oData, model) {
					if (oData.statusCode == "000000") {
						this._procDiagram(iData.instanceId,iData.deploymentId);
					} else {
						MessageToast.show(oData.message+"/"+oData.data);
					}
					oDialog.setBusy(false);
					this.cancelSignal();
				}.bind(this), 
				function (oData) {
					MessageToast.show("流程执行失败!");
					oDialog.setBusy(false);
				}.bind(this),
				vModel.getJSON()
			);
		},
		cancelSignal: function() {
			this.getView().byId("diagramSignal").destroy();
		},
		/**
		 * 执行任务时添加变量
		 */
		onAddVar: function(oEvent) {
			var oModel=this.getView().getModel("vars");
			// add data
			var aEntries = oModel.getData();
			aEntries.unshift({
				_key : null,
				_value : null
			});
			oModel.setData(aEntries);
		},
		/**
		 * 删除流程变量
		 */
		onDelVar: function(oEvent){
			var oDialog = this.getView().byId("diagramSignal");
			var aData=oDialog.getModel("acti").getData();
			var oModel=this.getView().getModel("vars");
			var sPath = oEvent.getSource().getBindingContext("vars").getPath();
			var _data = oModel.getData();
			var index = sPath.substr(sPath.lastIndexOf("/")+1);
			oDialog.setBusy(true);
			this.JsonModel().loadData(this,
				"bpm/removeVariable?taskId="+aData.id+"&variableName="+_data[index]._key,
				function (oData, model) {
					if (oData.statusCode == "000000") {
						_data.splice(index,1);
						oModel.setData(_data);
						MessageToast.show(oData.data.message);
					} else {
						MessageToast.show(oData.message);
					}
					oDialog.setBusy(false);
				}.bind(this), 
				function (oData) {
					MessageToast.show("指定受理人失败!");
					oDialog.setBusy(false);
				}.bind(this)
			);
		},
		/**
		 * 跳转指定页面
		 */
		handleNav: function(oEvent) {
			var navCon = this.getView().byId("navCon");
			var target = oEvent.getSource().data("target");
			if (target) {
				navCon.to(this.getView().byId(target), "slide");
			} else {
				navCon.back();
			}
		},
		/**
		 * 指定受理人
		 */
		handleAssignee: function(oEvent) {
			var oDialog = this.getView().byId("diagramSignal");
			var oModel= oDialog.getModel("acti");
			var _data=oModel.getData();
			oDialog.setBusy(true);
			
			var navCon = this.getView().byId("navCon");
			var target = oEvent.getSource().data("target");
			this.JsonModel().loadData(this,
				"bpm/assigneeTask?taskId="+_data.id+"&userId="+_data.newAssignee,
				function (oData, model) {
					if (oData.statusCode == "000000") {
						_data.assignee=_data.newAssignee;
						oModel.setData(_data);
						navCon.to(this.getView().byId(target));
						MessageToast.show("指定受理人成功!");
					} else {
						MessageToast.show(oData.message);
					}
					oDialog.setBusy(false);
				}.bind(this), 
				function (oData) {
					MessageToast.show("指定受理人失败!");
					oDialog.setBusy(false);
				}.bind(this)
			);
		},
		/**
		 * 指定所有权人
		 */
		handleOwner: function(oEvent) {
			var oDialog = this.getView().byId("diagramSignal");
			var oModel= oDialog.getModel("acti");
			var _data=oModel.getData();
			oDialog.setBusy(true);
			
			var navCon = this.getView().byId("navCon");
			var target = oEvent.getSource().data("target");
			_data.pending=(undefined===_data.pending?false:_data.pending)
			this.JsonModel().loadData(this,
				"bpm/ownerTask?taskId="+_data.id+"&userId="+_data.newOwner+"&pending="+_data.pending,
				function (oData, model) {
					if (oData.statusCode == "000000") {
						_data.owner=_data.newOwner;
						oModel.setData(_data);
						navCon.to(this.getView().byId(target));
						MessageToast.show("成功指定所有权人!");
					} else {
						MessageToast.show(oData.message);
					}
					oDialog.setBusy(false);
				}.bind(this), 
				function (oData) {
					MessageToast.show("指定所有权人失败!");
					oDialog.setBusy(false);
				}.bind(this)
			);
		},
		/**
		 * 显示/隐藏主页面
		 */
		handleMasterPress: function (oEvent) {
			this.bus.publish("flexible", "setDetailPage");
		},
		//附件======================================================================
		onChange: function(oEvent) {
			var oUploadCollection = oEvent.getSource();
			// Header Token
			var oCustomerHeaderToken = new UploadCollectionParameter({
				name: "x-csrf-token",
				value: "securityTokenFromModel"
			});
			oUploadCollection.addHeaderParameter(oCustomerHeaderToken);
		},
		/**
		 * 删除附件
		 */
		onFileDeleted: function(oEvent) {
			var _data=this.getView().getModel("attachments").getData();
			var sPath=oEvent.getSource().sDeletedItemId;
			var index = sPath.substr(sPath.lastIndexOf("-")+1);
			
			this.JsonModel().loadData(this,
				"bpm/attachmentDel?attachmentId="+oEvent.getParameter("documentId"),
				function (oData, model) {
					if (oData.statusCode == "000000") {
						_data.splice(index,1);
						this.getView().setModel(new JSONModel(_data),"attachments");
					} else {
						MessageToast.show(oData.message);
					}
				}.bind(this), 
				function (oData) {
					MessageToast.show("附件删除失败!");
					oDialog.setBusy(false);
				}.bind(this)
			);
		},

		onFilenameLengthExceed: function(oEvent) {
			var max=oEvent.getSource().getMaximumFilenameLength();
			MessageToast.show("文件名超长,请修改文件名称在"+max+"字符以内.");
		},

		onFileSizeExceed: function(oEvent) {
			var max=oEvent.getSource().getMaximumFileSize();
			MessageToast.show("请上传不超过（"+max+"M）的文件.");
		},

		onTypeMissmatch: function(oEvent) {
			MessageToast.show("Event typeMissmatch triggered");
		},
		
		onSelectChange: function(oEvent) {
			var oUploadCollection = this.getView().byId("procAttachmentId");
			oUploadCollection.setShowSeparators(oEvent.getParameters().selectedItem.getProperty("key"));
			MessageToast.show("Event selectChange triggered");
		},

		onStartUpload: function(oEvent) {
			var oUploadCollection = this.getView().byId("procAttachmentId");
			var oNotes = this.getView().byId("notesId");
			var cFiles = oUploadCollection.getItems().length;
			if (cFiles > 0) {
				oUploadCollection.upload();
				oNotes.setValue("");
			}else {
				MessageToast.show("请添加附件!");
			}
		},
		//Header Setting
		onBeforeUploadStarts: function(oEvent) {
			var oNotes = this.getView().byId("notesId");
			var iData=this.getView().getModel("inst").getData();
			// Slug
			oEvent.getParameters().addHeaderParameter(new UploadCollectionParameter({
				name: "name",
				value: encodeURI(oEvent.getParameter("fileName"))
			}));
			// Notes
			oEvent.getParameters().addHeaderParameter(new UploadCollectionParameter({
				name: "notes",
				value: encodeURI(oNotes.getValue())
			}));
			// piid
			oEvent.getParameters().addHeaderParameter(new UploadCollectionParameter({
				name: "piid",
				value: encodeURI(iData.instanceId)
			}));
		},
		/**
		 * 附件上载完成事件
		 */
		onUploadComplete: function(oEvent) {
			//刷新附件列表
			var oData=this.getView().getModel("inst").getData();
			this._procAttachment(oData.instanceId);
		},
		/**
		 * 附件Notes LiveChange
		 */
		notesLiveChange:function(oEvent) {
			var sValue = oEvent.getParameter("value");
			var oUploadCollection = this.getView().byId("procAttachmentId");
			if (sValue.length>0) {
				oUploadCollection.setUploadEnabled(true);
			}else {
				oUploadCollection.setUploadEnabled(false);
			}
		},
		//附件操作结束======================================================
		/**
		 * 点击业务编号
		 */
		showBusiness: function(oEvent) {
			var bsid=oEvent.getSource().getText();
			MessageToast.show(bsid);
		},
		showProcessDef: function(oEvent) {
			var oData=this.getView().getModel("inst").getData();
			MessageToast.show(oData.processKey);
		},
		/**
		 * 查看审批意见
		 */
		handleSideContentShow: function(oEvent) {
			var oData = this.currentData(oEvent,"hist");
			this.JsonModel().loadData(this,"bpm/findTaskComments?taskId=860043"+"&type="+oData.taskDefinitionKey,
				function (oData, model) {
					if (oData.statusCode == "000000") {
						$.each(oData.data, function(index, ele) {
							ele.time = this.convertDate(ele.time);
						}.bind(this))
						this.getView().setModel(new JSONModel(oData.data),"comments");
						this.getView().byId("taskSideContentId").setShowSideContent(true);
					} else {
						MessageToast.show(oData.message);
					}
				}.bind(this), 
				function (oData) {
					MessageToast.show("获取审批意见失败!");
				}.bind(this)
			);
		},
		/**
		 * 关闭审批意见
		 */
		handleSideContentHide: function (oEvent) {
			this.getView().byId("taskSideContentId").setShowSideContent(false);
		},
	})
});