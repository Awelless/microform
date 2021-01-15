<template>
    <div>
        <table>
            <tr>
                <th
                    v-for="field in fields"
                    :key="'field' + field.id"
                >
                    {{field.label}}
                </th>
            </tr>

            <response-row
                v-for="response in sortedResponses"
                :key="response.id"
                :response-attr="response"
                :fields="fields"
            />
        </table>
    </div>
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