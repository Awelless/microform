<template>
    <div>
        <button @click="fieldFormChangeStatus">
            Add Field
        </button>

        <field-form
            v-if="isFieldFormShown"
            :field="field"
            :is-shown="isFieldFormShown"
        />

        <table>
            <tr>
                <th>Label</th>
                <th>Type</th>
                <th>Required</th>
                <th>Is Active</th>
            </tr>
            <field-row
                v-for="field in sortedFields"
                :key="field.id"
                :field="field"
                :edit-field="editField"
            />
        </table>
    </div>
</template>

<script>
    import FieldRow from '../components/field/FieldRow.vue'
    import FieldForm from '../components/field/FieldForm.vue'
    import {mapActions, mapGetters} from 'vuex'

    export default {
        name: 'FieldList',
        components: {FieldForm, FieldRow},
        data() {
            return {
                field: null,
                isFieldFormShown: false
            }
        },
        computed: mapGetters(['sortedFields']),
        methods: {
            ...mapActions(['initFieldsAction']),
            fieldFormChangeStatus() {
                if (this.isFieldFormShown === true) {
                    this.field = null
                }

                this.isFieldFormShown = !this.isFieldFormShown
            },
            editField(field) {
                this.isFieldFormShown = true
                this.field = field
            }
        },
        created() {
            this.initFieldsAction()
        }
    }
</script>

<style scoped>

</style>