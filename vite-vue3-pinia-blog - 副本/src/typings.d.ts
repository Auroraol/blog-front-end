 // TODO 全局公共的 TS 类型声明
 // Token数据类型定义
export type TokenType = {
  accessToken: string;
  refreshToken: string;
}


// USER用户数据类型定义
export type UserType = {
    id: number;
    phone: string;
    username: string;
    password: string;
    gender: string;
    nickname: string;
    birthday: string;
    email: string;
    personalbrief: string;
    avatarimgurl: string;
    recentlylanded: string;
  }
  
  // 定义整个响应数据的类型
  export type ResponseResult<T> = {
    code: number;
    data: T;
    message: string;
  }