import Vue from 'vue'

const users = Vue.resource('/api/users')

export default {
    getMyProfile: () => users.get({}),
    save:       user => users.save({}, user),
    update:     user => users.update({}, user),
}