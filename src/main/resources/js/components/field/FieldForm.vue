<template>
    <div class="row mb-5">
        <div class="col-sm-4"></div>
        <div class="col-sm-4">
            <h5>Add Field</h5>

            <div class="mb-3">
                <label for="label" class="form-label">
                    Label
                    <span class="text-danger">
                        *
                    </span>
                </label>
                <input
                    type="text"
                    :class="errors.has('label') ? 'form-control is-invalid' : 'form-control'"
                    aria-describedby="labelFeedback"
                    id="label"
                    v-model="label"
                >
                <div v-if="errors.has('label')" class="invalid-feedback" id="labelFeedback">
                    {{errors.get('label')}}
                </div>
            </div>

            <div class="mb-3">
                <label for="type" class="form-label">
                    Type
                </label>
                <select
                    class="form-select"
                    id="type"
                    v-model="type"
                >
                    <option value="SINGLE_LINE_TEXT">Single Line Text</option>
                    <option value="MULTILINE_TEXT">MultiLine Text</option>
                    <option value="RADIO_BUTTON">Radio Button</option>
                    <option value="CHECKBOX">Checkbox</option>
                    <option value="COMBOBOX">Combobox</option>
                    <option value="DATE">Date</option>
                </select>
            </div>

            <div class="mb-3" v-if="type === 'RADIO_BUTTON' || type === 'COMBOBOX'">
                <label for="options" class="form-label">
                    Options
                    <span class="text-danger">
                        *
                    </span>
                </label>
                <textarea
                    id="options"
                    :class="errors.has('options') ? 'form-control is-invalid' : 'form-control'"
                    aria-describedby="optionsFeedback"
                    v-model="fieldOptionsText"
                ></textarea>
                <div v-if="errors.has('options')" class="invalid-feedback" id="optionsFeedback">
                    {{errors.get('options')}}
                </div>
            </div>

            <div class="row mb-3">
                <div class="col">
                    <input
                        type="checkbox"
                        :class="errors.has('active') ? 'form-check-input is-invalid' : 'form-check-input'"
                        id="required"
                        aria-describedby="activeFeedback"
                        v-model="required"
                    >
                    <label for="required" class="form-check-label">Required</label>
                </div>

                <div class="col">
                    <input
                        type="checkbox"
                        :class="errors.has('active') ? 'form-check-input is-invalid' : 'form-check-input'"
                        id="isActive"
                        v-model="active"
                    >
                    <label for="isActive" class="form-check-label">Is Active</label>
                </div>

                <div class="little">
                    <div v-if="errors.has('active')" class="text-danger">
                        {{errors.get('active')}}
                    </div>
                </div>
            </div>

            <div class="text-center">
                <button class="btn btn-primary me-2" @click="save">Save</button>
                <button class="btn btn-light" @click="init(field)">Reset</button>
            </div>
        </div>
        <div class="col-sm-4"></div>
    </div>
</template>

<script>
    import {mapActions} from 'vuex'

    export default {
        name: 'FieldForm',
        props: ['field', 'success'],
        data () {
            return {
                id: null,
                label: '',
                type: 'SINGLE_LINE_TEXT',
                required: false,
                active: true,
                fieldOptionsText: '',
                errors: new Map
            }
        },
        watch: {
            field(newVal, oldVal) {
                this.init(newVal)
            }
        },
        methods: {
            ...mapActions(['addFieldAction', 'updateFieldAction']),
            isValid(options) {
                this.errors = new Map

                if (this.label.length === 0) {
                    this.errors.set('label', `Label shouldn't be empty`)
                }

                if (this.label.length > 255) {
                    this.errors.set('label', `Label length shouldn't be longer than 255`)
                }

                if ((this.type === 'RADIO_BUTTON' || this.type === 'COMBOBOX') &&
                    options.length === 0) {
                    this.errors.set('options', `You should set at least 1 option`)
                }

                if (this.active === false && this.required === true) {
                    this.errors.set('active', `Field can't be required and inactive at the same time`)
                }

                return this.errors.size === 0
            },
            save() {
                const options = this.fieldOptionsText.match(/(\w| |,|:|\+|=|<|>|\$|%|&|\*|\/|\\|-)+/g) || []
                options.forEach((option, i) => {
                    if (option.length === 0 || !option.trim()) {
                        options.splice(i, 1)
                    }
                })

                if (!this.isValid(options)) {
                    return
                }

                const fieldToSend = {
                    id: this.id,
                    label: this.label,
                    type: this.type,
                    required: this.required,
                    active: this.active,
                    options: options
                }

                try {
                    if (this.id) {
                        this.updateFieldAction(fieldToSend)
                    } else {
                        this.addFieldAction(fieldToSend)
                    }
                } catch (e) {
                    this.$router.push('/login')
                }

                this.clear()
                this.success()
            },
            clear() {
                this.id       = null
                this.label    = ''
                this.type     = 'SINGLE_LINE_TEXT'
                this.required = false
                this.active   = true

                this.fieldOptionsText = ''
            },
            init(newField) {
                if (newField) {
                    this.id       = newField.id
                    this.label    = newField.label
                    this.type     = newField.type
                    this.required = newField.required
                    this.active   = newField.active

                    if (newField.options) {
                        this.fieldOptionsText = newField.options.join('\n')
                    }
                } else {
                    this.clear()
                }
            }
        },
        created() {
            this.init(this.field)
        }
    }
</script>

<style scoped>
    .little {
        font-size: 14px;
    }
</style>