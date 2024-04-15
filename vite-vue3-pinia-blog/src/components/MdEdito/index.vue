<template>
  <div class="project-preview">
    <div class="container">
      <MdEditor
        ref="editorRef"
         :editorId="editorId"
        :toolbars="toolbars"
        :footers="['markdownTotal', '=', 0, 'scrollSwitch']"
        showCodeRowNumber
        autoDetectCode
        @onUploadImg="uploadImg"
      >
        <template #defToolbars>
          <Mark />
          <Emoji />
          <ExportPDF :modelValue="text" height="700px" />
        </template>
        <template #defFooters>
          <TimeNow />
        </template>
      </MdEditor>
      <br />
      <span class="tips-text">
        {{ tips
        }}<a
          href="https://github.com/imzbf/md-editor-v3/tree/docs/src/components"
          target="_blank"
          >components</a
        >
      </span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, watch, ref, onMounted } from 'vue';
import { MdEditor } from 'md-editor-v3';
import { Emoji, Mark, ExportPDF } from '@vavt/v3-extension';
import type { ExposeParam } from 'md-editor-v3';
import '@vavt/v3-extension/lib/asset/style.css';
import { toolbars } from './staticConfig';
import TimeNow from '/@/components/TimeNow/index.vue';


const editorId = 'editor-preview';

const editorRef = ref<ExposeParam>();

const test= ref("")

onMounted(() => {
  console.log(editorRef.value?.on('catalog', console.log));
});
</script>

<script lang="ts">
export default {
  name: 'PreviewPage'
};
</script>