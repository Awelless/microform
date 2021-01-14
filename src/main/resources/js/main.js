import Vue from 'vue'
import './api/resource'
import router from './router'
import store from './store'
import '@babel/polyfill'
import App from './App.vue'

new Vue({
    el: '#app',
    store,
    router,
    render: a => a(App)
})