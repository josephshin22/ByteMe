import React, {useEffect, useState} from "react";
import api from "@/api.js";
import {Button} from "@/components/ui/button.jsx";
import {useNavigate} from "react-router-dom";
import {Pencil, Trash} from "lucide-react";

const ScheduleCard = ({semesterSchedules}) => {

    const [schedules, setSchedules] = useState(semesterSchedules.schedules);
    const [showDeleteOptions, setShowDeleteOptions] = useState(false);
    const navigate = useNavigate();

    const handleDelete = (scheduleId) => {
        api.delete(`/schedules/${scheduleId}`)
            .then(() => {
                setSchedules((prev) => prev.filter(s => s.scheduleID !== scheduleId));
                setShowDeleteOptions(false);
            })
            .catch(err => {
                console.error("Error deleting schedule:", err);
            });
    };

    const handleCardClick = () => {
        if (!showDeleteOptions) {
            navigate(`/schedules/${semesterSchedules.semester}`);
        }
    };

    return (
        <div
            className={`relative ${!showDeleteOptions ? "group" : ""}`}
            onClick={handleCardClick}
        >
            <div
                className={`shadow cursor-pointer flex flex-col bg-white rounded-lg p-4 min-h-40 outline-0 outline-border transition-all duration-100 ${!showDeleteOptions ? "group-hover:outline-1 group-hover:shadow-lg group-hover:-translate-y-0.5" : ""}`}>

                <div className="flex items-start justify-between">
                    <div>
                        <h2 className="text-lg font-medium">{semesterSchedules.semester.replaceAll("_", " ")}</h2>
                        <p className="text-xs text-slate-400">Junior Year {semesterSchedules.semester.split("_").pop()}</p>
                    </div>

                    <div className="translate-x-1 -translate-y-1">
                        {!showDeleteOptions && schedules.length > 0 && (
                            <Button
                                onClick={(e) => {
                                    e.stopPropagation();
                                    setShowDeleteOptions(true);
                                }}
                                className="flex-end hover:bg-slate-100"
                                // size="xs"
                                variant="icon"
                            >
                                <Pencil/>
                            </Button>
                        )}
                    </div>
                </div>

                <div className="flex flex-col mt-3">
                    {schedules.length ? (
                        <>
                            {schedules.map((schedule, index) => (
                                <p key={index}>{schedule.name} {schedule.scheduleID}</p>
                            ))}
                        </>
                    ) : (
                        <p className="text-slate-500">No schedules</p>
                    )}
                </div>


                {showDeleteOptions && schedules.length > 0 && (
                    <div
                        className="absolute top-0 left-0 w-full h-full bg-white/95 p-4 flex flex-col gap-2 rounded-lg shadow-md z-10">
                        <div className="flex-1 overflow-y-auto max-h-40 flex flex-col gap-2">
                            {schedules.map((s) => (
                                <div key={s.scheduleID}>
                                    <Button
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            handleDelete(s.scheduleID)
                                        }}
                                        className="bg-red-100 hover:bg-red-200 text-red-800 transition-colors duration-150 text-left whitespace-normal break-words w-full"
                                        size="sm"
                                    >
                                        <Trash/>
                                        {s.name} {s.scheduleID}
                                    </Button>
                                </div>
                            ))}
                        </div>
                        <div className="mt-auto pt-2">
                            <Button
                                variant="ghost"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    setShowDeleteOptions(false)
                                }}
                                size="sm"
                                className="w-full"
                            >
                                Cancel
                            </Button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}

export default ScheduleCard;