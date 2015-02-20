$(function() {
    $('.scrollable').livequery(function() {
        var resultTableContainer = $(this).find('#resultTableContainer');
        var leftHeaderContainer = $(this).find('#leftHeaderContainer');
        var headerContainer = $(this).find('#headerContainer');

        resultTableContainer.scroll(function() {
            leftHeaderContainer.scrollTop(resultTableContainer.scrollTop());
            headerContainer.scrollLeft(resultTableContainer.scrollLeft());
        });
    });
});
