sap.ui.define([ 
	"harry/app/controller/BaseController",
	"harry/app/controller/BaseType",
	"sap/ui/model/json/JSONModel", 
	"sap/ui/commons/MessageBox",
	"sap/m/MessageToast", 
	"sap/ui/Device",
	'sap/ui/model/ValidateException',
	'sap/ui/unified/FileUploaderParameter',
	'harry/app/format/formatter'
	], function(Controller, BaseType, JSONModel, MessageBox,MessageToast,Device, ValidateException,FileUploaderParameter, formatter) {
	"use strict";
	return Controller.extend("harry.app.controller.user.UserAdd", {

/**
* Called when a controller is instantiated and its View controls (if available) are already created.
* Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
* @memberOf app.controller.user.UserAdd
*/
	onInit: function() {
		var oView = this.getView();
		// attach handlers for validation errors
		sap.ui.getCore().attachValidationError(this.handleValidationError);
		sap.ui.getCore().attachValidationSuccess(this.handleValidationSuccess.bind(this));
		sap.ui.getCore().getMessageManager().registerObject(oView.byId("email"), true);
//		sap.ui.getCore().getMessageManager().registerObject(oView.byId("password2"), true);
		
		this.getRouter().getTarget("userAdd").attachDisplay(this.handleAttachDisplay, this);
//		this.getView().byId("rtokens").attachTokenChange(this.onTokenChange,this);
		
		this.getView().setModel(new JSONModel({path:"../../../fssc/file/upload"}),"f");
	},

/**
* Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
* (NOT before the first rendering! onInit() is used for that one!).
* @memberOf app.controller.user.UserAdd
*/
//	onBeforeRendering: function() {
//
//	},

/**
* Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
* This hook is the same one that SAPUI5 controls get after being rendered.
* @memberOf app.controller.user.UserAdd
*/
//	onAfterRendering: function() {
//
//	},

/**
* Called when the Controller is destroyed. Use this one to free resources and finalize activities.
* @memberOf app.controller.user.UserAdd
*/
//	onExit: function() {
//
//	},
	/**
	 * AttachDisplay
	 */
	handleAttachDisplay : function(oEvent) {
		this.initData();
		this._oData = oEvent.getParameter("data");
		if (this._oData !== undefined) {
			if ("userInfo"===this._oData.fromTarget) {
				this.getView().setModel(new JSONModel(this._oData),"target");
				this.getView().setModel(new JSONModel(this._oData.data),"usr");
				
				$.each(this._oData.roles, function(index, ele) {
					ele.editable = false;
				}.bind(this))
				this.getView().setModel(new JSONModel(this._oData.roles),"roles");
			}
		}
	},
	
	initData: function(){
		this.getView().setModel(new JSONModel({
			userId : null,
			password : null,
			name : null,
			phone : null,
			email : null,
			company : null,
			state : 0
		}),"usr");
		this.byId("rtokens").removeAllTokens();
		this.onPressCancel();
	},
	
	handleValidationError:function(evt) {
		var control = evt.getParameter("element");
		if (control && control.setValueState) {
			control.setValueState("Error");
		}
	},
	handleValidationSuccess:function(evt){
		var control = evt.getParameter("element");
		var id=control.getId();
		var oValue=control.getValue();
		if (control && control.setValueState) {
			var msg;
			if (id.indexOf("userId") != -1) {
				this.JsonModel().loadData(this,"user/validId?id="+oValue,
					function(oData, model) {
						if (oData.statusCode != "000000") {
							msg=oData.data
						}
					}.bind(this), 
					function(oData) {
						MessageToast.show("系统异常!");
					}.bind(this),null,false
				);
			}
			if (id.indexOf("password2") != -1) {
				var password=this.getView().getModel("usr").getData().password;
				if (oValue!=password) {
					msg="请重新输入确认密码！";
				}
			}
			if (msg) {
				control.setValueStateText(msg);
				throw new ValidateException(msg);
			}
			control.setValueState("None");
		}
	},
	
	/**
	 * Custom model type for validating an E-Mail address
	 * @class
	 * @extends sap.ui.model.SimpleType
	 */
	customEMailType : BaseType.extend("email", {
		formatValue: function (oValue) {
			return oValue;
		},
		parseValue: function (oValue) {
			//parsing step takes place before validating step, value could be altered here
			return oValue;
		},
		validateValue: function (oValue) {
			if (oValue=="") {
				return;
			}
			// The following Regex is NOT a completely correct one and only used for demonstration purposes.
			// RFC 5322 cannot even checked by a Regex and the Regex for RFC 822 is very long and complex.
			var rexMail = /^\w+[\w-+\.]*\@\w+([-\.]\w+)*\.[a-zA-Z]{2,}$/;
			if (!oValue.match(rexMail)) {
				throw new ValidateException("'" + oValue + "' is not a valid email address");
			}
		}
	}),
	/**
	 * <!-- value="{path : 'usr>/userid',type : '.customPassword'}"> -->
	 */
	customPassword : BaseType.extend("Password", {
		formatValue: function (oValue) {
			return oValue;
		},
		parseValue: function (oValue) {
			//parsing step takes place before validating step, value could be altered here
			return oValue;
		},
		validateValue: function (oValue) {
			if (oValue=="") {
				return;
			}
			var password=this.getView().getModel("usr").getData().password;
			if (oValue!=password) {
				throw new ValidateException("请重新输入确认密码！");
			}
		}
	}),
	
	onPressSave: function(oEvent){
		// collect input controls
		var oView = this.getView();
		var inputs = [
			oView.byId("userId"),
			oView.byId("password"),
			oView.byId("password2"),
			oView.byId("name"),
			oView.byId("phone"),
			oView.byId("email")
		];
		var valid = true;
		jQuery.each(inputs, function(i, input) {
			if ("Error" === input.getValueState()) {
				valid=false;
			}else if (input.getRequired()&&!input.getValue()) {
				input.setValueState("Error");
				valid=false;
			}
		});
		if (!valid) {
			return false;
		}
		var uModel=this.getView().getModel("usr");
		var uData=uModel.getData();
		uData.roles=this.getView().getModel("roles").getData();
		uModel.refresh();
		this.JsonModel().loadData(this,"user/save",
			function(oData, model) {
				if (oData.statusCode != "000000") {
					MessageToast.show(oData.message);
				}else {
					this.initData();
					this.getView().byId("preview").setSrc(null);
					MessageToast.show(oData.message);
				}
			}.bind(this), 
			function(oData) {
				MessageToast.show("系统异常!");
			}.bind(this),
			uModel.getJSON()
		);
	},
	onPressCancel: function(oEvent){
		var oView = this.getView();
		var inputs = [
			oView.byId("userId"),
			oView.byId("password"),
			oView.byId("password2"),
			oView.byId("name"),
			oView.byId("phone"),
			oView.byId("email"),
			oView.byId("company")
		];
		jQuery.each(inputs, function(i, input) {
			input.setValueState("None");
		});
	},
	/**
	 * 附件=========================================================
	 */
	handleUploadComplete: function(oEvent) {
		var sResponse = oEvent.getParameter("response");
		if (sResponse) {
			var data=$.parseJSON(sResponse.substring(sResponse.indexOf("{"),sResponse.indexOf("}")+1));
			if ("000000"===data.statusCode) {
				oEvent.getSource().setValue("");
				var oModel=this.getView().getModel("usr");
				oModel.getData()["photo"]=data.data;
				oModel.getData()["preview"]="../../../fssc/file/preview?id="+data.data;
				oModel.refresh();
			} else {
				MessageToast.show(data.message);
			}
		}
	},

	handleFileChange: function(oEvent) {
	},
	
	handleTypeMissmatch: function(oEvent) {
		var aFileTypes = oEvent.getSource().getFileType();
		jQuery.each(aFileTypes, function(key, value) {aFileTypes[key] = "*." +  value;});
		var sSupportedFileTypes = aFileTypes.join(", ");
		MessageToast.show("The file type *." + oEvent.getParameter("fileType") +
								" is not supported. Choose one of the following types: " +
								sSupportedFileTypes);
	},
	
	handleSizeExceed: function(oEvent) {
		var max=oEvent.getSource().getMaximumFileSize();
		MessageToast.show("请上传不超过（"+max+"M）的文件.");
	},
	onDisplayBack: function(oEvent) {
		var fromTarget=this.getView().getModel("target").getData().fromTarget;
		this.getRouter().getTargets().display(fromTarget);
	},
	onAddRoles: function(oEvent) {
		var oView=this.getView();
		var oDialog = oView.byId("roleListId");
		
		this.JsonModel().loadData(this,"role/list",
			function(oData, model) {
				if (oData.statusCode == "000000") {
					$.each(oData.data, function(index, ele) {
						ele.created = this.convertDate(ele.created);
					}.bind(this))
					if (!oDialog) {
						oDialog = sap.ui.xmlfragment(oView.getId(), "harry.app.fragment.role.RoleList",this);
						oDialog.setModel(new JSONModel(oData.data),"roles");
						oView.addDependent(oDialog);
					}
					oDialog.open();
				}
			}.bind(this), 
			function(oData) {
				MessageToast.show("系统异常!");
			}.bind(this)
		);
	},
	cancelRoleList: function(oEvent){
		this.getView().byId("roleListId").destroy();
	},
	confirmRoleSelect: function(oEvent){
		var oView=this.getView();
		
		var oModel=oView.getModel("roles");
		if (oModel==undefined) {
			this.getView().setModel(new JSONModel(new Array()),"roles");
			var oModel=this.getView().getModel("roles");
		}
		var aEntries = oModel.getData();
		// add data
		var oTable=this.byId("rlistid");
		var aIndices = oTable.getSelectedIndices();
		if (aIndices.length > 0) {
			$.each(aIndices, function(index, i) {
				var context=oTable.getContextByIndex(i);
				var cData=context.getModel().getProperty(context.getPath());
				var set=true;
				$.each(aEntries, function(idx, ele) {
					if (ele.roleId===cData.roleId) {
						set=false;
						return;
					}
				}.bind(this))
				if(set) {
					aEntries.unshift({
						roleId : cData.roleId,
						name : cData.name,
						editable: true
					});
				}
			}.bind(this))
		}
		
		oModel.refresh();
		oView.byId("roleListId").close();
	},
	onRemoveRole: function(oEvent) {
		var oModel=this.getView().getModel("roles");
		
		var context=oEvent.getSource().getBindingContext("roles");
		var sPath = context.getPath();
		var _data = oModel.getData();
		
		var index = sPath.substr(sPath.lastIndexOf("/")+1);
		_data.splice(index,1);
		oModel.refresh();
	},
	onTokenChange: function(oEvent) {
	},
})
});