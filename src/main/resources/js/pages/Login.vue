<template>
    <div class="row">
        <div class="col-sm-4"></div>
        <div class="col-sm-4">
            <div class="text-center">
                <h4>Log In</h4>
            </div>

            <message />

            <div class="mb-3">
                <input type="text" class="form-control" placeholder="Email" v-model="email" @keyup.enter="doLogIn">
            </div>

            <div class="mb-3">
                <input type="password" class="form-control" placeholder="Password" v-model="password" @keyup.enter="doLogIn">
            </div>

            <div class="text-center">
                <button class="btn btn-primary me-2" @click="doLogIn">Log In</button>
                <router-link class="btn btn-light text-primary" to="/registration">Sign Up</router-link>
            </div>
        </div>
        <div class="col-sm-4"></div>
    </div>
</template>

<script>
    import {mapActions, mapGetters, mapMutations} from 'vuex'
    import {failureMessage} from '../util/loginMessages'
    import Message from '../components/Message.vue'

    export default {
        name: 'Login',
        components: {Message},
        data() {
            return {
                email: '',
                password: ''
            }
        },
        computed: mapGetters(['principal']),
        watch: {
            principal(newVal, oldVal) {
                if (newVal !== null) {
                    this.$router.push('/')
                }
            }
        },
        methods: {
            ...mapActions(['initPrincipalAction']),
            ...mapMutations(['updateMessageMutation', 'removeMessageMutation']),
            doLogIn() {
                this.removeMessageMutation()

                if (this.username === '' || this.password === '') {
                    return
                }

                this.$http.post('/api/auth/login', {}, { params: {email: this.email, password: this.password}}).then(response => {
                    this.initPrincipalAction()
                    this.$router.push('/')
                }, response => {
                    response.json().then(data => {
                        this.updateMessageMutation(failureMessage(data.error))
                    })
                    this.password = ''
                })
            }
        },
        created() {
            if (this.principal !== null) {
                this.$router.push('/')
            }
        },
        beforeRouteLeave(to, from, next) {
            this.removeMessageMutation()
            next()
        }
    }
</script>

<style scoped>

</style>