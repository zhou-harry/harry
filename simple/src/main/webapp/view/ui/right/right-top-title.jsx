define(['react-addons'], function(React) {	
	/**
	 * <TopTitle data(link)/>
	 */
	var TopTitle = React.createClass({
		
		render: function() {
			
			return (
				<div className="col-sm-3">
					<h2 className="tittle-content-header">
						<i className="icon-document-edit"></i> <span>{this.props.link.value.title}</span>
					</h2>
				</div>
			); 
		}
	});
	
  return TopTitle;
});