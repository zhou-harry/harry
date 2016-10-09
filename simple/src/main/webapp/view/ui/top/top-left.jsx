define(['react-addons'], function(React) {
/**
 * <TopNavbar props{link} />
 */
var TopNavbar = React.createClass({
	getInitialState: function() {
		return {
			data: [],
		};      
	},
	componentWillMount: function() { 
		$.ajax({     
			url: "menu/menuByParent?parentId=TL",   
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
				var props = {
					element: ele,
					link: contentLink
				};
				return (<TopNavbarLi key={i}{...props} />); 
			}
		);
		return (
			<ul className="nav navbar-nav">
				{Nodes}
			</ul>
		); 
	}
});

/**
 * <TopNavbarLi props{link,element} />
 */
var TopNavbarLi = React.createClass({
	getInitialState: function() {
		return {
			data: [],
			noft: '12',
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
		var Nodes='';
		var contentLink=this.props.link;
		if(this.props.element.typeId=='MT007'){
			Nodes = this.state.data.map(
				function (ele,i) {
					var props = { 
						element: ele,
						link: contentLink,
					};
					return (<TopNavbarImg key={i}{...props} />); 
				}
			);
		}else if(this.props.element.typeId=='MT008'){
			Nodes = this.state.data.map(
				function (ele,i) {
					var props = { 
						element: ele,
						link: contentLink,
					};
					return (<TopNavbarMaki key={i}{...props} />); 
				}
			);
		}
		var Element;
		if(Nodes.length > 0){
			Element=<ul role="menu" className="dropdown-menu dropdown-wrap" style={{margin: '12px 0 0 0',width: '230px'}}>
					{Nodes}</ul>;
		}
		return (
			<li>
				<a data-toggle="dropdown" className="dropdown-toggle" href={this.props.element.href} title={this.props.element.name} onClick= {this.handleClick}>
					<i style={{fontSize: '19px'}} className={this.props.element.style}></i>
					<div className="noft-red">{this.state.noft}</div>
				</a>
				{Element}
			</li>
		); 
	}
});

/**
 * <TopNavbarImg props{link,element(url,img,name)} />
 */
var TopNavbarImg = React.createClass({
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
		var noft=this.state.noft.length>0?<b>{this.state.noft}</b>:'';
		return (
			<li>
				<a href={this.props.element.url} onClick= {this.handleClick}> 
					<img alt="" className="img-msg img-circle" src={this.props.element.img}/>
					{this.props.element.name}
					{noft}
				</a>
				<li className="divider"></li>
			</li>
		); 
	}
});

/**
 * <TopNavbarMaki props{link,element(url,style,name)} />
 */
var TopNavbarMaki = React.createClass({
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
		var noft=this.state.noft.length>0?<b>{this.state.noft}</b>:'';
		return (
			<li>
				<a href={this.props.element.url} onClick= {this.handleClick}> 
					<span style={{background: '#0DB8DF'}} className={this.props.element.style}/>
					<i>{this.props.element.name}</i> 
					{noft}
				</a>
				<li className="divider"></li>
			</li>
		); 
	}
});


return TopNavbar;
});