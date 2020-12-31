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
    import {sendMessage} from "../../pages/util/ws";

    export default {
    props: ['messages', 'messageAttr'],//messageAttr for editing message
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
        save() {
            sendMessage({id: this.id, text: this.text})
            this.text = ''
            this.id = ''
         /*   const message = { text: this.text}

            if (this.id) {
                this.$resource('/message{/id}').update({id: this.id}, message).then(result =>
                    result.json().then(data => {
                        const index = getIndex(this.messages, data.id)
                        this.messages.splice(index, 1, data)
                        this.text = ''
                        this.id = ''
                    })
                )
            } else {
                this.$resource('/message{/id}').save({}, message).then(result =>//.then because of save() returns promise
                    result.json().then(data => {//data is message with id we need to set it in messages
                        this.messages.push(data);
                        this.text = ''
                    })
                )
            }*/
        }
    }
}
</script>

<style>

</style>