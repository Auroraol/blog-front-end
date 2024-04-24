<template>
  <component v-bind="linkProps(to)" v-bind:is="linkProps(to)">
    <slot />
  </component>
</template> 

<script setup>
import { isExternal } from "@/utils/validate";

const linkProps = (url) => {
  if (isExternal(url)) {
    return {
      is: "a",
      href: url,
      target: "_blank",
      rel: "noopener",
    };
  }
  return {
    is: "router-link",
    to: url,
  };
};
</script>
