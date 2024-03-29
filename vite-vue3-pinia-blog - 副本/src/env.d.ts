/// <reference types="vite/client" />

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  // eslint-disable-next-line @typescript-eslint/no-explicit-any, @typescript-eslint/ban-types
  const component: DefineComponent<{}, {}, any>
  export default component
}
/*
让我解释一下：

/// <reference types="vite/client" />: 这是用于告诉 TypeScript 引用 Vite 客户端类型的注释。

declare module '*.vue': 这是一个模块声明，指定了当你导入以 .vue 结尾的文件时应该如何处理。

import type { DefineComponent } from 'vue': 这里导入了 DefineComponent 类型，它是 Vue 3 中用于定义组件类型的接口。

const component: DefineComponent<{}, {}, any>: 这是对模块中的默认导出进行的实际声明。DefineComponent 是一个泛型接口，
接受三个参数，分别是组件的 Props、State 和 Emits 类型。在这个声明中，Props 和 State 都是空对象 {}，而 Emits 是 any，表示允许任何事件。

export default component: 这是将上述声明的组件导出为默认导出。在 Vue 单文件组件中，<script setup> 语法中的默认导出通常是一个 DefineComponent 实例，用于定义组件。

总的来说，这个声明文件的作用是告诉 TypeScript 如何处理导入的 .vue 文件，以便获得正确的类型提示和类型检查。

*/