<script setup lang='ts'>
onMounted(() => {
  /*星星的密集程度，数字越大越多*/
  var stars = 900;
  var starsContainer = document.querySelector(".stars_starrySky");

  /*星星的看起来的距离,值越大越远,可自行调制到自己满意的样子*/
  var r = 850;
  for (var i = 0; i < stars; i++) {
    var star = document.createElement("div");
    star.classList.add("star_starrySky");
    starsContainer!.appendChild(star);
  }

  var starElements = document.querySelectorAll(".star_starrySky");
  starElements.forEach(function (starElement) {
    var s = 0.2 + Math.random() * 1;
    var curR = r + Math.random() * 300;
    (starElement as HTMLElement).style.transformOrigin = "0 0 " + curR + "px";
    (starElement as HTMLElement).style.transform =
      "translate3d(0,0,-" +
      curR +
      "px) rotateY(" +
      Math.random() * 360 +
      "deg) rotateX(" +
      Math.random() * -50 +
      "deg) scale(" +
      s +
      "," +
      s +
      ")";
  });
});
</script>

<template>
  <div class="box_starrySky">
    <!--背景层，不要删除，否则没有作用-->
    <div class="stars_starrySky"></div>
  </div>
</template>

<style lang="less">
@keyframes Rotate_StarrySky {
  0% {
    transform: perspective(400px) rotateZ(20deg) rotateX(-40deg) rotateY(0);
  }

  100% {
    transform: perspective(400px) rotateZ(20deg) rotateX(-40deg)
      rotateY(-360deg);
  }
}
.box_starrySky {
  overflow: hidden;
  // position: absolute;
  position: relative;
  z-index: 1;

  .stars_starrySky {
    transform: perspective(500px);
    transform-style: preserve-3d;
    position: absolute;
    bottom: 0;
    perspective-origin: 50% 100%;
    left: 50%;
    animation: Rotate_StarrySky 90s infinite linear;
  }

  .star_starrySky {
    width: 2px;
    height: 2px;
    background: #f7f7b6;
    position: absolute;
    top: 0;
    left: 0;
    transform-origin: 0 0 -300px;
    transform: translate3d(0, 0, -300px);
    backface-visibility: hidden;
  }

  .table {
    width: 400px;
    height: 350px;
    margin: 80px auto;
  }

  .table form {
    width: 100%;
  }

  .table .name {
    width: 280px;
    margin: 20px auto 30px auto;
    display: block;
    height: 30px;
    border-radius: 20px;
    border: none;
    background: rgba(0, 0, 0, 0.2);
    text-indent: 0.5em;
  }

  .table .btn {
    width: 100px;
    height: 30px;
    background: rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    border: none;
    color: white;
    margin: 0 auto;
    display: block;
  }
}
</style>