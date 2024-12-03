<template>
    <div>
        <h3>날짜 및 시간 선택</h3>
        <input type="datetime-local" :value="value" @input="updateDateTime" />
    </div>
</template>

<script>
export default {
    name: "DateTimePickerComponent",
    props: {
        value: {
            type: String,
            required: false
        }
    },
    methods: {
        updateDateTime(e) {
            this.$emit("input", e.target.value);
        }
    },
};
</script>

<style>
h3 {
    margin-bottom: 10px;
}

input {
    margin-top: 5px;
    padding: 5px;
}

button {
    margin-top: 10px;
    padding: 5px 10px;
}
</style>
