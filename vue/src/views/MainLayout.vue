<template>
    <div class="layout">
        <AppSideBar />
        <div class="content">
            <router-view />
        </div>
    </div>
</template>

<script>
import AppSideBar from "../components/Sidebar.vue";

export default {
    name: "MainLayout",
    components: {
        AppSideBar,
    },
};
</script>

<style>
.layout {
    display: flex;
}
.content {
    flex: 1;
    padding: 20px;
}
</style>
