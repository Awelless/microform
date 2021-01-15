import Vue from 'vue'
import VueRouter from 'vue-router'
import FieldList from './pages/FieldList.vue'
import ResponseList from './pages/ResponseList.vue'
import Login from './pages/Login.vue'
import Registration from './pages/Registration.vue'
import ResponseForm from './pages/ResponseForm.vue'
import EditProfile from './pages/EditProfile.vue'

Vue.use(VueRouter)

const routes = [
    { path: '/',             component: ResponseForm },
    { path: '/fields',       component: FieldList },
    { path: '/responses',    component: ResponseList },
    { path: '/login',        component: Login },
    { path: '/registration', component: Registration },
    { path: '/edit-profile', component: EditProfile },
    { path: '*',             component: ResponseForm },
]

export default new VueRouter({
    mode: 'history',
    routes
})