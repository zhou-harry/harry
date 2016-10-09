define(['react-addons','app','jsx!block-half','jsx!block','jsx!form-basic','jsx!tree','jsx!testTick','jsx!toolMixin'], 
function(React,App,Block,Block2,FormBasic,Tree,TestTick,ToolMixin) {	
	/**
	 * <Index data(link)/>
	 */
	var Index = React.createClass({
		mixins: [ToolMixin], // 引用 mixin
		componentDidMount : function(){
			this.setTimeout(App.toggle, App.min); 
		},
		render: function() {
			
			return (
				<div>
					<Block.Wrap>
						<Block.Half id='basic1' title='主面板2' size='6'>
							<FormBasic />
						</Block.Half>
						<Block.Half id='basic2' title='主面板3' size='6'>
							<FormBasic />
						</Block.Half>
					</Block.Wrap>
					<Block2 id='role' title='测试案例' size='12'>
						<TestTick />
					</Block2>
				</div>
			); 
		}
	});
	
  return Index;
});