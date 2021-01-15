<template>
    <header>
        <router-link to="/">MicroForm</router-link>
        <router-link to="/responses">All Responses</router-link>

        <router-link v-if="isLoggedIn" to="/fields">All Fields</router-link>
        <router-link v-if="isLoggedIn" to="/edit-profile">Edit Profile</router-link>
        <button v-if="isLoggedIn" @click="logout">Log Out</button>

        <button v-if="!isLoggedIn" @click="login">Log In</button>
        <button v-if="!isLoggedIn" @click="register">Sign Up</button>
    </header>
</template>

<script>
    import {mapActions, mapGetters, mapMutations} from 'vuex'

    export default {
        name: 'AppHeader',
        data() {
            return {
                isLoggedIn: false
            }
        },
        computed: mapGetters(['principal']),
        watch: {
            principal(newVal, oldVal) {
                this.isLoggedIn = !!newVal
            }
        },
        methods: {
            ...mapMutations(['removePrincipalMutation']),
            logout() {
                this.$http.post('/api/auth/logout')
                this.removePrincipalMutation()
                this.$router.push('/')
            },
            login() {
                this.$router.push('/login')
            },
            register() {
                this.$router.push('/registration')
            }
        }
    }
</script>

<style scoped>

</style>