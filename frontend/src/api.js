import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:7000/api", // Javalin backend URL
  headers: {
    "Content-Type": "application/json",
  },
});

//api.interceptors.response.use(
//  (response) => response,
//  (error) => {
//    console.error("API Error:", error.response?.data || error.message);
//    return Promise.reject(error);
//  }
//);

export default api;
