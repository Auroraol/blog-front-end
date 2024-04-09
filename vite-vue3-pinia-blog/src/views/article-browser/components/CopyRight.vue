<template>
  <div class="post-container">
    <div class="post-copyright">
      <p>
        <span class="bold">{{ original === 1 ? "本" : "原" }}文链接：</span
        >{{ url }}
      </p>
      <p>
        <span class="bold">版权声明：</span> 本站点所有文章除特别声明外，均采用
        CC BY 3.0 CN协议进行许可。转载请署名作者且注明文章出处。
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from "vue";

defineProps({
  url: {
    type: String,
    default: "",
  },
  original: {
    type: Number,
    default: 1,
  },
});
</script>

<style lang="less" scoped>
.post-container {
  margin-top: 50px;

  .info {
    color: #999;
    font-size: 12px;
  }

  .post-copyright {
    padding: 4px 10px;
    border-left: 3px solid #ff1700;
    background-color: #f9f9f9;
    list-style: none;
    font-size: 12px;
    line-height: 15px;

    .bold {
      font-weight: bold;
    }
  }
}
</style>
