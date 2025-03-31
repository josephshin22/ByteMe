import React, { useState } from "react";
import { Bookmark, PlusCircle } from "lucide-react";
import CourseModal from "./CourseModal.jsx";

export default function CourseCard() {

    const [isModalOpen, setIsModalOpen] = useState(false);

    return (
        <>
            <div
                className="flex items-stretch shadow w-full bg-background rounded-lg cursor-pointer text-sm outline-0 outline-border hover:outline-1 "
            >
                {/* Course Code and Section */}
                <div
                    className="w-20 p-4 text-center font-semibold border-r border-slate-300 flex items-center"
                    onClick={() => setIsModalOpen(true)}
                >
                    ACCT 302 A</div>

                {/* Course Details */}
                <div className="flex-1 p-4 space-y-1" onClick={() => setIsModalOpen(true)}>
                    <div className="flex justify-between">
                        <div className="font-medium">INTERMEDIATE ACCOUNTING II</div>
                        <div className="font-medium">MWF 9:00-9:50am</div>
                    </div>
                    <div className="flex justify-between ">
                        <div className="text-slate-500">Greybill, Keith B.</div>
                        <div className="flex space-x-3 ">
                            <div className="text-slate-500">• STEM 376</div>
                            <div className="text-slate-500">• 5/20 open seats</div>
                            <div className="text-slate-500">• 3 seats</div>
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

            <CourseModal isOpen={isModalOpen} onClose={()=> setIsModalOpen(false)} />
        </>
    );
}
