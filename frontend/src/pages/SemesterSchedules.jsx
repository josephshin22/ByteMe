import { useParams } from "react-router-dom";
import {useEffect, useState} from "react";
import api from "@/api.js";
import Calendar from "../components/Calendar.jsx";
import {Button} from "@/components/ui/button.jsx";
import {ChevronsDownUp, ChevronsUpDown} from "lucide-react";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.jsx";
import * as React from "react";


function SemesterSchedules() {
    const { semester } = useParams();
    const [schedules, setSchedules] = useState([]);

    useEffect(() => {
        api.get(`/schedules?semester=${semester}`)
            .then((response) => {
                setSchedules(response.data);
            })
            .catch((error) => {
                console.error("Error fetching schedules:", error);
            });
    }, [semester]);
    console.log("Schedules:", schedules);

    const [abbreviateName, setAbbreviateName] = useState(false);
    const [moreInfo, setMoreInfo] = useState(false);

    const [selectedSemester, setSelectedSemester] = useState(semester);
    const handleSemesterChange = (value) => {
        setSelectedSemester(value);
        window.location.href = `/schedules/${value}`;
    };

    const [availableSemesters, setAvailibleSchedules] = useState([]);
    useEffect(() => {
        api.get("/schedules")
            .then((response) => {
                setAvailibleSchedules(response.data);
            })
            .catch((error) => {
                console.error("Error fetching schedules:", error);
            });
    }, []);

    return (
        <div className="">
            
            <div className="z-[1] fixed top-24 flex justify-between w-full max-w-6xl pr-6 sm:pr-8 md:pr-12">
                <div className="p-1 px-4 pr-1 bg-white rounded-lg shadow-md flex align-middle items-center justify-center border border-slate-200">
                    <h1 className="text-lg font-semibold">{selectedSemester.replaceAll("_", " ")}</h1>

                    {/* Semester Picker */}
                    <Select
                        value={selectedSemester}
                        onValueChange={(value) => handleSemesterChange(value)}
                    >
                        <SelectTrigger className="sm:w-auto w-full border-none shadow-none cursor-pointer">
                            <span className="sr-only">Select Semester</span>
                        </SelectTrigger>
                        <SelectContent>
                            {availableSemesters
                                .map((s) => (
                                    <SelectItem key={s} value={s}>
                                        {s.replaceAll("_", " ")}
                                    </SelectItem>
                                ))
                            }
                        </SelectContent>
                    </Select>


                </div>
                <div className="flex align-middle justify-center items-center p-1 bg-white rounded-lg shadow-md border border-slate-200">
                    <Button variant="ghost" onClick={() => setAbbreviateName(!abbreviateName)}>{abbreviateName ? "Show Names" : "Show Codes"}</Button>
                    <Button variant="ghost" onClick={() => setMoreInfo(!moreInfo)}>
                        {moreInfo ? <ChevronsDownUp/> : <ChevronsUpDown/>}
                        {moreInfo ? "Less Info" : "More Info"}
                    </Button>
                </div>
            </div>

            <div className="mt-16 flex">
                {schedules.map((schedule, index) => (
                    <div key={index} className="min-w-[900px]">
                        <Calendar key={schedule.id} schedule={schedule} abbreviateName={abbreviateName} moreInfo={moreInfo} />
                    </div>
                ))}
            </div>

        </div>
    );

}

export default SemesterSchedules;
