import java.util.ArrayList;
import java.util.Scanner;

public class Search {

    ArrayList<Course> results;
    ArrayList<Course> masterList;
    Filter searchFilter;

    public Search() {

    }
    public ArrayList<Course> conductSearchLoop(ArrayList<Course> masterList, Filter searchFilter) {
       boolean loop = true;
        while(loop) {
            System.out.println("Welcome to course search!");
            System.out.println("Type 'exit' to exit. Type f to modify filters, r to reset filters, or s to search.");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("exit")){
                loop = false;
            } else if (input.equalsIgnoreCase("f")) {
                while(true) {
                    System.out.println(searchFilter);
                    System.out.println("Modify filters. Type 0 to exit filter modification.");
                    System.out.println("Type the number of the filter you want to modify:");

                    int numInput = scanner.nextInt();
                    if(numInput == 1){
                        while(true) {
                            System.out.println("Enter course code.");
                            System.out.println("You can enter a full course code (e.g., HUMA 200 B), course code no section (e. g. HUMA 200), or just the subject code (e.g., HUMA):");
                            String courseCode = scanner.next();
                            if(courseCode.matches("[A-Z]{4} \\d{3}")|| courseCode.matches("[A-Z]{4}")||courseCode.matches("[A-Z]{4} \\d{3} [A-Z]{1}")) {
                                searchFilter.setCourseCode(courseCode);
                                break; // Exit the inner loop if valid input
                            } else {
                                System.out.println("Invalid course code format. Please try again.");
                            }
                        }
                    }

                    else if(numInput == 2){
                        // Modify the second filter
                        while(true)
                        {

                            System.out.println("Show full courses? (true/false):");
                            input = scanner.next();
                            if(input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
                                searchFilter.setFull(Boolean.parseBoolean(input));
                                break; // Exit the inner loop if valid input
                            } else {
                                System.out.println("Invalid input. Please enter 'true' or 'false'.");
                            }

                        }

                    }
                    else if(numInput == 3){
                        // Modify the third filter
                        while (true) {
                            System.out.println("Enter week days (e.g., M, T, W, R, F) separated by spaces:");
                            input = scanner.next();
                            String[] days = input.split(" ");
                            ArrayList<String> weekDays = new ArrayList<>();
                            for (String day : days) {
                                if (day.matches("[MTWRF]")) {
                                    weekDays.add(day);

                                } else {
                                    System.out.println("Invalid day: " + day + ". Please enter valid days (M, T, W, R, F).");
                                    continue;
                                }
                            }
                            searchFilter.setWeekDays(weekDays);
                            break;
                        }
                    }
                    else if(numInput == 4){
                        // Modify the fourth filter
                        while (true) {
                            System.out.println("Enter start time in HH:mm:ss format:");
                            input = scanner.next();
                            if (input.matches("\\d{2}:\\d{2}:\\d{2}")) {
                                searchFilter.setStartTime(input);
                                break; // Exit the inner loop if valid input
                            } else {
                                System.out.println("Invalid time format. Please enter in HH:mm:ss format.");
                            }
                        }
                    }
                    else if(numInput == 5){
                        // Modify the fifth filter
                        while (true) {
                            System.out.println("Enter end time in HH:mm:ss format:");
                            input = scanner.next();
                            if (input.matches("\\d{2}:\\d{2}:\\d{2}")) {
                                searchFilter.setEndTime(input);
                                break; // Exit the inner loop if valid input
                            } else {
                                System.out.println("Invalid time format. Please enter in HH:mm:ss format.");
                            }
                        }
                    }
                    else if(numInput == 6){
                        // Modify the sixth filter
                        while (true) {
                            System.out.println("Enter number of credits (integer):");
                            input = scanner.next();
                            if (input.matches("\\d+")) {
                                searchFilter.setCredits(Integer.parseInt(input));
                                break; // Exit the inner loop if valid input
                            } else {
                                System.out.println("Invalid input. Please enter an integer for credits.");
                            }
                        }
                    }
                    else if (numInput == 0) {
                        System.out.println("Exiting filter modification.");
                        break; // Exit the filter modification loop
                    }
                    else
                    {
                        System.out.println("Invalid input. Please try again.");
                    }
                }

                // Add code to modify filters here

            } else if (input.equalsIgnoreCase("r")) {
                searchFilter = new Filter(); // Reset the filter
                System.out.println("Filters reset.");
            } else if (input.equalsIgnoreCase("s")) {
                results = singleSearch();
                if(results != null) {
                    for(Course c : results) {
                       c.showCourse();
                    }
                } else {
                    System.out.println("No courses found matching the criteria.");
                }
            } else {
                System.out.println("Invalid input. Please try again.");
            }

        }
        return null;
    }
    public ArrayList<Course> singleSearch() {
        results = new ArrayList<Course>();

        System.out.println("Welcome to course search!");
        System.out.println("Press enter to only search based on filters.");
        System.out.println("Or, type a course name or a subsection of one to search for a specific course (e.g. 'psych' or 'psychology':");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        return null;

    }
    public boolean checkCourse(Course c, String input)
    {
        if(!input.trim().isEmpty())
        {
            if(!c.getName().toLowerCase().contains(input.toLowerCase()))
            {
                return false;
            }
        }
        if(searchFilter.getCourseCode() != null && !searchFilter.getCourseCode().trim().isEmpty())
        {
            //"[A-Z]{4} \\d{3}")|| courseCode.matches("[A-Z]{4}")||courseCode.matches("[A-Z]{4} \\d{3} [A-Z]{1}")
           if(searchFilter.getCourseCode().matches("[A-Z]{4} \\d{3} [A-Z]{1}"))
            {
               
            }
        }




        return true;

    }
}
