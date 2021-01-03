<template>
    <v-layout row>
        <v-text-field
                label="new message"
                placeholder="write something"
                v-model="text" />  <!--v-model to connect input and text-->
        <v-btn @click="save">  <!--v-on: == @, save is the method called by pressing button-->
            Save
        </v-btn>
    </v-layout>
</template>

<script>
    import {mapActions} from 'vuex'

    export default {
    props: ['messageAttr'],//messageAttr for editing message
    data() {
        return {
            text: '',
            id: ''
        }
    },
    watch: {
        messageAttr: function(newVal, oldVal) {
            this.text = newVal.text
            this.id = newVal.id
        }
    },
    methods: {
        ...mapActions(['addMessageAction', 'updateMessageAction']),
        save() {
            const message = {
                id: this.id,
                text: this.text
            }

            if (this.id) {
                this.updateMessageAction(message)
            } else {
                this.addMessageAction(message)
            }

            this.text = ''
            this.id = ''
        }
    }
}
</script>

<style>

</style>