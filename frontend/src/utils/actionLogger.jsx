const ACTION_LOG_KEY = "scheduleActionLog";

export const logScheduleAction = (type, course) => {
    const log = JSON.parse(localStorage.getItem(ACTION_LOG_KEY)) || [];

    log.push({
        type, // "add" or "remove"
        course,
        timestamp: new Date().toISOString(),
    });

    localStorage.setItem(ACTION_LOG_KEY, JSON.stringify(log));
};

export const getActionLog = () => {
    return JSON.parse(localStorage.getItem(ACTION_LOG_KEY)) || [];
};

export const clearActionLog = () => {
    localStorage.removeItem(ACTION_LOG_KEY);
};

const saveCourses = (updatedCourses) => {
    setCourses(updatedCourses);
    localStorage.setItem("savedCourses", JSON.stringify(updatedCourses));
};
