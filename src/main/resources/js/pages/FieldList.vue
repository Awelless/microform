<template>
    <div class="mt-4 mb-3">
        <div class="row">
            <div class="col-sm-1"></div>
            <div class="col-sm-10">
                <div class="row">
                    <div class="col-sm-6">
                        <h4>Fields</h4>
                    </div>
                    <div class="col-sm-6 text-end">
                        <button class="btn btn-primary" @click="fieldFormChangeStatus">+ Add Field</button>
                    </div>
                </div>

                <field-form
                    v-if="isFieldFormShown"
                    :field="field"
                    :is-shown="isFieldFormShown"
                    :change-status="fieldFormChangeStatus"
                />

                <table class="table">
                    <thead>
                        <tr>
                            <th scope="col">Label</th>
                            <th scope="col">Type</th>
                            <th scope="col">Required</th>
                            <th scope="col">Is Active</th>
                            <th scope="col"></th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <field-row
                        v-for="field in sortedFields"
                        :key="field.id"
                        :field="field"
                        :edit-field="editField"
                    />
                </table>
            </div>
            <div class="col-sm-1"></div>
        </div>
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
            if (this.$store.state.principal === null) {
                this.$router.push('/')
            }
            this.initFieldsAction()
        }
    }
</script>

<style scoped>

</style>