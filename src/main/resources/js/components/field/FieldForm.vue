<template>
    <div>
        <label for="label">Label</label>
        <input type="text" id="label" v-model="label">

        <label for="type">Type</label>
        <select id="type" v-model="type">
            <option value="SINGLE_LINE_TEXT">Single Line Text</option>
            <option value="MULTILINE_TEXT">MultiLine Text</option>
            <option value="RADIO_BUTTON">Radio Button</option>
            <option value="CHECKBOX">Checkbox</option>
            <option value="COMBOBOX">Combobox</option>
            <option value="DATE">Date</option>
        </select>

        <template v-if="type === 'RADIO_BUTTON' || type === 'COMBOBOX'">
            <label for="options">Options</label>
            <textarea id="options" v-model="fieldOptionsText"></textarea>
        </template>

        <label for="required">Required</label>
        <input type="checkbox" id="required" v-model="required">

        <label for="isActive">Is Active</label>
        <input type="checkbox" id="isActive" v-model="active">

        <button @click="save">Save</button>
    </div>
</template>

<script>
    import {mapActions} from 'vuex'

    export default {
        name: 'FieldForm',
        props: ['field'],
        data () {
            return {
                id: null,
                label: '',
                type: 'SINGLE_LINE_TEXT',
                required: false,
                active: true,
                fieldOptionsText: ''
            }
        },
        watch: {
            field(newVal, oldVal) {
                this.updateFields(newVal)
            }
        },
        methods: {
            ...mapActions(['addFieldAction', 'updateFieldAction']),
            save() {
                const options = this.fieldOptionsText.match(/(\w| |,|:|\+|.)+/g) || []

                const fieldToSend = {
                    id: this.id,
                    label: this.label,
                    type: this.type,
                    required: this.required,
                    active: this.active,
                    options: options
                }

                if (this.id) {
                    this.updateFieldAction(fieldToSend)
                } else {
                    this.addFieldAction(fieldToSend)
                }

                this.clear()
            },
            updateFields(newField) {
                if (newField) {
                    this.id       = newField.id
                    this.label    = newField.label
                    this.type     = newField.type
                    this.required = newField.required
                    this.active   = newField.active

                    if (newField.options) {
                        this.fieldOptionsText = newField.options.join('\n')
                    }
                }
            },
            clear() {
                this.id       = null
                this.label    = ''
                this.type     = 'SINGLE_LINE_TEXT'
                this.required = false
                this.active   = true

                this.fieldOptionsText = ''
            }
        },
        created() {
            this.updateFields(this.field)
        }
    }
</script>

<style scoped>

</style>