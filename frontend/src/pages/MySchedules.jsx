import { useState } from "react";
import ScheduleCard from "../components/ScheduleCard.jsx";
import {PlusCircle} from "lucide-react";
import { Link } from "react-router-dom";
import {Button} from "@/components/ui/button.jsx";
import { ArrowDown, ArrowUp } from "lucide-react";

const schedules = ["Spring 2026", "Fall 2025", "Summer 2025", "Winter 2025"];

function MySchedules() {

    const [isDescending, setIsDescending] = useState(true);

    const sortedSchedules = schedules.sort((a, b) => {
        const seasons = { "Winter": 0, "Spring": 1, "Summer": 2, "Fall": 3 };
        const [seasonA, yearA] = a.split(' ');
        const [seasonB, yearB] = b.split(' ');
        return isDescending
            ? yearB - yearA || seasons[seasonB] - seasons[seasonA]
            : yearA - yearB || seasons[seasonA] - seasons[seasonB];
    });

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

                <div className="cursor-pointer py-6 w-full h-full flex flex-col space-y-1 items-center justify-center border-[1.5px] border-slate-400 border-dashed rounded-lg  hover:border-black">
                    <PlusCircle className="h-5" />
                    <p>Add Semester</p>
                </div>

            </div>
        </div>
    );
}

export default MySchedules;
