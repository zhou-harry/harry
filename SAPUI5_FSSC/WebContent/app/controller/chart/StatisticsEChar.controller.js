sap.ui.define([ 
		'harry/app/controller/BaseController',
		'sap/ui/model/json/JSONModel' ,
		'sap/m/MessageToast'
	], function(BaseController, JSONModel, MessageToast) {
	"use strict";
	var StatisticsEChar= BaseController.extend("harry.app.controller.chart.StatisticsEChar", {

	/**
	 * Called when a controller is instantiated and its View controls (if
	 * available) are already created. Can be used to modify the View before it
	 * is displayed, to bind event handlers and do other one-time
	 * initialization.
	 * 
	 * @memberOf app.view.chart.StatisticsEChar
	 */
	 onInit: function() {
		var base = +new Date(1968, 9, 3);
		var oneDay = 24 * 3600 * 1000;
		var date = [];

		var data = [ Math.random() * 300 ];

		for (var i = 1; i < 20000; i++) {
			var now = new Date(base += oneDay);
			date.push([ now.getFullYear(), now.getMonth() + 1, now.getDate() ].join('/'));
			data.push(Math.round((Math.random() - 0.5) * 20 + data[i - 1]));
		}

		var oViewModel = new JSONModel({
			barData : {
				series : [ "衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子" ],
				seriesData : [ {
					name : '一月销量',
					data : [ 5, 20, 36, 10, 10, 20 ]
				}, {
					name : '二月销量',
					data : [ 3, 15, 44, 32, 12, 9 ]
				} ]
			},
			pieData : {
				series : [ '直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎' ],
				seriesData : [ {
					name : '直接访问',
					value : 335
				}, {
					name : '邮件营销',
					value : 310
				}, {
					name : '联盟广告',
					value : 234
				}, {
					name : '视频广告',
					value : 546
				}, {
					name : '搜索引擎',
					value : 397
				} ]
			},
			areaData : {
				series : date,
				seriesData : data
			}
		});
		this.setModel(oViewModel);
	 },
	/**
	 * Similar to onAfterRendering, but this hook is invoked before the
	 * controller's View is re-rendered (NOT before the first rendering!
	 * onInit() is used for that one!).
	 * 
	 * @memberOf app.view.chart.StatisticsEChar
	 */
	// onBeforeRendering: function() {
	//
	// },
	/**
	 * Called when the View has been rendered (so its HTML is part of the
	 * document). Post-rendering manipulations of the HTML could be done here.
	 * This hook is the same one that SAPUI5 controls get after being rendered.
	 * 
	 * @memberOf app.view.chart.StatisticsEChar
	 */
	// onAfterRendering: function() {
	//
	// },
	/**
	 * Called when the Controller is destroyed. Use this one to free resources
	 * and finalize activities.
	 * 
	 * @memberOf app.view.chart.StatisticsEChar
	 */
	// onExit: function() {
	//
	// }
	 onRefresh : function(oEvent) {
		var data = {
			series : [ "衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子" ],
			seriesData : [ {
				name : '一月销量',
				data : [ 15, 10, 33, 89, 54, 20 ]
			}, {
				name : '二月销量',
				data : [ 83, 53, 44, 32, 12, 149 ]
			} ]
		}
		var oViewModel = new JSONModel({
			barData : data
		});
		this.setModel(oViewModel);

		var bar = this.getView().byId("barid");
		bar._onReset();
	},
	onPress : function(params) {
		var name = params.getParameter("name");
		var seriesName = params.getParameter("seriesName");
		var value = params.getParameter("value");
		MessageToast.show(name + seriesName + value);
	}
	})
	return StatisticsEChar;
});