<!-- 动态标签组件 -->
<template>
  <div class="tags-wrapper">
    <!--  标签 -->
    <div class="tag">
      <p>选择标签:</p>
      <div @click="saveEditor">
        <span
          class="labelTag no-choose"
          @click="chooseTag(tag)"
          v-for="tag in allTags"
          :key="tag.id"
          >{{ tag }}</span
        >
      </div>
    </div>
    <!--  已选标签 -->
    <div class="choose">
      <p>已选择({{}} /6):</p>
      <div>
        <el-tag
          v-for="tag in chooseTags"
          :key="tag"
          class="mx-1"
          closable
          :disable-transitions="false"
          @close="handleClose(tag)"
        >
          {{ tag }}
        </el-tag>
        <el-input
          v-if="inputVisible"
          ref="InputRef"
          v-model="inputValue"
          class="ml-1 w-20"
          size="small"
          @keyup.enter="handleInputConfirm"
          @blur="handleInputConfirm"
        />
        <el-button
          v-else
          class="button-new-tag ml-1"
          size="small"
          @click="addTag"
        >
          + New Tag
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, watch, onMounted } from "vue";
import { ElInput, ElMessage } from 'element-plus'
import { tagList } from "/@/api/tag/tag";
const props = defineProps({
  seletedTags: {
    type: Array,
    required: false,
    default: null,
  },
});

// const dynamicTags = ref(props.seletedTags || [])
const inputTagVisible = ref(false);
const inputTagValue = ref("");
const dynamicTags = reactive(["c++", "jvaa", "对对对", "sss"]);
const allTags = reactive(["c++", "jvaa", "对对对", "sss"]);
const chooseTags = reactive<string[]>([]);

onMounted(() => {
  initTag();
});

watch(dynamicTags, (val) => {});

// 加载标签列表
const initTag = () => {};

//选中筛选tag
const chooseTag = (tagName: string) => {
  //如果已经选中了则不要重复选中
  if (chooseTags.indexOf(tagName) !== -1) {
    return;
  } else {
    if (chooseTags.length < 6) {
      chooseTags.push(tagName);
    } else {
      alert("标签数量达到上限");
    }
  }
};


//已选择的标签，可自定义添加新标签
const inputValue = ref('')
const inputVisible = ref(false)
const InputRef = ref<InstanceType<typeof ElInput>>()  // 绑定<el-input></el-input>

const handleClose = (tag: string) => {
  chooseTags.splice(chooseTags.indexOf(tag), 1);
};

const addTag = () => {
    inputVisible.value = true
    nextTick(() => {
        InputRef.value!.input!.focus()
    })
}

const handleInputConfirm = () => {
    if (inputValue.value) {
        //判断，如果标签数量已经达到最大数量则不添加新标签并且给出弹窗
        if (chooseTags.length < 6) {
            chooseTags.push(inputValue.value)
        } else {
          ElMessage.warning('标签数量达到上限')
        }
    }
    // todo 保存草搞
    inputVisible.value = false
    inputValue.value = ''
}


</script>

<style lang="less" scoped>
.tag {
  margin-left: 1rem;
  margin-top: 1rem;
  display: flex;
  align-items: center;

  p {
    margin-right: 1.5rem;
  }
  .no-choose {
    font-size: 1.4rem;
    color: black;
    background-color: #fff;
    border: 0.1rem solid black;
    cursor: pointer;
    transition: all 0.1s;

    &:hover {
      box-shadow: 0.2rem 0.2rem var(--box-shadow);
      transform: translateY(-0.1rem);
    }
  }
}

.choose {
  margin-left: 1rem;
  margin-top: 1rem;
  display: flex;
  align-items: center;

  p {
    margin-right: 0.5rem;
  }

  :deep(.el-tag) {
    margin: 0 0.3rem;
  }
}
</style>
