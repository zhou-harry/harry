sap.ui.define([
		'harry/app/controller/BaseController', 
		'sap/ui/model/json/JSONModel', 
		'sap/ui/Device', 
		'sap/m/MessageToast', 
		'harry/app/format/formatter'
], function(BaseController, JSONModel, Device, MessageToast, formatter) {
	"use strict";
	return BaseController.extend("harry.app.controller.Home", {
		formatter : formatter,
		_iCarouselTimeout : 0, // a pointer to the current timeout
		_iCarouselLoopTime : 8000, // loop to next picture after 8 seconds

		onInit : function() {
			var oViewModel = new JSONModel({
				isPhone : Device.system.phone
			});
			this.setModel(oViewModel, "view");
			Device.media.attachHandler(function(oDevice) {
				this.getModel("view").setProperty("/isPhone", oDevice.name === "Phone");
			}.bind(this));

			this.getRouter().getTarget("home").attachDisplay(this.handleAttachDisplay, this);
		},
		/**
		 * lifecycle hook that will initialize the welcome carousel
		 */
		onAfterRendering : function() {
			this.onCarouselPageChanged();
		},
		/**
		 * AttachDisplay
		 */
		handleAttachDisplay : function(oEvent) {
			this._initCategory();
		},
		/**
		 * clear previous animation and initialize the loop animation of the
		 * welcome carousel
		 */
		onCarouselPageChanged : function() {
			clearTimeout(this._iCarouselTimeout);
			this._iCarouselTimeout = setTimeout(function() {
				var oWelcomeCarousel = this.byId("welcomeCarousel");
				if (oWelcomeCarousel) {
					oWelcomeCarousel.next();
					this.onCarouselPageChanged();
				}
			}.bind(this), this._iCarouselLoopTime);
		},
		/**
		 * 初始化数据
		 */
		_initCategory : function() {
			this.JsonModel().loadData(this, "bpm/findCategory", function(oData, model) {
				if (oData.statusCode == "000000") {
					this.getView().setModel(new JSONModel(oData.data), "category");
				} else {
					MessageToast.show(oData.message);
				}
			}.bind(this), function(oData) {
				MessageToast.show(oData.responseText);
			}.bind(this));
		},
		/**
		 * 事件
		 */
		toggleAssignee : function(oEvent) {
			this.getView().byId("candidateid").setPressed(false);
			if (oEvent.getSource().getPressed()) {
				this._initAssigneeProcess();
			} else {
				this._initTaskProcess();
			}
		},
		toggleCandidate : function(oEvent) {
			this.getView().byId("assigneeid").setPressed(false);
			if (oEvent.getSource().getPressed()) {
				this._initCandidateProcess();
			} else {
				this._initTaskProcess();
			}
		},
		press : function(oEvent) {
			var oList = oEvent.getSource(), context = oList.getBindingContext("category");
			var oData = context.getModel().getProperty(context.getPath());

			this.getRouter().getTargets().display("processList", {
				fromTarget : "home",
				data : oData
			});
		}
	});
});