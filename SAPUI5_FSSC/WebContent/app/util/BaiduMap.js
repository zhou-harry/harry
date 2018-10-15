sap.ui.define([ "sap/ui/core/Control", ], function(Control) {
	"use strict";
	var BaiduMap = Control.extend("harry.app.util.BaiduMap", {
		metadata : {
			properties : {
				lng : {
					type : "string",
					defaultValue : ""
				},
				lat : {
					type : "string",
					defaultValue : ""
				},
				title : {
					type : "string",
					defaultValue : ""
				},
				address : {
					type : "string",
					defaultValue : ""
				}
			},
			aggregations : {
				_html : {
					type : "sap.ui.core.HTML",
					multiple : false,
					visibility : "hidden"
				},
			},
			events : {
				press : {
					enablePreventDefault : true
				}
			},
		},
		/**
		 * @memberOf harry.sapui5_gps.tool.Geolocation
		 */
		constructor : function(sId, mSettings) {
			Control.apply(this, arguments);
		},
		_sContainerId : null,
		_sResizeHandlerId : null,
		_html : '',
		_map : null,

		/**
		 * Initialize
		 */
		init : function() {
			this._sContainerId = this.getId() + "--container";
			this.setAggregation("_html", new sap.ui.core.HTML(this._sContainerId, {
				content : this._div(this._sContainerId)
			}));
		},

		_onReset : function(oEvent) {
			var id = this.getId() + "--container";
			alert("_onReset--------------");
		},
		onBeforeRendering : function() {
			sap.ui.core.ResizeHandler.deregister(this._sResizeHandlerId);
		},

		onAfterRendering : function() {
			this._sResizeHandlerId = sap.ui.core.ResizeHandler.register(this, jQuery.proxy(this._onReset, this));
			$("#allmap").css({
				"height" : "100%"
			});
			this.initMap();
		},
		_onResetMap : function() {
			if (null != this._map) {
				this._map.clearOverlays();
				setTimeout(function() {
					var point = new BMap.Point(this.getLng(), this.getLat());
					var marker = new BMap.Marker(point);
					
					this.searchInfo(marker, this.getTitle(), this.getAddress());
					
					this._map.addOverlay(marker);
					this._map.panTo(point);
				}.bind(this), 500);
			}
		},
		// 同步显示地图
		initMap : function() {
			// 百度地图API功能
			this._map = new BMap.Map("allmap");
			//初始化地图,设置中心点坐标和地图级别
			this._map.centerAndZoom(new BMap.Point(116.403616, 39.92006), 15);
			// 以当前坐标为中心
			// this.geolocation();

			this.location();
			this._onResetMap();

		},
		searchInfo : function(marker, title, content) {
			// 创建信息窗口对象
			var infoWindow = new BMap.InfoWindow(content, {
				width : 200,
				title : title
			});
			var point=marker.point;
			marker.addEventListener("click", function(r) {
				this._map.openInfoWindow(infoWindow, point); // 开启信息窗口
			}.bind(this));
		},
		location : function() {
			// 添加带有定位的导航控件
			var navigationControl = new BMap.NavigationControl({
				// 靠左上角位置
				anchor : BMAP_ANCHOR_TOP_LEFT,
				// LARGE类型
				type : BMAP_NAVIGATION_CONTROL_LARGE,
				// 启用显示定位
				enableGeolocation : true
			});
			this._map.addControl(navigationControl);
			// 添加定位控件
			var geolocationControl = new BMap.GeolocationControl();
			geolocationControl.addEventListener("locationSuccess", function(e) {
				// 定位成功事件
				var address = '';
				address += e.addressComponent.province;
				address += e.addressComponent.city;
				address += e.addressComponent.district;
				address += e.addressComponent.street;
				address += e.addressComponent.streetNumber;
				// alert("当前定位地址为：" + address);
			});
			geolocationControl.addEventListener("locationError", function(e) {
				// 定位失败事件
				alert(e.message);
			});
			this._map.addControl(geolocationControl);
		},
		/**
		 * 以当前坐标为初始位置
		 */
		geolocation : function() {
			var __map = this._map;
			var geolocation = new BMap.Geolocation();
			geolocation.getCurrentPosition(function(r) {
				this._map = new BMap.Map("allmap");
				this._map.centerAndZoom(r.point, 15);
				this._map.enableScrollWheelZoom(true);
				var mk = new BMap.Marker(r.point);
				this._map.addOverlay(mk);
				this._map.panTo(r.point);
			}.bind(this))
		},
		// 异步加载地图
		initMap2 : function() {
			// 百度地图API功能
			function loadJScript() {
				var script = document.createElement("script");
				script.type = "text/javascript";
				script.src = "http://api.map.baidu.com/api?v=2.0&ak=tWm4qRq07K61KbigS0dKZtWbTTCWZARX&callback=init";
				document.body.appendChild(script);
			}
			window.onload = loadJScript; // 异步加载地图
		},

		_div : function(id) {
//			this._html = $.ajax({
//				url : "app/html/baiduMap.html",
//				async : false
//			}).responseText;
//			return this._html;
			return "<div id='allmap'>";
		},

		/**
		 * Renders the root div and the HTML aggregation
		 * 
		 * @param {sap.ui.core.RenderManger}
		 *            oRM the render manager
		 * @param {sap.ui.core.Control}
		 *            oControl the control to be rendered
		 */
		renderer : function(oRM, oControl) {
			if (!oControl.getVisible()) {
				return;
			}
			oRM.renderControl(oControl.getAggregation("_html"));
		}
	});
	return BaiduMap;
});
