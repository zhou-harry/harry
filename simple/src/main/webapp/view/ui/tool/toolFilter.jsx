define(['react-addons','app'], 
function(React,App) {	
	/**
	 * <Filter props{request} />
	 */
	var Filter = React.createClass({
		handleClick: function(e){
			var value = this.refs.filter.getDOMNode().value.trim(); 
			this.props.request(value);
			e.preventDefault();
		},
		render: function() {
			return (
	                <div className="row" style={{marginBottom:'10px'}}>
                        <div className="col-sm-6">
                            <input className="form-control" ref="filter" placeholder="Search..." type="text"/>
                        </div>
                       
                        <div className="col-sm-6">
                            <a href="#" className="pull-left btn btn-info" title="筛选" onClick= {this.handleClick}>
                            	筛选
                        	</a>
                        </div>
                    </div>
			); 
		}
	});
	
  return Filter;
});