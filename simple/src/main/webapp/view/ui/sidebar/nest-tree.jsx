define(['react-addons','app'], function(React,App) {
/**
 * <NestTree props{link,element} />
 */
var NestTree = React.createClass({
	getInitialState: function() {
		return {
			data: [],
		};      
	},
	componentWillMount: function() { 
		$.ajax({     
			url: "menu/menuByParent?parentId="+this.props.element.menuid,   
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
	render: function() {
		var contentLink=this.props.link;
		var Nodes = this.state.data.map(
			function (ele,i) {
				var props = { link: contentLink,
							  element: ele,
				};
				return (<NestTreeLi key={i}{...props} />);
			}
		);
		return (
			<ul id="menu-showhide" className="topnav menu-left-nest">
                <li className="title-menu-left">
                    <span>{this.props.element.name}</span>
                    <i data-toggle="tooltip" className="entypo-cog pull-right config-wrap"></i>
                </li>
                {Nodes}
            </ul>
		); 
	}
});

/**
 * <NestTreeLi props{link,element} />
 */
var NestTreeLi = React.createClass({
	getInitialState: function() {
		return {
			data: [],
			noft: '',
		};      
	},
	componentWillMount: function() { 
		$.ajax({     
			url: "menu/menuByParent?parentId="+this.props.element.menuid,   
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
	handleClick: function(e) {
  		if ("#"!=this.props.element.url) {
			this.props.link.requestChange({
				title: this.props.element.name ,
				url: this.props.link.value.url,
				subUrl: this.props.element.url,
			});
		}
  		e.preventDefault();
  	},
	render: function() {
		var SubTree=this.state.data.length > 0 ?<NestTreeUl data={this.state.data}/>:'';
		return (
			<li>
                <a className="tooltip-tip ajax-load" href={this.props.element.url} title={this.props.element.name} onClick= {this.handleClick}>
                    <i className={this.props.element.style}></i>
                    <span>{this.props.element.name}</span>
                    <div className="noft-blue">{this.state.noft}</div>
                </a>
                {SubTree}
            </li>
		); 
	}
});

/**
 * <NestTreeUl props{link,data} />
 */
var NestTreeUl = React.createClass({
	componentDidMount: function(){
		App.accordionze();
	},
	render: function() {
		var contentLink=this.props.link;
		var Nodes = this.props.data.map(
			function (ele,i) {
				var props = { link: contentLink,
							  element: ele,
				};
				return (<SubLi key={i}{...props} />);
			}
		);
		return (
			<ul>{Nodes}</ul>
		); 
	}
});

/**
 * <SubLi props{link,element} />
 */
var SubLi = React.createClass({
	getInitialState: function() {
		return {
			noft: '',
		};      
	},
	handleClick: function(e) {
  		if ("#"!=this.props.element.url) {
			this.props.link.requestChange({
				title: this.props.element.name ,
				url: this.props.link.value.url,
				subUrl: this.props.element.url,
			});
		}
  		e.preventDefault();
  	},
	render: function() {
		return (
			<li>
                <a className="tooltip-tip2 ajax-load" href={this.props.element.url} title={this.props.element.name} onClick= {this.handleClick}>
                    <i className={this.props.element.style}></i>
                    <span>{this.props.element.name}</span>
                    <div className="noft-blue">{this.state.noft}</div>
                </a>
            </li>
		); 
	}
});

return NestTree;
});