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
    import messagesApi from 'api/messages'

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
            const message = {
                id: this.id,
                text: this.text
            }

            if (this.id) {
                messagesApi.update(message).then(result =>
                    result.json().then(data => {
                        const index = this.messages.findIndex(item => item.id === data.id)
                        this.messages.splice(index, 1, data)
                    })
                )
            } else {
                messagesApi.add(message).then(result =>//.then because of save() returns promise
                    result.json().then(data => {//data is message with id we need to set it in messages
                        const index = this.messages.findIndex(item => item.id === data.id)
                        if (index > -1){
                            this.messages.splice(index, 1, data)
                        } else {
                            this.messages.push(data);
                        }

                    })
                )
            }

            this.text = ''
            this.id = ''
        }
    }
}
</script>

<style>

</style>