export function formatCourseTimes(times) {
    const daysMap = {
        "M": "Monday",
        "T": "Tuesday",
        "W": "Wednesday",
        "R": "Thursday",
        "F": "Friday",
        "S": "Saturday",
        "U": "Sunday"
    };

    const dayGroups = times.reduce((acc, time) => {
        const key = `${time.start_time}-${time.end_time}`;
        if (!acc[key]) {
            acc[key] = [];
        }
        acc[key].push(time.day);
        return acc;
    }, {});

    return Object.entries(dayGroups).map(([time, days]) => {
        const formattedDays = days.join("");
        const [start, end] = time.split("-");
        const startTime = new Date(`1970-01-01T${start}Z`).toLocaleTimeString([], { hour: 'numeric', minute: '2-digit' });
        const endTime = new Date(`1970-01-01T${end}Z`).toLocaleTimeString([], { hour: 'numeric', minute: '2-digit' });
        return `${formattedDays} ${startTime}-${endTime}`;
    }).join(", ");
}