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
	   var dataFall = [];
	   $(window).scroll(function(){
	   			$grid.masonry('layout');
                var scrollTop = $(this).scrollTop();
                var scrollHeight = $(document).height();
                var windowHeight = $(this).height(); 
                if(scrollTop + windowHeight == scrollHeight){
                	var itemsLength = 0;
                	itemsLength = $(".grid-item").length;
            		 $.ajax({
            			 type: "GET",
		           		 dataType:"json",
					     data:{itemsLength:itemsLength},
					     url:'http://simplepicwx.tunnel.qydev.com/wx/discover/getMoreDiscover',
			             success:function(result){
			            	if(result.length <= 0){
			            		//展示我是有底线的
			            	}else{
			            		dataFall = result;
			            		setTimeout(function(){
			            			appendFall();
						        },500)
			            	}
			             },
			             error:function(e){
			             	console.log('请求失败')
			             }
			         })
                }
         })  
        
        function appendFall(){
          $.each(dataFall, function(index ,value) {
          	var dataLength = dataFall.length;
          	$grid.imagesLoaded().done( function() {
	        $grid.masonry('layout');
	           });
	      var detailUrl;
      	  var $griDiv = $('<div class="grid-item item">');
      	  
      	  var $aUrl = $('<a href="/wx/discover/getDiscoverInfo?id=' + value.id + '" class="dsb">');
      	  $aUrl.appendTo($griDiv);
      	  
      	  var $img = $("<img class='item-img'>");
      	  $img.attr('src',value.pictureUrl).appendTo($aUrl);


      	  var $section = $('<section class="section-p">');
      	  $section.appendTo($aUrl);
      	  var $p1 = $("<p class='name'>");
      	  $p1.html('<img class="index-user-headimg" src="'+value.avatarUrl+'">'+value.nickName+'</p>').appendTo($section);

      	  var $p2 = $('<div class="count-wrap">');
      	  var $str = '<span class="comment">'+value.shareNumber+'</span><span class="praise">'+value.likeNumber+'</span>'
      	  $p2.html($str).appendTo($section);
      	  

      	  var $items = $griDiv;
		  $items.imagesLoaded().done(function(){
				 $grid.masonry('layout');
	             $grid.append( $items ).masonry('appended', $items);
			})
           });
        }
    
})


