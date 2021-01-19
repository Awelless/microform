<template>
    <div class="row mt-4 mb-3">
        <div class="col-sm-4"></div>
        <div class="col-sm-4">
            <response-success
                v-if="isSent"
                :submit-one-more-response="submitOneMoreResponse"
            />
            <template v-else>
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
                            :class="errors.has(i) ? 'form-control is-invalid' : 'form-control'"
                            :aria-describedby="i + 'Feedback'"
                            :id="fields.id + '#' + i"
                            v-model="responseFields[i]"
                        >

                        <textarea
                            v-else-if="field.type === 'MULTILINE_TEXT'"
                            :class="errors.has(i) ? 'form-control is-invalid' : 'form-control'"
                            :aria-describedby="i + 'Feedback'"
                            :id="fields.id + '#' + i"
                            v-model="responseFields[i]"
                        />

                        <select
                            v-else-if="field.type === 'COMBOBOX'"
                            :class="errors.has(i) ? 'form-select is-invalid' : 'form-select'"
                            :aria-describedby="i + 'Feedback'"
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
                            :class="errors.has(i) ? 'form-control is-invalid' : 'form-control'"
                            :aria-describedby="i + 'Feedback'"
                            :id="fields.id + '#' + i"
                            v-model="responseFields[i]"
                        >

                        <div v-if="errors.has(i)" class="invalid-feedback" :id="i + 'Feedback'">
                            {{errors.get(i)}}
                        </div>
                    </template>

                    <div
                        v-else-if="field.type === 'RADIO_BUTTON' || field.type === 'CHECKBOX'"
                    >
                        <div v-if="field.type === 'RADIO_BUTTON'" :class="(errors.has(i) && responseFields[i] === '') ? 'was-validated' : ''">
                            <span class="form-label">
                                {{field.label}}
                                <span v-if="field.required" class="text-danger">
                                    *
                                </span>
                            </span>

                            <div
                                class="form-check"
                                v-for="(option, j) in field.options"
                            >
                                <input
                                    type="radio"
                                    class="form-check-input"
                                    :id="field.id + '#' + option"
                                    :value="option"
                                    v-model="responseFields[i]"
                                    name="radio-stacked"
                                    required
                                >
                                <label
                                    class="form-check-label"
                                    :for="field.id + '#' + option"
                                >
                                    {{ option }}
                                </label>

                                <div v-if="errors.has(i) && j + 1 === field.options.length" class="invalid-feedback">
                                    {{errors.get(i)}}
                                </div>
                            </div>
                        </div>

                        <div v-else class="form-check">
                            <label class="form-check-label" :for="fields.id + '#' + i">
                                {{field.label}}
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
                    <button class="btn btn-light" @click="reset">Reset</button>
                </div>
            </template>
        </div>
        <div class="col-sm-4"></div>
    </div>
</template>

<script>
    import fieldsApi from '../api/fields'
    import ResponseSuccess from '../components/response/ResponseSuccess.vue'
    import {mapActions, mapMutations} from 'vuex'
    import router from '../router'

    export default {
        name: 'ResponseForm',
        components: {ResponseSuccess},
        data() {
            return {
                fields: [],
                responseFields: [],
                isSent: false,
                errors: new Map
            }
        },
        methods: {
            ...mapActions(['addResponseAction']),
            ...mapMutations(['removePrincipalMutation']),
            submit() {
                this.errors = new Map

                let responseBody = new Map

                this.responseFields.forEach((value, i) => {
                    if (typeof value === 'boolean') {
                        responseBody.set(this.fields[i].id, value)
                    } else {
                        const result = value.trim()

                        if (this.fields[i].required && result.length === 0) {
                            this.errors.set(i, 'This field is necessary')
                        }

                        if (result.length > 0) {
                            const fieldId = this.fields[i].id
                            responseBody.set(fieldId, result)
                        }
                    }
                })

                if (this.errors.size > 0) {
                    return
                }

                const response = {
                    body: Object.fromEntries(responseBody)
                }

                this.addResponseAction(response)

                this.isSent = true

                this.reset()
            },
            reset() {
                this.errors = new Map

                for (let i = 0; i < this.responseFields.length; ++i) {
                    this.responseFields.splice(i, 1, '')
                    if (this.fields[i].type === 'CHECKBOX') {
                        this.responseFields[i] = false
                    }
                }
            },
            submitOneMoreResponse() {
                this.isSent = false
            }
        },
        async created() {
            fieldsApi.getActive().then(response => {
                response.json().then(data => {
                    this.fields = data.sort((a, b) => a.id - b.id)

                    this.fields.forEach((item, i) => {
                        this.responseFields.push('')
                        if (this.fields[i].type === 'CHECKBOX') {
                            this.responseFields[i] = false
                        }
                    })
                })
            }, response => {
                this.removePrincipalMutation()
                this.$router.push('/login')
            })
        }
    }
</script>

<style scoped>

</style>