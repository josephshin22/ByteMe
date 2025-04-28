import { useCallback } from 'react';

// Define your function as a regular function
const handleAddCourse = async (course, scheduleId) => {
  try {
    console.log("Adding course: " + course.id + " with schedule ID: " + scheduleId);

    const response = await fetch(`http://localhost:7000/api/user-courses?courseId=${course.id}&scheduleId=${scheduleId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.error || "Unknown error occurred");
    }

    const data = await response.json();
    console.log("Course added successfully:", data.message);
  } catch (error) {
    console.error("Failed to add course:", error);
  }
};

const handleRemoveCourse = async (course, scheduleId) => {
  try {
    console.log("Removing course: " + course.id + " with schedule ID: " + scheduleId);

    const response = await fetch(`http://localhost:7000/api/user-courses?courseId=${course.id}&scheduleId=${scheduleId}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.error || "Unknown error occurred");
    }

    const data = await response.json();
    console.log("Course removed successfully:", data.message);
  } catch (error) {
    console.error("Failed to remove course:", error);
  }
}

// Create your hook that returns the functions
const useCourseActions = () => {
  // Wrap functions with useCallback if needed
  const addCourse = useCallback((course) => handleAddCourse(course), []);

  // Return all the functions you need
  return {
    addCourse
    // other functions...
  };
};

// Export both the hook and the standalone function
export { handleAddCourse, handleRemoveCourse };
export default useCourseActions;