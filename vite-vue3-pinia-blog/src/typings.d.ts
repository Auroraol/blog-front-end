 // TODO 全局公共的 TS 类型声明
  // 定义整个响应数据的类型
  export type ResponseResult<T> = {
    code: number;
    data?: T;
    message: string;
  }