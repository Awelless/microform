import Vue from 'vue'

const users = Vue.resource('/api/users{/modifier}')

export default {
    get:      () => users.get({}),
    save:   user => users.save({}, user),
    update: user => users.update({}, user),
    changePassword: (oldPassword, newPassword) => users.update({ modifier: 'change-password' }, { oldPassword, newPassword}),
}