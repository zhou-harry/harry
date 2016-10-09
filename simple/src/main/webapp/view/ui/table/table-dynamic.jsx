define(['react-addons','jquery','app','jsx!toolMixin'], 
function(React,$,App,ToolMixin) {
/**
 * <Table props{link,headData,children} />
 */
var Table = React.createClass({
	mixins: [ToolMixin], // 引用 mixin
	componentDidMount : function(){
		//this.setTimeout(App.footable_table, App.mid); 
	},
	componentWillReceiveProps : function(nextProps){
		this.setTimeout(App.footable_table, App.mid); 
	},
	render: function() {
		return (
			<table className="table-striped footable-res footable metro-blue" data-page-size="10">
                <Thead data={this.props.headData}/>
                <tbody>
                	 {this.props.children[0]}
                </tbody>
                <tfoot>
                    <tr>
                    	<td colSpan={this.props.headData.length/2}>
                            {this.props.children[1]}
                        </td>
                        <td colSpan={this.props.headData.length-this.props.headData.length/2}>
                            <div className="pagination pagination-centered"></div>
                        </td>
                    </tr>
                </tfoot>
            </table>
		); 
	}
});

/**
 * <Thead props{data} />
 */
var Thead = React.createClass({
	render: function() {
		var Nodes = this.props.data.map(
			function (ele,i) {
				return (<th key={i} id={ele.key} >{ele.title}</th>); 
			}
		);
		return (
            <thead>
                <tr>
                    {Nodes}
                </tr>
            </thead>
		); 
	}
});


return Table;
});