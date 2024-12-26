<template>
    <div>
        <h1>ErrorLog</h1>
        <!-- 하위 컴포넌트에 v-model 바인딩(양방향) -->
        <DatePicker v-model="selectedDate" mode="date" />
        <button @click="fetchLogs">조회</button>
        <LogContainer :logs="logs" />
    </div>
</template>

<script>
import DatePicker from "@/components/include/DatePicker.vue";
import LogContainer from "@/components/include/LogContainer.vue";

export default {
    name: "ErrorLog",
    components: {
        DatePicker,
        LogContainer
    },
    data() {
        return {
            selectedDate: "",
            logs: []
        };
    },
    created() {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');

        this.selectedDate = `${year}-${month}-${day}`;
        this.fetchLogs();
    },
    methods: {
        async fetchLogs() {
            try {
                this.logs = [];
                const res = await this.$axios.get('/log/errorView?date=' + this.selectedDate);
                this.logs = res.data;
            } catch (e) {
                console.error(e);
                alert('조회중 서버에서 오류가 발생했습니다.');
            }
        },
    }
};
</script>
