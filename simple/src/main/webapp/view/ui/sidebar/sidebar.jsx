define(['react-addons','app','jsx!block','jsx!logo','jsx!toggle','jsx!dark','jsx!tree-wrap'], 
function(React,App,Block,Logo,Toggle,Dark,TreeWrap) {
/**
 * <Sidebar props{link} />
 */
var Sidebar = React.createClass({
	render: function() {
		return (
			<div id="skin-select">
		        <Logo />
		        
		        <Toggle/>
		        
		   		<Dark />
		
		        <TreeWrap link={this.props.link}/>
		    </div>
		); 
	}
});

return Sidebar;
});