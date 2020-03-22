// 教师展示轮播
$(function() {
    var width_ = $(".switchable_triggers>li").length * 20 + "px";
    $(".switchable_triggers").css("width", width_);
    $(".switchable_triggers").css("margin", "10px auto")
    $(".carousel_box>a").eq(0).clone().appendTo($(".carousel_box"))
    $(".carousel_box>a").eq(1).clone().appendTo($(".carousel_box"))
    $(".carousel_box>a").eq(2).clone().appendTo($(".carousel_box"))
    $(".carousel_box>a").eq(3).clone().appendTo($(".carousel_box"))
    var current_index = 0;
    var total = $(".carousel_box>a").length - 4;

    function carousel_play() {
        current_index++;
        if (current_index == total) {
            $(".switchable_triggers>li").eq(0).css("background-position", "-80px 0px");
            $(".switchable_triggers>li").eq(0).siblings().css("background-position", "-80px -10px");
        }
        if (current_index > total) {
            $(".carousel_box").css({
                marginLeft: 0
            });
            current_index = 1;
        }
        $(".carousel_box").stop().animate({
            marginLeft: -300 * current_index + "px"
        }, 500);
        $(".switchable_triggers>li").eq(current_index).css("background-position", "-80px 0px");
        $(".switchable_triggers>li").eq(current_index).siblings().css("background-position", "-80px -10px");
    }
    var timer = setInterval(carousel_play, 2000)
    $(".switchable_triggers>li").hover(function() {
        switchable_triggers_index = $(this).index();
        clearInterval(timer);
        current_index = switchable_triggers_index - 1;
        carousel_play();
    }, function() {
        timer = setInterval(carousel_play, 2000);
    })
    $(".carousel").mousemove(function() {
        clearInterval(timer);
    });
    $(".carousel").mouseleave(function() {
        timer = setInterval(carousel_play, 2000);
    })
});