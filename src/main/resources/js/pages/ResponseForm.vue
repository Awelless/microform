<template>
    <response-success
        v-if="isSent"
        :submit-one-more-response="submitOneMoreResponse"
    />
    <div v-else>
        <template
            v-for="(field, i) in fields"
        >
            <label :for="fields.id + '#' + i">{{field.label}}</label>

            <input v-if="field.type === 'SINGLE_LINE_TEXT'" type="text" :id="fields.id + '#' + i" v-model="responseFields[i]">

            <textarea v-else-if="field.type === 'MULTILINE_TEXT'" :id="fields.id + '#' + i" v-model="responseFields[i]" />

            <template v-else-if="field.type === 'RADIO_BUTTON'" :id="fields.id + '#' + i">
                <template
                    v-for="option in field.options"
                >
                    <label :for="field.id + '#' +option">{{ option }}</label><br>
                    <input type="radio" :id="field.id + '#' +option" name="gender" :value="option" v-model="responseFields[i]">
                </template>
            </template>

            <input v-else-if="field.type === 'CHECKBOX'" type="checkbox" :id="fields.id + '#' + i" v-model="responseFields[i]">

            <select v-else-if="field.type === 'COMBOBOX'" :id="fields.id + '#' + i" v-model="responseFields[i]">
                <option
                    v-for="option in field.options"
                    :key="field.id + '#' + option"
                >
                    {{option}}
                </option>
            </select>

            <input v-else type="date" :id="fields.id + '#' + i" v-model="responseFields[i]">

        </template>

        <button @click="submit">Submit</button>
        <button @click="reset">Reset</button>
    </div>
</template>

<script>
    import fieldsApi from '../api/fields'
    import {mapActions} from 'vuex'
    import ResponseSuccess from '../components/response/ResponseSuccess.vue'

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