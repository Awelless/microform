<template>
    <nav class="navbar navbar-expand-sm navbar-light" style="background-color: #ffffff;">
        <div class="container-fluid">
            <router-link class="navbar-brand" to="/">
                <h4>
                    <b>
                      Micro
                      <span class="text-primary">Form</span>
                    </b>
                </h4>
            </router-link>

            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
                <ul class="navbar-nav me-auto"></ul>

                <router-link class="nav-link text-muted" to="/responses">All Responses</router-link>

                <router-link class="nav-link text-muted" v-if="isLoggedIn" to="/fields">All Fields</router-link>

                <template v-if="isLoggedIn">
                    <div class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            {{principal.firstName}} {{principal.lastName}}
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                            <li><router-link class="dropdown-item" to="/edit-profile">Edit Profile</router-link></li>
                            <li><router-link class="dropdown-item" to="/change-password">Change Password</router-link></li>
                            <li><button class="dropdown-item" @click="logout">Log Out</button></li>
                        </ul>
                    </div>
                </template>

                <template v-if="!isLoggedIn && $route.path !== '/login' && $route.path !== '/registration'">
                    <button class="btn btn-primary me-2" @click="login">Log In</button>
                    <button class="btn btn-light text-primary" @click="register">Sign Up</button>
                </template>
            </div>
        </div>
    </nav>
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