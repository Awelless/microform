<template>
    <div>
        <div v-if="success">
            {{success}}
        </div>

        <label for="current">Current Password</label>
        <input type="password" id="current" v-model="currentPassword">

        <label for="new">New Password</label>
        <input type="password" id="new" v-model="newPassword">

        <label for="confirm">Confirm Password</label>
        <input type="password" id="confirm" v-model="confirmPassword">

        <button @click="save">Save</button>
        <button @click="changeComponent">Edit Profile</button>
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
                errors: new Map
            }
        },
        methods: {
            isValid() {
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
                    return
                }

                this.$http.put(
                    '/api/users/change-password', {},
                    {params: {oldPassword: this.currentPassword, newPassword: this.newPassword}}
                ).then(response => {
                    this.success = 'Password is updated'
                }, response => {
                    response.json().then(data => {
                        this.errors.set('oldPassword', data.error)
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