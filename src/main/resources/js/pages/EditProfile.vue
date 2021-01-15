<template>
    <div>
        <safe-edit
            v-if="isSafeEditVisible"
            :change-component="changeComponent"
        />
        <password-change
            v-else
            :change-component="changeComponent"
        />
    </div>
</template>

<script>
    import SafeEdit from '../components/profile/SafeEdit.vue'
    import PasswordChange from '../components/profile/PasswordChange.vue'

    export default {
        name: 'EditProfile',
        components: {PasswordChange, SafeEdit},
        data() {
            return {
                isSafeEditVisible: true
            }
        },
        methods: {
            changeComponent() {
                this.isSafeEditVisible = !this.isSafeEditVisible
            }
        },
        created() {
            if (this.$store.state.principal === null) {
                this.$router.push('/')
            }
        }
    }
</script>

<style scoped>

</style>