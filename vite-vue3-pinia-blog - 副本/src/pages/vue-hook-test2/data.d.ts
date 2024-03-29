// 模块 TS 类型
// 写这个的目的是为了在编写代码时提供类型安全性、智能提示以及代码可读性。  注意: 只有手动触发的才能补全

// 定义单个对象的类型
export type HitokotoType = {
  id: number;
  uuid: string;
  hitokoto: string;
  type: string;
  from: string;
  from_who: string | null;
  creator: string;
  creator_uid: number;
  reviewer: number;
  commit_from: string;
  created_at: string;
  length: number;
}
