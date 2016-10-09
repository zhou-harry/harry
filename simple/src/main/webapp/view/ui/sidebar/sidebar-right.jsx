define(['react-addons','app'], 
function(React,App) {
/**
 * RIGHT SLIDER CONTENT
 * <SidebarRight props{title,type,tabs[]} />
 */
var SidebarRight = React.createClass({
	componentDidMount: function(){
	},
	render: function() {
		return (
			<div className="sb-slidebar sb-right">
				<div className="right-wrapper">
					<div className="row">
						<h3>
							<span><i className="entypo-gauge"></i>&nbsp;&nbsp;MAIN WIDGET</span>
						</h3>
						<div className="col-sm-12">
		
							<div className="widget-knob">
								<span className="chart" style={{position: 'relative', dataPercent:'86'}}>
									<span className="percent"></span>
								</span>
							</div>
							<div className="widget-def">
								<b>Distance traveled</b> <br></br> <i>86% to the check point</i>
							</div>
		
							<div className="widget-knob">
								<span className="speed-car" style={{position: 'relative',dataPercent:'60'}}> <span className="percent2"></span>
								</span>
							</div>
							<div className="widget-def">
								<b>The average speed</b> <br></br> <i>30KM/h avarage speed</i>
							</div>
		
							<div className="widget-knob">
								<span className="overall" style={{position: 'relative',dataPercent:'25'}}>
									<span className="percent3"></span>
								</span>
							</div>
							<div className="widget-def">
								<b>Overall result</b> <br></br> <i>30KM/h avarage Result</i>
							</div>
						</div>
					</div>
				</div>
		
				<div style={{marginTop: '0'}} className="right-wrapper">
					<div className="row">
						<h3>
							<span><i className="entypo-chat"></i>&nbsp;&nbsp;CHAT</span>
						</h3>
						<div className="col-sm-12">
							<span className="label label-warning label-chat">Online</span>
							<ul className="chat">
								<li><a href="#"> <span> <img alt=""
											className="img-chat img-circle"
											src="./assets/img/1.jpg"/>
									</span><b>Dave Junior</b> <br></br>
									<i>Last seen : 08:00 PM</i>
								</a></li>
								<li><a href="#"> <span> <img alt=""
											className="img-chat img-circle"
											src="./assets/img/1.jpg"/>
									</span><b>Kenneth Lucas</b> <br></br>
									<i>Last seen : 07:21 PM</i>
								</a></li>
								<li><a href="#"> <span> <img alt=""
											className="img-chat img-circle"
											src="./assets/img/1.jpg"/>
									</span><b>Heidi Perez</b> <br></br>
									<i>Last seen : 05:43 PM</i>
								</a></li>
		
		
							</ul>
		
							<span className="label label-chat">Offline</span>
							<ul className="chat">
								<li><a href="#"> <span> <img alt=""
											className="img-chat img-offline img-circle"
											src="./assets/img/1.jpg"/>
									</span><b>Dave Junior</b> <br></br>
									<i>Last seen : 08:00 PM</i>
								</a></li>
								<li><a href="#"> <span> <img alt=""
											className="img-chat img-offline img-circle"
											src="./assets/img/1.jpg"/>
									</span><b>Kenneth Lucas</b> <br></br>
									<i>Last seen : 07:21 PM</i>
								</a></li>
								<li><a href="#"> <span> <img alt=""
											className="img-chat img-offline img-circle"
											src="./assets/img/1.jpg"/>
									</span><b>Heidi Perez</b> <br></br>
									<i>Last seen : 05:43 PM</i>
								</a></li>
								<li><a href="#"> <span> <img alt=""
											className="img-chat img-offline img-circle"
											src="./assets/img/1.jpg"/>
									</span><b>Kenneth Lucas</b> <br></br>
									<i>Last seen : 07:21 PM</i>
								</a></li>
								<li><a href="#"> <span> <img alt=""
											className="img-chat img-offline img-circle"
											src="./assets/img/1.jpg"/>
									</span><b>Heidi Perez</b> <br></br>
									<i>Last seen : 05:43 PM</i>
								</a></li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		); 
	}
});

return SidebarRight;
});