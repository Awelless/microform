<template>
    <div class="row">
        <div class="col-sm-4"></div>
        <div class="col-sm-4">
            <div class="text-center">
                <h4>Sign Up</h4>
            </div>

            <div class="mb-3 input-group has-validation">
                <input
                    type="text"
                    :class="errors.has('email') ? 'form-control is-invalid' : 'form-control'"
                    placeholder="Email"
                    v-model="email"
                    aria-describedby="emailFeedback"
                    @keyup.enter="save"
                >
                <span class="input-group-text text-danger bg-light">*</span>
                <div v-if="errors.has('email')" class="invalid-feedback" id="emailFeedback">
                    {{errors.get('email')}}
                </div>
            </div>

            <div class="mb-3 input-group has-validation">
                <input
                    type="password"
                    :class="errors.has('password') ? 'form-control is-invalid' : 'form-control'"
                    placeholder="Password"
                    aria-describedby="passwordFeedback"
                    v-model="password"
                    @keyup.enter="save"
                >
                <span class="input-group-text text-danger bg-light">*</span>
                <div v-if="errors.has('password')" class="invalid-feedback" id="passwordFeedback">
                    {{errors.get('password')}}
                </div>
            </div>

            <div class="mb-3 input-group has-validation">
                <input
                    type="password"
                    :class="errors.has('confirm') ? 'form-control is-invalid' : 'form-control'"
                    placeholder="Confirm Password"
                    aria-describedby="confirmFeedback"
                    v-model="confirmPassword"
                    @keyup.enter="save"
                >
                <span class="input-group-text text-danger bg-light">*</span>
                <div v-if="errors.has('confirm')" class="invalid-feedback" id="confirmFeedback">
                    {{errors.get('confirm')}}
                </div>
            </div>

            <div class="mb-3">
                <input
                    type="text"
                    :class="errors.has('firstName') ? 'form-control is-invalid' : 'form-control'"
                    placeholder="First Name"
                    aria-describedby="firstNameFeedback"
                    v-model="firstName"
                    @keyup.enter="save"
                >
                <div v-if="errors.has('firstName')" class="invalid-feedback" id="firstNameFeedback">
                    {{errors.get('firstName')}}
                </div>
            </div>

            <div class="mb-3">
                <input
                    type="text"
                    :class="errors.has('lastName') ? 'form-control is-invalid' : 'form-control'"
                    placeholder="Last Name"
                    aria-describedby="lastNameFeedback"
                    v-model="lastName"
                    @keyup.enter="save"
                >
                <div v-if="errors.has('lastName')" class="invalid-feedback" id="lastNameFeedback">
                    {{errors.get('lastName')}}
                </div>
            </div>

            <div class="input-group mb-3 has-validation">
                <span class="input-group-text">+</span>
                <input
                    type="text"
                    :class="errors.has('phoneNumber') ? 'form-control is-invalid' : 'form-control'"
                    placeholder="Phone Number"
                    aria-describedby="phoneNumberFeedback"
                    v-model="phoneNumber"
                    @keyup.enter="save"
                >
                <div v-if="errors.has('phoneNumber')" class="invalid-feedback" id="phoneNumberFeedback">
                    {{errors.get('phoneNumber')}}
                </div>
            </div>

            <div class="text-center">
                <button class="btn btn-primary me-2" @click="save">Sign Up</button>
                <router-link class="btn btn-light text-primary" to="/login">Log In</router-link>
            </div>
        </div>
        <div class="col-sm-4"></div>
    </div>
</template>

<script>
    import usersApi from '../api/users'
    import {mapGetters, mapMutations} from 'vuex'
    import {successMessage} from '../util/loginMessages'

    export default {
        name: 'Registration',
        data() {
            return {
                email: '',
                password: '',
                confirmPassword: '',
                firstName: '',
                lastName: '',
                phoneNumber: '',
                errors: new Map
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
            ...mapMutations(['addLoginMessageMutation']),
            isValid() {
                this.errors = new Map

                if (!this.email.match(/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/)) {
                    this.errors.set('email', `Email is invalid`)
                }

                if (this.email.length === 0) {
                    this.errors.set('email', `Email is necessary`)
                }

                if (this.password.length < 6 || this.password.length > 255) {
                    this.errors.set('password', 'Password length should be from 6 to 255')
                }

                if (this.password.length === 0) {
                    this.errors.set('password', `Password shouldn't be empty`)
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
                    this.addLoginMessageMutation(successMessage('Account is created'))
                    this.$router.push('/login')
                }, response => {
                    //email isn't unique
                    response.json().then(data => {
                        this.errors.set('email', data.error)
                    })

                    this.password = ''
                    this.confirmPassword = ''
                })
            }
        },
        created() {
            if (this.principal !== null) {
                this.$router.push('/')
            }
        },
    }
</script>

<style scoped>

</style>