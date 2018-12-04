sap.ui.define(["sap/ui/model/json/JSONModel",'sap/m/MessageToast' ],
		function(JSONModel,MessageToast) {
			"use strict";
			return JSONModel.extend("harry.app.service.JSONModelUtil", {
				//declare our new method including two new parameters fnSuccess and fnError, our callback functions
				loadData: function(that,sURL, fnSuccess, fnError, oParameters, bAsync, sType, bMerge, bCache){
						var path=this.getPath();
						if (bAsync !== false) {
							bAsync = true;
						}
						if (!sType)	{
							sType = "POST";
						}
						if (bCache === undefined) {
							bCache = this.bCache;
						}
						sURL=path+sURL;
						this.fireRequestSent({url : sURL, type : sType, async : bAsync, info : "cache="+bCache+";bMerge=" + bMerge});

						jQuery.ajax({
						  url: sURL,
						  contentType : "application/json",
						  async: bAsync,
						  dataType: 'json',
						  cache: bCache,
						  data: oParameters,
						  type: sType,
						  timeout: 10000,
						  xhrFields:{
					            withCredentials:true 
							  },
							  crossDomain: true,
						  success: function(oData) {
							if (!oData) {
								jQuery.sap.log.fatal("The following problem occurred: No data was retrieved by service: " + sURL);
							}
							this.setData(oData, bMerge);
							this.fireRequestCompleted({url : sURL, type : sType, async : bAsync, info : "cache=false;bMerge=" + bMerge});
							// call the callback success function if informed
							if (typeof fnSuccess === 'function') {
			                    fnSuccess(oData,this);
			                }

						  }.bind(this),
						  error: function(XMLHttpRequest, textStatus, errorThrown){
							jQuery.sap.log.fatal("The following problem occurred: " + textStatus, XMLHttpRequest.responseText + ","
										+ XMLHttpRequest.status + "," + XMLHttpRequest.statusText);
							this.fireRequestCompleted({url : sURL, type : sType, async : bAsync, info : "cache=false;bMerge=" + bMerge});
							this.fireRequestFailed({message : textStatus,
								statusCode : XMLHttpRequest.status, statusText : XMLHttpRequest.statusText, responseText : XMLHttpRequest.responseText});
							if (401===XMLHttpRequest.status) {
								that.getRouter().getTargets().display("login");
								MessageToast.show(XMLHttpRequest.responseText,{closeOnBrowserNavigation: false});
							}else if (typeof fnError === 'function') {// call the callback error function if informed
			                    if (500==XMLHttpRequest.status) {
			                    	var obj = eval('(' + XMLHttpRequest.responseText + ')');
			                    	MessageToast.show(obj.message);
								}
			                    if ("timeout"==textStatus) {
									MessageToast.show("访问超时,请检查网络或稍后再试.");
								}
								fnError({message : textStatus, statusCode : XMLHttpRequest.status, statusText : XMLHttpRequest.statusText, responseText : XMLHttpRequest.responseText});
			                }
						  }.bind(this)
						});
				},
//				getPath: function(){
//					var curWwwPath=window.document.location.href;
//					var pathName=window.document.location.pathname;
//				    var pos=curWwwPath.indexOf(pathName);
//				    var localhostPath=curWwwPath.substring(0,pos);
//				    return localhostPath+"/fssc/";
//				},
				
				getPath: function(){
					var curWwwPath=window.document.location.href;
					var pathName=window.document.location.pathname;
				    var pos=curWwwPath.indexOf(":8086"+pathName);
				    var localhostPath=curWwwPath.substring(0,pos);
				    return localhostPath+":8081/";
				},
			});
		}
);

