import {useEffect, useState} from "react";
import CourseCard from "../components/CourseCard";
import {Button} from "@/components/ui/button";
import * as React from "react";


function SavedCourses() {

    const [courses, setCourses] = useState([]);

    useEffect(() => {
        const saved = JSON.parse(localStorage.getItem("savedCourses")) || [];
        setCourses(saved);
    }, []);


    return (
        <div>
            <h1 className="font-semibold text-xl mb-4">Saved Courses</h1>
            {courses.length ? (
                <div className="flex flex-col gap-3">
                    {courses.map((course, index) => (
                        <CourseCard key={index} course={course} />
                    ))}
                </div>
            ) : (
                <div className="text-slate-500">No courses saved yet.</div>
            )}
        </div>
    );
}

export default SavedCourses;
