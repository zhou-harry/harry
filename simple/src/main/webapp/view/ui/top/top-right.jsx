define(['react-addons','jquery'], function(React,$) {
/**
 * <TopRight props{data} />
 */
var TopRight = React.createClass({
	render: function() {
		var dataRight={src:"./assets/img/1.jpg",name:"Harry",data:[{href:"#",style:"entypo-user",title:"My Profile"},
																   {href:"#",style:"entypo-vcard",title:"Account Setting"},
																   {href:"#",style:"entypo-basket",title:"Purchase"}]};
		return (
			<ul style={{marginRight: '0px'}} className="nav navbar-nav navbar-right">
				<TopUser link={this.props.link} data={dataRight.data}/>
				<TopSetting/>
				<TopAdd/>
			</ul>
		); 
	}
});

/**
 * <TopUser props{link} />
 */
var TopUser = React.createClass({
	getInitialState: function() {
		return {
			element: ''
		};      
	},
	componentWillMount: function() { 
		$.ajax({     
			url: "user/sessionUser",   
			type : "post",
			dataType : "json",   
			success: function(data) {
				if (this.isMounted()) {
					this.setState({element: data});
				}
			}.bind(this)   
		});
	},
	render: function() {
		var Nodes = this.props.data.map(
			function (ele,i) {
				var props = { 
					  		href: ele.href,
							style: ele.style,
						  	title: ele.title,
				};
				return (<TopEntypoList key={i}{...props} />); 
			}
		);
		return (
			<li>
				<a data-toggle="dropdown" className="dropdown-toggle" href="#">
					<img alt="" className="admin-pic img-circle" src={this.state.element.photo}/>
					Hi,{this.state.element.descript}
					<b className="caret"></b>
				</a>
				<ul role="menu" className="dropdown-setting dropdown-menu">
					{Nodes}
					<li className="divider"/>
					<li className="hidden-xs">
						<a href="user/logout" >
							<span className="entypo-logout"></span>
							&#160;&#160;logout
						</a>
					</li>
				</ul>
			</li>
		); 
	}
});

/**
 * <TopEntypoList props{href,style,title} />
 */
var TopEntypoList = React.createClass({
	render: function() {
		return (
			<li className="hidden-xs">
				<a href={this.props.href}>
					<span className={this.props.style}></span>
					&#160;&#160;{this.props.title}
				</a>
			</li>
		); 
	}
});

/**
 * <TopSetting props{} />
 */
var TopSetting = React.createClass({
	render: function() {
		return (
			<li className="hidden-xs">
				<a data-toggle="dropdown" className="dropdown-toggle" href="#">
					<span className="icon-gear"></span>
					&#160;&#160;Setting
				</a>
				<ul role="menu" className="dropdown-setting dropdown-menu">
					<li className="theme-bg">
						<div id="button-bg"></div>
						<div id="button-bg2"></div>
						<div id="button-bg3"></div>
						<div id="button-bg5"></div>
						<div id="button-bg6"></div>
						<div id="button-bg7"></div>
						<div id="button-bg8"></div>
						<div id="button-bg9"></div>
						<div id="button-bg10"></div>
						<div id="button-bg11"></div>
						<div id="button-bg12"></div>
						<div id="button-bg13"></div>
					</li>
				</ul>
			</li>
		); 
	}
});

/**
 * <TopAdd props{} />
 */
var TopAdd = React.createClass({
	render: function() {
		return (
			<li className="hidden-xs">
				<a className="toggle-left" href="#"> 
					<span style={{fontSize: '20px'}} className="entypo-list-add"></span>
				</a>
			</li>
		); 
	}
});

return TopRight;
});