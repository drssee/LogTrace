import axios from "axios";

const instance = axios.create({
    timeout: 5000,
    headers: {
        "Content-Type": "application/json"
    }
});

export default instance;