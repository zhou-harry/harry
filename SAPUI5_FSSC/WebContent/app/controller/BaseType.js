sap.ui.define([
	'sap/ui/model/SimpleType',
	'sap/ui/model/ValidateException',
	"harry/app/service/JSONModelUtil"
	], function (SimpleType, ValidateException, JSONModelUtil) {
		"use strict";

		return SimpleType.extend("harry.app.controller.BaseType", {
			
			JsonModel : function() {
				return new JSONModelUtil();
			}
			
		});

	});