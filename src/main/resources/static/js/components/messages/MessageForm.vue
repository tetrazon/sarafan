<template>
    <div>
        <input type="text" placeholder="write something" v-model="text" />  <!--v-model to connect input and text-->
        <input type="button"  value="save" @click="save" />  <!--v-on: == @, save is the method called by pressing button-->
    </div>
</template>

<script>
    function getIndex(list, id) {
        for (var i = 0; i < list.length; i++) {
            if (list[i].id === id) {
                return i
            }
            return -1
        }
    }
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
            const message = { text: this.text}

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
            }
        }
    }
}
</script>

<style>

</style>