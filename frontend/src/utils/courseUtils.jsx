export const handleSave = (course) => {
    const saved = JSON.parse(localStorage.getItem("savedCourses")) || [];

    const isAlreadySaved = saved.some(
        (c) => c.subjCode === course.subjCode && c.number === course.number && c.section === course.section && c.semester === course.semester
    );

    if (!isAlreadySaved) {
        saved.push(course);
        localStorage.setItem("savedCourses", JSON.stringify(saved));
        console.log("Course saved:", course);
    } else {
        console.log("Course already saved");
    }
};