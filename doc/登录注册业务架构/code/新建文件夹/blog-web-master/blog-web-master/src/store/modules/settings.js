import defaultSettings from '@/settings'

const { fixedHeader, sidebarLogo, defaultAvatar } = defaultSettings

const state = {
  fixedHeader: fixedHeader,
  sidebarLogo: sidebarLogo,
  defaultAvatar: defaultAvatar
}

const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  }
}

const actions = {
  changeSetting({ commit }, data) {
    commit('CHANGE_SETTING', data)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
