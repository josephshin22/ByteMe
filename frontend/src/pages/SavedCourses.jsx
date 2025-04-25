import {useEffect, useState} from "react";
import CourseCard from "../components/CourseCard";
import { Button } from "@/components/ui/button";
import * as React from "react";
import CourseModal from "@/components/CourseModal.jsx";
import {Trash} from "lucide-react";


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
                    {['2025_Spring', '2024_Fall', '2024_Early_Summer', '2024_Spring', '2023_Winter_Online', '2023_Fall']
                        .filter((semester) => courses.some((course) => course.semester === semester))
                        .map((semester) => (
                            <div key={semester} className="mb-6">
                                {/* Semester Label */}
                                <h2 className="font-medium text-slate-500 mb-2">{semester.replaceAll("_", " ")}</h2>
                                {/* Courses for the Semester */}
                                <div className="flex flex-col gap-3">
                                {courses
                                    .filter((course) => course.semester === semester)
                                    .map((course, index) => (
                                        <div key={index} className="flex gap-2 w-full">
                                            {/* Left: Course Card */}
                                            <div className="flex-1">
                                                <CourseCard course={course} disableSaveBtn={true} />
                                            </div>
                                            {/* Right: Remove Button */}
                                            <div className="w-28 flex-none">
                                                <div
                                                    onClick={() => handleRemove(course)}
                                                    className="cursor-pointer rounded-lg flex items-center justify-center px-4 font-medium flex-col space-y-0.5 h-full w-20 bg-red-100 hover:bg-red-200 text-red-800 transition-colors duration-150"
                                                >
                                                    <Trash className="h-5 w-5 mt-0.5" />
                                                    <p className="text-sm">Remove</p>
                                                </div>
                                            </div>
                                        </div>
                                    ))}
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
