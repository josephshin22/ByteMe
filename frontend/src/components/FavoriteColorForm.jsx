// src/components/FavoriteColorForm.jsx
import { useState, useEffect } from "react";
import { collection, addDoc, query, orderBy, limit, getDocs } from "firebase/firestore";
import { db } from "../firebase";
import {Input} from "@/components/ui/input.jsx";
import {Button} from "@/components/ui/button.jsx";
import {Label} from "@/components/ui/label.jsx";
import {Check, Flame} from "lucide-react";


const FavoriteColorForm = () => {
    const [color, setColor] = useState("");
    const [submitted, setSubmitted] = useState(false);

    const [favoriteColor, setFavoriteColor] = useState("");

    const [message, setMessage] = useState("");

    const fetchFavoriteColor = async () => {
        try {
            const q = query(collection(db, "favoriteColors"), orderBy("timestamp", "desc"), limit(1));
            const querySnapshot = await getDocs(q);
            if (!querySnapshot.empty) {
                const latestColor = querySnapshot.docs[0].data().color;
                setFavoriteColor(latestColor);
            }
        } catch (err) {
            console.error("Error fetching favorite color:", err);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!color.trim()) return;

        try {
            setColor("");
            await addDoc(collection(db, "favoriteColors"), {
                color: color.trim(),
                timestamp: new Date()
            });
            setSubmitted(true);
            setMessage("Submitted!");
            setTimeout(() => setMessage(""), 1200);
            fetchFavoriteColor();
        } catch (err) {
            console.error("Error adding document:", err);
        }
    };

    useEffect(() => {
        fetchFavoriteColor();
    }, [submitted]);

    return (
        <div className="grid gap-2 border border-slate-300 p-4 rounded-md ">
            <span className="flex items-center gap-1 text-orange-400 text-sm font-medium"><Flame size={15}/> Firebase connected</span>

            <Label>What's your favorite color?
                {message && (
                    <span className="flex items-center gap-1 text-green-500"><Check size={15}/> Submitted</span>
                )}
            </Label>
            <form onSubmit={handleSubmit} className="flex gap-2">
                <Input
                    type="text"
                    value={color}
                    placeholder="Color"
                    onChange={(e) => setColor(e.target.value)}
                />
                <Button
                    type="submit"
                >
                    Submit
                </Button>

            </form>

            <div className="mt-4 grid gap-2">
                <Label>Your Favorite Color Is:</Label>
                <p>{favoriteColor ? `"${favoriteColor}"` : <span className="text-orange-400">fetching from firebase...</span>}</p>
            </div>
        </div>
    );
};

export default FavoriteColorForm;
