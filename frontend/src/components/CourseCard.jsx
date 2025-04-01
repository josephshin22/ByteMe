import React, { useState } from "react";
import { Bookmark, PlusCircle } from "lucide-react";
import CourseModal from "./CourseModal.jsx";

export default function CourseCard({ course }) {

    const [isModalOpen, setIsModalOpen] = useState(false);

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
                    {course.abbreviation} {course.courseNum} {course.section}</div>

                {/* Course Details */}
                <div className="flex-1 p-4 space-y-1" onClick={() => setIsModalOpen(true)}>
                    <div className="flex justify-between">
                        <div className="font-medium">{course.name}</div>
                        <div className="font-medium">{course.times}</div>
                    </div>
                    <div className="flex justify-between ">
                        <div className="text-slate-500">{course.faculty}</div>
                        <div className="flex space-x-3 ">
                            <div className="text-slate-500">• {course.location}</div>
                            <div className="text-slate-500">• {course.openSeats}/{course.totalSeats} open seats</div>
                            <div className="text-slate-500">• {course.credits} credits</div>
                        </div>
                    </div>
                </div>

                {/* Buttons */}
                <div className="flex items-center">
                    <div className="cursor-pointer flex-col space-y-0.5 bg-blue-100 hover:bg-blue-200 text-blue-800 flex items-center justify-center px-4 h-full w-20 font-medium">
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
