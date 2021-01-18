<template>
    <div class="mt-4 mb-3">
        <div class="row">
            <div class="col-sm-1"></div>
            <div class="col-sm-10">
                <h4>Responses</h4>

                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th
                                v-for="field in fields"
                                :key="'field' + field.id"
                                scope="col"
                            >
                                {{field.label}}
                            </th>
                        </tr>
                        </thead>

                        <tbody>
                        <response-row
                            v-for="response in sortedResponses"
                            :key="response.id"
                            :response-attr="response"
                            :fields="fields"
                        />
                        </tbody>
                    </table>
                </div>
                <lazy-loader />
            </div>
            <div class="col-sm-1"></div>
        </div>
    </div>
</template>

<script>
    import fieldsApi from '../api/fields'
    import ResponseRow from '../components/response/ResponseRow.vue'
    import {mapActions, mapGetters} from 'vuex'
    import LazyLoader from '../components/LazyLoader.vue'

    export default {
        name: 'ResponseList',
        components: {LazyLoader, ResponseRow},
        data() {
            return {
                fields: []
            }
        },
        computed: mapGetters(['sortedResponses']),
        async created() {
            const response = await fieldsApi.getActive()
            const data     = await response.json()
            this.fields    = data.sort((a, b) => a.id - b.id)
        }
    }
</script>

<style scoped>
    .table-responsive {
        overflow-x: auto;
    }
</style>