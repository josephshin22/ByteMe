import { useState } from "react";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { Label } from "@/components/ui/label"
import {Minus, Plus} from "lucide-react";

export default function Profile() {
    const availableMajors = [
        'Computer Science',
        'Accounting',
        'Business Administration',
        'Economics',
        'Mathematics',
        'Engineering',
        'Psychology',
        'Biology',
        'Chemistry',
        'Finance'
    ];
    const availableMinors = [
        'Computer Science',
        'Accounting',
        'Business Administration',
        'Economics',
        'Mathematics',
        'Design',
        'Music',
        'Art',
    ];

    // State to track selected majors/minors
    const [selectedMajors, setSelectedMajors] = useState(['Computer Science']);
    const [selectedMinors, setSelectedMinors] = useState([]);

    // Function to update a specific major/minor selection
    const handleMajorChange = (index, value) => {
        const newSelectedMajors = [...selectedMajors];
        newSelectedMajors[index] = value;
        setSelectedMajors(newSelectedMajors);
    };
    const handleMinorChange = (index, value) => {
        const newSelectedMinors = [...selectedMinors];
        newSelectedMinors[index] = value;
        setSelectedMinors(newSelectedMinors);
    };

    // Function to add a new major selection input
    const addMajorInput = () => {
        if (selectedMajors.length < availableMajors.length) {
            setSelectedMajors([...selectedMajors, '']);
        }
    };
    const addMinorInput = () => {
        if (selectedMinors.length < availableMinors.length) {
            setSelectedMinors([...selectedMinors, '']);
        }
    };

    // Function to remove a major selection input
    const removeMajorInput = (index) => {
        const newSelectedMajors = selectedMajors.filter((_, i) => i !== index);
        setSelectedMajors(newSelectedMajors);
    };
    const removeMinorInput = (index) => {
        const newSelectedMinors = selectedMinors.filter((_, i) => i !== index);
        setSelectedMinors(newSelectedMinors);
    };

    return (
        <div>
            <h1 className="text-2xl font-semibold mb-8 hover:text-blue-500">Username's Profile</h1>

            <Separator className="my-8" />

            {/* User Info */}
            <div className="flex gap-8">

                <div className="w-30">
                    <h2 className="text-lg">User Info</h2>
                </div>

                <div className="w-full grid gap-6">

                    {/* Username */}
                    <div className="grid w-full max-w-sm items-center gap-2 mt-2">
                        <Label htmlFor="username">Username</Label>
                        <Input id="username" placeholder="Username" className="max-w-sm" />
                    </div>

                    {/* Email */}
                    <div className="grid w-full max-w-sm items-center gap-2">
                        <Label htmlFor="email">Email</Label>
                        <Input type="email" id="email" placeholder="Email" className="max-w-sm" />
                    </div>

                </div>
            </div>

            <Separator className="my-8" />

            {/* Academics */}
            <div className="flex gap-8 ">

                <div className="w-30">
                    <h2 className="text-lg">Academics</h2>
                </div>

                <div className="w-full grid gap-6">

                    {/* Year */}
                    <div className="grid w-full max-w-sm items-center gap-2 mt-2">
                        <Label>Year</Label>
                        <Select>
                            <SelectTrigger>
                                <SelectValue placeholder="Select year" />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="freshman">Freshman</SelectItem>
                                <SelectItem value="sophomore">Sophomore</SelectItem>
                                <SelectItem value="junior">Junior</SelectItem>
                                <SelectItem value="senior">Senior</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>

                    <div className="flex gap-8 items-start max-w-3xl">
                        {/* Majors */}
                        <div className="grid w-full items-center gap-2">
                            <Label className="text-lg font-semibold">Major(s)</Label>
                            {selectedMajors.map((major, index) => (
                                <div key={index} className="flex items-center space-x-2">
                                    <Select
                                        value={major}
                                        onValueChange={(value) => handleMajorChange(index, value)}
                                    >
                                        <SelectTrigger className="w-56">
                                            <SelectValue placeholder="Select Major" />
                                        </SelectTrigger>
                                        <SelectContent>
                                            {availableMajors
                                                .filter(
                                                    // Filter out already selected majors and the current major
                                                    m => !selectedMajors.includes(m) || m === major
                                                )
                                                .map((m) => (
                                                    <SelectItem key={m} value={m}>
                                                        {m}
                                                    </SelectItem>
                                                ))
                                            }
                                        </SelectContent>
                                    </Select>

                                    {/* Remove button for each input except the first */}
                                    {index > 0 && (
                                        <Button
                                            variant="outline"
                                            size="icon"
                                            onClick={() => removeMajorInput(index)}
                                        >
                                            <Minus />
                                        </Button>
                                    )}
                                </div>
                            ))}

                            {/* Add Major button (only show if less than 4 majors are selected) */}
                            {selectedMajors.length < 4 && (
                                <Button
                                    onClick={addMajorInput}
                                    disabled={selectedMajors.some(major => major === '')}
                                    className="w-fit"
                                >
                                    <Plus/> Add Major
                                </Button>
                            )}
                        </div>

                        {/* Minors */}
                        <div className="grid w-full items-center gap-2">
                            <Label className="text-lg font-semibold">Minor(s)</Label>
                            {selectedMinors.map((minor, index) => (
                                <div key={index} className="flex items-center space-x-2">
                                    <Select
                                        value={minor}
                                        onValueChange={(value) => handleMinorChange(index, value)}
                                    >
                                        <SelectTrigger className="w-56">
                                            <SelectValue placeholder="Select Minor" />
                                        </SelectTrigger>
                                        <SelectContent>
                                            {availableMinors
                                                .filter(
                                                    // Filter out already selected minors and the current minor
                                                    m => !selectedMinors.includes(m) || m === minor
                                                )
                                                .map((m) => (
                                                    <SelectItem key={m} value={m}>
                                                        {m}
                                                    </SelectItem>
                                                ))
                                            }
                                        </SelectContent>
                                    </Select>

                                    {/* Remove button for each input except the first */}
                                    {/*{index > 0 && (*/}
                                        <Button
                                            variant="outline"
                                            size="icon"
                                            onClick={() => removeMinorInput(index)}
                                        >
                                            <Minus />
                                        </Button>
                                    {/*)}*/}
                                </div>
                            ))}

                            {/* Add Minor button (only show if less than 4 minors are selected) */}
                            {selectedMinors.length < 4 && (
                                <Button
                                    onClick={addMinorInput}
                                    disabled={selectedMinors.some(minor => minor === '')}
                                    className="w-fit"
                                >
                                    <Plus/> Add Minor
                                </Button>
                            )}
                        </div>
                    </div>
                </div>
            </div>

            <Separator className="my-8" />

            {/* Classes */}
            <div className="flex gap-8">

                <div className="w-30">
                    <h2 className="text-lg">Classes</h2>
                </div>

                <div className="w-full grid gap-6">
                    <div className="flex gap-8 items-start max-w-3xl">

                        <div className="space-y-3 w-full">
                            <h3 className="text-lg font-semibold">Major Classes</h3>

                            <div className="space-y-6">
                                {selectedMajors.map((major, index) => (
                                    major && (
                                        <div key={index} className="space-y-2">
                                            <p className="text-sm font-semibold">{major}</p>
                                            <Button>Select completed classes</Button>
                                        </div>
                                    )
                                ))}
                            </div>
                        </div>

                        <div className="space-y-3 w-full">
                            <h3 className="text-lg font-semibold">Minor Classes</h3>

                            {selectedMinors.length === 1 && selectedMinors[0] === '' ? (
                                <div className="text-slate-400">No minors selected</div>
                            ) : (
                                <div className="space-y-6">
                                    {selectedMinors.map((minor, index) => (
                                        minor && (
                                            <div key={index} className="space-y-2">
                                                <p className="text-sm font-semibold">{minor}</p>
                                                <Button>Select completed classes</Button>
                                            </div>
                                        )
                                    ))}
                                </div>
                            )}

                        </div>




                    </div>


                </div>
            </div>


        </div>
    );
}
