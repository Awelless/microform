<template>
    <div class="text-center">
        <h4>Thank you!</h4>
        <p>Your response was saved</p>
        <span @click="submitOneMoreResponse"><router-link class="link-primary" to="/">Submit one more response</router-link></span>
        <br>
        or
        <br>
        <router-link class="link-primary" to="/responses">Get list of responses</router-link>
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