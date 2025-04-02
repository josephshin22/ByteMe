import { useEffect, useState } from "react";
import api from "../api.js";
import CourseCard from "../components/CourseCard";
import {Input} from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import {Button} from "@/components/ui/button";
import {
    Check,
    ChevronDown,
    ChevronDownIcon, Rows2, Rows3,
    SearchIcon,
    X
} from "lucide-react";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Command, CommandGroup, CommandItem } from "@/components/ui/command";
import {Switch} from "@/components/ui/switch";
import {Label} from "@/components/ui/label.jsx";
import {Separator} from "@/components/ui/separator.jsx";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table"
import * as React from "react";
import CourseModal from "@/components/CourseModal.jsx";

const exampleCoursesJson = `[
  {
    "name": "Intro to Computer Science",
    "courseNum": "101",
    "abbreviation": "COMP",
    "faculty": "Dr. Smith",
    "location": "Room 101",
    "numCredits": 3,
    "times": "MWF 10:00-11:00 AM",
    "openSeats": 5,
    "totalSeats": 30,
    "section": "A",
    "semester": "Fall 2025"
  },
  {
    "name": "Calculus I",
    "courseNum": "201",
    "abbreviation": "MATH",
    "faculty": "Dr. Johnson",
    "location": "Room 202",
    "numCredits": 4,
    "times": "TR 9:30-10:45 AM",
    "openSeats": 2,
    "totalSeats": 25,
    "section": "B",
    "semester": "Fall 2025"
  },
  {
    "name": "Physics I",
    "courseNum": "101",
    "abbreviation": "PHYS",
    "faculty": "Dr. Brown",
    "location": "Room 303",
    "numCredits": 4,
    "times": "MWF 1:00-2:00 PM",
    "openSeats": 8,
    "totalSeats": 20,
    "section": "C",
    "semester": "Fall 2025"
  }
]`;

