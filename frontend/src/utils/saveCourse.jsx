export const saveCourse = (course) => {
    const saved = JSON.parse(localStorage.getItem("savedCourses")) || [];


    const isAlreadySaved = saved.some(
        (c) => c.subjCode === course.subjCode && c.number === course.number && c.section === course.section && c.semester === course.semester
    );


    if (!isAlreadySaved) {
        saved.push(course);
        localStorage.setItem("savedCourses", JSON.stringify(saved));
        console.log("Course saved:", course);
        return true;
    } else {
        const updatedCourses = saved.filter(
            (c) => !(c.subjCode === course.subjCode && c.number === course.number && c.section === course.section && c.semester === course.semester)
        );
        localStorage.setItem("savedCourses", JSON.stringify(updatedCourses));
        console.log("Course unsaved:", course);
        return false;
    }
};
