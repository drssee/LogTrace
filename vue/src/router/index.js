import Vue from "vue";
import Router from "vue-router";
import LogView from "../components/LogView.vue";
import ErrorLog from "../components/ErrorLog.vue";
import AppConfig from "../components/Config.vue";

Vue.use(Router);

export default new Router({
    mode: "hash",
    routes: [
        { path: "/logView", component: LogView },
        { path: "/errorLog", component: ErrorLog },
        { path: "/config", component: AppConfig },
        { path: "/", redirect: "/logView" }, // 기본 경로
    ],
});
