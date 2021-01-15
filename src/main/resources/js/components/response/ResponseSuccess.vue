<template>
    <div>
        <h4>Thank you!</h4>
        Your response was saved
        <br>
        <button @click="submitOneMoreResponse">Submit one more response</button>
        <br>
        <router-link to="/responses">Get list of responses</router-link>
    </div>
</template>

<script>
    export default {
        name: 'ResponseSuccess',
        props: ['submitOneMoreResponse']
    }
</script>

<style scoped>

</style>