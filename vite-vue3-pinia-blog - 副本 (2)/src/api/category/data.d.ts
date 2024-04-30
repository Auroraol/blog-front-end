/* 模块 TS 类型
   写这个的目的是为了在编写代码时提供类型安全性、智能提示以及代码可读性。
/*/



// 分类列表信息
export type categoryItem = {
    id: number;
    name: string;
    parentId: number;
    deleted: number;
}