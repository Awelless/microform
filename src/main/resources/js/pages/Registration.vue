<template>
    <div>
        <h3>Sign Up</h3>

        <label for="email">Email</label>
        <input type="text" id="email" v-model="email" @keyup.enter="save">

        <label for="username">Username</label>
        <input type="text" id="username" v-model="username" @keyup.enter="save">

        <label for="password">Password</label>
        <input type="password" id="password" v-model="password" @keyup.enter="save">

        <label for="confirm">Confirm Password</label>
        <input type="password" id="confirm" v-model="confirmPassword" @keyup.enter="save">

        <label for="firstName">First Name</label>
        <input type="text" id="firstName" v-model="firstName" @keyup.enter="save">

        <label for="lastName">Last Name</label>
        <input type="text" id="lastName" v-model="lastName" @keyup.enter="save">

        <label for="phone">Phone Number</label>
        <input type="text" id="phone" v-model="phoneNumber" @keyup.enter="save">

        <br>
        <button @click="save">Sign Up</button>
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
                confirmPassword: '',
                firstName: '',
                lastName: '',
                phoneNumber: '',
                errors: new Map
            }
        },
        methods: {
            isValid() {
                this.errors = new Map

                if (!this.email.match(/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/)) {
                    this.errors.set('email', `Email is invalid`)
                }

                if (this.email.length === 0) {
                    this.errors.set('email', `Email is necessary`)
                }

                if (!this.username.match(/\w+/)) {
                    this.errors.set('username', `Username should contain only letters of latin alphabet, numbers and '_' symbol`)
                }

                if (this.username.length < 2 || this.username.length > 63) {
                    this.errors.set('username', `Username length should be from 2 to 63`)
                }

                if (this.password.length < 6 || this.password.length > 255) {
                    this.errors.set('password', 'Password length should be from 6 to 255')
                }

                if (!this.password.match(/\w+/) && !this.errors.has('password')) {
                    this.errors.set('password', `Password should contain only letters of latin alphabet, numbers and '_' symbol`)
                }

                if (this.password !== this.confirmPassword) {
                    this.errors.set('confirm', 'Passwords are not equal')
                }

                if (this.firstName.length > 255) {
                    this.errors.set('firstName', `First Name length shouldn't be longer than 255`)
                }

                if (this.lastName.length > 255) {
                    this.errors.set('lastName', `Last Name length shouldn't be longer than 255`)
                }

                if (this.phoneNumber.length > 0 && !this.phoneNumber.match(/\d{12}/)) {
                    this.errors.set('phoneNumber', `Phone number is invalid`)
                }

                return this.errors.size === 0
            },
            save() {
                if (!this.isValid()) {
                    this.password = ''
                    this.confirmPassword = ''
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
                    //user isn't unique
                    response.json().then(data => {
                        const error = data.error
                        if (error === 'Username is already taken') {
                            this.errors.set('username', error)
                        } else {
                            this.errors.set('email', error)
                        }
                    })

                    this.password = ''
                    this.confirmPassword = ''

                    console.log(this.errors)
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