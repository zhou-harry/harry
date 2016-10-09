define(['react-addons','jquery','app','jsx!block','jsx!toolMixin'], 
function(React,$,App,Block,ToolMixin) {

var TickTock = React.createClass({
  mixins: [ToolMixin], // 引用 mixin
  getInitialState: function() {
    return {seconds: 0};
  },
  componentDidMount: function() {
    this.setInterval(this.tick, 1000); // 调用 mixin 的方法
    //this.setTimeout(this.tick, 1000); 
  },
  tick: function() {
    this.setState({seconds: this.state.seconds + 1});
  },
  render: function() {
    return (
      <p>
        React has been running for {this.state.seconds} seconds.
      </p>
    );
  }
});
return TickTock;
});