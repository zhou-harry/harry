define(['react-addons','app','jsx!sidebar','jsx!right-top-title','jsx!right-top-loginInfo','jsx!right-top-widget','jsx!right-top-navi'], 
function(React,App,Sidebar,TopTitle,LoginInfo,Widget,Navi) {
/**
 * <RightWrap props{link} />
 * <Navi link={this.props.link}/>
 */
var RightWrap = React.createClass({
	
	render: function() {
		var SubContent = require(this.props.link.value.subUrl);
		return (
			<div className="wrap-fluid" style={{width: 'auto', marginLeft: '250px'}}>
				<div className="container-fluid paper-wrap bevel tlbr">
					<div className="row">
						<div id="paper-top">
							<TopTitle link={this.props.link}/>
							
							<LoginInfo link={this.props.link}/>
							
							<Widget link={this.props.link}/>
							
							<SubContent link={this.props.link}/>
						</div>
            		</div>
	            </div>
            </div>
		); 
	}
});

return RightWrap;
});