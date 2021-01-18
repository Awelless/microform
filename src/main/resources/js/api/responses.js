import Vue from 'vue'

const responses = Vue.resource('/api/responses')

export default {
    get:        () => responses.get({}),
    save: response => responses.save({}, response),
    page:     page => Vue.http.get('/api/responses', { params: { page }})
}