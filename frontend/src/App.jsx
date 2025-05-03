import React, {useEffect, useState} from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import FindCourses from "./pages/FindCourses.jsx";
import Profile from "./pages/Profile.jsx";
import NotFound from "./pages/NotFound.jsx";
import MySchedules from "./pages/MySchedules.jsx";
import SavedCourses from "./pages/SavedCourses.jsx";
import SemesterSchedules from "@/pages/SemesterSchedules.jsx";
import Chatbot from "@/pages/Chatbot.jsx";
import api from "@/api.js";
import { Toaster } from "@/components/ui/sonner"

function App() {

    const [schedules, setSchedules] = useState([]);

    function fetchSchedules() {
        api.get("/schedules")
            .then((res) => {
                setSchedules(res.data.semesterSchedules);
            })
            .catch((err) => {
                console.error("Error fetching schedules:", err);
            });
    }

    useEffect(() => {
        fetchSchedules()
    }, []);

    return (
        <Router>
            <div className="flex flex-col bg-slate-50">
                <Navbar className="z-50" schedules={schedules} onScheduleUpdate={fetchSchedules}/>
                <div className="p-6 pb-10 pt-24 px-3 sm:px-4 md:px-6 flex flex-col w-full max-w-6xl mx-auto">
                    <Routes>
                        <Route path="/" element={<FindCourses />} />
                        <Route path="/profile" element={<Profile />} />
                        <Route path="/schedules/" element={<MySchedules schedules={schedules} onScheduleUpdate={fetchSchedules} />} />
                        <Route path="/schedules/:semester" element={<SemesterSchedules />} />
                        <Route path="/saved-courses" element={<SavedCourses />} />
                        <Route path="/chatbot" element={<Chatbot />} />

                        <Route path="*" element={<NotFound />} />
                    </Routes>
                </div>
                <Toaster richColors position="top-center" />
            </div>
        </Router>
    );
}

export default App;