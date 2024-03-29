<template>
  <div class="notez-markdown" ref="editorDom" tabindex="1">
    <MdEditorV3
      @onUploadImg="handleOnUploadImg"
      v-model="content"
      :editor-id="editorId"
      :preview-theme="previewTheme"
      :toolbars="store.state.toolbars"
      :previewOnly="previewOnly"
      placeholder="请输入内容..."
      :preview="true"
    >
      <template #defToolbars>
        <MdEditorV3.DropdownToolbar
          title="emoji"
          :visible="data.emojiVisible"
          :onChange="emojiVisibleChanged"
        >
          <template #overlay>
            <div class="emoji-container">
              <ol class="emojis-box">
                <li
                  v-for="(item, i) in store.state.emojis"
                  :key="i"
                  @click="handlerEmoji(item)"
                >
                  {{ item }}
                </li>
              </ol>
            </div>
          </template>
          <template #trigger>
            <svg class="md-icon biao-qing" viewBox="0 0 1024 1024">
              <path
                d="M512 1024C230.4 1024 0 793.6 0 512S230.4 0 512 0s512 230.4 512 512-230.4 512-512 512z m0-102.4c226.742857 0 409.6-182.857143 409.6-409.6S738.742857 102.4 512 102.4 102.4 285.257143 102.4 512s182.857143 409.6 409.6 409.6z m-204.8-358.4h409.6c0 113.371429-91.428571 204.8-204.8 204.8s-204.8-91.428571-204.8-204.8z m0-102.4c-43.885714 0-76.8-32.914286-76.8-76.8s32.914286-76.8 76.8-76.8 76.8 32.914286 76.8 76.8-32.914286 76.8-76.8 76.8z m409.6 0c-43.885714 0-76.8-32.914286-76.8-76.8s32.914286-76.8 76.8-76.8c43.885714 0 76.8 32.914286 76.8 76.8s-32.914286 76.8-76.8 76.8z"
              ></path>
            </svg>
          </template>
        </MdEditorV3.DropdownToolbar>
        <MdEditorV3.NormalToolbar title="标记" @on-click="handlerOnMark">
          <template #trigger>
            <svg
              class="md-icon mark"
              viewBox="0 0 1024 1024"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                fill="currentColor"
                d="M256 128v698.88l196.032-156.864a96 96 0 0 1 119.936 0L768 826.816V128H256zm-32-64h576a32 32 0 0 1 32 32v797.44a32 32 0 0 1-51.968 24.96L531.968 720a32 32 0 0 0-39.936 0L243.968 918.4A32 32 0 0 1 192 893.44V96a32 32 0 0 1 32-32z"
              ></path>
            </svg>
          </template>
        </MdEditorV3.NormalToolbar>
      </template>
    </MdEditorV3>
  </div>
</template> 
<script setup>
import {
  // computed,
  // onBeforeUnmount,
  // onMounted,
  ref,
  nextTick,
  watch,
  // defineEmits,
  reactive,
} from "vue";
import axios from "@/axios";
import { useStore } from "vuex";
import MdEditorV3 from "md-editor-v3";
import "md-editor-v3/lib/style.css";
import "@/assets/iconfont.js";

import screenfull from "screenfull";

import katex from "katex";
import "katex/dist/katex.min.css";

import Cropper from "cropperjs";
import "cropperjs/dist/cropper.css";

import mermaid from "mermaid";

import highlight from "highlight.js";
// import 'highlight.js/styles/atom-one-dark-reasonable.css';

import prettier from "prettier";
import parserMarkdown from "prettier/parser-markdown";

MdEditorV3.config({
  editorExtensions: {
    prettier: {
      prettierInstance: prettier,
      parserMarkdownInstance: parserMarkdown,
    },
    highlight: {
      instance: highlight,
    },
    screenfull: {
      instance: screenfull,
    },
    katex: {
      instance: katex,
    },
    cropper: {
      instance: Cropper,
    },
    mermaid: {
      instance: mermaid,
    },
  },
});

//动态添加css样式文件
function setAddCodeStyle(cssName) {
  let dom = document.querySelector("#code-style");
  if (dom) {
    dom.remove();
  }
  let link = document.createElement("link");
  link.setAttribute("href", `/code/styles/${cssName}.css`);
  link.setAttribute("rel", "stylesheet");
  link.setAttribute("id", "code-style");
  document.querySelector("head").appendChild(link);
}

