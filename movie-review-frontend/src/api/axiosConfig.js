import axios from 'axios';

export default axios.create({
    //baseURL:'https://9c96-103-106-239-104.ap.ngrok.io',
    // baseURL:'localhost:8080',
    baseURL:'https://ed9c-2401-4900-1f29-b349-2d87-8ca6-8ad-9eeb.ngrok-free.app',
    headers: {"ngrok-skip-browser-warning": "true"}
});