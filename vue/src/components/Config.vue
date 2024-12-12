<template>
    <div>
        <h1>Config</h1>
            <div class="block-mt10">
                <label for="save">save</label>
                <select name="save" id="save" v-model="config.save">
                    <option v-for="saveMethod in config.save_METHODS" :key="saveMethod" :value="saveMethod">
                        {{ saveMethod }}
                    </option>
                </select>
            </div>
            <div class="block-mt10">
                <label for="alert">alert</label>
                <select name="alert" id="alert" v-model="config.alert">
                    <option v-for="alertMethod in config.alert_METHODS" :key="alertMethod" :value="alertMethod">
                        {{ alertMethod }}
                    </option>
                </select>
            </div>
            <div class="block-mt10">
                <label>adminUrl</label>
                <input type="text" name="adminUrl" v-model="config.adminUrl" readonly>

                <label>basePackage</label>
                <input type="text" name="basePackage" v-model="config.basePackage">

                <label>emailId</label>
                <input type="text" name="emailId" v-model="config.emailId">

                <label>emailPwd</label>
                <input type="text" name="emailPwd" v-model="config.emailPwd">
            </div>
            <button @click="updateConfig">수정</button>
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
                alert_METHODS: [],
                save_METHODS: []
            }
        }
    },
    created() {
        this.fetchConfig();
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
                    alert_METHODS: [],
                    save_METHODS: []
                }

                const res = await this.$axios.get('/log/config');
                this.config = res.data;
            } catch (e) {
                console.error(e);
                alert('조회중 서버에서 오류가 발생했습니다.');
            }
        },
        async updateConfig() {
            try {
                const res = await this.$axios.post('/log/config', this.config);
                if (res.status === 200) {
                    await this.fetchConfig();
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