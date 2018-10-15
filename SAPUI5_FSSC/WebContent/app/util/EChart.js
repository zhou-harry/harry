sap.ui.define([ "sap/ui/core/Control", ], function(Control) {
	"use strict";
	var EChart = Control.extend("harry.app.util.EChart", {

		metadata : {
			properties : {
				type : {
					type : "string",
					defaultValue : "line"
				},
				width : {
					type : "string",
					defaultValue : "100%"
				},
				height : {
					type : "string",
					defaultValue : "40px"
				},
				title : {
					type : "string",
					defaultValue : ""
				},
				subtitle : {
					type : "string",
					defaultValue : ""
				},
				location : {
					type : "string",
					defaultValue : "vertical"// horizontal
				},
				waterMarkText : {
					type : "string",
					defaultValue : ""// 
				},
				radius : {
					type : "string",
					defaultValue : [ '30%', '70%' ]
				// ['30%', '70%']
				},
			},
			aggregations : {
				_html : {
					type : "sap.ui.core.HTML",
					multiple : false,
					visibility : "hidden"
				},
				data : {
					type : "sap.ui.base.ManagedObject",
					multiple : true
				},
			},
			events : {
				press : {
					enablePreventDefault : true
				}
			},
			defaultAggregation : "data"
		},
		/**
		 * @memberOf app.tool.EChart
		 */
		constructor : function(sId, mSettings) {
			Control.apply(this, arguments);
			if (undefined == sId.type) {
				this.setType(this.getType());
			} else {
				this.setWidth(sId.width);
			}
			this.setHeight(sId.height);
			this.setTitle(sId.title);
			this.setSubtitle(sId.subtitle);
			this.setLocation(sId.location);
			this.setRadius(sId.radius);
			this.setWaterMarkText(sId.waterMarkText);
		},
		_sContainerId : null,
		_sResizeHandlerId : null,
		_myChart : null,
		_waterMark:null,

		_onReset : function(oEvent) {
			if (undefined == this._myChart) {
				// 基于准备好的dom，初始化echarts实例
				var dom = document.getElementById(this.getId() + "--container");
				dom.style.width = this.getWidth();
				dom.style.height = this.getHeight();
				this._myChart = echarts.init(dom);
				var oControl = this;
				this._myChart.on('click', function (params) {
					oControl.firePress(params);
				});
				this._getWaterMark();
			}
			this._option();
		},
		_getWaterMark : function() {
			// 水印
			var waterMarkText = this.getWaterMarkText();
			if (undefined == this._waterMark) {
				this._waterMark = document.createElement('canvas');
			}
			var ctx = this._waterMark.getContext('2d');
			this._waterMark.width = this._waterMark.height = 100;
			ctx.textAlign = 'center';
			ctx.textBaseline = 'middle';
			ctx.globalAlpha = 0.08;
			ctx.font = '20px Microsoft Yahei';
			ctx.translate(50, 50);
			ctx.rotate(-Math.PI / 4);
			ctx.fillText(waterMarkText, 0, 0);
		},

		_div : function(id) {
			return "<div id='" + id + "' style='width: " + this.getWidth() + "; height: " + this.getHeight()
			+ ";'></div>";
		},
		/**
		 * Initialize hidden html aggregation
		 */
		init : function() {
			this._sContainerId = this.getId() + "--container";
			this.setAggregation("_html", new sap.ui.core.HTML(this._sContainerId, {
				content : this._div(this._sContainerId)
			}));
		},
		
		onBeforeRendering : function() {
			sap.ui.core.ResizeHandler.deregister(this._sResizeHandlerId);
		},

		onAfterRendering : function() {
			this._sResizeHandlerId = sap.ui.core.ResizeHandler.register(this, jQuery.proxy(this._onReset, this));
			var $control = this.$();
			if ($control.length > 0) {
				this._onReset();
			}
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
			oRM.write("<div");
			oRM.writeControlData(oControl);
			oRM.addClass("divEChart");
			oRM.writeClasses();
			oRM.write(">");
			oRM.renderControl(oControl.getAggregation("_html"));
			oRM.write("</div>");
		}
	});
	return EChart;
});
