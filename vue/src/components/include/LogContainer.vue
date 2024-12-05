<template>
    <div class="log-container">
        <ul>
            <li v-for="(log, index) in logs" :key="index">
                {{ log }}
            </li>
        </ul>
    </div>
</template>

<script>
export default {
    name: "LogContainer",
    props: {
        logs: {
            type: Array,
            required: true,
        },
    },
};
</script>

<style>
.log-container {
    margin-top: 20px;
    height: 400px;
    overflow-y: auto;
    border: 1px solid #ccc;
    padding: 10px;
    background-color: #f9f9f9;
}

.log-container ul {
    margin: 0;
    padding: 0;
    list-style: none;
}

.log-container li {
    margin-bottom: 5px;
    font-size: 14px;
}
</style>