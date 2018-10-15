sap.ui.define([ 'harry/app/controller/BaseController', 'jquery.sap.global',
		'sap/ui/model/json/JSONModel' ], function(BaseController, jQuery,
		JSONModel) {
	"use strict";
	return BaseController.extend("harry.app.controller.map.MasterMap", {

	/**
	 * Called when a controller is instantiated and its View controls (if
	 * available) are already created. Can be used to modify the View before it
	 * is displayed, to bind event handlers and do other one-time
	 * initialization.
	 * 
	 * @memberOf app.view.map.MasterMap
	 */
	 onInit: function() {
		 this.getView().setModel(new JSONModel(),"map");
	 },
	/**
	 * Similar to onAfterRendering, but this hook is invoked before the
	 * controller's View is re-rendered (NOT before the first rendering!
	 * onInit() is used for that one!).
	 * 
	 * @memberOf app.view.map.MasterMap
	 */
	// onBeforeRendering: function() {
	//
	// },
	/**
	 * Called when the View has been rendered (so its HTML is part of the
	 * document). Post-rendering manipulations of the HTML could be done here.
	 * This hook is the same one that SAPUI5 controls get after being rendered.
	 * 
	 * @memberOf app.view.map.MasterMap
	 */
	// onAfterRendering: function() {
	//
	// },
	/**
	 * Called when the Controller is destroyed. Use this one to free resources
	 * and finalize activities.
	 * 
	 * @memberOf app.view.map.MasterMap
	 */
	// onExit: function() {
	//
	// }
	 initMap : function() {
			this.getView().byId("mapList").setBusy(true);
			var city="北京";
			var condi=this.getView().byId("searchid").getValue();
			var map = new BMap.Map("map"); // 创建Map实例
			map.centerAndZoom(new BMap.Point(116.404, 39.915), 15); // 初始化地图,设置中心点坐标和地图级别
//			map.addControl(new BMap.MapTypeControl()); // 添加地图类型控件
			map.setCurrentCity(city); // 设置地图显示的城市 此项是必须设置的
//			map.enableScrollWheelZoom(true); // 开启鼠标滚轮缩放
			var view = this.getView();
			var geolocation = new BMap.Geolocation();
			geolocation.getCurrentPosition(function(r) {
				if (this.getStatus() == BMAP_STATUS_SUCCESS) {
					var options = {
						onSearchComplete : function(results) {
							// 判断状态是否正确
							if (local.getStatus() == BMAP_STATUS_SUCCESS) {
								var model=view.getModel("map");
								var s = [];
								for (var i = 0; i < results.getCurrentNumPois(); i++) {
									var poi=results.getPoi(i);
									s.push({
										title : poi.title,
										address : poi.address,
										lat : poi.point.lat,
										lng : poi.point.lng,
										distance : (map.getDistance(r.point, poi.point)/1000).toFixed(2)
									});
								}
								model.setData(s, false);
							}
							view.byId("mapList").setBusy(false);
						}
					};
					var local = new BMap.LocalSearch(map, options);
					local.searchNearby(condi, r.point, 10000);
				} else {
					alert('failed' + this.getStatus());
				}
			}, {
				enableHighAccuracy : true
			})
		},
		onListItemPress:function(oEvent) {
			var oItem = oEvent.getSource();
			var list = this.getView().byId("mapList");

			var path = oItem.getBindingContext("map").getPath();
			var index = path.substr(path.lastIndexOf("/")+1);
			var oData=oItem.getBindingContext("map").getModel().oData[index];
			
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			
			oRouter.getTargets().display("detailMap", {
				fromTarget : "masterMap",
				data : oData
			});
		},
		
	})
});