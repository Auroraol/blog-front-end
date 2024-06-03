export type Chat = {
    id: number,
    room: number,
    data: ChatInfo
}

export type ChatInfo = {
    id?: number,
    avatar: string,
    name: string,
    content: string,
    date: Date
}

//数据库中直接请求过来的对应房间的消息记录的chatLog类型
export type ChatLog = {
    roomName: string
    account: string
    content: string
    date: string
}


//要渲染在客户端上的消息记录的类型，要用ChatLog的account请求对应的用户拿到信息
export type ChatLogBox = {
    id: number
    account: string
    head: string
    nickName: string
    date: string
    content: string
}

export type ChatUserInfo = Omit<ChatInfo, "id" | "content" | "date">