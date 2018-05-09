$(function(){
    /*瀑布流初始化设置*/
	var $grid = $('.grid').masonry({
		itemSelector : '.grid-item',
		gutter:10
    });
	
    // layout Masonry after each image loads
	$grid.imagesLoaded().done( function() {
		//console.log('uuuu===');
	  $grid.masonry('layout');
	});
})


