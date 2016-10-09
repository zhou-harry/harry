define(['react-addons','app'], function(React,App) {
/**
 * <NestDash props{link,element} />
 */
var NestDash = React.createClass({
	getInitialState: function() {
		return {
			data: [],
		};      
	},
	componentWillMount: function() { 
		var data=[{title:"Avg. Traffic", value:"25%", type:"-1"}
			,{title:"Convertion Rate", value:"80%", type:"1"}
			,{title:"Visitors", value:"13Km", type:"1"}];
		this.setState({data: data});
	},
	render: function() {
		var Nodes = this.state.data.map(
			function (ele,i) {
				var props = { element: ele};
				return (<NestDashLi key={i}{...props} />); 
			}
		);
		return (
			<div className="side-dash">
	            <h3>
	                <span>{this.props.element.name}</span>
	            </h3>
	            <ul className="side-bar-list">
		            {Nodes}
	            </ul>
	        </div>
		); 
	}
});

/**
 * <NestDashLi props{element} />
 */
var NestDashLi = React.createClass({
	render: function() {
		return (
			<li>
				{this.props.element.title}
                <div className="linebar">{this.props.element.value}</div>
            </li>
		); 
	}
});

/**
 * <DashList props{link,element} />
 */
var DashList = React.createClass({
	getInitialState: function() {
		return {
			data: [],
		};      
	},
	componentWillMount: function() { 
		var data=[{title:"Avg. Traffic", value:"25%", type:"-1"}
			,{title:"Convertion Rate", value:"80%", type:"1"}
			,{title:"Visitors", value:"13Km", type:"1"}];
		this.setState({data: data});
	},
	render: function() {
		var Nodes = this.state.data.map(
			function (ele,i) {
				var props = { element: ele};
				return (<DashLi key={i}{...props} />); 
			}
		);
		return (
			<div className="side-dash">
	            <h3>
	                <span>{this.props.element.name}</span>
	            </h3>
	            <ul className="side-dashh-list">
	            	{Nodes}
	            </ul>
	        </div>
		); 
	}
});
/**
 * <DashLi props{element(title,value,type(1,-1))} />
 */
var DashLi = React.createClass({
	render: function() {
		var color=this.props.element.type==1?'green':'#AB6DB0';
		var classType=this.props.element.type==1?'fa fa-arrow-circle-up':'fa fa-arrow-circle-down';
		return (
			<li>
                <i style={{color:color}} className={classType}>
                	<span>{this.props.element.value}</span>
                </i>
            	{this.props.element.title}
            </li>
		); 
	}
});


return {
	Dash: NestDash,
	List: DashList,
};
});