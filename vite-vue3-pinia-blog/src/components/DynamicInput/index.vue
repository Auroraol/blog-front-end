<!-- 动态输入组件 -->
<template>
  <div class="input-wrapper">
    <!-- 输入框 -->
    <el-input
      v-if="inputVisible"
      v-model="inputValue"
      :style="'width:' + width"
      :placeholder="placeholder"
      size="small"
      @keyup.enter.native="inputConfirm"
      @blur="
        inputVisible = false;
        inputValue = '';
      "
    >
      <template #suffix>
        <el-icon title="关闭" class="close"> </el-icon>
        <el-icon
          title="关闭"
          @click="
            inputVisible = false;
            inputValue = '';
          "
          ><Close
        /></el-icon>
      </template>
    </el-input>
    <!-- 新增按钮 -->
    <el-button
      v-else
      type="primary"
      size="small"
      @click="inputVisible = true"
      >{{ content }}</el-button
    >
  </div>
</template>

<script>
export default {
  name: "DynamicInput",
  props: {
    placeholder: {
      type: String,
      default: "输入内容",
    },
    width: {
      type: String,
      default: "120px",
    },
    content: {
      type: String,
      default: "+ Add",
    },
  },
  data() {
    return {
      inputVisible: false,
      inputValue: "",
    };
  },

  methods: {
    // 输入框回车事件
    inputConfirm() {
      this.$emit("inputConfirm", this.inputValue);
      this.inputValue = "";
      this.inputVisible = false;
    },
  },
};
</script>

<style lang="less" scoped>
.input-wrapper {
  & /deep/ .el-input__suffix {
    top: 9px;
  }

  .el-icon-close {
    cursor: pointer;
  }
}
</style>


/*使用
<dynamic-input
        placeholder="输入名称"
        content="+ New Tag"
        @inputConfirm="inputConfirm"
/>


// 动态输入框回车事件
const inputConfirm = (val) => {
  if (!val) {
    ElMessage.error("标签名称不能为空");
    return;
  }
  const params = { tagName: val };
  addTag(params).then((res) => {
    ElMessage.success("新增成功");
    pageNum.value = 1;
    loadData();
  });
};
*/