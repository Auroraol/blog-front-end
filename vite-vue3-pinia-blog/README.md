# 数安信官网

# Vue 3 + TypeScript + Vite

#  包含什么？

此项目可直接运行 集成了vuex router 封装axios + loading + 返回顶部 + 模块化vuex + 模块化路由

# 安装

yarn

# 运行

yarn dev

# 发布

yarn build

# 发布到 GitHub Pages

yarn gh-pages

# 发布到 NPM

yarn publish

# 发布到 NPM 私有仓库

yarn publish --registry=https://registry.npm.taobao.org

# 发布到 NPM 私有仓库，并使用

yarn publish --registry=https://registry.npm.taobao.org --access=public

```
vite-vue3-template-main
├─ .env
├─ .env.development
├─ .env.production
├─ .env.staging
├─ .gitignore
├─ index.html
├─ package-lock.json
├─ package.json
├─ public
│  ├─ favicon.ico
│  └─ image
│     └─ home
│        └─ 404.jpeg
├─ README.md
├─ src
│  ├─ api
│  │  └─ api.js
│  ├─ App.vue
│  ├─ assets
│  │  └─ style
│  │     ├─ common.css
│  │     └─ reset.css
│  ├─ auto-import.d.ts
│  ├─ components
│  │  └─ Breadcrumb
│  │     └─ index.vue
│  ├─ components.d.ts
│  ├─ env.d.ts
│  ├─ main.ts
│  ├─ pages
│  │  ├─ edit-table
│  │  │  └─ index.vue
│  │  ├─ error
│  │  │  └─ 404.vue
│  │  ├─ home
│  │  │  └─ home.vue
│  │  ├─ vaxiso
│  │  │  └─ vaxiso.vue
│  │  └─ vuex
│  │     └─ vuex.vue
│  ├─ plugins
│  │  ├─ BackTop
│  │  │  ├─ index.ts
│  │  │  └─ MyBackTop.vue
│  │  └─ Loading
│  │     ├─ index.ts
│  │     └─ loading.vue
│  ├─ route
│  │  ├─ no-fond.ts
│  │  └─ router.ts
│  ├─ store
│  │  ├─ getters.ts
│  │  ├─ index.ts
│  │  └─ modules
│  │     ├─ count.ts
│  │     ├─ dict.ts
│  │     └─ user.ts
│  └─ utils
│     ├─ format
│     │  └─ format-time.ts
│     └─ network
│        ├─ base-url.js
│        ├─ http.js
│        └─ request.js
├─ tsconfig.json
├─ tsconfig.node.json
└─ vite.config.ts

```