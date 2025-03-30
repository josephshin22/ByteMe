import { useEffect, useState } from "react";
import api from "../api.js";
import CourseCard from "../components/CourseCard.jsx";

function FindCourses() {

    const [message, setMessage] = useState("Loading...");

    useEffect(() => {
        api
            .get("/hello")
            .then((res) => setMessage(res.data.message))
            .catch((err) => console.error("Error fetching data:", err));
    }, []);

    return (
        <div>
            <h1 className="font-semibold text-xl mb-4 ">Find Courses</h1>
            <p>Message from backend: {message}</p>

            <CourseCard/>
        </div>
    );
}

export default FindCourses;
