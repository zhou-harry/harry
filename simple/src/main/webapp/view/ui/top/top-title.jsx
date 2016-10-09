define(['react-addons'], function(React) {
/**
 * <TopTitle props{data} />
 */
var TopTitle = React.createClass({
	render: function() {
		var Nodes = this.props.data.map(
			function (ele,i) {
				var props = { 
							style: ele.style,
					  		value: ele.value,
				};
				return (<TopTitleUl key={i}{...props} />); 
			}
		);
		return (
			<div id="nt-title-container" className="navbar-left running-text visible-lg">
				{Nodes}
			</div>
		); 
	}
});

/**
 * <TopTitleUl props{style,value} />
 */
var TopTitleUl = React.createClass({
	render: function() {
		return (
			<ul className="date-top">
				<li className={this.props.style} style={{marginRight: '5px'}}/>
				<li>{this.props.value}</li>
			</ul>
		); 
	}
});


return TopTitle;
});