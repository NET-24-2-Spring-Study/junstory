import axios from "axios";
import Cookies from "universal-cookie";
import { requestRefreshToken, saveToken } from "./api";
const jwtAxios = axios.create();

const cookies = new Cookies(null, {path: "/", maxAge: 2592000});

const beforeRequest = (config) => {
    console.log("before request");

    //check if token is in cookies
    const accessToken = cookies.get("accessToken");

    if(!accessToken){
        throw Error("no token");
    }
    config.headers["Authorization"] = "Bearer " + accessToken

    return config;
}

const beforeResponse = (res) => {
    console.log("before response");

    return res;
}

const errorResponse = (error) => {
    console.log("error response");
    const status = error.response.status;
    const res = error.response.data;
    const errorMsg = res.error;
    console.log(status, res, errorMsg)
    const refreshFn = async() =>{
        console.log("Refresh token");
        const data = await requestRefreshToken();
        console.log(data);

        saveToken("accessToken", data.accessToken);
        saveToken("refreshToken", data.refreshToken);

        error.config.headers["Authorization"] = "Bearer " + data.accessToken;
        return await axios(error.config);
    }

        if(errorMsg.indexOf("expired") > 0){
            return refreshFn();
        } else{
            return Promise.reject(error);
        }
}

jwtAxios.interceptors.request.use(beforeRequest);
jwtAxios.interceptors.response.use(beforeResponse, errorResponse);
export default jwtAxios;
