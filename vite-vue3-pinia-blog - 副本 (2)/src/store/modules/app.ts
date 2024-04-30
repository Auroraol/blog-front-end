import { defineStore } from 'pinia';
import Cookies from 'js-cookie';

export const useAppStore = defineStore('app', {
  state: () => ({
    sidebar: {
      opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
      withoutAnimation: false
    },
    device: 'desktop'
  }),

  actions: {
    toggleSidebar() {
      this.sidebar.opened = !this.sidebar.opened;
      this.sidebar.withoutAnimation = false;
      if (this.sidebar.opened) {
        Cookies.set('sidebarStatus', "1");
      } else {
        Cookies.set('sidebarStatus', "0");
      }
    },
    closeSidebar(withoutAnimation) {
      Cookies.set('sidebarStatus', "0");
      this.sidebar.opened = false;
      this.sidebar.withoutAnimation = withoutAnimation;
    },
    toggleDevice(device) {
      this.device = device;
    }
  },
})

/**
 !!+ Cookies.get('sidebarStatus')：在条件成立的情况下，这段代码首先将获取到的 Cookie 值转换为数字类型，然后通过一元操作符 + 将其转换为数字，接着再次使用一元操作符 ! 对其取反，最终得到一个布尔值。这个过程可以将任何非空字符串转换为布尔值 true，并且将数字 0 转换为 false，因为 0 被认为是假值。
toggleSidebar()：用于切换侧边栏的状态，根据当前状态打开或关闭侧边栏，并根据状态设置相应的 Cookie。
closeSidebar(withoutAnimation)：用于关闭侧边栏，接受一个参数 withoutAnimation，表示是否要关闭动画效果，并设置相应的 Cookie。
toggleDevice(device)：用于切换设备类型，接受一个参数 device，用于设置当前设备类型。
 */