import { defineStore } from "pinia";
import { ChatUserInfo } from "/@/types/chat";

export const useChatStore = defineStore("chat", {
  state: () => ({
    // 当前房间号
    room: undefined as number | undefined,
    // 当前用户信息
    chatUserInfo: undefined as ChatUserInfo | undefined
    
    ,

    name: "",
    avatar: "",

  }),
  getters: {
    // 判断登记的信息是否为空
    isNull: (state)=> {
      return !(state.chatUserInfo?.name && state.chatUserInfo?.avatar);
    },
  },
  actions: {
    updateRoom(n: number) {
      this.room = n;
    },
    updateChatUserInfo(data: ChatUserInfo) {
      this.chatUserInfo = data;
    },
    updateChatUserInfo2(name, avatar) {
        this.name = name;
        avatar = avatar;
      },
  },
});
