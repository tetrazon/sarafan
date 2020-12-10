function getIndex(list, id) {
    for (var i = 0; i < list.length; i++) {
        if (list[i].id === id) {
            return i;
        }
        return -1;
    }
}

var messageApi = Vue.resource('/message{/id}');

Vue.component('message-form', {
    props: ['messages', 'messageAttr'],//messageAttr for editing message

    //data to save changed messages
    //data as a function to provide certain data in certain instance
    //in new instance root element might be an Object
    // (every instance in one copy, all data saved in instance exist in one copy)
    // but every component might me create infinite times, and if we will be create
    // data as object all copies ig instance will be have one shared data
    //that leads to errors
    //in order to avoid that problems, the data represented as function that returns
    // an object, that provide unique storage for every component
    data: function () {
        return {
            text: '',
            id: ''
        }
    },
    watch: {
        messageAttr: function(newVal, oldVal) {
            this.text = newVal.text;
            this.id = newVal.id;
        }
    },
    template:
        '<div>' +
        '<input type="text" placeholder="write something" v-model="text" />' + //v-model to connect input and text
        '<input type="button"  value="save" @click="save" />' + //v-on: == @, save is the method called by pressing button
        '</div>',
    methods: {
        save: function () {
            var message = { text: this.text};

            if (this.id) {
                messageApi.update({id: this.id}, message).then(result =>
                    result.json().then(data => {
                        var index = getIndex(this.messages, data.id);
                        this.messages.splice(index, 1, data);
                        this.text = ''
                        this.id = ''
                    })
                )
            } else {
                messageApi.save({}, message).then(result =>//.then because of save() returns promise
                    result.json().then(data => {//data is message with id we need to set it in messages
                        this.messages.push(data);
                        this.text = ''
                    })
                )
            }
        }
    }
});

Vue.component('message-row', {
        props:['message', 'editMethod', 'messages'],
        template:'<div>' +
            '<i>({{message.id }})</i>{{message.text}}' +
            '<span style="position: absolute; right: 0">' +
            '<input type="button" value="Edit" @click="edit" />' +
            '<input type="button" value="X" @click="del" />' +
            '</span>' +
            '</div>',
        methods: {
            edit: function () {
                this.editMethod(this.message);
            },
            del:function () {
                messageApi.remove({id: this.message.id}).then(result =>{
                    if (result.ok) {
                        this.messages.splice(this.messages.indexOf(this.message), 1)
                    }
                })
            }
        }
    }
)
Vue.component('messages-list', {
    props: ['messages'],
    data: function() {
        return {
            message: null
        }
    },
    template:
        '<div style="position: relative; width: 300px;">' +
        '<message-form :messages="messages" :messageAttr="message" />' +
        '<message-row v-for="message in messages" :key="message.id" :message="message" ' +
        ':editMethod="editMethod" :messages="messages"/>' +
        '</div>',

    methods: {
        editMethod: function (message) {
            this.message = message;
        }
    }
});

var app = new Vue({

    el: '#app',
    template:
        '<div' +
        '<div v-if="!profile">Need to authorizate through<a href="/login">Google</a></div>' +
        '<messages-list v-else :messages="messages"/>' +
        '</div>',
    data: {
        messages: frontendData.messages,
        profile: frontendData.profile
    },
    created: function () {
       /* messageApi.get().then(result =>
            result.json().then(data =>
                data.forEach(message => this.messages.push(message))
            )
        )*/
    },
});