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
        loginMessage: null,
        fieldPageParams: null
    },
    getters: {
        fieldPageParams: state => state.fieldPageParams,
        fieldPage: state => state.fields,
        sortedResponses: state => state.responses,
        principal: state => state.principal,
        loginMessage: state => state.loginMessage
    },
    mutations: {
        initFieldsMutation(state, fields) {
            state.fields = fields
        },
        updateFieldMutation(state, field) {
            const updateIndex = state.fields.findIndex(item => item.id === field.id)
            state.fields.splice(updateIndex, 1, field)
        },
        initResponsesMutation(state, responses) {
            state.responses = responses
        },
        addResponseMutation(state, response) {
            state.responses.splice(0, 0, response)
        },
        addResponsePageMutation(state, responses) {
            let allResponses = new Map

            state.responses.forEach(value => allResponses.set(value.id, value))
            responses.forEach(value => allResponses.set(value.id, value))

            state.responses = Object.values(Object.fromEntries(allResponses)).reverse()
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
        },
        updatePageParamsMutation(state, pageParams) {
            state.fieldPageParams = pageParams
        },
    },
    actions: {
        loadFieldsPageAction({dispatch, commit}, page) {
            fieldsApi.page(page).then(response => {
                response.json().then(data => {
                    if (data.currentPage > data.totalPages && page > 1) {
                        dispatch('loadFieldsPageAction', data.totalPages)
                    } else {
                        console.log(data)
                        commit('initFieldsMutation', data.body)
                        commit('updatePageParamsMutation', {currentPage: data.currentPage, totalPages: data.totalPages})
                    }
                })
            }, response => {
                commit('removePrincipalMutation')
                commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                router.push('/login')
            })
        },
        addFieldAction({dispatch, commit, state}, field) {
            fieldsApi.save(field).then(response => {
                dispatch('loadFieldsPageAction', 1)
            }, response => {
                commit('removePrincipalMutation')
                commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                router.push('/login')
            })
        },
        updateFieldAction({commit}, field) {
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
        removeFieldAction({dispatch, commit, state}, field) {
            fieldsApi.remove(field.id).then(response => {
                dispatch('loadFieldsPageAction', state.fieldPageParams.currentPage)
            }, response => {
                commit('removePrincipalMutation')
                commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                router.push('/login')
            })
        },
        loadResponsesPageAction({commit, state}, {page, setFullyScrolled}) {
            responsesApi.page(page).then(response => {
                response.json().then(data => {
                    if (data.currentPage > data.totalPages && page > 1) {
                        setFullyScrolled()
                    } else {
                        commit('addResponsePageMutation', data.body)
                    }
                })
            }, response => {
                commit('removePrincipalMutation')
                commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                router.push('/login')
            })
        },
        addResponseAction({commit, state}, responseItem) {
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
        initPrincipalAction({commit}) {
            usersApi.getMyProfile().then(response => {
                response.json().then(data => {
                    commit('updatePrincipalMutation', data)
                })
            })
        },
        updatePrincipalAction({commit}, principal) {
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