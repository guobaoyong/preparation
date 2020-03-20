
var testUrl;

$(function() {

	var hh = document.documentElement.clientHeight;
	var ls = document.documentElement.clientWidth;
	if (ls < 640) {
		$(".select dt").click(function() {
			if ($(this).next("div").css("display") == 'none') {
				$(".theme-popover-mask").height(hh);
				$(".theme-popover").css("position", "fixed");
				$(this).next("div").slideToggle("slow");
				$(".select div").not($(this).next()).hide();
			}
			else{
				$(".theme-popover-mask").height(0);
				$(".theme-popover").css("position", "static");
				$(this).next("div").slideUp("slow");;
			}
		});

		$(".eliminateCriteria").on("click", function() {
			$(".dd-conent").hide();
		});

		$(".select dd").on("click", function() {
			$(".theme-popover-mask").height(0);
			$(".theme-popover").css("position", "static");
			$(".dd-conent").hide();
		});

		$(".theme-popover-mask").on("click", function() {
			$(".theme-popover-mask").height(0);
			$(".theme-popover").css("position", "static");
			$(".dd-conent").hide();
		});

	}

	$("span.love").click(function() {
		$(this).toggleClass("active");
	});

	$(".Item2").on("click",function(){
		//选择的高校
		var listsNumber = $(this).data("listsnumber");
		var deptname = $(this).data("deptname");
		$("#selectB").val(listsNumber);
		$("#selectB").text(deptname);
	});
	$(".Item3").on("click",function(){
		//选择的类别
		var listsNumber = $(this).data("listsnumber");
		var srcTypeName = $(this).data("srctypename");
		$("#selectC").val(listsNumber);
		$("#selectC").text(srcTypeName);
	});

	//自带样式效果
	$(document).on("click","#selectB", function() {
		$(this).remove();
		$("#select2 .select-all").addClass("selected").siblings().removeClass("selected");
	});

	$(document).on("click","#selectC", function() {
		$(this).remove();
		$("#select3 .select-all").addClass("selected").siblings().removeClass("selected");
	});


	$(document).on("click",".select dd", function() {
		if ($(".select-result dd").length > 1) {
			$(".select-no").hide();
			$(".eliminateCriteria").show();
			$(".select-result").show();
		} else {
			$(".select-no").show();
			$(".select-result").hide();
		}
	});

	//清除按钮
	$(".eliminateCriteria").on("click", function() {
		$("#selectB").val("");
		$("#selectC").val("");
		$(".select-all").addClass("selected").siblings().removeClass("selected");
		$(".eliminateCriteria").hide();
		$(".select-no").show();
		$(".select-result").hide();
	});

//	高度改变
	$(".select-all").on('click',function () {
		var allText = $(this).children().text();
		if(allText=="全部"){
			$(this).parent().css("height","auto");
			$(this).children().text("收起");
		}else{
			$(this).parent().css("height","62px");
			$(this).children().text("全部");
		}
	})
});

function formSubmit() {
	// 取高校、类别、关键词
	var selectB = $("#selectB").val();
	var selectC = $("#selectC").val();
	var q = $("#q").val();
	window.location.href = '/front/article?deptName='+selectB+'&arcType='+selectC+'&q='+q;
}


