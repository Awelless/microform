<template>
    <table class="table">
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
</template>

<script>
    import fieldsApi from '../api/fields'
    import ResponseRow from '../components/response/ResponseRow.vue'
    import { mapGetters } from 'vuex'

    export default {
        name: 'ResponseList',
        components: {ResponseRow},
        data() {
            return {
                fields: []
            }
        },
        computed: mapGetters(['sortedResponses']),
        async created() {
            const response = await fieldsApi.getAll()
            const data     = await response.json()
            this.fields    = data.sort((a, b) => a.id - b.id)
        }
    }
</script>

<style scoped>

</style>