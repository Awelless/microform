<template>
    <div>
        <app-header />
        <router-view />
    </div>
</template>

<script>
    import AppHeader from './components/Navbar.vue'
    import { connect, setHandler, disconnect } from './ws'
    import { mapActions, mapMutations } from 'vuex'

    export default {
        name: 'App',
        components: {AppHeader},
        methods: {
            ...mapActions(['initPrincipalAction']),
            ...mapMutations(['addResponseMutation'])
        },
        created() {
            this.initPrincipalAction()

            connect()
            setHandler(data => {
                this.addResponseMutation(data)
            })
        },
        beforeDestroy() {
            disconnect()
        }
    }
</script>

<style scoped>

</style>