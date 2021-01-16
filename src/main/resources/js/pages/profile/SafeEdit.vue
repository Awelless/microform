<template>
    <div class="row mt-4 mb-3">
        <div class="col-sm-4"></div>
        <div class="col-sm-4">
            <h4 class="text-center">Edit Profile</h4>

            <div v-if="success" class="alert alert-success">
                {{success}}
            </div>

            <div class="mb-3">
                <input
                    type="text"
                    class="form-control"
                    placeholder="Email"
                    v-model="email"
                    aria-describedby="emailFeedback"
                    @keyup.enter="save"
                    disabled
                >
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
                <button class="btn btn-primary me-2" @click="save">Save</button>
                <button class="btn btn-light" @click="init(principal)">Reset</button>
            </div>
        </div>
        <div class="col-sm-4"></div>
    </div>
</template>

<script>
    import {mapActions, mapGetters} from 'vuex'

    export default {
        name: 'SafeEdit',
        props: ['changeComponent'],
        data() {
            return {
                email: '',
                firstName: '',
                lastName: '',
                phoneNumber: '',
                errors: new Map,
                success: null
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

                if (this.phoneNumber.length > 0 && !this.phoneNumber.match(/\d{12}/)) {
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
                    phoneNumber: this.phoneNumber
                }

                this.updatePrincipalAction(newUser)

                this.success = 'Profile is updated'

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