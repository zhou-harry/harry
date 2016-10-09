define(['react-addons','app','jsx!block','jsx!sidebar','jsx!right-wrap','jsx!top','jsx!sidebar-right'], 
function(React,App,Block,Sidebar,RightWrap,Top,SidebarRight) {
/**
 * <Dashboard props{link} />
 */
var Dashboard = React.createClass({
	
	render: function() {
		return (
			<div id="sb-site">
				<Top link={this.props.link} />
			    
				<Sidebar link={this.props.link} />
				
				<RightWrap link={this.props.link} />
				
				<SidebarRight link={this.props.link} />
            </div>
		); 
	}
});

return Dashboard;
});