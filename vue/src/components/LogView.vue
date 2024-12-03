<template>
    <div>
        <h1>LogView</h1>
        <p>날짜와 시간으로 로그를 조회해 보여줘야함(default: 현재 시각)</p>
        <!-- 하위 컴포넌트에 v-model 바인딩(양방향) -->
        <DatePicker v-model="selectedDateTime" />
        <button @click="fetchLogs">조회</button>

        <!-- 로그 출력 영역 -->
        <div class="log-container">
            <ul>
                <li v-for="(log, index) in logs" :key="index">
                    {{ log }}
                </li>
            </ul>
        </div>
    </div>
</template>

<script>
import DatePicker from "../components/DatePicker.vue";

export default {
    name: "LogView",
    components: {
        DatePicker,
    },
    data() {
        return {
            selectedDateTime: "",
            logs: [],
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
    },
    methods: {
        fetchLogs() {
            console.log(`selectedDateTime: ${this.selectedDateTime}`);
            this.$axios.get('/log/logView?dateTime='+this.selectedDateTime)

            this.logs = [
                `로그 1 (${this.selectedDateTime})`,
                `로그 2 (${this.selectedDateTime})`,
                `로그 3 (${this.selectedDateTime})`,
                `로그 4 (${this.selectedDateTime})`,
                `로그 5 (${this.selectedDateTime})`,
                `로그 6 (${this.selectedDateTime})`,
            ];
        },
    },
    watch: {
        selectedDateTime(newValue, oldValue) {
            console.log(`날짜 및 시간 변경: 이전 값 = ${oldValue}, 새로운 값 = ${newValue}`);
        },
    },
};
</script>

<style>
.log-container {
    margin-top: 20px;
    height: 200px;
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
