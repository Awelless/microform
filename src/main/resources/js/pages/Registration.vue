<template>
    <div>
        <h3>Sign Up</h3>

        <div v-if="error">
            Error: {{error}}
        </div>

        <h6>Email</h6>
        <input type="text" v-model="email" @keyup.enter="doSignUp">

        <h6>Username</h6>
        <input type="text" v-model="username" @keyup.enter="doSignUp">

        <h6>Password</h6>
        <input type="password" v-model="password" @keyup.enter="doSignUp">

        <h6>Confirm Password</h6>
        <input type="password" v-model="passwordConfirm" @keyup.enter="doSignUp">

        <h6>First Name</h6>
        <input type="text" v-model="firstName" @keyup.enter="doSignUp">

        <h6>Last Name</h6>
        <input type="text" v-model="lastName" @keyup.enter="doSignUp">

        <h6>Phone Number</h6>
        + <input type="text" v-model="phoneNumber" @keyup.enter="doSignUp">

        <br>
        <button @click="doSignUp">Sign Up</button>
        <router-link to="/login">Log In</router-link>
    </div>
</template>

<script>
    import usersApi from '../api/users'

    export default {
        name: 'Registration',
        data() {
            return {
                email: '',
                username: '',
                password: '',
                passwordConfirm: '',
                firstName: '',
                lastName: '',
                phoneNumber: '',
                error: null
            }
        },
        methods: {
            async doSignUp() {
                if (this.email === '' || this.username === '' || this.password === '' || this.password !== this.passwordConfirm) {
                    return
                }

                const user = {
                    email:       this.email,
                    username:    this.username,
                    password:    this.password,
                    firstName:   this.firstName,
                    lastName:    this.lastName,
                    phoneNumber: this.phoneNumber,
                }

                usersApi.save(user).then(response => {
                    this.$router.push('/login')
                }, response => {
                    response.json().then(data => {
                        this.error = data.error
                    })
                })
            }
        }
    }
</script>

<style scoped>

</style>