// TODO 全局公共的 TS 类型声明

// 分类列表信息响应数据类型
export type categoryItem = {
  id: number;
  name: string;
  parentId: number;
  deleted: number;
};

// 定义文章响应数据的类型
export type ArticleRequestData = {
  id: number | null;
  original: number;
  status: number;
  categoryId: number | string;
  title: string;
  summary: string;
  content: string;
  htmlContent?: string; // 可选属性
  cover: string;
  tagIds?: number[]; // 可选属性
  reproduce?: string; // 可选属性
};

// 用户信息
export type userInfoResponseData = {
  id: number;
  username: string;
  password: string;
  mobile: number;
  nickname: string;
  gender: number;
  birthday: string;
  email?: string;
  brief?: string;
  avatar?: string;
  status: number;
  admin: number;
  createTime: string;
  roles: string[];
};
// 分页参数
export type Page = {
  current: number;
  size: number;
};
