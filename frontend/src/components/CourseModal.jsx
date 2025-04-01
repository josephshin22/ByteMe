import React from "react";
import {Bookmark, PlusCircle, X} from "lucide-react";
import {Separator} from "@/components/ui/separator.jsx";

const CourseModal = ({ isOpen, onClose, course }) => {
    if (!isOpen) return null;

    const handleOutsideClick = (e) => {
        if (e.target.id === "modal-overlay") {
            onClose();
        }
    };

    return (
        <div id="modal-overlay" className="fixed inset-0 p-4 bg-black/50 flex flex-col items-center justify-center" onClick={handleOutsideClick}>
            <div className="bg-background max-w-4xl max-h-[90vh] mx-4 rounded-lg shadow-lg relative flex flex-col"> {/* Flex column added */}

                <X onClick={onClose} size={20} className="text-slate-500 cursor-pointer absolute top-3 right-3" />

                <div className="p-6 pb-0">
                    <h2 className="text-sm text-muted-foreground">{course.abbreviation} {course.courseNum} {course.section}</h2>
                    <h1 className="text-lg font-semibold mb-2">{course.name}</h1>
                    <Separator />
                </div>

                {/* Scrollable Content */}
                <div className="p-6 pt-2 overflow-y-auto flex-1"> {/* Make this flex-grow */}

                    {/* Course Info */}
                    <div className="mt-2 space-y-2 text-muted-foreground flex flex-wrap space-x-5 max-w-3xl">
                        <p>&bull; {course.faculty}</p>
                        <p>&bull; {course.times}</p>
                        <p>&bull; {course.location}</p>
                        <p>&bull; DATE RANGE</p>
                        <p>&bull; {course.openSeats}/{course.totalSeats} open seats</p>
                        <p>&bull; {course.credits} credits</p>
                        <p>&bull; REF NUM</p>
                    </div>

                    {/* Course Description */}
                    <div className="mt-4">
                        <h3 className="font-semibold">Course Description</h3>
                        <p className="text-muted-foreground text-sm leading-relaxed">
                            A continued study of the financial statements with an emphasis on the liabilities and stockholder’s equity sections of the balance sheet. Includes topics such as accounting for investments, current and long-term debt, earnings per share (EPS), accounting for income taxes, leases, post-retirement benefits, accounting changes and the statement of cash flows. This course also focuses on accounting theory and the interpretation of current financial accounting standards.
                        </p>
                    </div>

                    {/* Prerequisites */}
                    <div className="mt-4">
                        <h3 className="font-semibold">Prerequisites:</h3>
                        <p className="text-muted-foreground text-sm">Accounting 202. Three hours.</p>
                    </div>
                </div>

                {/* Buttons (Fixed at Bottom) */}
                <div className="flex items-stretch">
                    <div className="rounded-bl-lg flex-1 py-3 cursor-pointer bg-slate-200 hover:bg-slate-300 text-black flex space-x-2 items-center justify-center">
                        <Bookmark className="w-4.5" />
                        <p>Save for Later</p>
                    </div>
                    <div className="rounded-br-lg flex-1 py-3 cursor-pointer bg-slate-600 hover:bg-slate-700 text-white flex space-x-2 items-center justify-center">
                        <PlusCircle className="w-4.5" />
                        <p>Add to Schedule</p>
                    </div>
                </div>
            </div>
        </div>

    );
};

export default CourseModal;
