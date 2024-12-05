<template>
    <div class="sidebar">
        <ul>
            <li><router-link to="/logView">로그확인</router-link></li>
            <li><router-link to="/errorLog">에러로그확인</router-link></li>
            <li><router-link to="/config">설정</router-link></li>
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
