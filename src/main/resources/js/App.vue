<template>
    <div>
        <app-header />
        <router-view />
    </div>
</template>

<script>
    import AppHeader from './components/AppHeader.vue'
    import { connect, setHandler, disconnect } from './ws'
    import { mapActions, mapMutations } from 'vuex'

    export default {
        name: 'App',
        components: {AppHeader},
        methods: {
            ...mapActions(['initResponsesAction']),
            ...mapMutations(['addResponseMutation'])
        },
        created() {
            this.initResponsesAction()
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