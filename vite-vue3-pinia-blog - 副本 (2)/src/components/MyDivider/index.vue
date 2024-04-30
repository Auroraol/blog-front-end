<template>
    <div class="my-divider">
        <div class="line" :style="{ width: leftWidth }"></div>
        <span class="label">{{ label }}</span>
        <div class="line" :style="{ width: rightWidth }"></div>
    </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';

const props = defineProps({
    label: {
        type: String,
        default: ''
    },
    contentPosition: {
        type: String,
        default: 'center'
    }
});

const leftWidth = ref('50%');
const rightWidth = ref('50%');

const setLineWidth = () => {
    let p = props.contentPosition;
    switch (p) {
        case 'center': {
            leftWidth.value = '50%';
            rightWidth.value = '50%';
            break;
        }
        case 'left': {
            leftWidth.value = '10%';
            rightWidth.value = '90%';
            break;
        }
        case 'right': {
            leftWidth.value = '90%';
            rightWidth.value = '10%';
            break;
        }
    }
};

watch(() => props.contentPosition, () => {
    setLineWidth();
});

setLineWidth();
</script>

<style lang="less" scoped>
.my-divider {
    position: relative;
    width: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    margin: 15px 0;
    color: #000;

    .line {
        background: #000;
        height: 1px;
    }

    .label {
        width: auto;
        padding: 0 12px;
        text-align: center;
        transform: translateY(-1px);
        white-space: nowrap; // 不换行(单行)
    }
}
</style>
