sap.ui.define([ "harry/app/util/EChart", ], function(EChart) {
	"use strict";
	var ChartPie = EChart.extend("harry.app.util.EChartPie", {
		/**
		 * @memberOf harry.app.control.EChartPie
		 */
		_seriesData : null,
		_legends : [],

		init : function() {
			EChart.prototype.init.call(this);
			this.setType("pie");
		},
		_binding : function() {
			var aData = this.getBinding("data").getCurrentContexts().map(
					function(oContext) {
						var name = oContext.sPath.substr(oContext.sPath
								.lastIndexOf("/") + 1);
						if ("series" == name) {
							this._legends = oContext.getObject();
						} else if ("seriesData" == name) {
							this._seriesData = oContext.getObject();

							// $.each(this._seriesData, function(i, ele) {
							// ele.type = this.getType();
							// this._legends[i] = ele.name;
							// }.bind(this))
						}
					}.bind(this));
		},

		_option : function(dom) {
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
					trigger : 'item',
					formatter : "{a} <br/>{b} : {c} ({d}%)"
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
				legend : {
					orient : 'vertical',
					left : 'left',
					data : this._legends
				},
				series : [ {
					name : this.getTitle(),
					type : 'pie',
					radius : this.getRadius(),
					center : [ '50%', '50%' ],
					data : this._seriesData,
				} ]
			};
			this._myChart.hideLoading();
			// 使用刚指定的配置项和数据显示图表。
			this._myChart.setOption(option);
		},

		renderer : function() {
			EChart.prototype.getRenderer().render.apply(this, arguments);
		}
	});
	return ChartPie;
});
