<template>
    <tr>
        <td
            v-for="field in fields"
            :key="response.id + '#' + field.id"
        >
            {{response.has(field.id.toString()) ? response.get(field.id.toString()) : 'N/A'}}
        </td>
    </tr>
</template>

<script>
    export default {
        name: 'ResponseRow',
        props: ['responseAttr', 'fields'],
        data() {
            return {
                response: new Map()
            }
        },
        created() {
            for (const [key, value] of Object.entries(this.responseAttr.body)) {
                this.response.set(key, value)
            }
        }
    }
</script>

<style scoped>
  td {
    white-space:pre-line;
  }
</style>