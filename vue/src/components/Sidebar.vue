<template>
    <div class="sidebar">
        <ul>
            <li><router-link to="/logView">메뉴 1</router-link></li>
            <li><router-link to="/errorLog">메뉴 2</router-link></li>
            <li><router-link to="/config">메뉴 3</router-link></li>
        </ul>
    </div>
</template>

<script>
export default {
    name: "AppSideBar",
};
</script>

<style>
.sidebar {
    width: 200px;
    background: #f4f4f4;
    height: 100vh;
    padding: 10px;
}
ul {
    list-style: none;
    padding: 0;
}
li {
    margin: 10px 0;
}
</style>
