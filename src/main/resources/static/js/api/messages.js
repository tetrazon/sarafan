import Vue from 'vue'

// create resource for getting data from http methods
const messages = Vue.resource('/message{/id}')

export default {
    add: message => messages.save({}, message),
    update: message => messages.update({id:message.id}, message),
    remove: id => messages.remove({id})
}