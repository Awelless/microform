<template>
    <response-success
        v-if="isSent"
        :submit-one-more-response="submitOneMoreResponse"
    />
    <div v-else>
        <template
            v-for="(field, i) in fields"
        >
            <h6>{{field.label}}</h6>

            <input v-if="field.type === 'SINGLE_LINE_TEXT'" type="text" v-model="responseFields[i]">

            <textarea v-else-if="field.type === 'MULTILINE_TEXT'" v-model="responseFields[i]" />

            <template v-else-if="field.type === 'RADIO_BUTTON'">
                <template
                    v-for="option in field.options"
                >
                    <input type="radio" :id="field.id + '#' +option" name="gender" :value="option" v-model="responseFields[i]">
                    <label :for="field.id + '#' +option">{{ option }}</label><br>
                </template>
            </template>

            <input v-else-if="field.type === 'CHECKBOX'" type="checkbox" v-model="responseFields[i]">

            <select v-else-if="field.type === 'COMBOBOX'" v-model="responseFields[i]">
                <option
                    v-for="option in field.options"
                    :key="field.id + '#' + option"
                >
                    {{option}}
                </option>
            </select>

            <input v-else type="date" v-model="responseFields[i]">

        </template>

        <button @click="submit">Submit</button>
        <button @click="reset">Reset</button>
    </div>
</template>

<script>
    import fieldsApi from '../api/fields'
    import {mapActions} from 'vuex'
    import ResponseSuccess from '../components/ResponseSuccess.vue'

    export default {
        name: 'ResponseForm',
        components: {ResponseSuccess},
        data() {
            return {
                fields: [],
                responseFields: [],
                isSent: false
            }
        },
        methods: {
            ...mapActions(['addResponseAction']),
            submit() {
                let responseBody = new Map()
                this.responseFields.forEach((item, i) => {
                    const fieldId = this.fields[i].id
                    responseBody.set(fieldId, item)
                })

                const response = {
                    body: Object.fromEntries(responseBody)
                }

                this.addResponseAction(response)

                this.isSent = true

                this.responseFields.fill('')
            },
            reset() {
                for (let i = 0; i < this.responseFields.length; ++i) {
                    this.responseFields.splice(i, 1, '')
                }
            },
            submitOneMoreResponse() {
                this.isSent = false
            }
        },
        async created() {
            const response = await fieldsApi.getActive()
            const data     = await response.json()
            this.fields    = data.sort((a, b) => a.id - b.id)

            this.fields.forEach(item => this.responseFields.push(''))
        }
    }
</script>

<style scoped>

</style>