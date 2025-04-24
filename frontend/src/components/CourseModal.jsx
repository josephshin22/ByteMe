import React from "react";
import {Armchair, Bookmark, Calendar, CircleDollarSign, Clock, PlusCircle, School, User, X} from "lucide-react";
import {Separator} from "@/components/ui/separator.jsx";
import {formatCourseTimes} from "@/utils/formatCourseTimes.jsx";
import {handleSave} from "@/utils/courseUtils.jsx";

const CourseModal = ({ isOpen, onClose, course }) => {
    if (!isOpen) return null;

    const handleOutsideClick = (e) => {
        if (e.target.id === "modal-overlay") {
            onClose();
        }
    };

    return (
        <div id="modal-overlay" className="z-20 fixed inset-0 p-4 bg-black/50 flex flex-col items-center justify-center" onClick={handleOutsideClick}>
            <div className="bg-background max-w-3xl max-h-[90vh] mx-4 rounded-lg shadow-lg relative flex flex-col"> {/* Flex column added */}

                <X onClick={onClose} size={20} className="text-slate-500 cursor-pointer absolute top-3 right-3" />

                <div className="p-6 pb-0">
                    <div className="flex space-x-1 flex-wrap">
                        <h1 className="text-lg font-semibold">{course.subjCode} {course.number} {course.section}</h1>
                        <h1 className="text-lg font-thin text-slate-500">&bull;</h1>
                        <h1 className="text-lg mb-2">{course.name.toLowerCase().replace(/\b\w/g, char => char.toUpperCase())}</h1>
                    </div>
                    <Separator />
                </div>

                {/* Smaller info */}
                <div className="block sm:hidden px-6 mt-4 space-y-2">
                    <div className="space-y-2 flex flex-wrap space-x-5 max-w-3xl items-start">
                        <div className="flex gap-2 items-center justify-center">
                            <User size={18}/>
                            <p>{course.faculty.join(", ")}</p>
                        </div>
                    </div>
                    <div className="space-y-2 flex flex-wrap space-x-5 max-w-3xl items-start">
                        <div className="flex gap-2 items-center">
                            <Clock size={18}/>
                            <p>{formatCourseTimes(course.times)}</p>
                        </div>

                        <div className="flex gap-2 items-center">
                            <Calendar size={18}/>
                            <p>{course.semester.replaceAll("_", " ")}</p>
                        </div>
                    </div>
                    <div className="space-y-2 flex flex-wrap space-x-5 max-w-3xl items-start">
                        <div className="flex gap-2 items-center">
                            <School size={18}/>
                            <p>{course.location}</p>
                        </div>

                        <div className="flex gap-2 items-center">
                            <Armchair size={18}/>
                            <p>{course.openSeats}/{course.totalSeats} seats open</p>
                        </div>

                        <div className="flex gap-2 items-center">
                            <CircleDollarSign size={18}/>
                            <p>{course.credits} credit{course.credits !== 1 && "s"}</p>
                        </div>
                    </div>
                </div>

                {/* Scrollable Content */}
                <div className="mt-4 p-6 pt-2 overflow-y-auto flex-1 flex gap-12">

                    {/* Course Description */}
                    <div className="pb-4">
                        <div>
                            <h3 className="font-semibold">Course Description</h3>
                            <p className="text-muted-foreground text-sm">
                                A continued study of the financial statements with an emphasis on the liabilities and stockholder’s equity sections of the balance sheet. Includes topics such as accounting for investments, current and long-term debt, earnings per share (EPS), accounting for income taxes, leases, post-retirement benefits, accounting changes and the statement of cash flows. This course also focuses on accounting theory and the interpretation of current financial accounting standards.
                            </p>
                        </div>

                        {/* Prerequisites */}
                        <div className="mt-4 pb-4">
                            <h3 className="font-semibold">Prerequisites:</h3>
                            <p className="text-muted-foreground text-sm">Accounting 202. Three hours.</p>
                        </div>
                    </div>

                    {/* Course Info */}
                    <div className="hidden sm:flex space-y-2 flex-col space-x-5 max-w-128 min-w-2/5 items-start">
                        <div className="flex gap-2 items-center justify-center">
                            <User className="min-w-[18px]" size={18}/>
                            <p>{course.faculty.join(", ")}</p>
                        </div>

                        <div className="flex gap-2 items-center">
                            <Clock className="min-w-[18px]" size={18}/>
                            <p>{formatCourseTimes(course.times)}</p>
                        </div>

                        <div className="flex gap-2 items-center">
                            <Calendar className="min-w-[18px]" size={18}/>
                            <p>{course.semester.replaceAll("_"," ")}</p>
                        </div>



                        <Separator className="my-2"/>

                        <div className="flex gap-2 items-center">
                            <School className="min-w-[18px]" size={18}/>
                            <p>{course.location}</p>
                        </div>

                        <div className="flex gap-2 items-center">
                            <Armchair className="min-w-[18px]" size={18}/>
                            <p>{course.openSeats}/{course.totalSeats} seats open</p>
                        </div>

                        <div className="flex gap-2 items-center">
                            <CircleDollarSign className="min-w-[18px]" size={18}/>
                            <p>{course.credits} credit{course.credits !== 1 && "s"}</p>
                        </div>
                    </div>

                </div>

                {/* Buttons (Fixed at Bottom) */}
                <div className="flex items-stretch">
                    <div className="rounded-bl-lg flex-1 py-3 cursor-pointer bg-blue-100 hover:bg-blue-200 text-blue-800 flex space-x-2 items-center justify-center"
                    onClick={() => {handleSave(course)}}
                    >
                        <Bookmark className="w-4.5" />
                        <p>Save for Later</p>
                    </div>
                    <div className="rounded-br-lg flex-1 py-3 cursor-pointer bg-green-100 hover:bg-green-200 text-green-800 flex space-x-2 items-center justify-center">
                        <PlusCircle className="w-4.5" />
                        <p>Add to Schedule</p>
                    </div>
                </div>
            </div>
        </div>

    );
};

export default CourseModal;
