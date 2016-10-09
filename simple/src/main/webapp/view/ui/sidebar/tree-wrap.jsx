define(['react-addons','app','jsx!nest-tree','jsx!nest-dash'], 
function(React,App,NestTree,NestDash) {
/**
 * <TreeWrap props{} />
 */
var TreeWrap = React.createClass({
	getInitialState: function() {
		return {
			data: [],
		};      
	},
	componentWillMount: function() { 
		$.ajax({     
			url: "menu/menuByParent?parentId=S",   
			type : "post",
			dataType : "json",
			success: function(data) {
				if (this.isMounted()) {
					//alert(JSON.stringify(data));
					this.setState({data: data});
				}
			}.bind(this)   
		});
	},
	componentDidMount: function(){
		App.tooltip();
	},
	render: function() {
		var contentLink=this.props.link;
		var Nodes = this.state.data.map(
			function (ele,i) {
				var props = { link: contentLink,
							  element: ele,
				};
				if('MT001'==ele.typeId){
					return (<NestTree key={i}{...props} />); 
				}else if('MT002'==ele.typeId){
					return (<NestDash.Dash key={i}{...props} />); 
				}else if('MT003'==ele.typeId){
					return (<NestDash.List key={i}{...props} />); 
				}
			}
		);
		return (
			<div className="skin-part">
	            <div id="tree-wrap">
	                <div className="side-bar">
	                	{Nodes}
        			</div>
        		</div>
	        </div>
		); 
	}
});

return TreeWrap;
});