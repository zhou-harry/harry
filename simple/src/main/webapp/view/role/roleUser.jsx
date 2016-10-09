define(['react-addons','app','jsx!block-half','jsx!table-dynamic','jsx!tree','jsx!toolMixin','jsx!toolFilter','jsx!block'], 
function(React,App,Block,TableDynamic,Tree,ToolMixin,ToolFilter,Block2) {	
	/**
	 * <Role data(link)/>
	 */
	var Role = React.createClass({
		mixins: [ToolMixin], // 引用 mixin
		getInitialState: function() {
			return {
				title: [],
				data: [],
				tree: [],
			};
		},
		componentWillMount: function() {
			var headData=[{key:'roleid',title:'角色编号'},
						{key:'rolename',title:'角色名称'},
						{key:'parentid',title:'父角色编号'},
						{key:'status',title:'状态'}];
			this.setState({title: headData});
			$.ajax({
				url: "config/roleTree",   
				type : "post",
				dataType : "json",
				success: function(data) {
					if (this.isMounted()) {
						//alert(JSON.stringify(data));
						this.setState({tree: data,});
					}
				}.bind(this)
			});
		},
		componentDidMount : function(){
			this.setTimeout(App.toggle, App.min); 
		},
		filterHandle: function(value){
			alert(value);
		},
		render: function() {
			var contentLink=this.props.link;
			return (
				<Block.Wrap>
					<Block.Half id='basic' title='角色组' size='3'>
						<Tree data={this.state.tree}/>
					</Block.Half>
					<Block.Half id='basic1' title='用户分组信息' size='9'>
						<ToolFilter request={this.filterHandle}/>
						<TableDynamic headData={this.state.title} data={this.state.data}>
							<button className="btn btn-info" type="submit">添加角色</button>
						</TableDynamic>
					</Block.Half>
				</Block.Wrap>
			); 
		}
	});
	
  return Role;
});