import React, { useState } from "react";
import { Bookmark, PlusCircle } from "lucide-react";
import CourseModal from "./CourseModal.jsx";
import {formatCourseTimes} from "@/utils/formatCourseTimes.jsx";
export default function CourseCard({ course }) {

    const [isModalOpen, setIsModalOpen] = useState(false);

    const handleSave = (course) => {
        const saved = JSON.parse(localStorage.getItem("savedCourses")) || [];

        const isAlreadySaved = saved.some(
            (c) => c.subjCode === course.subjCode && c.number === course.number && c.section === course.section
        );

        if (!isAlreadySaved) {
            saved.push(course);
            localStorage.setItem("savedCourses", JSON.stringify(saved));
            console.log("Course saved:", course);
        } else {
            console.log("Course already saved");
        }
    };

    return (
        <div className="group">
            <div
                className="flex items-stretch shadow w-full bg-background rounded-lg cursor-pointer text-sm outline-0 outline-border transition-all duration-100 group-hover:outline-1 group-hover:shadow-lg group-hover:-translate-y-0.5"
            >
                {/* Course Code and Section */}
                <div
                    className="w-20 p-4 text-center font-semibold border-r border-slate-300 flex items-center"
                    onClick={() => setIsModalOpen(true)}
                >
                    {course.subjCode} {course.number} {course.section}</div>

                {/* Course Details */}
                <div className="flex-1 p-4 space-y-1" onClick={() => setIsModalOpen(true)}>
                    <div className="flex justify-between">
                        <div className="font-medium">{course.name}</div>
                        <div className="font-medium">{formatCourseTimes(course.times)}</div>
                    </div>
                    <div className="flex justify-between ">
                        <div className="text-slate-500">{course.faculty.join(", ")}</div>
                        <div className="flex space-x-2 text-slate-500">
                            <div>{course.location}</div>
                            <p>&bull;</p>
                            <div>{course.openSeats}/{course.totalSeats} seats open</div>
                            <p>&bull;</p>
                            <div>{course.credits} credit{course.credits !== 1 && "s"}</div>
                        </div>
                    </div>
                </div>

                {/* Buttons */}
                <div className="flex items-center">
                    <div className="cursor-pointer flex-col space-y-0.5 bg-blue-100 hover:bg-blue-200 text-blue-800 flex items-center justify-center px-4 h-full w-20 font-medium"
                         onClick={() => handleSave(course)}
                    >
                        <Bookmark  className="h-5 w-5 mt-0.5" />
                        <p>Save</p>
                    </div>
                    <div className="cursor-pointer flex-col space-y-0.5 bg-green-100 hover:bg-green-200 text-green-800 flex items-center justify-center px-4 h-full w-20 rounded-r-lg font-medium">
                        <PlusCircle className="h-5 w-5 mt-0.5" />
                        <p>Add</p>
                    </div>
                </div>
            </div>

            <CourseModal isOpen={isModalOpen} onClose={()=> setIsModalOpen(false)} course={course}/>
        </div>
    );
}
