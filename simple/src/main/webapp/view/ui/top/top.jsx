define(['react-addons','app','jsx!block','jsx!top-left','jsx!top-title','jsx!top-right'], 
function(React,App,Block,TopLeft,TopTitle,TopRight) {
/**
 * <Top props{link} />
 */
var Top = React.createClass({
	componentDidMount: function(){
		App.background();
		App.right_slid_menu();
	},
	render: function() {
		var dataTitle=[{style:"entypo-basket",value:"定制项1"},{style:"entypo-bag",value:"定制项2"}];
		
		return (
			<nav role="navigation" className="navbar navbar-static-top" style={{width: 'auto', marginLeft: '250px'}}>
				<div className="container-fluid">
					<div id="bs-example-navbar-collapse-1" className="collapse navbar-collapse">
					
						<TopTitle link={this.props.link} data={dataTitle}/>
						
						<TopLeft link={this.props.link}/>
						
						<TopRight link={this.props.link}/>
					
					</div>
				</div>
            </nav>
		); 
	}
});

return Top;
});