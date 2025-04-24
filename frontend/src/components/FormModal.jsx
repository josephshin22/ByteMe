import React from "react";
import {Armchair, Bookmark, Calendar, CircleDollarSign, Clock, PlusCircle, School, User, X} from "lucide-react";
import {Separator} from "@/components/ui/separator.jsx";
import {formatCourseTimes} from "@/utils/formatCourseTimes.jsx";
import {Button} from "@/components/ui/button.jsx";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.jsx";

const FormModal = ({ isOpen, onClose, onSubmit, dropdownOptions, title }) => {
    if (!isOpen) return null;

    const handleOutsideClick = (e) => {
        if (e.target.id === "form-modal-overlay") {
            onClose();
        }
    };

    return (
        <div id="form-modal-overlay" className="z-20 fixed inset-0 p-4 bg-black/50 flex flex-col items-center justify-center" onClick={handleOutsideClick}>
            <div className="bg-white p-6 rounded-lg shadow-lg min-w-sm">

                <h2 className="font-semibold text-lg mb-4">{title}</h2>

                <Select onValueChange={(value) => console.log(value)}>
                    <SelectTrigger className="w-full">
                        <SelectValue placeholder="Choose..." />
                    </SelectTrigger>
                    <SelectContent>
                        {dropdownOptions.map((semester) => (
                            <SelectItem key={semester} value={semester}>
                                {semester.replaceAll("_", " ")}
                            </SelectItem>
                        ))}
                    </SelectContent>
                </Select>

                <div className="flex gap-2 mt-4">
                    <Button
                        onClick={() => {
                            onClose();
                            onSubmit()
                        } }
                    >
                        Submit
                    </Button>
                    <Button
                        variant="ghost"
                        onClick={onClose}
                    >
                        Cancel
                    </Button>
                </div>
            </div>
        </div>
    );
};

export default FormModal;
