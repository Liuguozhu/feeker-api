var onOff = true;
var thisClick = -1;
/**
 $('li span').on('click', function () {
    if (thisClick != $(this).parent().index()) {
        $(this).css({'border-bottom': '1px solid #E6E6E6'}).parent().siblings().find('span').css({'border-bottom': 'none'})
        $(this).siblings('i').css({'display': 'block'}).parent().siblings().find('i').css({'display': 'none'})
        $(this).css({'background': 'url(front/img/top.png) no-repeat 6% center'}).parent().siblings().find('span').css({'background': 'url(front/img/set.png) no-repeat 6% center'})
        $(this).siblings('i').find('p').on('click', function () {
            $(this).css({
                'background': '#3eb94e',
                'color': '#fff'
            }).parent().siblings().find('p').css({'background': ' ', 'color': ' '})
            $(this).siblings('div').css({'display': 'block'}).parent().siblings().find('div').css({'display': 'none'})
        })
        onOff = false;
    } else if (onOff) {
        $(this).css({'border-bottom': '1px solid #E6E6E6'}).parent().siblings().find('span').css({'border-bottom': 'none'})
        $(this).siblings('i').css({'display': 'block'}).parent().siblings().find('i').css({'display': 'none'})
        $(this).css({'background': 'url(front/img/top.png) no-repeat 6% center'})
        $(this).siblings('i').find('p').on('click', function () {
            $(this).css({
                'background': '#3eb94e',
                'color': '#fff'
            }).parent().siblings().find('p').css({'background': ' ', 'color': ' '})
            $(this).siblings('div').css({'display': 'block'}).parent().siblings().find('div').css({'display': 'none'})
        })
        onOff = false;
    } else {
        $(this).css({'border-bottom': 'none'})
        $(this).siblings('i').css({'display': 'none'})
        $(this).css({'background': 'url(front/img/set.png) no-repeat 6% center'})
        $(this).siblings('i').find('p').on('click', function () {
            $(this).siblings('div').css({'display': 'none'});
        })
        onOff = true;
    }
    thisClick = $(this).parent().index();
});
 **/
$('li span').on('click', function () {
    if (thisClick != $(this).parent().index()) {
        $(this).css({'border-bottom': '1px solid #E6E6E6'})
            .parent().siblings().find('span').css({'border-bottom': 'none'})
        $(this).siblings('i').css({'display': 'block'}).parent().siblings().find('i').css({'display': 'none'})
        $(this).css({'background': 'url(front/img/top.png) no-repeat 6% center'})
            .parent().siblings().find('span').css({'background': 'url(front/img/set.png) no-repeat 6% center'})
        onOff = false;
    } else if (onOff) {
        $(this).css({'border-bottom': '1px solid #E6E6E6'})
            .parent().siblings().find('span').css({'border-bottom': 'none'})
        $(this).siblings('i').css({'display': 'block'}).parent().siblings().find('i').css({'display': 'none'})
        $(this).css({'background': 'url(front/img/top.png) no-repeat 6% center'})

        onOff = false;
    } else {
        $(this).css({'border-bottom': 'none'})
        $(this).siblings('i').css({'display': 'none'})
        $(this).css({'background': 'url(front/img/set.png) no-repeat 6% center'})
        onOff = true;
    }
    thisClick = $(this).parent().index();
});

$('li span').siblings('i').find('p').on('click', function () {
    $(this).parent().parent().parent().parent().siblings().find('p').css({'background': ' ', 'color': ' '});
    $(this).css({ 'background': '#3eb94e','color': '#fff' }).parent().siblings().find('p').css({'background': ' ', 'color': ' '});
    var li = $(this).parent();
    var url = $(this).parent().attr("data-html");
    $("#iframe").attr("src", url);
});

$(function () {
    $(".all li span").eq(0).css({'background': 'url(front/img/top.png) no-repeat 6% center'});
    $(".all li span").eq(0).css({'border-bottom': '1px solid #E6E6E6'})
        .parent().siblings().find('span').css({'border-bottom': 'none'});
    $(".all li span").eq(0).siblings('i').find('p').eq(0).css({
        'background': '#3eb94e',
        'color': '#fff'
    });
    onOff = false;
    thisClick = $(".all li span").index();
    $(".all li i").eq(0).css({'display': 'block'});
    var url = $(".all li i").find("li").eq(0).attr("data-html");
    $("#iframe").attr("src", url);
});