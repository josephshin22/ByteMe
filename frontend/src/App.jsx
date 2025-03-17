import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import FindCourses from "./pages/FindCourses.jsx";
import Profile from "./pages/Profile.jsx";
import NotFound from "./pages/NotFound.jsx";
import MySchedules from "./pages/MySchedules.jsx";
import SavedCourses from "./pages/SavedCourses.jsx";

function App() {
    return (
        <Router>
            <div className="flex flex-col">
                <Navbar />
                <div className=" p-6 py-10 px-3 sm:px-4 md:px-6 flex flex-col ">
                    <Routes>
                        <Route path="/" element={<FindCourses />} />
                        <Route path="/profile" element={<Profile />} />
                        <Route path="/my-schedules" element={<MySchedules />} />
                        <Route path="/saved-courses" element={<SavedCourses />} />
                        <Route path="*" element={<NotFound />} />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default App;