import {useEffect, useState} from "react";
import CourseCard from "../components/CourseCard";
import { Button } from "@/components/ui/button";
import * as React from "react";


function SavedCourses() {

    const [courses, setCourses] = useState([]);

    useEffect(() => {
        const saved = JSON.parse(localStorage.getItem("savedCourses")) || [];
        setCourses(saved);
    }, []);

    const handleRemove = (courseToRemove) => {
        const updatedCourses = courses.filter(
            (c) =>
                !(
                    c.subjCode === courseToRemove.subjCode &&
                    c.number === courseToRemove.number &&
                    c.section === courseToRemove.section
                )
        );

        setCourses(updatedCourses);
        localStorage.setItem("savedCourses", JSON.stringify(updatedCourses));
    };

    return (
        <div>
            <h1 className="font-semibold text-xl mb-4">Saved Courses</h1>
            {courses.length ? (
                <div className="flex flex-col gap-4">
                    {courses.map((course, index) => (
                        <div
                            key={index}
                            className="flex gap-4 w-full"
                        >
                            {/* Left: Course Card */}
                            <div className="flex-1">
                                <CourseCard course={course} />
                            </div>

                            {/* Right: Remove Button */}
                            <div className="w-28 flex-none">
                                <Button
                                    onClick={() => handleRemove(course)}
                                    className="h-full w-full bg-red-100 hover:bg-red-200 text-red-800 transition-colors duration-150"
                                    size="sm"
                                >
                                    Remove
                                </Button>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <div className="text-slate-500">No courses saved yet.</div>
            )}
        </div>
    );
}

export default SavedCourses;
