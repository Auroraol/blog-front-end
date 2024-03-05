// 模块 TS 类型
// 写这个的目的是为了在编写代码时提供类型安全性、智能提示以及代码可读性。

// 定义单个对象的类型
export type PostType = {
  userId: number;
  id: number;
  title: string;
  body: string;
};

// 定义整个响应数据的类型
export type ApiResponse = PostType[];