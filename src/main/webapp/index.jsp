<!DOCTYPE html>

<html lang="en">

<head>

        <meta charset="UTF-8">

      <title>   </title>

</head>

<style type="text/css">
  .box{
       width:100%;
       height: 100%;
       position: fixed;
       margin:auto;
       top: 0;
       left: 0;
       right:0;
       bottom: 0;
       background: red;
       opacity: 0.6;
       color: black;
       
   }
</style>

<body>

     <div class="box" id="box">dssdfsdfsdffffffffffffffffffffffffffffffffs</div>
	 <input type="button" value="按钮" onclick="hh()"/>
	 <input type="button" value="按钮" onclick="hh()"/>
</body>

<script type="text/javascript">
     function _touch(){
              var startx;//让startx在touch事件函数里是全局性变量。
              var endx;
               var el=document.getElementById('box');
              function cons(){   //独立封装这个事件可以保证执行顺序，从而能够访问得到应该访问的数据。
                     console.log(starty,endy);
                     if(startx>endx){  //判断左右移动程序
                            alert('left');
                      }else{
                          alert('right');
                     }
               }
               el.addEventListener('touchstart',function(e){
                       var touch=e.changedTouches;
                     startx=touch[0].clientX;
                    starty=touch[0].clientY;
            });
             el.addEventListener('touchend',function(e){
                 var touch=e.changedTouches;
                     endx=touch[0].clientX;
                      endy=touch[0].clientY;
                      cons();  //startx endx 的数据收集应该放在touchend事件后执行，而不是放在touchstart事件里执行，这样才能访问到touchstart和touchend这2个事件产生的startx和endx数据。另外startx和endx在_touch事件函数里是全局性的，所以在此函数中都可以访问得到，所以只需要注意事件执行的先后顺序即可。
           });
     }
       _touch()
</script>
</html>