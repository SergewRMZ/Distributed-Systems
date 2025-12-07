import axios from 'axios';

const backendURL = 'http://localhost:8080/api';


const api = axios.create({
  baseURL: backendURL,
  headers: {
    'Content-Type': 'application/json'
  }
});

export default api;