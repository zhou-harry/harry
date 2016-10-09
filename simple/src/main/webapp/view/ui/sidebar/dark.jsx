define(['react-addons','app'], function(React,App) {
/**
 * <Dark props{} />
 */
var Dark = React.createClass({
	render: function() {
		return (
			<div className="dark">
	            <form action="#">
	                <input type="text" name="search" className="search rounded id_search" placeholder="Search Menu..." autofocus=""/>
	            </form>
	        </div>
		); 
	}
});

return Dark;
});