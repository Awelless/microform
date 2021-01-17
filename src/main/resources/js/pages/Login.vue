<template>
    <div class="row">
        <div class="col-sm-4"></div>
        <div class="col-sm-4">
            <div class="text-center">
                <h4>Log In</h4>
            </div>

            <div v-if="loginMessage && loginMessage.type === 'SUCCESS'" class="alert alert-success" role="alert">
                {{ loginMessage.text }}
            </div>

            <div v-if="loginMessage && loginMessage.type === 'INFO'" class="alert alert-primary" role="alert">
                {{ loginMessage.text }}
            </div>

            <div v-if="loginMessage && loginMessage.type === 'FAILURE'" class="alert alert-danger" role="alert">
                {{ loginMessage.text }}
            </div>

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

    export default {
        name: 'Login',
        data() {
            return {
                email: '',
                password: ''
            }
        },
        computed: mapGetters(['loginMessage']),
        methods: {
            ...mapActions(['initFieldsAction', 'initPrincipalAction']),
            ...mapMutations(['addLoginMessageMutation', 'removeLoginMessageMutation']),
            async doLogIn() {
                this.removeLoginMessageMutation()

                if (this.username === '' || this.password === '') {
                    return
                }

                this.$http.post('/api/auth/login', {}, { params: {email: this.email, password: this.password}}).then(response => {
                    this.initFieldsAction()
                    this.initPrincipalAction()
                    this.$router.push('/')
                }, response => {
                    response.json().then(data => {
                        this.addLoginMessageMutation(failureMessage(data.error))
                    })
                    this.password = ''
                })
            }
        },
        created() {
            if (this.$store.state.principal !== null) {
                this.$router.push('/')
            }
        },
        beforeRouteLeave(to, from, next) {
            this.removeLoginMessageMutation()
            next()
        }
    }
</script>

<style scoped>

</style>