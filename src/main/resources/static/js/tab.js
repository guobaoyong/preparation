$(function(){
	var _index;
	$(".tab_head>li").click(function(){
		_index=$(this).index();
		$(this).siblings().removeClass("tab_head_current");
		$(this).addClass("tab_head_current");
		$(".tab_contain").eq(_index).siblings().removeClass("tab_contain_current");
		$(".tab_contain").eq(_index).addClass("tab_contain_current");
	});
});