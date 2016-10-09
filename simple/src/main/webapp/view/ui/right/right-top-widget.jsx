define(['react-addons'], function(React) {	
	/**
	 * <Widget data(link)/>
	 */
	var Widget = React.createClass({
		
		render: function() {
			
			return (
				<div className="col-sm-2">
					<div className="devider-vertical visible-lg"></div>
					<div className="btn-group btn-wigdet pull-right visible-lg">
						<div className="btn">Widget</div>
						<button data-toggle="dropdown" className="btn dropdown-toggle"
							type="button">
							<span className="caret"></span> <span className="sr-only">Toggle
								Dropdown</span>
						</button>
						<ul role="menu" className="dropdown-menu">
							<li><a href="#"> <span
									className="entypo-plus-circled margin-iconic"></span>Add New
							</a></li>
							<li><a href="#"> <span
									className="entypo-heart margin-iconic"></span>Favorite
							</a></li>
							<li><a href="#"> <span className="entypo-cog margin-iconic"></span>Setting
							</a></li>
						</ul>
					</div>
				</div>
			); 
		}
	});
	
  return Widget;
});