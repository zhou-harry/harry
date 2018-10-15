sap.ui.define([ "sap/ui/core/Control", ], function(Control) {
	"use strict";
	var Diagram = Control.extend("harry.app.util.Diagram", {

		metadata : {
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
		 * @memberOf harry.app.util.Diagram
		 */
		constructor : function(sId, mSettings) {
			Control.apply(this, arguments);
		},
		_sResizeHandlerId : null,

		_onReset : function(oEvent) {
			$("div").remove(".slickDiagram");
			// 事件委托监听。
			if (this.getBinding("data")) {
				var aData = this.getBinding("data").getCurrentContexts().map(function(oContext) {
					var _data=oContext.getModel().getData();
					$("#"+this._sContainerId).find('img').attr("src",_data.src);
					$.each(_data.div, function(index, ele) {
						
						$("#"+this._sContainerId+ele.key).remove();
						
						$("#"+this._sContainerId).append("<div id='"+this._sContainerId+ele.key+"' class='slickDiagram' />");
						$("#" + this._sContainerId+ele.key).css({
							"position":	"absolute",
							"width"	:	ele.width-5,
							"height": 	ele.height-5,
							"left"	:	ele.x,
							"top"	:	ele.y-0.5
						});
						$("#" + this._sContainerId+ele.key).unbind("click");
						$("#" + this._sContainerId+ele.key).bind('click', function(e) {
							this.firePress({
								key : ele.key
							});
						}.bind(this));
					}.bind(this))
				}.bind(this));
			}
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
			this._sResizeHandlerId = sap.ui.core.ResizeHandler.register(this, jQuery.proxy(this._onReset(), this));
		},
		_div : function(id) {
			return "<div id='" + id + "'>" +
					"<img style='position: absolute; top: 0px; left: 0px;' />"+
					"</div>";
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
	return Diagram;
});
