import Vue from 'vue'
import Vuex from 'vuex'

import fieldsApi from './api/fields'
import responsesApi from './api/responses'
import usersApi from './api/users'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        fields: [],
        responses: [],
        principal: null
    },
    getters: {
        sortedFields: state => (state.fields).sort((a, b) => b.id - a.id),
        sortedResponses: state => (state.responses).sort((a, b) => b.id - a.id),
        principal: state => state.principal
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
        }
    },
    actions: {
        async initFieldsAction({commit}) {
            const response = await fieldsApi.getAll()
            const data     = await response.json()

            commit('initFieldsMutation', data)
        },
        async addFieldAction({commit}, field) {
            const response = await fieldsApi.save(field)
            const data     = await response.json()

            commit('addFieldMutation', data)
        },
        async updateFieldAction({commit}, field) {
            const response = await fieldsApi.update(field)
            const data = await response.json()

            commit('updateFieldMutation', data)
        },
        async removeFieldAction({commit}, field) {
            const response = await fieldsApi.remove(field.id)

            if (response.ok) {
                commit('removeFieldMutation', field)
            }
        },
        async initResponsesAction({commit}) {
            const response = await responsesApi.get()
            const data     = await response.json()

            commit('initResponsesMutation', data)
        },
        async addResponseAction({commit, state}, responseItem) {
            const response = await responsesApi.save(responseItem)
            const data     = await response.json()

            if (state.responses.find(item => item.id === data.id)) {
                return
            }

            commit('addResponseMutation', data)
        },
        async initPrincipalAction({commit}) {
            try {
                const response = await usersApi.getMyProfile()
                const data     = await response.json()

                commit('updatePrincipalMutation', data)
            } catch (error) {}
        },
        async updatePrincipalAction({commit}, principal) {
            const response = await usersApi.update(principal)
            const data     = await response.json()

            commit('updatePrincipalMutation', data)
        }
    }
})