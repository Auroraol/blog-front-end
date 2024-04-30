<template>
  <div class="tags">
    <span
      v-for="(tag, index) in tags"
      :key="index"
      class="tag"
      @click="tagClick(tag.id)"
      >{{ tag.name }}</span
    >
  </div>
</template>

<script setup lang="ts">
import { defineProps } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

defineProps({
  tags: {
    type: Array,
    default() {
      return [];
    },
  },
});

const tagClick = (id: string) => {
  router.push({
    path: "/tag",
    query: { id: id },
  });
};

</script>

<style lang="scss" scoped>
.tags {
  font-size: 11px;
  line-height: 60px;
  border-bottom: 1px solid #eee;

  .tag {
    cursor: pointer;
    border: 1px #999 solid;
    border-radius: 20px;
    padding: 6px 12px;
    margin-right: 3px;

    &:hover {
      border: 1px #007fff solid;
      color: #007fff;
    }
  }
}
</style>
