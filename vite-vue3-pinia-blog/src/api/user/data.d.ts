/* 模块 TS 类型
   写这个的目的是为了在编写代码时提供类型安全性、智能提示以及代码可读性。
/*/

// 账号登录响应结果
export type LoginResponseType = {
    access_token: string
    token_type: string
    refresh_token: string
}

// 用户信息
export type userInfoResponseType = {
    id: number;
    username: string;
    password: string;
    mobile: number;
    nickname: string;
    gender: number;
    birthday: string;
    status: number;
    admin: number;
    createTime: string;
    roles: string[];
}