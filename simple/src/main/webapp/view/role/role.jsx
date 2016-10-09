define(['react-addons','app','jsx!block','jsx!table-dynamic','jsx!tree','jsx!toolMixin','jsx!toolFilter'], 
function(React,App,Block,TableDynamic,Tree,ToolMixin,ToolFilter) {	
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
			$.ajax({
				url: "config/roleInfo",   
				type : "post",
				dataType : "json",
				success: function(data) {
					if (this.isMounted()) {
						//alert(JSON.stringify(data));
						this.setState({title: headData,data: data,});
					}
				}.bind(this)
			});
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
			$.ajax({
				url: "config/roleFilter?filter="+encodeURI(encodeURI(value)),   
				type : "post",
				dataType : "json",
				success: function(data) {
					if (this.isMounted()) {
						this.setState({data: data,});
					}
				}.bind(this)
			});
		},
		render: function() {
			var contentLink=this.props.link;
			var Nodes = this.state.data.map(
				function (ele,i) {
					var props = {
						element: ele,
						link: contentLink
					};
					return (<Data key={i}{...props} />); 
				}
			);
			return (
				<Block id='basic3' title='角色详细信息'>
					<ToolFilter request={this.filterHandle}/>
					<TableDynamic headData={this.state.title} data={this.state.data}>
						{Nodes}
						<button className="btn btn-info" type="submit">添加角色</button>
					</TableDynamic>
				</Block>
			); 
		}
	});
	/**
	 * <Data props{link,element} />
	 */
	var Data = React.createClass({
		render: function() {
			var statusClass='status-metro status-active';
			var statusName='生效';
			if(this.props.element.status==0){
				statusClass='status-metro status-disabled';
				statusName='失效';
			}
			return (
	                <tr>
	                    <td><a href='#'>{this.props.element.roleid}</a></td>
	                    <td>{this.props.element.rolename}</td>
	                    <td>{this.props.element.parentid}</td>
	                    <td>
	                        <span className={statusClass} title={statusName}>{statusName}</span>
	                    </td>
	                </tr>
			); 
		}
	});
	
  return Role;
});