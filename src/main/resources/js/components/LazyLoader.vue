<template>

</template>

<script>
    import {mapActions} from 'vuex'

    export default {
        name: 'LazyLoader',
        data() {
            return {
                page: 2,
                isFullyScrolled: false
            }
        },
        methods: mapActions(['loadResponsesPageAction']),
        mounted() {
            this.loadResponsesPageAction({
                page: 1,
                setFullyScrolled: () => this.isFullyScrolled = true
            })

            window.onscroll = () => {
                const el = document.documentElement
                const isBottomOfScreen = (el.scrollTop + window.innerHeight + 10) > el.offsetHeight

                if(isBottomOfScreen) {
                    if (!this.isFullyScrolled) {
                        this.loadResponsesPageAction({
                            page: this.page++,
                            setFullyScrolled: () => this.isFullyScrolled = true
                        })
                    }
                }
            }
        },
        beforeDestroy() {
            window.onscroll = null
        }
    }
</script>

<style scoped>

</style>