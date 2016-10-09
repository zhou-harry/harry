define(['react-addons','jquery','app','jsx!toolMixin'], 
function(React,$,App,ToolMixin) {
/**
 * <Tree props{link,data} />
 */
var Tree = React.createClass({
	mixins: [ToolMixin], // 引用 mixin
	componentDidMount : function(){
		this.setTimeout(App.treeview, App.mid); 
	},
	render: function() {
		var contentLink=this.props.link;
		var Nodes = this.props.data.map(
			function (ele,i) {
				var props = {
					element: ele,
					link: contentLink
				};
				return (<Data key={i}{...props} />); 
			}
		);
		return (
			<table id="table1" className="controller">
                {Nodes}
            </table>
		); 
	}
});
/**
 * <Data props{link,element} />
 */
var Data = React.createClass({
	render: function() {
		return (
			<tr data-level={this.props.element.level} data-id={this.props.element.isleaf} id={this.props.element.id}>
                <td>{this.props.element.title}</td>
            </tr>
		); 
	}
});
return Tree;
});