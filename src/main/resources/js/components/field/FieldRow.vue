<template>
    <tr>
        <td>{{field.label}}</td>
        <td>{{type}}</td>
        <td>{{field.required}}</td>
        <td>{{field.active}}</td>

        <td><button class="btn btn-light" @click="edit"><i class="bi bi-pencil-square"></i></button></td>
        <td><button class="btn btn-outline-danger" @click="del"><i class="bi bi-trash"></i></button></td>
    </tr>
</template>

<script>
    import {mapActions, mapMutations} from 'vuex'
    import {successMessage} from '../../util/loginMessages'

    export default {
        name: 'FieldRow',
        props: ['field', 'editField', 'shouldClearForm'],
        data() {
            return {
                type: ''
            }
        },
        methods: {
            ...mapActions(['removeFieldAction']),
            ...mapMutations(['updateMessageMutation', 'removeMessageMutation']),
            edit() {
                this.removeMessageMutation()
                this.editField(this.field)
            },
            del() {
                this.updateMessageMutation(successMessage('Field is deleted'))
                this.shouldClearForm()
                this.removeFieldAction(this.field)
            }
        },
        created() {
            if (this.field.type === 'SINGLE_LINE_TEXT') {
                this.type = 'Single Line Text'
            } else if (this.field.type === 'MULTILINE_TEXT') {
                this.type = 'Multiline Text'
            } else if (this.field.type === 'RADIO_BUTTON') {
                this.type = 'Radio Button'
            } else if (this.field.type === 'CHECKBOX') {
                this.type = 'Checkbox'
            } else if (this.field.type === 'COMBOBOX') {
                this.type = 'Combobox'
            } else {
                this.type = 'Date'
            }
        }
    }
</script>

<style scoped>

</style>