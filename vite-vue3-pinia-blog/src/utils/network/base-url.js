//当切换项目的开发、测试或生产环境时，process.env.NODE_ENV会自动加载成相应环境的配置文件
let baseURL = "";
if (process.env.NODE_ENV === "development") {
  // 开发环境  因为我这里写了配置跨域的重定路径所以是api
  baseURL = "/api";
} else if (process.env.NODE_ENV === "production") {
  // 正式环境 真正的上线网址 不是跨域别名路径,在network不显示
  baseURL = "https://admin.itrustnow.com";
} else {
  // 测试环境
  baseURL = "https://admin.itrustnow.com";
}
export default baseURL;  //baseURL的赋值是在模块级别进行的，这意味着它在模块加载时就会运行，并在导出模块时已经有了确定的值。
