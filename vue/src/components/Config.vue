<template>
    <div>
        <h1>Config 조회</h1>
        <div class="block-mt10">
            <label>save</label>
            <input type="text" name="save" v-model="config.save" readonly>

            <div class="block-mt10">
                <label for="alert">alert</label>
                <select name="alert" id="alert" v-model="config.alert">
                    <option v-for="alertMethod in config.alert_METHODS" :key="alertMethod" :value="alertMethod">
                        {{ alertMethod }}
                    </option>
                </select>
            </div>

            <label>adminUrl</label>
            <input type="text" name="adminUrl" v-model="config.adminUrl" readonly>

            <label>basePackage</label>
            <input type="text" name="basePackage" v-model="config.basePackage" readonly>

            <label>emailId</label>
            <input type="text" name="emailId" v-model="config.emailId" :readonly="isMailReadonly">

            <label>emailPwd</label>
            <input type="text" name="emailPwd" v-model="config.emailPwd" :readonly="isMailReadonly">
        </div>
        <button @click="changeAlert">수정</button>
    </div>
</template>
<script>
export default {
    name: "AppConfig",
    data() {
        return {
            config: {
                save: "",
                alert: "",
                adminUrl: "",
                basePackage: "",
                emailId: "",
                emailPwd: "",
                alert_METHODS: []
            }
        }
    },
    created() {
        this.fetchConfig();
    },
    computed: {
        isMailReadonly() {
            return this.config.alert !== 'MAIL';
        }
    },
    methods: {
        async fetchConfig() {
            try {
                this.config = {
                    save: "",
                    alert: "",
                    adminUrl: "",
                    basePackage: "",
                    emailId: "",
                    emailPwd: "",
                    alert_METHODS: []
                }

                const res = await this.$axios.get('/log/config');
                this.config = res.data;
            } catch (e) {
                console.error(e);
                alert('조회중 서버에서 오류가 발생했습니다.');
            }
        },
        async changeAlert() {
            try {
                const res = await this.$axios.post('/log/config/alert', this.config);
                if (res.status === 200) {
                    await this.fetchConfig();
                    alert('수정되었습니다.');
                }
            } catch (e) {
                console.error(e);
                alert('조회중 서버에서 오류가 발생했습니다.');
            }
        }
    }
};
</script>

<style>
.block-mt10 label, .block-mt10 input {
    display: block;
    margin-bottom: 10px;
}
input[readonly] {
    background-color: #f0f0f0;
    border: 1px solid #ccc;
    cursor: not-allowed;
    color: #666;
}
</style>