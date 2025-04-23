import React, { useState, useRef, useEffect } from 'react';
import { Search, ChevronDown, User, Menu, X } from 'lucide-react';
import { Link, useLocation } from "react-router-dom";
import api from "@/api.js";



const Navbar = () => {
    const [isScheduleOpen, setIsScheduleOpen] = useState(false);
    const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
    const [isSearchExpanded, setIsSearchExpanded] = useState(window.innerWidth >= 880);
    const [isMobileScheduleOpen, setIsMobileScheduleOpen] = useState(false);
    // const [theme, setTheme] = useState(localStorage.getItem('theme') || 'light');
    const dropdownRef = useRef(null);
    const searchInputRef = useRef(null);
    const mobileMenuRef = useRef(null);
    const scheduleButtonRef = useRef(null);
    const scheduleMenuRef = useRef(null);

    const location = useLocation(); // Get the current path

    // const schedules = ["Spring 2026", "Fall 2025", "Summer 2025", "Winter 2025"];
    const [schedules, setSchedules] = useState([]);
    useEffect(() => {
        api.get("/schedules")
            .then((response) => {
                setSchedules(response.data);
            })
            .catch((error) => {
                console.error("Error fetching schedules:", error);
            });
    }, []);

    const sortedSchedules = schedules.sort((a, b) => {
        const seasons = { "Winter": 0, "Spring": 1, "Summer": 2, "Fall": 3 };
        const [seasonA, yearA] = a.split('_');
        const [seasonB, yearB] = b.split('_');
        return yearB - yearA || seasons[seasonB] - seasons[seasonA];
    });

    useEffect(() => {
        if (isMobileMenuOpen) {
            setIsMobileScheduleOpen(true);
        } else {
            setIsMobileScheduleOpen(false);
        }
    }, [isMobileMenuOpen]);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (mobileMenuRef.current && !mobileMenuRef.current.contains(event.target) &&
                !event.target.classList.contains('hamburger-btn')) {
                setIsMobileMenuOpen(false);
            }

            if (isSearchExpanded && searchInputRef.current && !searchInputRef.current.contains(event.target) &&
                window.innerWidth < 880) {
                setIsSearchExpanded(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, [isSearchExpanded]);

    useEffect(() => {
        const handleResize = () => {
            if (window.innerWidth >= 768) {
                setIsMobileMenuOpen(false);
            }
            if (window.innerWidth >= 880) {
                setIsSearchExpanded(true);
            } else {
                setIsSearchExpanded(false);
            }
        };

        window.addEventListener("resize", handleResize);
        return () => window.removeEventListener("resize", handleResize);
    }, []);

    // Handle mouse interactions for schedule dropdown
    useEffect(() => {
        const handleMouseEnter = () => {
            setIsScheduleOpen(true);
        };

        const handleMouseLeave = (e) => {
            // Check if the mouse is not entering the other element
            if (
                !scheduleButtonRef.current?.contains(e.relatedTarget) &&
                !scheduleMenuRef.current?.contains(e.relatedTarget)
            ) {
                setIsScheduleOpen(false);
            }
        };

        const buttonElement = scheduleButtonRef.current;
        const menuElement = scheduleMenuRef.current;

        if (buttonElement) {
            buttonElement.addEventListener('mouseenter', handleMouseEnter);
            buttonElement.addEventListener('mouseleave', handleMouseLeave);
        }

        if (menuElement) {
            menuElement.addEventListener('mouseenter', handleMouseEnter);
            menuElement.addEventListener('mouseleave', handleMouseLeave);
        }

        return () => {
            if (buttonElement) {
                buttonElement.removeEventListener('mouseenter', handleMouseEnter);
                buttonElement.removeEventListener('mouseleave', handleMouseLeave);
            }
            if (menuElement) {
                menuElement.removeEventListener('mouseenter', handleMouseEnter);
                menuElement.removeEventListener('mouseleave', handleMouseLeave);
            }
        };
    }, [isScheduleOpen]);

    // useEffect(() => {
    //     document.documentElement.classList.toggle('dark', theme === 'dark');
    //     localStorage.setItem('theme', theme);
    // }, [theme]);

    // const toggleTheme = () => {
    //     setTheme(theme === 'light' ? 'dark' : 'light');
    // };



    return (
        <nav className="fixed w-full bg-background top-0 shadow z-10">

            <div className="w-full flex items-center justify-between max-w-6xl mx-auto md:px-6 py-3 px-4">

                <button
                    className="hamburger-btn md:hidden flex items-center justify-center"
                    onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
                >
                    {isMobileMenuOpen ? (
                        <X className="cursor-pointer h-6 w-6 text-slate-700" />
                    ) : (
                        <Menu className="cursor-pointer h-6 w-6 text-slate-700" />
                    )}
                </button>

                <div className="hidden md:flex items-center space-x-5 pr-6 text-sm">
                    <Link to="/" className={`${
                        location.pathname === "/" && "underline underline-offset-6" }`}>
                        Find Courses
                    </Link>
                    <div className="relative" ref={dropdownRef}>
                        <div
                            ref={scheduleButtonRef}
                            className={`flex items-center space-x-1 ${location.pathname === "/schedules" && "underline underline-offset-6" }`}
                        >
                            <Link to="/schedules" onClick={() => setIsScheduleOpen(false)}>
                                My Schedules
                            </Link>
                            <ChevronDown className="h-4 w-4 cursor-pointer" />
                        </div>
                        {isScheduleOpen && (
                            <div
                                ref={scheduleMenuRef}
                                className="absolute top-full left-0 w-48 z-100"
                            >
                                <div className="bg-background border border-border rounded-lg shadow-lg py-1 mt-2">
                                    {sortedSchedules.map((semester, index) => (
                                        <Link key={index} className="block px-4 py-2 hover:bg-muted" to={`/schedules/${semester}`} onClick={()=>{setIsScheduleOpen(false)}}>
                                            {semester.replaceAll("_", " ")}
                                        </Link>
                                    ))}
                                    {/*<Link to="#" className="block px-4 py-2 hover:bg-muted">+ Add New Schedule</Link>*/}
                                </div>
                            </div>
                        )}
                    </div>
                    <Link to="saved-courses" className={`hidden md:inline-block mr-4 ${
                        location.pathname === "/saved-courses" && "underline underline-offset-6" }
                            `}
                    >Saved Courses</Link>
                    <Link to="chatbot" className={`hidden md:inline-block mr-2 ${
                        location.pathname === "/chatbot" && "underline underline-offset-6" }
                            `}
                    >Chatbot</Link>
                </div>

                {isMobileMenuOpen && (
                    <div
                        ref={mobileMenuRef}
                        className=" fixed top-16 left-0 h-screen w-64 bg-white border-r border-slate-200 shadow-lg z-20 transform transition-transform duration-300 ease-in-out"
                    >
                        <div className=" flex flex-col py-4">
                            <Link to="/" className={`px-5 py-3 hover:bg-slate-100 ${
                                location.pathname === "/" && "underline underline-offset-6"}` }>Find Courses</Link>

                            <Link to="/schedules" className={`flex justify-between px-5 py-3 hover:bg-slate-100 ${
                                location.pathname === "/schedules" && "underline underline-offset-6"}` }>My Schedules
                                <div
                                    className="cursor-pointer flex items-center justify-between font-medium "
                                    onClick={() => setIsMobileScheduleOpen(!isMobileScheduleOpen)}
                                >
                                    <ChevronDown strokeWidth={2} className={` h-5 w-5 transition-transform ${isMobileScheduleOpen ? 'rotate-180' : ''}`} />
                                </div>
                            </Link>


                            {isMobileScheduleOpen && (
                                <div className="my-2 ml-8 flex flex-col space-y-2 text-sm">
                                    <Link to="#" className="hover:text-slate-500">Fall 2024</Link>
                                    <Link to="#" className="hover:text-slate-500">Spring 2025</Link>
                                    <Link to="#" className="hover:text-slate-500">+ Add New Schedule</Link>
                                </div>
                            )}

                            <Link to="/saved-courses" className={`px-5 py-3 hover:bg-slate-100 ${
                                location.pathname === "/saved-courses" && "underline underline-offset-6"}`}>Saved Courses</Link>
                        </div>
                    </div>
                )}

                <div className="flex items-center space-x-5 text-sm">
                    <div className="relative">
                        {isSearchExpanded ? (
                            <div ref={searchInputRef} className="flex items-center">
                                <input
                                    type="text"
                                    placeholder="Search courses..."
                                    className="pl-10 pr-4 py-1.5 bg-slate-100 rounded-md focus:outline-none focus:ring-1 focus:ring-slate-300 min-w-full"
                                />
                                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-slate-400 h-4 w-4" />
                            </div>
                        ) : (
                            <button
                                onClick={() => setIsSearchExpanded(true)}
                                className="flex items-center justify-center cursor-pointer"
                            >
                                <Search className="h-5 w-5 text-slate-500 hover:text-slate-700" />
                            </button>
                        )}
                    </div>

                    {/* Light/dark mode toggle */}
                    {/*<button onClick={toggleTheme} className="flex items-center justify-center cursor-pointer">*/}
                    {/*    {theme === 'light' ? (*/}
                    {/*        <Moon className="h-5 w-5 text-slate-500 hover:text-slate-700" />*/}
                    {/*    ) : (*/}
                    {/*        <Sun className="h-5 w-5 text-slate-500 hover:text-slate-700" />*/}
                    {/*    )}*/}
                    {/*</button>*/}

                    {/* Profile */}
                    <Link to="/profile" className="flex items-center">
                        <span className={`hidden md:inline-block mr-2 ${
                            location.pathname === "/profile" && "underline underline-offset-6" }
                            `}>Username</span>
                        <div className="flex items-center justify-center w-8 h-8 rounded-full bg-slate-200">
                            <User className="h-5 w-5 text-slate-500 hover:text-slate-700" />
                        </div>
                    </Link>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;