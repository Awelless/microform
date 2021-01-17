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

                <div v-if="successMessage" class="alert alert-success mt-2">
                    {{successMessage}}
                </div>

                <field-form
                    v-if="isFieldFormShown"
                    :field="field"
                    :success="success"
                />

                <div class="table-responsive">
                    <table class="table table-hover">
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

                        <tbody>
                            <field-row
                                v-for="field in sortedFields"
                                :key="field.id"
                                :field="field"
                                :edit-field="editField"
                            />
                        </tbody>
                    </table>
                </div>
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
                isFieldFormShown: false,
                successMessage: null
            }
        },
        computed: mapGetters(['sortedFields']),
        methods: {
            ...mapActions(['initFieldsAction']),
            fieldFormChangeStatus() {
                if (this.isFieldFormShown === true) {
                    this.field = null
                } else {
                    this.successMessage = null
                }

                this.isFieldFormShown = !this.isFieldFormShown
            },
            editField(field) {
                this.successMessage = null

                this.isFieldFormShown = true
                this.field = field
            },
            success() {
                this.successMessage = 'Field is saved'
                this.fieldFormChangeStatus()
            }
        },
        created() {
            if (this.$store.state.principal === null) {
                this.$router.push('/login')
            }

            this.initFieldsAction()
        }
    }
</script>

<style scoped>
    .table-responsive {
        overflow-x: auto;
    }
</style>