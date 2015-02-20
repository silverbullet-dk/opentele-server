$(function () {
    var leftMenu = $('#leftmenu');
    var leftMenuOffset = leftMenu.offset();
    var leftMenuTop = leftMenuOffset.top;
    var menuHeight = leftMenu.height();

    // Make a clone of the menu
    $('<div/>').addClass('leftmenu').css({
        width: leftMenu.width(),
        height: leftMenu.height(),
        float: 'left',
        display: 'block',
        margin: '10px 10px 0 10px'
    }).insertBefore(leftMenu);

    // Attach the menu to the body and fix it to the window
    leftMenu.css({position: 'fixed', top: leftMenuOffset.top, left: leftMenuOffset.left, marginLeft: 0, marginTop: 0}).appendTo('body');

    // Make it follow the scroll
    $(window).on('scroll resize',function () {
        var windowTop = $(window).scrollTop();
        var windowHeight = $(window).height();

        var minimumTop = Math.min(10, windowHeight-menuHeight); // For very small screens
        var newMenuTop = windowTop > 0 ? Math.max(minimumTop, leftMenuTop - windowTop) : leftMenuTop;

        leftMenu.css('top', newMenuTop);
    }).trigger('scroll');
});
