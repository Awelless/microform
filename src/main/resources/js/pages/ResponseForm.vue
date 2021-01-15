<template>
    <div class="row">
        <div class="col-sm-4"></div>
        <div class="col-sm-4">
            <response-success
                v-if="isSent"
                :submit-one-more-response="submitOneMoreResponse"
            />
            <div v-else>
                <div
                    class="mb-3"
                    v-for="(field, i) in fields"
                >
                    <template v-if="field.type === 'SINGLE_LINE_TEXT' || field.type === 'MULTILINE_TEXT' || field.type === 'COMBOBOX' || field.type === 'DATE'">
                        <label class="form-label" :for="fields.id + '#' + i">
                            {{field.label}}
                            <span v-if="field.required" class="text-danger">
                                *
                            </span>
                        </label>
                        <input
                            v-if="field.type === 'SINGLE_LINE_TEXT'"
                            type="text"
                            class="form-control"
                            :id="fields.id + '#' + i"
                            v-model="responseFields[i]"
                        >

                        <textarea
                            v-else-if="field.type === 'MULTILINE_TEXT'"
                            class="form-control"
                            :id="fields.id + '#' + i"
                            v-model="responseFields[i]"
                        />

                        <select
                            v-else-if="field.type === 'COMBOBOX'"
                            class="form-select"
                            :id="fields.id + '#' + i"
                            v-model="responseFields[i]"
                        >
                            <option
                                v-for="option in field.options"
                                :key="field.id + '#' + option"
                            >
                                {{option}}
                            </option>
                        </select>

                        <input
                            v-else
                            type="date"
                            class="form-control"
                            :id="fields.id + '#' + i"
                            v-model="responseFields[i]"
                        >
                    </template>

                    <div v-else-if="field.type === 'RADIO_BUTTON' || field.type === 'CHECKBOX'">
                        <template v-if="field.type === 'RADIO_BUTTON'" :id="fields.id + '#' + i">
                            <span class="form-label">
                                {{field.label}}
                                <span v-if="field.required" class="text-danger">
                                    *
                                </span>
                            </span>

                            <div
                                class="form-check"
                                v-for="option in field.options"
                            >
                                <input
                                    type="radio"
                                    class="form-check-input"
                                    :id="field.id + '#' + option"
                                    :value="option"
                                    v-model="responseFields[i]"
                                >
                                <label
                                    class="form-check-label"
                                    :for="field.id + '#' + option"
                                >
                                    {{ option }}
                                </label>
                            </div>
                        </template>

                        <div v-else class="form-check">
                            <label class="form-check-label" :for="fields.id + '#' + i">
                                {{field.label}}
                                <span v-if="field.required" class="text-danger">
                                    *
                                </span>
                            </label>
                            <input
                                type="checkbox"
                                class="form-check-input"
                                :id="fields.id + '#' + i"
                                v-model="responseFields[i]"
                            >
                        </div>
                    </div>
                </div>

                <div class="text-center">
                    <button class="btn btn-primary me-2" @click="submit">Submit</button>
                    <button class="btn btn-light text-primary" @click="reset">Reset</button>
                </div>
            </div>
        </div>
        <div class="col-sm-4"></div>
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