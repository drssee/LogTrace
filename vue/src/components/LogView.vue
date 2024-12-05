<template>
    <div>
        <h1>LogView</h1>
        <!-- 하위 컴포넌트에 v-model 바인딩(양방향) -->
        <DatePicker v-model="selectedDateTime" mode="datetime"/>
        <button @click="fetchLogs">조회</button>
        <LogContainer :logs="logs" />
    </div>
</template>

<script>
import DatePicker from "@/components/include/DatePicker.vue";
import LogContainer from "@/components/include/LogContainer.vue";

export default {
    name: "LogView",
    components: {
        DatePicker,
        LogContainer
    },
    data() {
        return {
            selectedDateTime: "",
            logs: []
        };
    },
    created() {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');
        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');

        this.selectedDateTime = `${year}-${month}-${day}T${hours}:${minutes}`;
        this.fetchLogs();
    },
    methods: {
        async fetchLogs() {
            this.logs = [];
            const res = await this.$axios.get('/log/logView?dateTime='+this.selectedDateTime);
            this.logs = res.data;
        },
    }
};
</script>
