<script setup lang="ts">
import { defineProps } from "vue";

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

// if (props.icon) {
//   vnodes.push(<svg-icon icon-class={props.icon} />);
// }

// if (props.title) {
//   vnodes.push(<span slot="title">{props.title}</span>);
// }

return vnodes;
</script>
