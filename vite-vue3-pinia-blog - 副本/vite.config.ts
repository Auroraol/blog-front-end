import { defineConfig, loadEnv } from "vite";
import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";
import { VueHooksPlusResolver } from "@vue-hooks-plus/resolvers";
import vue from "@vitejs/plugin-vue";
import { ElementPlusResolver } from "unplugin-vue-components/resolvers";
import * as path from "path";




export default ({ mode }) => {
  //参数mode为开放模式或生产模式
  //console.log(mode);  // development or product
  const env = loadEnv(mode, process.cwd()); // 获取.env文件里定义的环境变量
  //console.log(env);   //变量在命令行里打印出来
  //console.log(env.VITE_API_URL)
  const port = Number(env.VITE_PORT) || 80
  return defineConfig({
    base: "./",
    // 配置插件
    plugins: [
      vue(), //vue
      // 引入unplugin-auto-import
      AutoImport({
        //引入vue 自动注册api插件
        imports: ["vue", "vue-router", "vuex"], // 配置需要自动导入的库
        include: [/\.[tj]sx?$/, /\.vue$/, /\.vue\?vue/, /\.md$/],
        dts: "src/auto-import.d.ts", // 自动引入生成api的地址
        resolvers: [VueHooksPlusResolver()], //VueHooks 使用
      }),
      // 引入Element-plus
      AutoImport({
        //plus按需引入
        resolvers: [ElementPlusResolver()],
      }),
      Components({
        //plus按需引入
        resolvers: [ElementPlusResolver()],
        dts: "src/components.d.ts", //自动引入生成的组件的地址
      }),
      // TODO
    ],

    build: {
      minify: "terser",
      terserOptions: {
        compress: {
          //生产环境时移除console
          drop_console: true,
          drop_debugger: true,
        },
      },
    },

    resolve: {
      //配置根路径别名： import('/@/pages/login/login.vue')
      alias: {
        "/@": path.resolve(__dirname, "src"),
      },
    },

    /******配置开发服务器******/
    // 配置前端服务地址和端口
    server: {
      host: "0.0.0.0", //使用IP能访问
      port: port, // 端口号
      open: true, // 启动时自动在浏览器打开
      // https: true, // 是否开启 https
      cors: true, //为开发服务器配置 CORS
      hmr: true, // 热更新
      fs: {
        // 可以为项目根目录的上一级提供服务
        allow: [".."],
      },
      //配置自定义代理规则，跨域
      proxy: {
        [env.VITE_APP_BASE_API]: {
          target: env.VITE_API_URL,
          changeOrigin: true,
          ws: true,  // 允许websocket代理
          rewrite: (path) => {
            const regex = new RegExp(`^${env.VITE_APP_BASE_API}`);
            return path.replace(regex, '');
          },
        },
      },
      // '/api': {
      //   target: 'http://localhost:9000',
      //   changeOrigin: true,
      //   rewrite: (path) => path.replace(/^\/api/, '')
      // }
    // }
    },

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
};
