import { defineStore } from 'pinia';
import Cookies from 'js-cookie';

export const appStore = defineStore('app', {
  state: () => ({
    sidebar: {
      opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
      withoutAnimation: false
    },
    device: 'desktop'
  }),

  actions: {
    toggleSidebar() {
      this.opened = !this.opened;
      this.withoutAnimation = false;
      if (this.opened) {
        Cookies.set('sidebarStatus', 1);
      } else {
        Cookies.set('sidebarStatus', 0);
      }
    },
    closeSidebar(withoutAnimation) {
      Cookies.set('sidebarStatus', 0);
      this.opened = false;
      this.withoutAnimation = withoutAnimation;
    },
    toggleDevice(device) {
      this.device = device;
    }
  },
})
