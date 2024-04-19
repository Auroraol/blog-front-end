<template>
  <div class="container-more">
    <div class="head">
      <div class="hedaImg">
        <img src="/image/imgs/portrait.jpg" alt="headImg">
      </div> 
      <h1>你好!</h1>
    </div>
    <div class="info">
      <!-- 随便写什么 -->
      <p>欢迎光临博客网站</p>
      <p>这是vue3 + springboot全栈项目</p>
      <p>喜欢的话可以去github点个星星</p>
      <p>希望明天能更好</p>
    </div>
    <div class="bottom">
      <p>
        前端参考:
        <a
          href="https://github.com/1209058433/ImperoBlog"
          style="color: #551a8b"
          target="_blank"
          >https://github.com/1209058433/ImperoBlog</a
        >
      </p>
      <p>
        仓库地址:
        <a
          href="https://github.com/Auroraol/my_blog"
          style="color: #551a8b"
          target="_blank"
          >
          
          https://github.com/Auroraol/my_blog</a
        >
      </p>
      <p>君子终日乾乾，夕惕若厉，无咎。与君共勉！</p>
      <div class="image">
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import gsap from "gsap";

onMounted(() => {
  gsapAnimation();
});


const myInfo = "";

// 动画
const gsapAnimation = () => {
  const tween = gsap.timeline();
  tween.from('.hedaImg', {
        scale: 1.5,
        ease: "elastic.out(1.5,0.2)",
        duration: 1.5
    }).from('.hedaImg', {
        y: 200,
        duration: 0.3
    })
    .from(".head h1", {
      y: 20,
      opacity: 0,
    })
    .from(".info p", {
      y: 20,
      stagger: 0.1,
      opacity: 0,
    })
    .from(".bottom", {
      y: 20,
      opacity: 0,
      delay: 0.3,
    });
};

//网页彩蛋
const easterKey = ["w", "z", "f"]; //彩蛋秘籍
let easterArr = new Array<string>(); //存放按键记录
window.addEventListener("keyup", (key) => {
  if (easterKey.includes(key.key)) {
    //按键属于目标字母
    easterArr.push(key.key); //存放
    if (easterArr.length === easterKey.length) {
      //字母到目标秘籍长度
      let str = easterArr.join(""); // 转为字符串
      if (str === "wzf") {
        //召唤彩蛋！
        alert("我超，丰😨");
        easterArr = []; //清空
      }
    }
  } else {
    easterArr = []; //清空
  }
});
</script>

<style scoped lang="less">
.container-more {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 70%;
  min-height: 100vh;
  margin: 0 auto;
  border-radius: 0.6rem;
  background-color: #fff;
  border-radius: 1rem;
  margin-top: 5px;

  .head {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 2rem;

    .hedaImg {
      width: 15rem;
      height: 15rem;
      overflow: hidden;
      border-radius: 50%;

      img {
        width: 100%;
      }
    }

    h1 {
      font-size: 3rem;
    }
  }

  .info {
    line-height: initial;
    p {
      font-weight: 600;
      font-size: 1.5rem;
      text-align: center;
    }
  }

  .bottom {
    margin: 4rem;
 line-height: initial;
    p {
      font-size: 1.5rem;
      font-weight: 600;
    //   text-align: center; /* 让段落内文本居中对齐 */
      margin: 1rem;
 
    a {
      display: block; /* 将链接显示为块级元素 */
      text-align: left; /* 让链接内文本左对齐 */
    }
   }
    .image {
      width: 100%;
      display: flex;

      img {
        width: 45%;
        flex: 1;
      }
    }
  }
}

@media only screen and (max-width: 1050px) {
  .container-more {
    width: 100%;
  }
}
</style>