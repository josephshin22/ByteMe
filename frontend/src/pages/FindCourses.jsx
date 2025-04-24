import {useEffect, useRef, useState} from "react";
import api from "../api.js";
import CourseCard from "../components/CourseCard";
import {Input} from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import {Button} from "@/components/ui/button";
import {
    Bookmark,
    Check,
    ChevronDown,
    ChevronDownIcon, ChevronLeft, ChevronRight, PlusCircle, Rows2, Rows3,
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
import {
    Pagination,
    PaginationContent, PaginationEllipsis,
    PaginationItem,
    PaginationLink
} from "@/components/ui/pagination.jsx";
import {formatCourseTimes} from "@/utils/formatCourseTimes.jsx";
import {saveCourse} from "@/utils/saveCourse.jsx";

function FindCourses() {

    const availableSemesters = ['2023_Fall', '2023_Winter_Online', '2024_Spring', '2024_Early_Summer', '2024_Fall', '2025_Spring'];
    const [selectedSemester, setSelectedSemester] = useState('2025_Spring');

    const [startTimeFilter, setStartTimeFilter] = useState('');
    const [endTimeFilter, setEndTimeFilter] = useState('');
    const [courses, setCourses] = useState([]);
    const [page, setPage] = useState(1);
    const [coursesPerPage, setCoursesPerPage] = useState(10);
    const [totalPages, setTotalPages] = useState(1);

    const [pageButtonClicked, setPageButtonClicked] = useState(false);
    const handleSemesterChange = (value) => {
        setSelectedSemester(value);
        setPage(1);
        // console.log("Selected semester:", value);
    };
    const [searchInput, setSearchInput] = useState('');
    const [code, setCode] = useState('');
    const [day1, setDay1] = useState('');
    const [day2, setDay2] = useState('');
    const [day3, setDay3] = useState('');
    const [day4, setDay4] = useState('');
    const [day5, setDay5] = useState('');
    const [hideFullCourses, setHideFullCourses] = useState(false); // State to control showing full courses
    const [credits, setCredits] = useState('');
    const [filteredCourses, setFilteredCourses] = useState([]); // Filtered courses

    const clearTimeFilters = () => {
        setStartTimeFilter('');
        setEndTimeFilter('');
    }

    const coursesRef = useRef(null);

    useEffect(() => {
        // const endpoint = searchInput
        //     ? `/search-courses?semester=${selectedSemester}&searchTerm=${encodeURIComponent(searchInput)}&page=${page}&limit=${coursesPerPage}&code=${code}&day1=${day1}&day2=${day2}&day3=${day3}&day4=${day4}&day5=${day5}&startTime=${startTimeFilter}&endTime=${endTimeFilter}&hideFullCourses=${hideFullCourses}`
        //     : `/courses?page=${page}&limit=${coursesPerPage}&semester=${selectedSemester}`;
        const endpoint = `/search-courses?semester=${selectedSemester}&searchTerm=${encodeURIComponent(searchInput)}&page=${page}&limit=${coursesPerPage}&code=${code}&day1=${day1}&day2=${day2}&day3=${day3}&day4=${day4}&day5=${day5}&startTime=${startTimeFilter}&endTime=${endTimeFilter}&hideFullCourses=${hideFullCourses}&credits=${credits}`

        api.get(endpoint)
            .then((res) => {
                setCourses(res.data.courses);
                setFilteredCourses(res.data.courses);
                setTotalPages(res.data.totalPages);
                if (coursesRef.current && pageButtonClicked) {
                    const offset = 80;
                    const topPosition = coursesRef.current.getBoundingClientRect().top + window.pageYOffset - offset;
                    window.scrollTo({ top: topPosition, behavior: 'smooth' });
                    setPageButtonClicked(false);
                }
            })
            .catch((err) => console.error("Error fetching courses:", err));
    }, [searchInput, page, selectedSemester, code, day1, day2, day3, day4, day5, hideFullCourses, startTimeFilter, endTimeFilter, credits]);
    // console.log("Courses:", courses);

    const options = [
        {label: "Monday", value: "M"},
        { label: "Tuesday", value: "T" },
        { label: "Wednesday", value: "W" },
        { label: "Thursday", value: "R" },
        {label: "Friday", value: "F"},
    ];

    const [selected, setSelected] = useState([]);

    const toggleSelection = (value) => {
        // setSelected((prev) =>
        //     prev.includes(value) ? prev.filter((v) => v !== value) : [...prev, value]
        // );
        setSelected((prev) => {
            if (prev.includes(value)) {
                // Deselect the value
                const updated = prev.filter((v) => v !== value);

                // Reset the corresponding day state
                if (day1 === value) setDay1('');
                else if (day2 === value) setDay2('');
                else if (day3 === value) setDay3('');
                else if (day4 === value) setDay4('');
                else if (day5 === value) setDay5('');

                return updated;
            } else {
                // Select the value
                const updated = [...prev, value];

                // Set the first empty day state
                if (!day1) setDay1(value);
                else if (!day2) setDay2(value);
                else if (!day3) setDay3(value);
                else if (!day4) setDay4(value);
                else if (!day5) setDay5(value);

                return updated;
            }
        });
    };

    const [cardView, setCardView] = useState(true);

    const [isCollapsed, setIsCollapsed] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [courseForModal, setCourseForModal] = useState('');

    const handleTimeChange = (value, setTime) => {
        // Allow typing any value
        setTime(value);
        // console.log("Updated state:", value);
        // Validate the input when it's complete
        // const timeRegex = /^([01]\d|2[0-3]):([0-5]\d)$/; // Matches "HH:mm" format
        // if (!timeRegex.test(value) && value.length === 5) {
        //     console.error("Invalid time format. Please use HH:mm.");
        // }
    };

    function handleTableRowClick(course) {
        setCourseForModal(course);
        setIsModalOpen(true);
    }

    function updateSearch(){
        setPage(1);
    }

    const [savedCourses, setSavedCourses] = useState([]);

    useEffect(() => {
        const saved = JSON.parse(localStorage.getItem("savedCourses")) || [];
        setSavedCourses(saved);
    }, []);

    function courseIsSaved(course) {
        return savedCourses.some(savedCourse => {
            const match =  savedCourse.subjCode === course.subjCode &&
                savedCourse.number === course.number &&
                savedCourse.section === course.section &&
                savedCourse.semester === course.semester;

            // console.log("match? ", match)
            return match;
        });
    }

    return (
        <div className="relative">
            <h1 className="font-semibold text-xl mb-4 ">Find Courses</h1>

            {/* Search/Filters Card */}
            <div className="bg-background relative p-4 rounded-lg shadow z-0">

                {/* Search Row */}
                <div className="flex justify-between mb-4 gap-2 sm:flex-row flex-col">

                    {/* Search Bar */}
                    <div className="relative w-full">
                        <Input id="search" placeholder="Search for courses..." className="pl-10" autoFocus
                               value={searchInput} onChange={(e)=> {setSearchInput(e.target.value);
                            console.log("Search Input:", e.target.value);}} //log current value
                        />
                        <span className="absolute inset-y-0 left-0 flex items-center pl-3">
                            <SearchIcon className="h-5 w-5 text-gray-400" />
                        </span>
                    </div>

                    <div className="flex justify-between gap-2">

                        {/* Semester Picker */}
                        <Select
                            value={selectedSemester}
                            onValueChange={(value) => handleSemesterChange(value)}
                        >
                            <SelectTrigger className="sm:w-auto w-full">
                                <SelectValue placeholder="Select Semester" />
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

                        {/* Search Button - get rid of this if we can do live search results*/}
                        {/*<Button className="px-6" onClick={() => setSearchInput(searchInput.trim())}>*/}
                        {/*    Search*/}
                        {/*</Button>*/}
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
                                    value={code}
                                    onChange={(e) => {
                                        setCode(e.target.value);
                                        updateSearch();
                                    }}
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
                                <Input
                                    className="h-8 shadow-none rounded-none max-w-28"
                                    placeholder="ex: '08:00'"
                                    value={startTimeFilter}
                                    onChange={(e) => handleTimeChange(e.target.value, setStartTimeFilter)}
                                />
                                <div className=" text-slate-500 font-medium text-xs px-2">to</div>
                                <Input
                                    className="h-8 shadow-none rounded-none rounded-r-lg max-w-28"
                                    placeholder="ex: '16:00'"
                                    value={endTimeFilter}
                                    onChange={(e) => handleTimeChange(e.target.value, setEndTimeFilter)}
                                />
                                {startTimeFilter !== '' || endTimeFilter !== '' && (
                                    <div className=" text-slate-500 font-medium text-xs px-2"><X onClick={clearTimeFilters} size={14} /></div>
                                )}
                            </div>
                            {/* Credits Filter */}
                            <div className="h-8 flex items-center rounded-lg bg-input shadow-xs">
                                <div className="text-slate-500 font-medium text-xs px-2">CREDITS</div>
                                <Input
                                    className="h-8 shadow-none rounded-none rounded-r-lg max-w-36"
                                    placeholder="ex: '3'"
                                    value={credits}
                                    onChange={(e) => setCredits(e.target.value)}
                                />
                            </div>

                        </div>

                        {/* Switch Filters */}
                        {/*<div className="mb-2 mr-6 flex gap-4 flex-wrap ">*/}
                        {/*    <div className="flex items-center space-x-2 min-w-fit">*/}
                        {/*        <Switch id="required-switch" />*/}
                        {/*        <Label htmlFor="required-switch" className="font-normal">Required for me</Label>*/}
                        {/*    </div>*/}
                        {/*    <div className="flex items-center space-x-2 min-w-fit">*/}
                        {/*        <Switch id="hide-completed-switch" />*/}
                        {/*        <Label htmlFor="hide-completed" className="font-normal">Hide completed classes</Label>*/}
                        {/*    </div>*/}
                        {/*    <div className="flex items-center space-x-2 min-w-fit">*/}
                        {/*        <Switch*/}
                        {/*            id="hide-full-switch"*/}
                        {/*            onCheckedChange={() => setHideFullCourses(!hideFullCourses)}*/}
                        {/*        />*/}
                        {/*        <Label htmlFor="hide-full-switch" className="font-normal">Hide full classes</Label>*/}
                        {/*    </div>*/}
                        {/*</div>*/}
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

            <div className="flex justify-between">
                <h2 className="font-medium text-lg text-">{selectedSemester.replaceAll("_", " ")} Results</h2>
                <Button variant="ghost" onClick={() => setCardView(!cardView)}>
                    {cardView ? <>Table <Rows3/></> : <>Cards <Rows2/></>}
                </Button>
            </div>

            <div ref={coursesRef} className="mt-2 mb-8">

                {cardView ? ( // Card view
                    <>
                        {/*{courses.length ? (*/}
                        {filteredCourses.length > 0 ? (
                            <div className="flex flex-col gap-3">
                                {/*{courses.map((course, index) => (*/}
                                {/*    <CourseCard key={index} course={course}/>*/}
                                {filteredCourses.map((course) => (
                                    <CourseCard key={`${course.subject || "N/A"}-${course.number || "N/A"}-${course.section || "N/A"}-${course.semester || "N/A"}`} course={course} isSaved={courseIsSaved(course)} />
                                ))}
                            </div>
                        ) : (
                            <div className="flex flex-col gap-3">
                                {/*<div className="h-20 flex items-center justify-center w-full rounded-lg text-sm animate-shine">*/}
                                {/*    Fetching courses...*/}
                                {/*</div>*/}

                                {courses.length > 0 ? (
                                    courses.map((course) => (
                                        <CourseCard key={`${course.subject || "N/A"}-${course.number || "N/A"}-${course.section || "N/A"}-${course.semester || "N/A"}`} course={course} isSaved={courseIsSaved(course)} />
                                    ))
                                ) : (
                                    // <div className="h-20 flex items-center justify-center w-full rounded-lg text-sm animate-shine">
                                    //     Fetching courses...
                                    // </div>
                                    <div className="h-20 flex items-center justify-center w-full rounded-lg text-sm text-slate-600 bg-slate-100">
                                        No courses found
                                    </div>
                                )}
                            </div>
                        )}
                    </>
                ):(
                    // Table View
                    <div className="bg-background rounded-lg shadow">
                        <Table>
                            <TableHeader>
                                <TableRow className="hover:bg-background">
                                    <TableHead>Code</TableHead>
                                    <TableHead>Name</TableHead>
                                    <TableHead>Faculty</TableHead>
                                    <TableHead>Seats</TableHead>
                                    <TableHead>Credits</TableHead>
                                    <TableHead>Location</TableHead>
                                    <TableHead>Time</TableHead>
                                </TableRow>
                            </TableHeader>
                            <TableBody>
                                {/*{courses.length ? courses.map((course, index) => (*/}
                                {/*    <TableRow key={index} className="cursor-pointer hover:shadow-sm" onClick={() => handleTableRowClick(course) }>*/}
                                {/*        <TableCell>{course.subjCode} {course.number} {course.section}</TableCell>*/}
                                {courses.length ? filteredCourses.map((course) => (
                                    <TableRow key={`${course.subject||"N/A"}-${course.number||"N/A"}-${course.section||"N/A"}-${course.semester || "N/A"}`} className="cursor-pointer hover:shadow-sm" onClick={() => handleTableRowClick(course) }>
                                        <TableCell>{course.subject} {course.number} {course.section}</TableCell>
                                        <TableCell>{course.name}</TableCell>
                                        <TableCell>
                                            {course.faculty.map((facultyMember, index) => (
                                                <div key={index}>{facultyMember}</div>
                                            ))}
                                        </TableCell>
                                        <TableCell>{course.openSeats}/{course.totalSeats}</TableCell>
                                        <TableCell>{course.credits}</TableCell>
                                        <TableCell>{course.location}</TableCell>
                                        <TableCell>{formatCourseTimes(course.times)}</TableCell>
                                        <TableCell onClick={(e) => { e.stopPropagation(); saveCourse(course); }}><Bookmark size={16}/></TableCell>
                                        <TableCell onClick={(e) => { e.stopPropagation(); console.log("add"); }}><PlusCircle size={16}/></TableCell>
                                    </TableRow>

                                )) : (
                                    <TableRow >
                                        <TableCell colSpan={7} className="text-center animate-shine bg-gray-200">Fetching courses...</TableCell>
                                    </TableRow>
                                )}
                            </TableBody>
                        </Table>
                    </div>
                )}

            </div>

            {/* Pages navigation menu */}
            <div className="fixed bottom-4 left-1/2 transform -translate-x-1/2 ">
                <Pagination className="bg-background p-1 max-w-fit rounded-lg shadow-md border border-slate-200" >
                <PaginationContent>
                    {/*<PaginationItem>*/}
                    {/*    <Button variant="ghost" size="icon" onClick={() => setPage(1)} disabled={page === 1}>*/}
                    {/*        <ChevronFirst/>*/}
                    {/*    </Button>*/}
                    {/*</PaginationItem>*/}
                    <PaginationItem>
                        <Button
                            variant="ghost"
                            size="icon"
                            onClick={() => {
                                setPage((prev) => Math.max(prev - 1, 1))
                                setPageButtonClicked(true);
                            }}
                            disabled={page === 1}
                        >
                            <ChevronLeft/>
                        </Button>
                    </PaginationItem>

                    {Array.from({ length: totalPages }, (_, i) => i + 1)
                        .filter((num) => {
                            return (
                                num === 1 ||
                                num === totalPages ||
                                (num >= page - 1 && num <= page + 1)
                            );
                        })
                        .reduce((acc, num, idx, arr) => {
                            if (idx > 0 && num - arr[idx - 1] > 1) {
                                acc.push("ellipsis");
                            }
                            acc.push(num);
                            return acc;
                        }, [])
                        .map((item, idx) =>
                            item === "ellipsis" ? (
                                <PaginationItem key={`ellipsis-${idx}`}>
                                    <PaginationEllipsis />
                                </PaginationItem>
                            ) : (
                                <PaginationItem key={item}>
                                    <PaginationLink
                                        href="#"
                                        isActive={item === page}
                                        onClick={(e) => {
                                            e.preventDefault();
                                            setPage(item);
                                            setPageButtonClicked(true);
                                        }}
                                    >
                                        {item}
                                    </PaginationLink>
                                </PaginationItem>
                            )
                        )}

                    <PaginationItem>
                        <Button
                            variant="ghost"
                            size="icon"
                            onClick={() => {
                                setPage((prev) => Math.min(prev + 1, totalPages))
                                setPageButtonClicked(true);
                            }}
                            disabled={page === totalPages}
                        >
                            <ChevronRight/>
                        </Button>
                    </PaginationItem>
                    {/*<PaginationItem>*/}
                    {/*    <Button variant="ghost" size="icon" onClick={() => setPage(totalPages)} disabled={page === totalPages}>*/}
                    {/*        <ChevronLast/>*/}
                    {/*    </Button>*/}
                    {/*</PaginationItem>*/}
                </PaginationContent>
            </Pagination>
            </div>


            <CourseModal isOpen={isModalOpen} onClose={()=> setIsModalOpen(false)} course={courseForModal}/>
        </div>
    );
}

export default FindCourses;
