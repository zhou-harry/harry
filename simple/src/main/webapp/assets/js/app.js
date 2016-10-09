 define(function() {
	//Timeout
	var min=1,mid=2,max=5;
	//BACKGROUND CHANGER
 	var background=function (){
 		 (function($) {
 		      $("#button-bg").click(function() {
 		          $("body").css({
 		              "background": "url('assets/img/bg5.jpg')no-repeat center center fixed"
 		          });
 		      });
 		      $("#button-bg2").click(function() {
 		          $("body").css({
 		              "background": "url('assets/img/bg2.jpg')no-repeat center center fixed"
 		          });
 		      });


 		      $("#button-bg3").click(function() {
 		          $("body").css({
 		              "background": "url('assets/img/bg.jpg')no-repeat center center fixed"
 		          });


 		      });

 		      $("#button-bg5").click(function() {
 		          $("body").css({
 		              "background": "url('assets/img/giftly.png')repeat"
 		          });

 		      });

 		      $("#button-bg6").click(function() {
 		          $("body").css({
 		              "background": "#2c3e50"
 		          });

 		      });

 		      $("#button-bg7").click(function() {
 		          $("body").css({
 		              "background": "url('assets/img/bg3.png')repeat"
 		          });

 		      });
 		      $("#button-bg8").click(function() {
 		          $("body").css({
 		              "background": "url('assets/img/bg8.jpg')no-repeat center center fixed"
 		          });
 		      });
 		      $("#button-bg9").click(function() {
 		          $("body").css({
 		              "background": "url('assets/img/bg9.jpg')no-repeat center center fixed"
 		          });
 		      });

 		      $("#button-bg10").click(function() {
 		          $("body").css({
 		              "background": "url('assets/img/bg10.jpg')no-repeat center center fixed"
 		          });
 		      });
 		      $("#button-bg11").click(function() {
 		          $("body").css({
 		              "background": "url('assets/img/bg11.jpg')no-repeat center center fixed"
 		          });
 		      });
 		      $("#button-bg12").click(function() {
 		          $("body").css({
 		              "background": "url('assets/img/bg12.jpg')no-repeat center center fixed"
 		          });
 		      });

 		      $("#button-bg13").click(function() {
 		          $("body").css({
 		              "background": "url('assets/img/bg13.jpg')repeat"
 		          });

 		      });
 		      /**
 		       * Background Changer end
 		       */
 		  })(jQuery);
	};
	//TOGGLE CLOSE
 	var toggle=function (){
 		(function($) {
 		    $('.nav-toggle').click(function() {
 		        //get collapse content selector
 		        var collapse_content_selector = $(this).attr('href');

 		        //make the collapse content to be shown or hide
 		        var toggle_switch = $(this);
 		        $(collapse_content_selector).slideToggle(function() {
 		            if ($(this).css('display') == 'block') {
 		                //change the button label to be 'Show'
 		                toggle_switch.html('<span class="entypo-minus-squared"></span>');
 		            } else {
 		                //change the button label to be 'Hide'
 		                toggle_switch.html('<span class="entypo-plus-squared"></span>');
 		            }
 		        });
 		    });

 		    $('.nav-toggle-alt').click(function() {
 		        //get collapse content selector
 		        var collapse_content_selector = $(this).attr('href');

 		        //make the collapse content to be shown or hide
 		        var toggle_switch = $(this);
 		        $(collapse_content_selector).slideToggle(function() {
 		            if ($(this).css('display') == 'block') {
 		                //change the button label to be 'Show'
 		                toggle_switch.html('<span class="entypo-up-open"></span>');
 		            } else {
 		                //change the button label to be 'Hide'
 		                toggle_switch.html('<span class="entypo-down-open"></span>');
 		            }
 		        });
 		        return false;
 		    });
 		    //CLOSE ELEMENT
 		    $(".gone").click(function() {
 		        var collapse_content_close = $(this).attr('href');
 		        $(collapse_content_close).hide();
 		    });
		 })(jQuery);
	};
	//tooltip
	var tooltip=function (){
		(function($) {
		    $('.tooltitle').tooltip();
		 })(jQuery);
	};
	//Acordion and Sliding menu
	var accordionze=function (){
		(function($) {
			$(".topnav").accordionze({
		        accordionze: true,
		        speed: 500,
		        closedSign: '<img src="assets/img/plus.png">',
		        openedSign: '<img src="assets/img/minus.png">'
		    });
		 })(jQuery);
	};
	//toggle skin select
	var toggle_skin_select=function (){
		(function($) {
			$("#skin-select #toggle").click(function() {
				if($(this).hasClass('active')) {
					$(this).removeClass('active')
					$('#skin-select').animate({ left:0 }, 100);	
					$('.wrap-fluid').css({"width":"auto","margin-left":"250px"});
					$('.navbar').css({"margin-left":"240px"});
					$('#skin-select li').css({"text-align":"left"});
					$('#skin-select li span, ul.topnav h4, .side-dash, .noft-blue, .noft-purple-number, .noft-blue-number, .title-menu-left').css({"display":"inline-block", "float":"none"});
					//$('body').css({"padding-left":"250px"});
					$('.ul.topnav li a:hover').css({" background-color":"green!important"});
					$('.ul.topnav h4').css({"display":"none"});
					$('.tooltip-tip2').addClass('tooltipster-disable');
					$('.tooltip-tip').addClass('tooltipster-disable');
					$('.datepicker-wrap').css({"position":"absolute", "right":"300px"});
					$('.skin-part').css({"visibility":"visible"});
					$('#menu-showhide, .menu-left-nest').css({"margin":"10px"});
					$('.dark').css({"visibility":"visible"});
					$('.search-hover').css({"display":"none"});
					$('.dropdown-wrap').css({"position":"absolute", "left":"0px", "top":"53px"});
				} else {
					$(this).addClass('active')
					//$('#skin-select').animate({ left:-200 }, 100);
					$('#skin-select').animate({ left:-200 }, 100);
					$('.wrap-fluid').css({"width":"auto", "margin-left":"50px"});
					$('.navbar').css({"margin-left":"50px"});
					$('#skin-select li').css({"text-align":"right"});
					$('#skin-select li span, ul.topnav h4, .side-dash, .noft-blue, .noft-purple-number, .noft-blue-number, .title-menu-left').css({"display":"none"});
					//$('body').css({"padding-left":"50px"});
					$('.tooltip-tip2').removeClass('tooltipster-disable');
					$('.tooltip-tip').removeClass('tooltipster-disable');
					$('.datepicker-wrap').css({"position":"absolute", "right":"84px"});
					$('.skin-part').css({"visibility":"visible", "top":"3px"});
					$('.dark').css({"visibility":"hidden"});
					$('#menu-showhide, .menu-left-nest').css({"margin":"0"});
					$('.search-hover').css({"display":"block", "position":"absolute", "right":"-100px"});
					$('.dropdown-wrap').css({"position":"absolute", "left":"-10px", "top":"53px"});
				}
				return false;
			});
			// show skin select for a second
			setTimeout(function(){ $("#skin-select #toggle").addClass('active').trigger('click'); },10)
		 })(jQuery);
	};
	
	//Right Sliding menu
	var right_slid_menu=function (){
		(function($) {
			var mySlidebars = new $.slidebars();
	        $('.toggle-left').on('click', function() {
	            mySlidebars.toggle('right');
	        });
		 })(jQuery);
	};

	//Footable
	var footable_table=function (){
		(function($) {
			$('.footable-res').footable();
		 })(jQuery);
	};
	
	//Treeview
	var treeview=function (){
		(function($) {
			var table1 = $('#table1').tabelize({
	            fullRowClickable: true,
	            onReady: function() {
	                console.log('ready');
	            },
	            onBeforeRowClick: function() {
	                //console.log('onBeforeRowClick');
	            },
	            onAfterRowClick: function() {
	                //console.log('onAfterRowClick');
	            },
	        });
		 })(jQuery);
	};
	
    return{
    	background: background,
    	toggle: toggle,
    	tooltip: tooltip,
    	accordionze: accordionze,
    	toggle_skin_select: toggle_skin_select,
    	right_slid_menu: right_slid_menu,
    	footable_table: footable_table,
    	treeview: treeview,
    	min: min,
    	mid: mid,
    	max: max,
	};
});
 