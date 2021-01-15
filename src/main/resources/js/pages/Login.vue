<template>
    <div>
        <h3>Log In</h3>

        <div v-if="error">
            Error: {{error}}
        </div>

        <h6>Username</h6>
        <input type="text" v-model="username" @keyup.enter="doLogIn">

        <h6>Password</h6>
        <input type="password" v-model="password" @keyup.enter="doLogIn">

        <br>
        <button @click="doLogIn">Log In</button>
        <router-link to="/registration">Sign Up</router-link>
    </div>
</template>

<script>
    import {mapActions} from 'vuex'

    export default {
        name: 'Login',
        data() {
            return {
                username: '',
                password: '',
                error: null
            }
        },
        methods: {
            ...mapActions(['initFieldsAction', 'initPrincipalAction']),
            async doLogIn() {
                if (this.username === '' || this.password === '') {
                    return
                }

                this.$http.post('/api/auth/login', {}, { params: {username: this.username, password: this.password}}).then(response => {
                    this.initFieldsAction()
                    this.initPrincipalAction()
                    this.$router.push('/')
                }, response => {
                    response.json().then(data => {
                        this.error = data.error
                    })
                    this.password = ''
                })
            }
        },
        created() {
            if (this.$store.state.principal !== null) {
                this.$router.push('/')
            }
        }
    }
</script>

<style scoped>

</style>