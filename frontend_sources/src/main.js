import axios from 'axios';
window.axios = axios;
window.axios.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

import Vue from 'vue';
import VueRouter from "vue-router";
import App from './App.vue';
import {BootstrapVue, IconsPlugin} from 'bootstrap-vue';
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faTrash, faPlusCircle,faPlus } from '@fortawesome/free-solid-svg-icons';

library.add(faTrash, faPlusCircle,faPlus);

Vue.component('font-awesome-icon', FontAwesomeIcon);

Vue.config.productionTip = false

Vue.use(VueRouter);
Vue.use(BootstrapVue);
Vue.use(IconsPlugin);
import Index from "./views/Index.vue";
import Details from "./views/Details.vue";
import Login from "./views/Login.vue";
import Register from "./views/Register.vue";

const routes = [
  {
    path: "/",
    alias: "/index",
    name: "index",
    component:Index,
  },
  {
    path: "/login",
    name: "login",
    component:Login
  },
  {
    path: "/hike/:id",
    name: "hike-details",
    component:Details
  },
  {
    path: "/register",
    name: "register",
    component:Register
  }
];

const router = new VueRouter({mode: 'history',routes: routes});


axios.interceptors.request.use(function (config)
{
  let token = localStorage.getItem('hike-site.jwt');
  if (token)
  {
    config.headers['Authorization'] = `Bearer `+token;
    let CSRF_TOKEN = document.cookie.match(new RegExp(`XSRF-TOKEN=([^;]+)`))[1];
    config.headers["X-XSRF-TOKEN"]= CSRF_TOKEN;
  }
  return config;
}, function (error) {
  return Promise.reject(error);
});

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth))
  {
    if (localStorage.getItem('hike-site.jwt') == null)
    {
      next({
        path: '/login',
        params: { nextUrl: to.fullPath }
      });
    }
    else
    {
      next();
    }
  } else
    next();
});

/* eslint-disable no-new  */
/* eslint-disable no-unused-vars*/
new Vue({
  router,
  render: h => h(App)
}).$mount('#app');
