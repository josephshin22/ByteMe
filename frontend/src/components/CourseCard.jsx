import React, {useEffect, useState} from "react";
import {
    Bookmark,
    BookmarkCheck,
    BookMarked,
    Check,
    Circle,
    CircleCheck,
    Loader,
    PlusCircle,
    School
} from "lucide-react";
import CourseModal from "./CourseModal.jsx";
import {formatCourseTimes} from "@/utils/formatCourseTimes.jsx";
import {saveCourse} from "@/utils/saveCourse.jsx";
import {handleAddCourse} from "@/utils/courseActions.jsx";

export default function CourseCard({ course, selectedSchedule, isSaved, disableSaveBtn }) {
    const [isInSchedule, setIsInSchedule] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isSavedOverride, setIsSavedOverride] = useState(isSaved);
    const [isAdding, setIsAdding] = useState(false);

    useEffect(() => {
        setTimeout(() => setIsAdding(false), 6000);
    }, [isAdding]);

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
                <div className="flex items-center p-1 gap-1">

                    {/*SAVE BUTTON*/}
                    {!disableSaveBtn && (
                        <div className="cursor-pointer flex-col space-y-0.5 bg-blue-100 hover:bg-blue-200 text-blue-800 flex items-center justify-center px-4 h-full w-20 font-medium rounded-xs"
                             onClick={()=> {
                                 const saved = saveCourse(course);
                                 console.log(saved);
                                 if (saved) {
                                     setIsSavedOverride(true);
                                 } else {
                                     setIsSavedOverride(false);
                                 }
                             }}
                        >
                            {isSaved || isSavedOverride ? (
                                <>
                                    <BookmarkCheck className="h-5 w-5 mt-0.5" />
                                    <p>Saved</p>
                                </>
                            ) : (
                                <>
                                    <Bookmark  className="h-5 w-5 mt-0.5" />
                                    <p>Save</p>
                                </>
                            )}
                        </div>
                    )}

                    {/*ADD BUTTON*/}
                    <div
                        onClick={() => {
                            if (!isAdding) {
                                console.log("Selected Schedule ID test:", selectedSchedule);  // Log the scheduleId
                                handleAddCourse(course, selectedSchedule)
                                setIsAdding(true)
                            }
                        }}
                        className={`${!isAdding ? "cursor-pointer" : "cursor-default"} flex-col space-y-0.5 bg-green-100 hover:bg-green-200 text-green-800 flex items-center justify-center px-4 h-full w-20 rounded-xs rounded-r-md font-medium`}
                    >
                        {isAdding ? (
                            <>
                                <PlusCircle className="opacity-50 h-5 w-5 mt-0.5" />
                                <p className="opacity-50">Add</p>
                            </>
                        ) : (
                            <>
                                <PlusCircle className="h-5 w-5 mt-0.5" />
                                <p>Add</p>
                            </>
                        )}
                    </div>
                </div>
            </div>

            <CourseModal isOpen={isModalOpen} onClose={()=> setIsModalOpen(false)} course={course}/>
        </div>
    );
}
