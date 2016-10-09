define(['react-addons'], function(React) {
	var SetMixin = {
	  componentWillMount: function() {
	    this.intervals = [];
	  },
	  setInterval: function() {
	    this.intervals.push(setInterval.apply(null, arguments));
	  },
	  setTimeout: function() {
	    this.intervals.push(setTimeout.apply(null, arguments));
	  },
	  componentWillUnmount: function() {
	    this.intervals.map(clearInterval);
	  }
	};
	
	return SetMixin;
});