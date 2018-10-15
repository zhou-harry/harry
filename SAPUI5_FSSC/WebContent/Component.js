sap.ui.define([
	"sap/ui/core/UIComponent",
	"harry/app/format/models",
	"sap/ui/model/resource/ResourceModel",
	"harry/app/service/JSONModelUtil",
], function (UIComponent, models, JSONModelUtil) {
	"use strict";
	return UIComponent.extend("harry.Component", {
		metadata: {
			manifest: "json"
		},
		init: function () {
			// call the init function of the parent
			UIComponent.prototype.init.apply(this, arguments);
			// create the views based on the url/hash
			this.getRouter().initialize();

			// set the device model
			this.setModel(models.createDeviceModel(), "device");
			
		},

		getContentDensityClass: function () {
			if (!this._sContentDensityClass) {
				if (!sap.ui.Device.support.touch){
					this._sContentDensityClass = "sapUiSizeCompact";
				} else {
					this._sContentDensityClass = "sapUiSizeCozy";
				}
			}
			return this._sContentDensityClass;
		},
		getPopoverDensityClass: function () {
			if (!sap.ui.Device.support.touch){
				return "sapUiSizeCompact";
			} else {
				return "sapUiSizeCozy";
			}
		}
	});
});