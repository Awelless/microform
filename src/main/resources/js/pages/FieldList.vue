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
                    :saved="saved"
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
                                v-for="field in fieldPage"
                                :key="field.id"
                                :field="field"
                                :edit-field="editField"
                                :create-success="createSuccess"
                            />
                        </tbody>
                    </table>
                </div>

                <nav>
                    <ul class="pagination justify-content-center">
                        <template v-for="page in pages">
                            <li v-if="page === null" class="page-item disabled">
                                <span class="page-link">...</span>
                            </li>
                            <li v-else-if="page === fieldPageParams.currentPage" class="page-item active">
                                <span class="page-link">{{page}}</span>
                            </li>
                            <li v-else class="page-item">
                                <a class="page-link" @click="loadPage(page)" href="#">{{page}}</a>
                            </li>
                        </template>
                    </ul>
                </nav>
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
                successMessage: null,
                pages: []
            }
        },
        computed: mapGetters(['fieldPage', 'fieldPageParams', 'principal']),
        watch: {
            principal(newVal, oldVal) {
                if (newVal === null) {
                    this.$router.push('/')
                } else {
                    this.loadFieldsPageAction(1)
                }
            },
            fieldPageParams(newVal, oldVal) {
                this.initPages(newVal)
            }
        },
        methods: {
            ...mapActions(['loadFieldsPageAction']),
            initPages(fieldPageParams) {
                this.pages = []
                const current = this.fieldPageParams.currentPage
                const total = this.fieldPageParams.totalPages

                if (total <= 9) {
                    for (let i = 1; i <= total; ++i) {
                        this.pages.push(i)
                    }
                } else if (current <= 5) {
                    for (let i = 1; i <= 7; ++i) {
                        this.pages.push(i)
                    }
                    this.pages.push(null, total)
                } else if (current >= total - 4) {
                    this.pages.push(1, null)
                    for (let i = total - 6; i <= total; ++i) {
                        this.pages.push(i)
                    }
                } else {
                    this.pages.push(1, null)
                    for (let i = current - 2; i <= current + 2; ++i) {
                        this.pages.push(i)
                    }
                    this.pages.push(null, total)
                }
            },
            createSuccess(message) {
                this.successMessage = message
                this.isFieldFormShown = false
            },
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
            loadPage(page) {
                this.successMessage = null
                this.loadFieldsPageAction(page)
            },
            saved() {
                this.createSuccess('Field is saved')
                this.isFieldFormShown = false
            }
        },
        created() {
            this.loadFieldsPageAction(1)
        }
    }
</script>

<style scoped>
    .table-responsive {
        overflow-x: auto;
    }
</style>