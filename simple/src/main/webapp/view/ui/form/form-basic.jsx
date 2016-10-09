define(['react-addons','jquery'], function(React,$) {
/**
 * <FormBasic props{} />
 */
var FormBasic = React.createClass({
	render: function() {
		return (
			<div className="form_center">
                <form role="form">
                    <div className="form-group">
                        <label htmlFor="exampleInputEmail1">Email address</label>
                        <input type="email" placeholder="Enter email" id="exampleInputEmail1" className="form-control"/>
                    </div>
                    <div className="form-group">
                        <label htmlFor="exampleInputPassword1">Password</label>
                        <input type="password" placeholder="Password" id="exampleInputPassword1" className="form-control"/>
                    </div>
                    <div className="form-group">
                        <label htmlFor="exampleInputFile">File input</label>
                        <input type="file" id="exampleInputFile"/>
                        <p className="help-block">Example block-level help text here.</p>
                    </div>
                    <div className="checkbox">
                        <label>
                            <input type="checkbox"/>Check me out
                        </label>
                    </div>
                    <button className="btn btn-info" type="submit">Submit</button>
                </form>
            </div>
		); 
	}
});

return FormBasic;
});