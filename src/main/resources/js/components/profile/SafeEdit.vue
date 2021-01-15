<template>
    <div>
        <label for="firstName">First Name</label>
        <input type="text" id="firstName" v-model="firstName">

        <label for="lastName">Last Name</label>
        <input type="text" id="lastName" v-model="lastName">

        <label for="email">Email</label>
        <input type="text" id="email" v-model="email">

        <label for="phoneNumber">Phone Number</label>
        <input type="text" id="phoneNumber" v-model="phoneNumber">

        <button @click="save">Save</button>
        <button @click="init(principal)">Reset</button>
        <button @click="changeComponent">Change Password</button>
    </div>
</template>

<script>
    import {mapActions, mapGetters} from 'vuex'

    export default {
        name: 'SafeEdit',
        props: ['changeComponent'],
        data() {
            return {
                firstName: '',
                lastName: '',
                email: '',
                phoneNumber: '',
                errors: new Map
            }
        },
        computed: mapGetters(['principal']),
        watch: {
            principal(newVal, oldVal) {
                this.init(newVal)
            }
        },
        methods: {
            ...mapActions(['updatePrincipalAction']),
            isValid() {
                this.errors = new Map

                if (this.firstName.length > 255) {
                    this.errors.set('firstName', `First Name length shouldn't be longer than 255`)
                }

                if (this.lastName.length > 255) {
                    this.errors.set('lastName', `Last Name length shouldn't be longer than 255`)
                }

                if (!this.email.match(/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/)) {
                    this.errors.set('email', `Email is invalid`)
                }

                if (this.email.length === 0) {
                    this.errors.set('email', `Email is necessary`)
                }

                if (!this.phoneNumber.match(/\d{12}/)) {
                    this.errors.set('phoneNumber', `Phone number is invalid`)
                }

                return this.errors.size === 0
            },
            save() {
                if (!this.isValid()) {
                    console.log(this.errors)
                    return
                }

                const newUser = {
                    firstName: this.firstName,
                    lastName: this.lastName,
                    email: this.email,
                    phoneNumber: this.phoneNumber
                }

                this.updatePrincipalAction(newUser)

                this.init(newUser)
            },
            init(principal) {
                this.firstName = principal.firstName
                this.lastName = principal.lastName
                this.email = principal.email
                this.phoneNumber = principal.phoneNumber
            }
        },
        created() {
            if (this.principal !== null) {
                this.init(this.principal)
            }
        }
    }
</script>

<style scoped>

</style>