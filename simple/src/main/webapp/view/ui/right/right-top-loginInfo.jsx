define(['react-addons'], function(React) {	
	/**
	 * <LoginInfo data(link)/>
	 *<button type="button" className="close" data-dismiss="alert">×</button>
	 */
	var LoginInfo = React.createClass({
		getInitialState: function() {
			return {
				element: ''
			};      
		},
		componentWillMount: function() { 
			$.ajax({     
				url: "user/sessionUser",   
				type : "post",
				dataType : "json",   
				success: function(data) {
					if (this.isMounted()) {
						this.setState({element: data});
					}
				}.bind(this)   
			});
		},
		render: function() {
			
			return (
				<div className="col-sm-7">
					<div className="devider-vertical visible-lg"></div>
					<div className="tittle-middle-header">
						<div className="alert">
							
							<span className="tittle-alert entypo-info-circled"/>
							欢迎回来,&nbsp; <strong>{this.state.element.descript}</strong>
							&nbsp;&nbsp;您上一次登录系统在: {this.state.element.lastLonginTime}
						</div>
					</div>
				</div>
			); 
		}
	});
	
  return LoginInfo;
});