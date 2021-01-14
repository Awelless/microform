<template>
    <tr>
        <td>{{field.label}}</td>
        <td>{{field.type}}</td>
        <td>{{field.required}}</td>
        <td>{{field.active}}</td>

        <td><button @click="edit">edit</button></td>
        <td><button @click="del">del</button></td>
    </tr>
</template>

<script>
    import {mapActions} from 'vuex'

    export default {
        name: 'FieldRow',
        props: ['field', 'editField'],
        methods: {
            ...mapActions(['removeFieldAction']),
            edit() {
                this.editField(this.field)
            },
            del() {
                this.removeFieldAction(this.field)
            }
        }
    }
</script>

<style scoped>

</style>