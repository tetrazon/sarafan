import Vue from 'vue'
import VueResource from 'vue-resource'
import App from 'pages/App.vue'
import { connect } from "./pages/util/ws";
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'

if (frontendData.profile) {
    connect();
}
Vue.use(Vuetify)
Vue.use(VueResource)
new Vue({
    vuetify : new Vuetify(),
    el: '#app',
    render: a => a(App)
})

