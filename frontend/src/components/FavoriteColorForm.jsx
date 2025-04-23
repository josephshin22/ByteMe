// src/components/FavoriteColorForm.jsx
import { useState } from "react";
import { collection, addDoc } from "firebase/firestore";
import { db } from "../firebase";
import {Input} from "@/components/ui/input.jsx";
import {Button} from "@/components/ui/button.jsx";
import {Label} from "@/components/ui/label.jsx";
import {Flame} from "lucide-react";

import { toast } from "sonner"


const FavoriteColorForm = () => {
    const [color, setColor] = useState("");
    const [submitted, setSubmitted] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!color.trim()) return;

        try {
            await addDoc(collection(db, "favoriteColors"), {
                color: color.trim(),
                timestamp: new Date()
            });
            toast("Favorite color updated");
            setSubmitted(true);
        } catch (err) {
            console.error("Error adding document:", err);
        }
    };

    return (
        <div className="grid gap-2">
            <Label>What's your favorite color? <span className="flex items-center gap-1 text-orange-400"><Flame size={15}/> Firebase connected</span></Label>
            <form onSubmit={handleSubmit} className="flex gap-2">
                <Input
                    type="text"
                    value={color}
                    onChange={(e) => setColor(e.target.value)}
                />
                <Button
                    type="submit"
                >
                    Submit
                </Button>
            </form>

            <Button
                variant="outline"
                onClick={() =>
                    toast("Event has been created", {
                        description: "Sunday, December 03, 2023 at 9:00 AM",
                        action: {
                            label: "Undo",
                            onClick: () => console.log("Undo"),
                        },
                    })
                }
            >
                Show Toast
            </Button>


        </div>
    );
};

export default FavoriteColorForm;
