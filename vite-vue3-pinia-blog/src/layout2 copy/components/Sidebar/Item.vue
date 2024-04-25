<script setup lang="ts">
import { defineProps } from "vue";
// import SvgIcon from "/@/components/SvgIcon/index.vue";

const props = defineProps({
  icon: {
    type: String,
    default: "",
  },
  title: {
    type: String,
    default: "",
  },
});

const vnodes = [];

if (props.icon) {
  vnodes.push(<SvgIcon name={props.icon} />);
}

if (props.title) {
  vnodes.push(<span slot="title">{props.title}</span>);
}

return vnodes;
</script>
