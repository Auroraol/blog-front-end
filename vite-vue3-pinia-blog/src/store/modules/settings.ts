import { defineStore } from 'pinia';
import defaultSettings from '/@/settings'
const { fixedHeader, sidebarLogo, defaultAvatar } = defaultSettings


export const useSettingsStore = defineStore( 'settings', {
  state: () => ({
    fixedHeader: fixedHeader, 
    sidebarLogo: sidebarLogo, 
    defaultAvatar: defaultAvatar
  }),
  actions: {
    changeSetting({ state }, { key, value }) {
      if (state.hasOwnProperty(key)) {
        state[key] = value;
      }
    }
  }
});

/**
fixedHeader、sidebarLogo 和 defaultAvatar 三个属性，分别对应应用程序的固定头部、侧边栏 Logo 和默认头像。
changeSetting({ state }, { key, value })：用于修改应用程序的设置。这个 action 接受两个参数，{ key, value }，其中 key 表示要修改的设置属性，value 表示新的属性值。在 action 中，首先检查要修改的属性是否存在于当前状态中，如果存在，则更新该属性的值为新的值。
 */