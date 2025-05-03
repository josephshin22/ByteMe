import { useCallback } from 'react';
import { toast } from "sonner";

// Define your function as a regular function
const handleAddCourse = async (course, scheduleId) => {
  if (scheduleId === "") {
    toast.error("Schedule not found");
    return; // Exit the function early
  }

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
      toast.error(errorData.error || "Unknown error occurred");
      throw new Error(errorData.error || "Unknown error occurred");
    }
    toast.success(`${course.fullCourseCode} added to New Schedule ${scheduleId}`);

    const data = await response.json();
    console.log(`course added successfully:`, data.message);
  } catch (error) {
    console.error("Failed to add course:", error);
    // toast.error("Failed to add course");
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
      toast.error(errorData.error || "Unknown error occurred");
      throw new Error(errorData.error || "Unknown error occurred");
    }

    const data = await response.json();
    console.log("Course removed successfully:", data.message);
  } catch (error) {
    console.error("Failed to remove course:", error);
    toast.error("Failed to remove course");
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