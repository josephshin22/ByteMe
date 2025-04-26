import React, {useEffect, useState} from "react";
import api from "@/api.js";
import {Button} from "@/components/ui/button.jsx";
import {useNavigate} from "react-router-dom";
import {Pencil} from "lucide-react";

const ScheduleCard = ({semester}) => {

    const [schedules, setSchedules] = useState([]);
    const [showDeleteOptions, setShowDeleteOptions] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        api.get(`/schedules?semester=${semester}`)
            .then((response) => {
                // response.data.map((schedule, index) => {
                //     api.get(`/user-courses?scheduleId=${schedule.scheduleID}`).then((courses) => {
                //         response.data[index].courses = courses.data.courses;
                //         console.log("courses data:", response.data[0])
                //     });
                // });
                setSchedules(response.data);
                // console.log('new', schedules);
            })
            .catch((error) => {
                console.error("Error fetching schedules:", error);
            });
    }, [semester]);

    console.log(`${semester} card schedules:`, schedules);

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
            navigate(`/schedules/${semester}`);
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
                        <h2 className="text-lg font-medium">{semester.replaceAll("_", " ")}</h2>
                        <p className="text-xs text-slate-400">Junior Year {semester.split("_").pop()}</p>
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
                                <p key={index}>{schedule.name}</p>
                            ))}
                        </>
                    ) : (
                        <p className="text-slate-500">No schedules</p>
                    )}
                </div>


                {showDeleteOptions && schedules.length > 0 && (
                    <div
                        className="absolute top-0 left-0 w-full h-full bg-white/95 p-4 flex flex-col gap-2 rounded-lg shadow-md z-10">
                        <p className="font-medium">Select schedule to remove:</p>
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
                                        {s.name}
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