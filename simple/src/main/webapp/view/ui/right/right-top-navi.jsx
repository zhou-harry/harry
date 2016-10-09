define(['react-addons'], function(React) {	
	/**
	 * <Navi data(link)/>
	 */
	var Navi = React.createClass({
		
		render: function() {
			
			return (
				<ul id="breadcrumb">
					<li><span className="entypo-home"></span></li>
					<li><i className="fa fa-lg fa-angle-right"></i></li>
					<li><a href="#" title="Sample page 1">Form</a></li>
					<li><i className="fa fa-lg fa-angle-right"></i></li>
					<li><a href="#" title="Sample page 1">Form Element</a></li>
					<li className="pull-right">
						<div className="input-group input-widget">
							<input style={{borderRadius: '15px'}} type="text"
								placeholder="Search..." className="form-control"/>
						</div>
					</li>
				</ul>
			); 
		}
	});
	
  return Navi;
});