function FindCourses() {

    const courses = JSON.parse(exampleCoursesJson);

    const availableSemesters = [
        'Fall 2025', 'Spring 2026'
    ];

    const [message, setMessage] = useState("Loading...");
    const [selectedSemesters, setSelectedSemesters] = useState(['Fall 2025']);
    const [startTimeFilter, setStartTimeFilter] = useState('');
    const [endTimeFilter, setEndTimeFilter] = useState('');

    const handleSemesterChange = (index, value) => {
        const newSelectedSemesters = [...selectedSemesters];
        newSelectedSemesters[index] = value;
        setSelectedSemesters(newSelectedSemesters);
    };

    const clearTimeFilters = () => {
        setStartTimeFilter('');
        setEndTimeFilter('');
    }

    useEffect(() => {
        api
            .get("/hello")
            .then((res) => setMessage(res.data.message))
            .catch((err) => console.error("Error fetching data:", err));
    }, []);

    const options = [
        { label: "Option 1", value: "option1" },
        { label: "Option 2", value: "option2" },
        { label: "Option 3", value: "option3" },
    ];

    const [selected, setSelected] = useState([]);

    const toggleSelection = (value) => {
        setSelected((prev) =>
            prev.includes(value) ? prev.filter((v) => v !== value) : [...prev, value]
        );
    };

    const [cardView, setCardView] = useState(true);

    const [isCollapsed, setIsCollapsed] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [courseForModal, setCourseForModal] = useState('');

    function handleTableRowClick(course) {
        setCourseForModal(course);
        setIsModalOpen(true);
    }

    return (
        <div>
            <div className="flex justify-between">
                <h1 className="font-semibold text-xl mb-4 ">Find Courses</h1>
                <Button variant="icon" onClick={() => setCardView(!cardView)}>
                    {cardView ? <Rows2/> : <Rows3/>}
                </Button>
            </div>

            {/* Search/Filters Card */}
            <div className="bg-background relative p-4 rounded-lg shadow z-0">

                {/* Search Row */}
                <div className="flex justify-between mb-4 gap-2 sm:flex-row flex-col">

                    {/* Search Bar */}
                    <div className="relative w-full">
                        <Input id="search" placeholder="Search for classes..." className="pl-10" />
                        <span className="absolute inset-y-0 left-0 flex items-center pl-3">
                            <SearchIcon className="h-5 w-5 text-gray-400" />
                        </span>
                    </div>

                    <div className="flex justify-between gap-2">

                        {/* Semester Picker */}
                        {selectedSemesters.map((semester, index) => (
                            <Select
                                value={semester}
                                onValueChange={(value) => handleSemesterChange(index, value)}
                            >
                                <SelectTrigger className="sm:w-auto w-full">
                                    <SelectValue placeholder="Select Semester" />
                                </SelectTrigger>
                                <SelectContent>
                                    {availableSemesters
                                        .filter(
                                            // Filter out already selected minors and the current minor
                                            s => !selectedSemesters.includes(s) || s === semester
                                        )
                                        .map((s) => (
                                            <SelectItem key={s} value={s}>
                                                {s}
                                            </SelectItem>
                                        ))
                                    }
                                </SelectContent>
                            </Select>
                        ))}

                        {/* Search Button - get rid of this if we can do live search results*/}
                        <Button className="px-6">Search</Button>
                    </div>
                </div>

                {!isCollapsed ? (
                    <>
                        {/* Input Filters */}
                        <div className="flex gap-2 mb-4 items-center flex-wrap">
                            <h3 className="font-medium text-xs mr-2 text-slate-500">FILTERS</h3>
                            {/* Course Code */}
                            <div className="flex items-center rounded-lg bg-input shadow-xs">
                                <div className=" text-slate-500 font-medium text-xs px-2">CODE</div>
                                <Input
                                    className="h-8 shadow-none rounded-l-none max-w-36"
                                    placeholder='"HUMA 200"'
                                />
                            </div>

                            {/* Days */}
                            <div className="flex items-center rounded-lg bg-input shadow-xs">
                                <div className=" text-slate-500 font-medium text-xs px-2">DAYS</div>
                                <Popover>
                                    <PopoverTrigger asChild>
                                        <Button variant="outline" className="h-8 flex justify-between shadow-none rounded-l-none">
                                            {selected.length > 0 ? (
                                                options.filter((opt) => selected.includes(opt.value)).map((opt) => opt.label).join(", ")
                                            ) : (
                                                <span className="text-slate-500 font-normal">Select options</span>
                                            )}
                                            <ChevronDown className="w-4 h-4 ml-2 opacity-25" />
                                        </Button>
                                    </PopoverTrigger>
                                    <PopoverContent className="w-[200px] p-0 ">
                                        <Command>
                                            <CommandGroup>
                                                {options.map((option) => (
                                                    <CommandItem
                                                        key={option.value}
                                                        onSelect={() => toggleSelection(option.value)}
                                                        className="flex items-center justify-between"
                                                    >
                                                        {option.label}
                                                        {selected.includes(option.value) && <Check className="w-4 h-4 text-primary" />}
                                                    </CommandItem>
                                                ))}
                                            </CommandGroup>
                                        </Command>
                                    </PopoverContent>
                                </Popover>
                            </div>

                            {/* Time */}
                            <div className="h-8 flex items-center rounded-lg bg-input shadow-xs">
                                <div className=" text-slate-500 font-medium text-xs px-2">TIME</div>
                                <Select
                                    value={startTimeFilter}
                                    onValueChange={(value) => setStartTimeFilter(value)}
                                >
                                    <SelectTrigger className="max-h-8 shadow-none rounded-none">
                                        <SelectValue placeholder="--:--" />
                                    </SelectTrigger>
                                    <SelectContent>
                                        <SelectItem value="freshman">Freshman</SelectItem>
                                        <SelectItem value="sophomore">Sophomore</SelectItem>
                                        <SelectItem value="junior">Junior</SelectItem>
                                        <SelectItem value="senior">Senior</SelectItem>
                                    </SelectContent>
                                </Select>
                                <div className=" text-slate-500 font-medium text-xs px-2">to</div>
                                <Select
                                    value={endTimeFilter}
                                    onValueChange={(value) => setEndTimeFilter(value)}
                                >
                                    <SelectTrigger className={`max-h-8 shadow-none ${startTimeFilter === '' && endTimeFilter === '' ? 'rounded-l-none' : 'rounded-none'}`}>
                                        <SelectValue placeholder="--:--" />
                                    </SelectTrigger>
                                    <SelectContent >
                                        <SelectItem value="freshman">Freshman</SelectItem>
                                        <SelectItem value="sophomore">Sophomore</SelectItem>
                                        <SelectItem value="junior">Junior</SelectItem>
                                        <SelectItem value="senior">Senior</SelectItem>
                                    </SelectContent>
                                </Select>
                                {startTimeFilter !== '' || endTimeFilter !== '' && (
                                    <div className=" text-slate-500 font-medium text-xs px-2"><X onClick={clearTimeFilters} size={14} /></div>
                                )}
                            </div>


                        </div>

                        {/* Switch Filters */}
                        <div className="mb-2 mr-6 flex gap-4 flex-wrap ">
                            <div className="flex items-center space-x-2 min-w-fit">
                                <Switch id="required-switch" />
                                <Label htmlFor="required-switch" className="font-normal">Required for me</Label>
                            </div>
                            <div className="flex items-center space-x-2 min-w-fit">
                                <Switch id="hide-completed-switch" />
                                <Label htmlFor="hide-completed" className="font-normal">Hide complete classes</Label>
                            </div>
                            <div className="flex items-center space-x-2 min-w-fit">
                                <Switch id="hide-full-switch" />
                                <Label htmlFor="hide-full-switch" className="font-normal">Hide full classes</Label>
                            </div>
                        </div>
                    </>
                ) : (
                    <h3 className="font-medium text-xs mr-2 text-slate-500 cursor-pointer" onClick={() => setIsCollapsed(!isCollapsed)}>FILTERS</h3>
                )}

                <ChevronDownIcon
                    className={`absolute bottom-4 right-4 size-4 text-slate-500 cursor-pointer transition-transform duration-300 ease-in-out ${!isCollapsed ? 'rotate-180' : ''}`}
                    onClick={() => setIsCollapsed(!isCollapsed)}
                />

            </div>

            <Separator className="my-4" />

            {cardView ? (
                // Card view
                <div className="flex flex-col gap-3">
                    {courses.map((course, index) => (
                        <CourseCard key={index} course={course}/>
                    ))}
                </div>
            ):(
                // Table View
                <div className="bg-background mt-6 rounded-lg shadow">
                <Table>
                <TableHeader>
                    <TableRow className="hover:bg-background">
                        <TableHead>Code</TableHead>
                        <TableHead>Name</TableHead>
                        <TableHead>Faculty</TableHead>
                        <TableHead className="w-[100px]">Seats Open</TableHead>
                        <TableHead>Credits</TableHead>
                        <TableHead>Location</TableHead>
                        <TableHead>Time</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {courses.map((course, index) => (
                        <TableRow key={index} className="cursor-pointer hover:shadow-sm" onClick={() => handleTableRowClick(course) }>
                            <TableCell>{course.abbreviation} {course.courseNum} {course.section}</TableCell>
                            <TableCell>{course.name}</TableCell>
                            <TableCell>{course.faculty}</TableCell>
                            <TableCell>{course.openSeats}/{course.totalSeats}</TableCell>
                            <TableCell>{course.credits}</TableCell>
                            <TableCell>{course.location}</TableCell>
                            <TableCell>{course.times}</TableCell>
                        </TableRow>

                    ))}
                </TableBody>
            </Table>
            </div>
            )}



            <CourseModal isOpen={isModalOpen} onClose={()=> setIsModalOpen(false)} course={courseForModal}/>


            <p className="mt-6">Message from backend: {message}</p>
        </div>
    );
}

export default FindCourses;
