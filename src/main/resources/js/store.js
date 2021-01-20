import Vue from 'vue'
import Vuex from 'vuex'

import fieldsApi from './api/fields'
import responsesApi from './api/responses'
import usersApi from './api/users'
import router from './router'
import {infoMessage, failureMessage} from './util/loginMessages'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        fields: [],
        responses: [],
        principal: null,
        message: null,
        fieldPageParams: null
    },
    getters: {
        fieldPageParams: state => state.fieldPageParams,
        fieldPage: state => state.fields,
        sortedResponses: state => state.responses,
        principal: state => state.principal,
        message: state => state.message
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
        updateMessageMutation(state, message) {
            state.message = message
        },
        removeMessageMutation(state) {
            state.message = null
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
                        commit('initFieldsMutation', data.body)
                        commit('updatePageParamsMutation', {currentPage: data.currentPage, totalPages: data.totalPages})
                    }
                })
            }, response => {
                if (response.status === 401) {
                    commit('removePrincipalMutation')
                    commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                    router.push('/login')
                } else {
                    response.json().then(data => {
                        commit('updateMessageMutation', failureMessage(data.error))
                    })
                }
            })
        },
        addFieldAction({dispatch, commit, state}, field) {
            fieldsApi.save(field).then(response => {
                dispatch('loadFieldsPageAction', 1)
            }, response => {
                if (response.status === 401) {
                    commit('removePrincipalMutation')
                    commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                    router.push('/login')
                } else {
                    response.json().then(data => {
                        commit('updateMessageMutation', failureMessage(data.error))
                    })
                }
            })
        },
        updateFieldAction({commit}, field) {
            fieldsApi.update(field).then(response => {
                response.json().then(data => {
                    commit('updateFieldMutation', data)
                })
            }, response => {
                if (response.status === 401) {
                    commit('removePrincipalMutation')
                    commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                    router.push('/login')
                } else {
                    response.json().then(data => {
                        commit('updateMessageMutation', failureMessage(data.error))
                    })
                }
            })
        },
        removeFieldAction({dispatch, commit, state}, field) {
            fieldsApi.remove(field.id).then(response => {
                dispatch('loadFieldsPageAction', state.fieldPageParams.currentPage)
            }, response => {
                if (response.status === 401) {
                    commit('removePrincipalMutation')
                    commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                    router.push('/login')
                } else {
                    response.json().then(data => {
                        commit('updateMessageMutation', failureMessage(data.error))
                    })
                }
            })
        },
        loadResponsesPageAction({commit, state}, {page, setFullyScrolled}) {
            responsesApi.page(page).then(response => {
                response.json().then(data => {
                    if (data.currentPage >= data.totalPages) {
                        setFullyScrolled()
                    }

                    commit('addResponsePageMutation', data.body)
                })
            }, response => {
                if (response.status === 401) {
                    commit('removePrincipalMutation')
                    commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                    router.push('/login')
                } else {
                    response.json().then(data => {
                        commit('updateMessageMutation', failureMessage(data.error))
                    })
                }
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
                if (response.status === 401) {
                    commit('removePrincipalMutation')
                    commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                    router.push('/login')
                } else {
                    response.json().then(data => {
                        commit('updateMessageMutation', failureMessage(data.error))
                    })
                }
            })
        },
        initPrincipalAction({commit}) {
            usersApi.getMyProfile().then(response => {
                response.json().then(data => {
                    commit('updatePrincipalMutation', data)
                })
            }, response => {})
        },
        updatePrincipalAction({commit}, principal) {
            usersApi.update(principal).then(response => {
                response.json().then(data => {
                    commit('updatePrincipalMutation', data)      
                })
            }, response => {
                if (response.status === 401) {
                    commit('removePrincipalMutation')
                    commit('addLoginMessageMutation', infoMessage('Please, Log In again'))
                    router.push('/login')
                } else {
                    response.json().then(data => {
                        commit('updateMessageMutation', failureMessage(data.error))
                    })
                }
            })
        }
    }
})