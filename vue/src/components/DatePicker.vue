<template>
    <div>
        <h3>날짜 및 시간 선택</h3>
        <input type="date" :value="dateValue" @input="updateDate" />
        <select :value="hourValue" @change="updateHour">
            <option v-for="hour in hours" :key="hour" :value="hour">
                {{ hour }}시
            </option>
        </select>
        <select :value="minuteValue" @change="updateMinute">
            <option v-for="minute in minutes" :key="minute" :value="minute">
                {{ minute }}분
            </option>
        </select>
    </div>
</template>

<script>
export default {
    name: "DateTimePickerComponent",
    props: {
        value: {
            type: String,
            required: false,
        },
    },
    computed: {
        dateValue() {
            return this.value ? this.value.split("T")[0] : "";
        },
        hourValue() {
            return this.value ? this.value.split("T")[1]?.split(":")[0] : "00";
        },
        minuteValue() {
            return this.value ? this.value.split("T")[1]?.split(":")[1] : "00";
        },
    },
    data() {
        return {
            hours: Array.from({length: 24}, (_, i) => String(i).padStart(2, "0")),
            minutes: Array.from({length: 60}, (_, i) => String(i).padStart(2, "0")),
        };
    },
    methods: {
        updateDate(e) {
            const newDate = e.target.value;
            const time = `${this.hourValue}:${this.minuteValue}`;
            this.$emit("input", `${newDate}T${time}`);
        },
        updateHour(e) {
            const newHour = e.target.value;
            const date = this.dateValue || new Date().toISOString().split("T")[0];
            const time = `${newHour}:${this.minuteValue}`;
            this.$emit("input", `${date}T${time}`);
        },
        updateMinute(e) {
            const newMinute = e.target.value;
            const date = this.dateValue || new Date().toISOString().split("T")[0];
            const time = `${this.hourValue}:${newMinute}`;
            this.$emit("input", `${date}T${time}`);
        },
    },
};
</script>

<style>
h3 {
    margin-bottom: 10px;
}

input,
select {
    margin-top: 5px;
    padding: 5px;
}
</style>
