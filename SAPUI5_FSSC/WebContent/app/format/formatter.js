sap.ui.define([
	"sap/ui/Device"
	], function (Device) {
		"use strict";
		return {
			/**
			 * @public
			 * @param {boolean} bIsPhone the value to be checked
			 * @returns {string} path to image
			 */
			srcImageValue : function (bIsPhone) {
				var sImageSrc = "";
				if (Device.system.phone === false) {
					sImageSrc = "./images/homeImage.jpg";
				} else {
					sImageSrc = "./images/homeImage_small.jpg";
				}
				return sImageSrc;
			},
			
			loginImageValue : function (bIsPhone) {
				var sImageSrc = "";
				if (Device.system.phone === false) {
					sImageSrc = "./images/logo2.jpg";
				} else {
					sImageSrc = "./images/logo2_small.jpg";
				}
				return sImageSrc;
			},
			
			loginWidthValue : function () {
				var value = "100%";
				if (Device.system.phone === false) {
					value = "30%";
				} else {
					value = "85%";
				}
				return value;
			},
			v_navCon: function (type) {
				var _value = true;
				if("receiveTask" === type){
					_value = false;
				}
				return _value;
			},
			v_assignee: function (type) {
				var _value = false;
				if ("multiTask" === type) {
					_value = true;
				} else if("userTask" === type){
					_value = true;
				} else if("copyTask" === type){
					_value = true;
				}
				return _value;
			},
			v_owner: function (type) {
				var _value = false;
				if("userTask" === type){
					_value = true;
				}
				return _value;
			},
			v_b_task : function (type) {
				var _value = false;
				if ("multiTask" === type) {
					_value = true;
				} else if("userTask" === type){
					_value = true;
				}
				return _value;
			},
			_taskDefinitionKey: function (key) {
				var _value = false;
				if ("multiTask" === key) {
					_value = "会签";
				} else if("userTask" === key){
					_value = "单签";
				} else if("copyTask" === key){
					_value = "抄送";
				}
				return _value;
			},
			_taskDefinitionKey: function (key) {
				var _value = false;
				if ("multiTask" === key) {
					_value = "会签";
				} else if("userTask" === key){
					_value = "单签";
				} else if("copyTask" === key){
					_value = "抄送";
				}
				return _value;
			},
			_stateDeleteReason: function (text) {
				var _value = "None";
				if ("COMPLETE" === text) {
					_value = "Success";
				} else if("COPY_READ" === text){
					_value = "None";
				} else if("REJECT" === text){
					_value = "Error";
				}else if("PENDING" === text){
					_value = "Warning";
				}else if("JOB_DELEGETE" === text){
					_value = "Warning";
				}
				return _value;
			},
			_usrState: function (val){
				if (val === 1) {
					return "Success";
				} else if (val === 0) {
					return "Warning";
				} else if (val === 2){
					return "Error";
				} else {
					return "None";
				}
			},
			_usrStateText: function (val){
				if (val === 1) {
					return "激活";
				} else if (val === 0) {
					return "未激活";
				} else if (val === 2){
					return "已锁定";
				}
			},
			_usrStateIcon: function (val){
				if (val === 1) {
					return "sap-icon://message-success";
				} else if (val === 0) {
					return "sap-icon://message-warning";
				} else if (val === 2){
					return "sap-icon://message-error";
				}
			},
			dimensionType: function (val){
				if (val == 1) {
					return "流程";
				}else if (val == 2){
					return "任务";
				}
			},
			filterType: function (val){
				if (val == 1) {
					return "默认";
				}else if (val == 2){
					return "自定义";
				}else if (val == 3){
					return "引用";
				}
			},
			v_filterType_2: function (type){
				if (type == 2) {
					return true;
				}else {
					return false;
				}
			},
			v_filterType_3: function (type){
				if (type == 3) {
					return true;
				}else {
					return false;
				}
			},
			relationType: function (val){
				if (val == 1) {
					return "交集";
				}else if (val == 2){
					return "并集";
				}else if (val == 3){
					return "排斥";
				}
			},
			bpmRoleType: function (type){
				if (type == 1) {
					return "审批角色";
				}else if (type == 2){
					return "邮件通知角色";
				}else if (type == 3){
					return "抄送角色";
				}
			},
			taskType: function (type){
				if (type == 2){
					return "单签";
				}else if (type == 3){
					return "会签";
				}else if (type == 6){
					return "系统调度";
				}
			},
			taskStatusText: function (type){
				if (type == 1) {
					return "正常";
				}else if (type == 2){
					return "锁定";
				}else if (type == 3){
					return "删除";
				}
			},
			taskStatus: function (type){
				if (type == 1) {
					return "Success";
				}else if (type == 2){
					return "Warning";
				}else if (type == 3){
					return "Error";
				}
			},
			taskStatusIcon: function (type){
				if (type === 1) {
					return "sap-icon://message-success";
				} else if (type === 2) {
					return "sap-icon://message-warning";
				} else if (type === 3){
					return "sap-icon://message-error";
				}
			},
			taskHighlight: function (val){
				if ("COMPLETE"==val) {
					return "Success";
				}else if ("REJECT"==val) {
					return "Error";
				}else if ("PENDING"==val) {
					return "Information";
				}else if ("COPY_READ"==val) {
					return "None";
				}else if ("JOB_DELEGETE"==val) {
					return "Information";
				} else {
					return "Warning";
				}
			},
			taskPriority: function (val){
				if ("COMPLETE"==val) {
					return sap.ui.core.Priority.Low;
				}else if ("REJECT"==val) {
					return sap.ui.core.Priority.Medium;
				}else if ("PENDING"==val) {
					return sap.ui.core.Priority.None;
				}else if ("COPY_READ"==val) {
					return sap.ui.core.Priority.None;
				}else if ("JOB_DELEGETE"==val) {
					return sap.ui.core.Priority.None;
				} else {
					return sap.ui.core.Priority.High;
				}
			}
		};
	}
);