define(['react-addons','jquery','app'], function(React,$,App) {
/**
 * <Block props{children} />
 */
var Block = React.createClass({
	render: function() {
		return (
			<div className="content-wrap">
                <div className="row">
                    {this.props.children}
                </div>
            </div>
		); 
	}
});
/**
 * <Half props{id,title,size,children} />
 */
var Half = React.createClass({
	render: function() {
		return (
			<div className={"col-sm-"+this.props.size}>
                <div className="nest" id={this.props.id+'Close'}>
                
                    <Title title={this.props.title} hrefid={this.props.id}/>

                    <div className="body-nest" id={this.props.id}>
                        {this.props.children}
                    </div>
                    
                </div>
            </div>
		); 
	}
});
/**
 * <Title props{hrefid,title} />
 *  <div className="titleClose">
 *       <a className="gone" href={'#'+this.props.hrefid+'Close'}>
 *          <span className="entypo-cancel"></span>
 *       </a>
 *   </div>
 */
var Title = React.createClass({
	
	render: function() {
		return (
			<div className="title-alt">
                <h6>{this.props.title}</h6>
                <div className="titleToggle">
                    <a className="nav-toggle-alt" href={'#'+this.props.hrefid}>
                        <span className="entypo-up-open"></span>
                    </a>
                </div>
            </div>
		); 
	}
});

return {
	Wrap: Block,
	Half: Half,
};
});