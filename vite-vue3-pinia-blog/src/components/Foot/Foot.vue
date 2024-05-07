<template>
  <!-- 动图 -->
  <div class="header">
    <div>
      <svg
        class="waves"
        xmlns="http://www.w3.org/2000/svg"
        xmlns:xlink="http://www.w3.org/1999/xlink"
        viewBox="0 24 150 28"
        preserveAspectRatio="none"
        shape-rendering="auto"
      >
        <defs>
          <path
            id="gentle-wave"
            d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z"
          />
        </defs>
        <g class="parallax">
          <use
            xlink:href="#gentle-wave"
            x="48"
            y="0"
            fill="rgba(255,255,255,0.7)"
          />
          <use
            xlink:href="#gentle-wave"
            x="48"
            y="3"
            fill="rgba(255,255,255,0.5)"
          />
          <use
            xlink:href="#gentle-wave"
            x="48"
            y="5"
            fill="rgba(255,255,255,0.3)"
          />
          <use xlink:href="#gentle-wave" x="48" y="7" fill="#fff" />
        </g>
      </svg>
    </div>
  </div>
  <div class="content">
    <p>冀公网安备xxxxxxxx号</p>
  </div>
</template>

<style scoped lang="less">
.header {
  margin-top: 1rem;
  border-radius: 0.5rem;
  position: relative;
  text-align: center;
  background-image: linear-gradient(to top, #2580b3 55%, #ffe6fa 100%);
  color: white;
}

.waves {
  position: relative;
  width: 100%;
  margin-bottom: -7px;
  min-height: 5px;
  max-height: 35px;
}

.content {
  position: relative;
  font-size: 1.2rem;
  text-align: center;
  background-color: white;
}

@media screen and (max-width: 800px) {
  .record {
    box-sizing: border-box;
    padding: 1rem;

    .gongAn {
      display: flex;
      flex-direction: column;
    }
  }
}

.parallax > use {
  /* 使use元素执行move-forever动画 */
  animation: move-forever 25s cubic-bezier(0.55, 0.5, 0.45, 0.5) infinite;
}

.parallax > use:nth-child(1) {
  /* 延迟2秒启动动画  */
  animation-delay: -2s;
  /* 设置动画持续时间为7秒 */
  animation-duration: 7s;
}

.parallax > use:nth-child(2) {
  animation-delay: -3s;
  animation-duration: 10s;
}

.parallax > use:nth-child(3) {
  animation-delay: -4s;
  animation-duration: 13s;
}

.parallax > use:nth-child(4) {
  animation-delay: -5s;
  animation-duration: 20s;
}

@keyframes move-forever {
  0% {
    transform: translate3d(-90px, 0, 0);
  }

  100% {
    transform: translate3d(85px, 0, 0);
  }
}
</style>