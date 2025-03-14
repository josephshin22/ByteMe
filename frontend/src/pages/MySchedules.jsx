import ScheduleCard from "../components/ScheduleCard.jsx";
import {PlusCircle} from "lucide-react";

function MySchedules() {
    return (
        <div>
            <h1 className="font-semibold text-xl mb-4">My Schedules</h1>

            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 w-full max-w-5xl">
                <ScheduleCard/>
                <ScheduleCard/>
                <div className="cursor-pointer py-6 w-full h-full flex flex-col space-y-1 items-center justify-center border-[1.5px] border-slate-400 border-dashed rounded-lg  hover:border-black">
                    <PlusCircle className="h-5" />
                    <p>Add Semester</p>
                </div>
            </div>

        </div>
    );
}

export default MySchedules;
