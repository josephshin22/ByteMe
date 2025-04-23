// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getFirestore } from "firebase/firestore";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
    apiKey: "AIzaSyDVtyXD5ma8t99I7ZaBSrkDoQneq2iRXAM",
    authDomain: "byteme-f65a7.firebaseapp.com",
    projectId: "byteme-f65a7",
    storageBucket: "byteme-f65a7.firebasestorage.app",
    messagingSenderId: "190791856531",
    appId: "1:190791856531:web:5b4c0c0cc0e099e37cf866",
    measurementId: "G-CSG0RDKQ4Z"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);

// Get Firestore instance
const db = getFirestore(app);

export { db };