import { defineConfig } from 'vite';
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { VueHooksPlusResolver } from '@vue-hooks-plus/resolvers'
import vue from '@vitejs/plugin-vue';
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';
import * as path from 'path';

export default defineConfig({
  base: './',
  // 配置插件
  plugins: [
    vue(), //vue
    // 引入unplugin-auto-import
    AutoImport({
      //引入vue 自动注册api插件
      imports: ['vue', 'vue-router', 'vuex'], // 配置需要自动导入的库
      include: [/\.[tj]sx?$/, /\.vue$/, /\.vue\?vue/, /\.md$/],
      dts: 'src/auto-import.d.ts', // 自动引入生成api的地址
      resolvers: [VueHooksPlusResolver()], //VueHooks 使用
    }),
    // 引入Element-plus
    AutoImport({
      //plus按需引入
      resolvers: [ElementPlusResolver()]
    }),
    Components({
      //plus按需引入
      resolvers: [ElementPlusResolver()],
      dts: 'src/components.d.ts' //自动引入生成的组件的地址
    }),
      // TODO
  ],
  build: {
    minify: 'terser',
    terserOptions: {
      compress: {
        //生产环境时移除console
        drop_console: true,
        drop_debugger: true
      }
    }
  },

  resolve: {
    //配置根路径别名： import('/@/pages/login/login.vue')
    alias: {
      '/@': path.resolve(__dirname, 'src')
    }
  },
  
  // 跨域
  server: {
    //使用IP能访问
    host: '0.0.0.0',
    port: process.env.VITE_PORT,
    // 热更新
    hmr: true,
    //自定义代理规则
    proxy: {
      // 选项写法
      '/api': {
        // target: "https://admin.ccc.com",  //代理服务器路径
        // changeOrigin: true,
        // rewrite: (path) => path.replace(/^\/api/, ""),
      }
    }
  }
/*
      proxy: {
      '/api1': {// 匹配所有以 '/api1'开头的请求路径
        target: 'http://localhost:5000',// 代理目标的基础路径  //代理服务器路径
        changeOrigin: true,
        pathRewrite: {'^/api1': ''}   // 前缀,必须写
      },
      '/api2': {// 匹配所有以 '/api2'开头的请求路径
        target: 'http://localhost:5001',// 代理目标的基础路径
        changeOrigin: true,
        pathRewrite: {'^/api2': ''}   //这样配置后，当你的应用发起以 /api2 开头的请求时，代理将把请求转发到 代理目标的基础路径
      }
    }
  //changeOrigin设置为true时，服务器收到的请求头中的host为：localhost:5000
  //changeOrigin设置为false时，服务器收到的请求头中的host为：localhost:8080
  //changeOrigin默认值为true
*/
});
