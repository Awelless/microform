<template>
    <div class="mt-2">
        <div v-if="message && message.type === 'SUCCESS'" class="alert alert-success" role="alert">
            {{ message.text }}
        </div>

        <div v-else-if="message && message.type === 'INFO'" class="alert alert-primary" role="alert">
            {{ message.text }}
        </div>

        <div v-else-if="message && message.type === 'FAILURE'" class="alert alert-danger" role="alert">
            {{ message.text }}
        </div>
    </div>
</template>

<script>
    import {mapGetters} from 'vuex'

    export default {
        name: 'message',
        computed: mapGetters(['message'])
    }
</script>

<style scoped>

</style>