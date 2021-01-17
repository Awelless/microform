import Vue from 'vue'
import Vuex from 'vuex'

import fieldsApi from './api/fields'
import responsesApi from './api/responses'
import usersApi from './api/users'
import router from './router'
import {infoMessage} from './util/loginMessages'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        fields: [],
        responses: [],
        principal: null,
        loginMessage: null
    },
    getters: {
        sortedFields: state => (state.fields).sort((a, b) => b.id - a.id),
        sortedResponses: state => (state.responses).sort((a, b) => b.id - a.id),
        principal: state => state.principal,
        loginMessage: state => state.loginMessage
    },
    mutations: {
        initFieldsMutation(state, fields) {
            state.fields = fields
        },
        addFieldMutation(state, field) {
            state.fields.push(field)
        },
        updateFieldMutation(state, field) {
            const updateIndex = state.fields.findIndex(item => item.id === field.id)
            state.fields.splice(updateIndex, 1, field)
        },
        removeFieldMutation(state, field) {
            const deletionIndex = state.fields.findIndex(item => item.id === field.id)

            if (deletionIndex > -1) {
                state.fields.splice(deletionIndex, 1)
            }
        },
        initResponsesMutation(state, responses) {
            state.responses = responses
        },
        addResponseMutation(state, response) {
            state.responses.push(response)
        },
        updatePrincipalMutation(state, principal) {
            state.principal = principal
        },
        removePrincipalMutation(state) {
            state.principal = null
        },
        addLoginMessageMutation(state, message) {
            state.loginMessage = message
        },
        removeLoginMessageMutation(state) {
            state.loginMessage = null
        }
    },
    actions: {
        async initFieldsAction({commit, state}) {
            fieldsApi.getAll().then(response => {
                response.json().then(data => {
                    commit('initFieldsMutation', data)
                })
            }, response => {
                commit('removePrincipalMutation')
                commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                router.push('/login')
            })
        },
        async addFieldAction({commit}, field) {
            fieldsApi.save(field).then(response => {
                response.json().then(data => {
                    commit('addFieldMutation', data)
                })
            }, response => {
                commit('removePrincipalMutation')
                commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                router.push('/login')
            })
        },
        async updateFieldAction({commit}, field) {
            fieldsApi.update(field).then(response => {
                response.json().then(data => {
                    commit('updateFieldMutation', data)
                })
            }, response => {
                commit('removePrincipalMutation')
                commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                router.push('/login')
            })
        },
        async removeFieldAction({commit}, field) {
            fieldsApi.remove(field.id).then(response => {
                commit('removeFieldMutation', field)
            }, response => {
                commit('removePrincipalMutation')
                commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                router.push('/login')
            })
        },
        async initResponsesAction({commit}) {
            responsesApi.get().then(response => {
                response.json().then(data => {
                    commit('initResponsesMutation', data)
                })
            }, response => {
                commit('removePrincipalMutation')
                commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                router.push('/login')
            })
        },
        async addResponseAction({commit, state}, responseItem) {
            responsesApi.save(responseItem).then(response => {
                response.json().then(data => {
                    if (state.responses.find(item => item.id === data.id)) {
                        return
                    }

                    commit('addResponseMutation', data)
                })
            }, response => {
                commit('removePrincipalMutation')
                commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                router.push('/login')
            })
        },
        async initPrincipalAction({commit}) {
            usersApi.getMyProfile().then(response => {
                response.json().then(data => {
                    commit('updatePrincipalMutation', data)
                })
            })
        },
        async updatePrincipalAction({commit}, principal) {
            usersApi.update(principal).then(response => {
                response.json().then(data => {
                    commit('updatePrincipalMutation', data)      
                })
            }, response => {
                commit('removePrincipalMutation')
                commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                router.push('/login')
            })
        }
    }
})