// import { getItem } from "@/utils/tools";
// import cloneDeep from "lodash.clonedeep";
// import { Loading, CircleCheck } from "@element-plus/icons-vue";
// vuex
const store = useStore();
// const fileData = JSON.parse(JSON.stringify(store.state.fileData));
const editorId = ref("md-editor-id");
const previewTheme = ref("smart-blue"); //default、github、vuepress、mk-cute、smart-blue、cyanosis
// const codeTheme = ref("atom"); //atom、a11y、github、gradient、kimbie、paraiso、qtcreator、stackoverflow
setAddCodeStyle("atom-one-dark"); // a11y-dark  atom-one-dark monokai monokai-sublime vs vs2015 xcode paraiso-dark gradient-dark a11y-dark paraiso-dark

const data = reactive({
  emojiVisible: false,
});

const props = defineProps({
  content: {
    type: String,
  },
  save: {
    type: Function,
  },
  previewOnly: {
    default: false,
  },
});
const content = ref(props.content);

const handlerEmoji = (emoji) => {
  // 获取输入框
  const textarea = document.querySelector(`#${editorId.value}-textarea`);
  // 获取鼠标位置
  const endPoint = textarea.selectionStart;
  const prefixStr = textarea.value.substring(0, endPoint);
  const suffixStr = textarea.value.substring(endPoint, textarea.value.length);
  content.value = `${prefixStr}${emoji}${suffixStr}`;
  nextTick(() => {
    textarea.setSelectionRange(1 + endPoint, 1 + endPoint);
    textarea.focus();
  });
};

const handlerOnMark = () => {
  // 获取输入框
  const textarea = document.querySelector(`#${editorId.value}-textarea`);
  // 获取选中的内容
  const selection = window.getSelection()?.toString();
  // 获取鼠标位置
  const endPoint = textarea.selectionStart;

  // 生成标记文本
  const markStr = `@${selection}@`;

  // 根据鼠标位置分割旧文本
  // 前半部分
  const prefixStr = textarea.value.substring(0, endPoint);
  // 后半部分
  const suffixStr = textarea.value.substring(
    endPoint + (selection?.length || 0)
  );
  // console.log( );
  // emit("onChange", `${prefixStr}${markStr}${suffixStr}`);
  content.value = `${prefixStr}${markStr}${suffixStr}`;
  nextTick(() => {
    textarea.setSelectionRange(endPoint, markStr.length + endPoint);
    textarea.focus();
  });
};
const emojiVisibleChanged = (visible) => {
  data.emojiVisible = visible;
};

const handleOnUploadImg = async (files, callback) => {
  // const user = getItem("user") || {};
  const res = await Promise.all(
    Array.from(files).map((file) => {
      return new Promise((rev, rej) => {
        const form = new FormData();
        form.append("file", file);
        axios
          .post("/notez/images/post", form)
          .then((res) => rev(res))
          .catch((error) => rej(error));
      });
    })
  );

  callback(res.map((item) => item.data.data.url));
};

// const emit = defineEmits(["on-share-click"]);

// const loading = ref(true);

// const fileData = computed(() => store.state.fileData);

//防抖
let timer = null;
const anti_shake = () => {
  return function () {
    clearInterval(timer);
    timer = setTimeout(() => {
      handleSave(false);
    }, 800);
  };
};
const ctrl = anti_shake();

let isUpdate = false;
setTimeout(() => {
  isUpdate = true;
}, 3000);
watch(content, (val) => {
  // if (isUpdate && store.state.isAutoSave) ctrl(val);
});

const handleSave = (mark) => {
  if (content.value || props.save) props.save(mark);
};

// 编辑器事件监听
const editorDom = ref(null);
nextTick(() => {
  editorDom.value.onkeydown = (e) => {
    e = e || window.event;
    if (e.ctrlKey && e.keyCode == 83) {
      // ctr+S
      handleSave(true);
      return false;
    }
  };
});
// 分享
// const handleShare = () => {
//   emit("on-share-click", store.state.fileData);
// };

// // 加载数据
// const setContentData = (val) => {
//   nextTick(() => {
//     content.value = val;
//   });
// };
const getContent = () => {
  return content.value;
};
defineExpose({ getContent });

// // 组件销毁时，也及时销毁编辑器
// onBeforeUnmount(() => {
//   if (content.value || props.save)
//   console.log(fileData.name);
//     props.save({ data: content.value, file: fileData });
//   // const editor = getEditor(editorId);
//   // // sessionContent("set", store.state.fileData.bid, editor.children);
//   // if (editor == null) return;
//   // editor.destroy();
//   // removeEditor(editorId);
// });
</script>
<style lang="less" scoped>
.notez-markdown {
  :deep(.md-divider) {
    height: 45px;
  }

  .md {
    border: 0;
    height: calc(100vh - 65px);
  }
  :deep(.md-fullscreen){
    height:initial;
  }
  :deep(.mk-cute-theme) {
    color: initial;
  }
}
</style>      
