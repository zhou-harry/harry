define(['react-addons'], function(React) {	
	/**
	 * <Login link/>
	 */
	var Login = React.createClass({
		getDefaultProps: function() {
		    return {
		      	link : '',
		    };
		},
		componentDidMount : function(){
			$('#formid').ajaxForm(function(data) {
                if(data.status){
            		this.props.link.requestChange({
	            		title:"Dashboard",
	            		url:"jsx!dashboard",
	            		subUrl:"jsx!index"
            		});
                }else{
	                alert(data.message);
                }
            }.bind(this));
		},
		handlerRegiste : function(data){
			this.props.link.requestChange({
				title:"注册",
				url:"jsx!dashboard",
				subUrl:"jsx!index",
			});
			//alert("待启用!");
		},
		render: function() {
			return (
				<div className="" id="login-wrapper">
		            <div className="row">
		                <div className="col-md-4 col-md-offset-4">
		                    <div id="logo-login">
		                        <h1>
		                        	TMS终端管理系统
		                        </h1>
		                    </div>
		                </div>
	                </div>
					<div className="row">
		                <div className="col-md-4 col-md-offset-4">
		                    <div className="account-box"> 
		                        <form role="form" id="formid" name="user" action="user/login">
		                            <div className="form-group">
		                                <label htmlFor="inputUsername">用户名</label>
		                                <input type="text" id="inputUsername" name="userName" className="form-control valid"/>
		                            </div>
		                            <div className="form-group">
		                                <label htmlFor="inputPassword">密码</label>
		                                <input type="password" id="inputPassword" name="password" className="form-control valid"/>
		                            </div>
		                            <div className="pull-left">
		                                 <a className="forgotLnk" href="#" onClick={this.handlerRegiste}>注册</a>
		                            </div>
		                            <button className="btn btn-primary pull-right" type="submit" >
		                                	登 录
		                            </button>
		                        </form>
		                        <div className="row-block">
			                        <div className="row">
				                    </div>
		                        </div>
		                    </div>
		                </div>
	                </div>
				</div>
			); 
		}
	});
	
  return Login;
});