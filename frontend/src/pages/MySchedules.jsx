import React, { useState } from "react";
import ScheduleCard from "../components/ScheduleCard.jsx";
import {PlusCircle} from "lucide-react";
import { Link } from "react-router-dom";
import {Button} from "@/components/ui/button.jsx";
import { ArrowDown, ArrowUp } from "lucide-react";
import FormModal from "@/components/FormModal.jsx";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";

const schedules = ["2023_Fall", "2024_Spring"];


function MySchedules() {

    const [isDescending, setIsDescending] = useState(true);
    const [showModal, setShowModal] = useState(false);

    const sortedSchedules = schedules.sort((a, b) => {
        const seasons = { "Winter": 0, "Spring": 1, "Summer": 2, "Fall": 3 };
        const [seasonA, yearA] = a.split(' ');
        const [seasonB, yearB] = b.split(' ');
        return isDescending
            ? yearB - yearA || seasons[seasonB] - seasons[seasonA]
            : yearA - yearB || seasons[seasonA] - seasons[seasonB];
    });

    const handleOutsideClick = (e) => {
        if (e.target.id === "form-modal-overlay") {
            onClose();
        }
    };

    const availableSemesters = ['2023_Fall', '2023_Winter_Online', '2024_Spring', '2024_Early_Summer', '2024_Fall', '2025_Spring'];

    return (
        <div>
            <div className="flex justify-between">
                <h1 className="font-semibold text-xl mb-4 ">My Schedules</h1>
                <Button variant="ghost" onClick={() => setIsDescending(!isDescending)}>
                    {isDescending ? (
                            <>
                                Newest First
                                <ArrowDown />
                            </>
                        ):(
                            <>
                                Oldest First
                                <ArrowUp/>
                            </>
                        )}
                </Button>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 w-full max-w-5xl">

                {sortedSchedules.map((semester) => (
                    <Link to={`/schedules/${semester}`}>
                        <ScheduleCard semester={semester} />
                    </Link>
                ))}

                <div
                    className="cursor-pointer py-6 w-full h-full flex flex-col space-y-1 items-center justify-center border-[1.5px] border-slate-400 border-dashed rounded-lg hover:border-black"
                    onClick={() => setShowModal(true)}
                >
                    <PlusCircle className="h-5" />
                    <p>Add Semester</p>
                </div>

                {showModal && (
                    <div id="form-modal-overlay" className="z-20 fixed inset-0 p-4 bg-black/50 flex flex-col items-center justify-center" onClick={handleOutsideClick}>
                        <div className="bg-white p-6 rounded-lg shadow-lg min-w-sm">

                            <h2 className="font-semibold text-lg mb-4">Choose Semester</h2>

                            <Select onValueChange={(value) => console.log(value)}>
                                <SelectTrigger className="w-full">
                                    <SelectValue placeholder="Choose..." />
                                </SelectTrigger>
                                <SelectContent>
                                    {availableSemesters.map((semester) => (
                                        <SelectItem key={semester} value={semester}>
                                            {semester.replaceAll("_", " ")}
                                        </SelectItem>
                                    ))}
                                </SelectContent>
                            </Select>

                            <div className="flex gap-2 mt-4">
                                <Button
                                    onClick={() => {
                                        setShowModal(false)
                                    } }
                                >
                                    Submit
                                </Button>
                                <Button
                                    variant="ghost"
                                    onClick={()=> setShowModal(false)}
                                >
                                    Cancel
                                </Button>
                            </div>
                        </div>
                    </div>
                )}

            </div>
        </div>
    );
}

export default MySchedules;
