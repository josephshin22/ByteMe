import { useState, useMemo } from 'react';
import {Button} from "@/components/ui/button.jsx";
import CourseModal from "@/components/CourseModal.jsx";
import * as React from "react";
import { jsPDF } from "jspdf";
import html2canvas from "html2canvas-pro";

export default function WeeklyClassCalendar({ schedule, abbreviateName, moreInfo }) {
    const courseColors = {
        ACCT: 'bg-red-200',
        COMP: 'bg-slate-200',
        MATH: 'bg-orange-200',
        PHYS: 'bg-purple-200',
        CHEM: 'bg-pink-200',
        BIOL: 'bg-green-200',
        HUMA: 'bg-yellow-200',
        // Add more course codes and colors as needed
    };


    // Map schedule.courses to a compatible format
    const mapCoursesToClasses = (courses) => {
        return courses.map((course) => ({
            id: `${course.subjCode}-${course.number}-${course.section}`,
            course: course,
            title: course.name,
            code: `${course.subjCode} ${course.number} ${course.section}`,
            days: course.times.map((time) => {
                // Convert single-letter day codes to full day names
                const dayMap = { M: 'Monday', T: 'Tuesday', W: 'Wednesday', R: 'Thursday', F: 'Friday' };
                return dayMap[time.day];
            }),
            startTime: course.times[0]?.start_time.slice(0, 5), // Use the first time slot's start time
            endTime: course.times[0]?.end_time.slice(0, 5), // Use the first time slot's end time
            location: course.location,
            color: courseColors[course.subjCode] || 'bg-blue-200',
        }));
    };
    const [classes, setClasses] = useState(mapCoursesToClasses(schedule.courses));
    function handleClassClick(course) {
        setCourseForModal(course);
        setIsModalOpen(true);
    }

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [courseForModal, setCourseForModal] = useState('');

    // Days of the week
    const daysOfWeek = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];

    // Calculate the end hour for the calendar
    const calendarEndHour = useMemo(() => {
        // Default end hour is 16 (4 PM)
        let endHour = 16;

        // Check if any classes end after 4 PM
        classes.forEach(cls => {
            const [hours, minutes] = cls.endTime.split(':').map(Number);
            // Round up to the next hour if there are minutes
            const classEndHour = minutes > 0 ? hours + 1 : hours;
            // const classEndHour = hours;
            if (classEndHour > endHour) {
                endHour = classEndHour;
            }
        });


        return endHour;
    }, [classes]);
    // console.log(`schedule: ${schedule.name} calendarEndHour: ${calendarEndHour}`);

    // Generate time slots based on calendarEndHour
    const timeSlots = useMemo(() => {
        const slots = [];
        for (let hour = 8; hour <= calendarEndHour; hour++) {
            slots.push(`${hour}:00`);
        }
        return slots;
    }, [calendarEndHour]);

    // Format time to be displayed
    const formatTime = (timeString) => {
        const [hours, minutes] = timeString.split(':').map(Number);
        return `${hours % 12 || 12}:${minutes.toString().padStart(2, '0')} ${hours >= 12 ? 'PM' : 'AM'}`;
    };

    // Calculate position and height based on time - FIXED CALCULATION
    const calculateClassPosition = (startTime, endTime) => {
        const [startHour, startMinute] = startTime.split(':').map(Number);
        const [endHour, endMinute] = endTime.split(':').map(Number);

        // Calculate in minutes from start of day (8 AM)
        const startMinutes = (startHour - 8) * 60 + startMinute;
        const endMinutes = (endHour - 8) * 60 + endMinute;

        // Calculate the total number of minutes displayed on the calendar
        const totalMinutesDisplayed = (calendarEndHour - 7) * 60;

        // Calculate top position as percentage of displayed time
        const topPercentage = (startMinutes / totalMinutesDisplayed) * 100;

        // Calculate height as percentage of displayed time
        const heightPercentage = ((endMinutes - startMinutes) / totalMinutesDisplayed) * 100;

        return {
            top: `${topPercentage}%`,
            height: `${heightPercentage}%`
        };
    };

    // Get classes for a specific day
    const getClassesForDay = (day) => {
        return classes.filter(cls => cls.days.includes(day));
    };

    const exportToPDF = (schedule) => {
        const calendarElement = document.querySelector(`[data-schedule-id="schedule-${schedule.id}"]`);

        if (!calendarElement) {
            console.error("Calendar element not found");
            return;
        }

        // Clone the calendar element to avoid modifying the original
        const clonedElement = calendarElement.cloneNode(true);

        // Update the header with the current schedule name
        const header = clonedElement.querySelector("h2");
        if (header) {
            header.textContent = schedule.name;
        }

        // Apply styles to the cloned element
        clonedElement.style.width = "1200px";
        clonedElement.style.padding = "20px";
        clonedElement.style.margin = "20px";

        // Append the cloned element to the body (hidden) for rendering
        clonedElement.style.position = "absolute";
        clonedElement.style.top = "-9999px";
        document.body.appendChild(clonedElement);

        html2canvas(clonedElement, { scale: 7 })
            .then((canvas) => {
                const imgData = canvas.toDataURL("image/png");
                const pdf = new jsPDF("landscape", "mm", "a4");

                // Calculate image dimensions to fit A4 size
                const pdfWidth = 200;
                const pdfHeight = 220;
                pdf.addImage(imgData, "PNG", 0, 0, pdfWidth, pdfHeight);
                pdf.save(`${schedule.name.replace(/\s+/g, "_")}_Schedule.pdf`);
            })
            .catch((error) => {
                console.error("Error exporting calendar to PDF:", error);
            })
            .finally(() => {
                // Remove the cloned element from the DOM
                document.body.removeChild(clonedElement);
            });
    };


    return (

        <div className="flex flex-col bg-white rounded-lg shadow-lg p-4 h-full mr-8">
            {/*Calendar Header*/}
            <div className="flex justify-between items-center">
                <h2 className="text-lg font-semibold text-slate-500">{schedule.name}</h2>
                <Button variant="ghost" onClick={() => exportToPDF(schedule)}>
                    Export to PDF
                </Button>
            </div>

            {/* Week View */}
            <div className="flex flex-col flex-grow overflow-hidden">
                {/* Day headers */}
                <div className="flex">
                    <div className="w-20"></div>
                    {daysOfWeek.map((day, index) => (
                        <div
                            key={index}
                            className="flex-1 text-center font-medium text-sm"
                        >
                            {day}
                        </div>
                    ))}
                </div>

                {/* Week grid */}
                <div className="flex flex-grow overflow-auto pt-2 ">
                    {/* Time column */}
                    <div className="w-20 flex-shrink-0 -mt-3">
                        {timeSlots.map((time, index) => (
                            <div key={index} className={`${moreInfo ? "h-12" : "h-7"} text-right pr-2 text-xs text-slate-500 flex items-start justify-end`}>
                                <span className="pt-1">{formatTime(time)}</span>
                            </div>
                        ))}
                    </div>

                    {/* Day columns */}
                    <div className="flex flex-grow border-t border-r border-slate-300">
                        {daysOfWeek.map((day, dayIndex) => (
                            <div key={dayIndex} className="flex-1 relative border-l">
                                {/* Time grid lines */}
                                {timeSlots.map((time, timeIndex) => (
                                    <div key={timeIndex} className={`${moreInfo ? "h-12" : "h-7"} border-b border-slate-300`}></div>
                                ))}

                                {/* Classes */}
                                {getClassesForDay(day).map(cls => {
                                    const { top, height } = calculateClassPosition(cls.startTime, cls.endTime);
                                    return (
                                        <div
                                            key={cls.id}
                                            className={`cursor-pointer absolute left-1 right-1 ${cls.color} rounded p-1 overflow-hidden border border-slate-300 hover:scale-101 hover:shadow-md`}
                                            style={{ top, height }}
                                            onClick={() => handleClassClick(cls.course)}
                                        >
                                            {abbreviateName ? (
                                                <div className="text-xs font-semibold truncate">{cls.code}</div>
                                            ) : (
                                                <div className="text-xs font-semibold truncate">{cls.title.toLowerCase().replace(/\b\w/g, char => char.toUpperCase())}</div>
                                            )}
                                            {moreInfo && (
                                                <div className="text-[10px]">{formatTime(cls.startTime)} - {formatTime(cls.endTime)}</div>
                                            )}
                                        </div>
                                    );
                                })}
                            </div>
                        ))}
                    </div>
                </div>
            </div>

            <CourseModal isOpen={isModalOpen} onClose={()=> setIsModalOpen(false)} course={courseForModal}/>
        </div>
    );
}