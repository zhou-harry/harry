define(['react-addons','app'], function(React,App) {
/**
 * <Toggle props{} />
 */
var Toggle = React.createClass({
	componentDidMount: function(){
		App.toggle_skin_select();
	},
	render: function() {
		return (
			<a id="toggle">
	            <span className="entypo-menu"></span>
	        </a>
		); 
	}
});

return Toggle;
});