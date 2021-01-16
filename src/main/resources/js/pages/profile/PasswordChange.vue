<template>
    <div class="row mt-4 mb-3">
        <div class="col-sm-4"></div>
        <div class="col-sm-4">
            <h4 class="text-center">Change Password</h4>

            <div v-if="success" class="alert alert-success">
                {{success}}
            </div>

            <div class="mb-3 input-group has-validation">
                <input
                    type="password"
                    :class="wrongPassword ? 'form-control is-invalid' : 'form-control'"
                    placeholder="Password"
                    aria-describedby="passwordFeedback"
                    v-model="currentPassword"
                    @keyup.enter="save"
                >
                <span class="input-group-text text-danger bg-light">*</span>
                <div v-if="wrongPassword" class="invalid-feedback" id="passwordFeedback">
                    Password is incorrect
                </div>
            </div>

            <div class="mb-3 input-group has-validation">
                <input
                    type="password"
                    :class="errors.has('password') ? 'form-control is-invalid' : 'form-control'"
                    placeholder="New Password"
                    aria-describedby="newPasswordFeedback"
                    v-model="newPassword"
                    @keyup.enter="save"
                >
                <span class="input-group-text text-danger bg-light">*</span>
                <div v-if="errors.has('password')" class="invalid-feedback" id="newPasswordFeedback">
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


            <div class="text-center">
                <button class="btn btn-primary" @click="save">Save</button>
            </div>
        </div>
        <div class="col-sm-4"></div>
    </div>
</template>

<script>
    export default {
        name: 'PasswordChange',
        props: ['changeComponent'],
        data() {
            return {
                currentPassword: '',
                newPassword: '',
                confirmPassword: '',
                success: null,
                wrongPassword: false,
                errors: new Map
            }
        },
        methods: {
            isValid() {
                this.wrongPassword = false
                this.errors = new Map

                if (this.newPassword.length < 6 || this.newPassword.length > 255) {
                    this.errors.set('password', 'Password length should be from 6 to 255')
                }

                if (!this.newPassword.match(/\w+/) && !this.errors.has('password')) {
                    this.errors.set('password', `Password should contain only letters of latin alphabet, numbers and '_' symbol`)
                }

                if (this.newPassword !== this.confirmPassword) {
                    this.errors.set('confirm', 'Passwords are not equal')
                }

                return this.errors.size === 0
            },
            save() {
                if (!this.isValid()) {
                    this.currentPassword = ''
                    this.newPassword = ''
                    this.confirmPassword = ''

                    return
                }

                this.$http.put(
                    '/api/users/change-password', {},
                    {params: {oldPassword: this.currentPassword, newPassword: this.newPassword}}
                ).then(response => {
                    this.success = 'Password is updated'
                }, response => {
                    response.json().then(data => {
                        this.wrongPassword = true
                    })
                })

                this.currentPassword = ''
                this.newPassword = ''
                this.confirmPassword = ''
            }
        }
    }
</script>

<style scoped>

</style>