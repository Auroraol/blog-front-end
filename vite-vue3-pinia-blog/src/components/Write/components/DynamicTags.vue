<!-- 动态标签组件 -->
<template>
  <div class="tags-wrapper">
    <!--  标签 -->
    <div class="tag">
      <p>选择标签:</p>
      <div @click="saveEditor" class="tag-container">
        <span
          class="labelTag no-choose"
          @click="chooseTag(tag)"
          v-for="tag in dynamicTags"
          :key="tag.id"
          >{{ tag.name }}</span
        >
      </div>
    </div>
    <!--  已选标签 -->
    <div class="choose">
      <p>已选择({{ chooseTags.length }} /6):</p>
      <div>
        <el-tag
          v-for="tag in chooseTags"
          :key="tag"
          class="mx-1"
          closable
          :disable-transitions="false"
          @close="handleClose(tag)"
        >
          {{ tag.name }}
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
import { ElInput, ElMessage } from "element-plus";
import { addTag as apiAddTag, getTagId } from "/@/api/tag/tag";

const emits = defineEmits(["tagsChange"]);

const props = defineProps<{
  seletedTags: Tag[];
}>();

const dynamicTags = computed(() =>
  props.seletedTags && props.seletedTags.length > 0
    ? props.seletedTags
    : [
        {
          id: 1,
          name: "c++",
          deleted: 0,
        },
        {
          id: 2,
          name: "测试",
          deleted: 0,
        },
        {
          id: 3,
          name: "java",
          deleted: 0,
        },
      ]
);

interface Tag {
  id?: number;
  name: string;
  deleted?: number;
}

const chooseTags = reactive<Tag[]>([]);
const inputTagVisible = ref(false);
const inputTagValue = ref("");

onMounted(() => {});

watch(chooseTags, (val) => {
  emits("tagsChange", val);
});

//选中筛选tag
const chooseTag = (tagName: Tag) => {
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
const inputValue = ref("");
const inputVisible = ref(false);
const InputRef = ref<InstanceType<typeof ElInput>>(); // 绑定<el-input></el-input>

const handleClose = (tag: Tag) => {
  chooseTags.splice(chooseTags.indexOf(tag), 1);
};

const addTag = () => {
  inputVisible.value = true;
  nextTick(() => {
    InputRef.value!.input!.focus(); //焦点
  });
};

const handleInputConfirm = async () => {
  if (inputValue.value) {
    //判断，如果标签数量已经达到最大数量则不添加新标签并且给出弹窗
    if (chooseTags.length < 6) {
      try {
        const data = { tagName: inputValue.value };
        await apiAddTag(data); // 服务器增加标签
        const res = await getTagId(data);
        console.error(res);

        chooseTags.push({
          id: res,
          name: inputValue.value,
        });
      } catch (error) {
        console.error(error);
      }
    } else {
      ElMessage.warning("标签数量达到上限");
    }
  }
  // todo 保存草搞
  inputVisible.value = false;
  inputValue.value = "";
};
</script>

<style lang="less" scoped>
.tags-wrapper {
  .tag {
    margin-left: 1rem;
    margin-top: 1rem;
    display: flex;
    align-items: center;

    p {
      margin-right: 1.5rem;
    }

    .tag-container {
      display: flex;
      flex-wrap: wrap; /* 允许标签换行显示 */
      margin-left: 3rem;
    }

    .no-choose {
      margin: 0.2rem;
      padding: 0.3rem 1rem;
      border-radius: 0.5rem;
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
}
</style>
