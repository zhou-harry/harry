define(['react-addons'], function(React) {	
	/**
	 * <Container data(title,url)/>
	 */
	var Container = React.createClass({
		mixins: [React.addons.LinkedStateMixin],
		
		getInitialState: function() {
			return {
				data: this.props.data,
			};
  		},
		render: function() {
			var contentLink = this.linkState('data');
			
			var Content = require(this.state.data.url);
			return (
				<div>
					<Content link={contentLink}/>
				</div>
			); 
		}
	});
	
  return Container;
});