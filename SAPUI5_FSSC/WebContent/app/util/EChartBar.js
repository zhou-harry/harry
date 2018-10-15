sap.ui.define([ "harry/app/util/EChart", ], function(EChart) {
	"use strict";
	var ChartBar = EChart.extend("harry.app.util.EChartBar", {
		/**
		 * @memberOf harry.app.control.EChartBar
		 */
		_xdata : null,
		_ydata : null,
		_seriesData : null,
		_legends : [],

		init : function() {
			EChart.prototype.init.call(this);
			this.setType("bar");
		},
		_binding : function() {
			var aData = this.getBinding("data").getCurrentContexts().map(
					function(oContext) {
						var name = oContext.sPath.substr(oContext.sPath
								.lastIndexOf("/") + 1);
						if ("series" == name) {
							if ("vertical" == this.getLocation()) {
								this._xdata = oContext.getObject();
							} else {
								this._ydata = oContext.getObject();
							}
						} else if ("seriesData" == name) {
							this._seriesData = oContext.getObject();

							$.each(this._seriesData, function(i, ele) {
								ele.type = this.getType();
								this._legends[i] = ele.name;
							}.bind(this))
						}
					}.bind(this));
		},

		_option : function() {
			this._myChart.showLoading();
			this._binding();
			// 指定图表的配置项和数据
			var option = {
				backgroundColor : {
					type : 'pattern',
					image : this._waterMark,
					repeat : 'repeat'
				},
				title : {
					text : this.getTitle(),
					subtext : this.getSubtitle(),
					x : 'center'
				},
				tooltip : {
					trigger : 'axis',
					axisPointer : {
						type : 'shadow'
					}
				},
				legend : {
					orient : 'vertical',
					left : 'left',
					data : this._legends
				},
				toolbox : {
					show : true,
					feature : {
						dataView : {
							readOnly : false
						},
						restore : {
							title : '刷新'
						},
						saveAsImage : {},
					}
				},
				xAxis : {
					data : this._xdata
				},
				yAxis : {
					data : this._ydata
				},
				series : this._seriesData
			};
			this._myChart.hideLoading();
			// 使用刚指定的配置项和数据显示图表。
			this._myChart.setOption(option);
		},

		renderer : function() {
			EChart.prototype.getRenderer().render.apply(this, arguments);
		}
	});
	return ChartBar;
});
