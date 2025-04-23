export function saveCourse(course) {
    return fetch("/api/saved", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(course),
    })
    .then((response) => {
        if (!response.ok) {
            throw new Error("Failed to save course");
        }
        return response.json();
    })
    .catch((error) => {
        console.error("Error saving course:", error);
    });
}