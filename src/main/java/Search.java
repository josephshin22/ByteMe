import java.util.ArrayList;
import java.util.Scanner;

public class Search {

    ArrayList<Course> results;
    ArrayList<Course> masterList;
    Filter searchFilter;

    public Search() {
        searchFilter = new Filter();
    }
    public ArrayList<Course> conductSearchLoop(ArrayList<Course> masterList) {
        this.masterList = masterList;
        boolean loop = true;
        while(loop) {
            System.out.println("Welcome to course search!");
            System.out.println("Type 'exit' to exit. Type f to modify filters, r to reset filters, or s to search.");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("exit")){
                loop = false;
            }
            else if (input.equalsIgnoreCase("f"))
            {
                while(true) {
                    System.out.println(searchFilter);
                    System.out.println("Modify filters. Type 0 to exit filter modification.");
                    System.out.println("Type the number of the filter you want to modify:");

                    int numInput;
                    try {
                        numInput = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        continue;
                    }
                    if(numInput == 1){
                        while(true) {
                            scanner = new Scanner(System.in); // Reset scanner to avoid input issues
                            System.out.println("Enter course code.");
                            System.out.println("You can enter a full course code (e.g., HUMA 200 B), course code with no section (e. g. HUMA 200), or just the subject code (e.g., HUMA):");
                            String courseCode = scanner.nextLine();
                            courseCode = courseCode.toUpperCase(); // Convert to uppercase for consistency
                            if(courseCode.matches("[A-Z]{4}")||courseCode.matches("[A-Z]{4} \\d{3}")||courseCode.matches("[A-Z]{4} \\d{3} [A-Z]")) {

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
                            scanner = new Scanner(System.in); // Reset scanner to avoid input issues
                            input = scanner.nextLine();
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
                            System.out.println("Enter week days (e.g., M, T, W, R, F) :");
                            scanner = new Scanner(System.in); // Reset scanner to avoid input issues
                            input = scanner.nextLine();
                            input = input.trim(); // Remove leading and trailing whitespace
                            String[] days = input.split("");
                            ArrayList<String> weekDays = new ArrayList<>();
                            for (String day : days) {
                                day = day.toUpperCase() ;
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
                            scanner = new Scanner(System.in); // Reset scanner to avoid input issues
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
                            scanner = new Scanner(System.in); // Reset scanner to avoid input issues
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
                            scanner = new Scanner(System.in); // Reset scanner to avoid input issues
                            input = scanner.next();
                            if (input.matches("\\d+")) {
                                searchFilter.setCredits(Integer.parseInt(input));
                                break; // Exit the inner loop if valid input
                            } else {
                                System.out.println("Invalid input. Please enter an integer for credits.");
                            }
                        }
                    }
                    else if(numInput == 7){
                        // Modify the seventh filter
                        while (true) {
                            System.out.println("Enter semester (e.g. 2024_Fall):");
                            scanner = new Scanner(System.in); // Reset scanner to avoid input issues
                            input = scanner.nextLine();
                            //verify format of input string
                            if (input.matches("\\d{4}_\\w+")) {
                                searchFilter.setSemester(input);
                                break; // Exit the inner loop if valid input
                            } else {
                                System.out.println("Invalid input. Please enter in the format: Year_Semester.");

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
                searchFilter = null;
                searchFilter = new Filter();
                System.out.println("Filters reset.");
            } else if (input.equalsIgnoreCase("s")) {

                results = null;
                singleSearch();
                if(!results.isEmpty()) {
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
    public void singleSearch() {
        results = new ArrayList<Course>();

        System.out.println("Welcome to course search!");
        System.out.println("Press enter to only search based on filters.");
        System.out.println("Or, type in input to search with along with filters");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        for(Course c: masterList) {
            if(checkCourse(c, input)) {
                results.add(c);
            }
        }
    }
    public boolean checkCourse(Course c, String input)
    {

        if (!searchFilter.getFull()) {
            if(!c.getIs_open()) {
                return false;
            }
        }

        if(searchFilter.getCredits() != c.getNumCredits() && searchFilter.getCredits() != 0)
        {
            return false;
        }

        if(!input.trim().isEmpty())
        {
            if(!c.getName().toLowerCase().contains(input.toLowerCase())&&
             !c.fullCourseCode().trim().toLowerCase().contains(input.trim().toLowerCase()))
            {
                return false;

            }
        }
        if(searchFilter.getCourseCode() != null && !searchFilter.getCourseCode().trim().isEmpty())
        {
            //"[A-Z]{4} \\d{3}")|| courseCode.matches("[A-Z]{4}")||courseCode.matches("[A-Z]{4} \\d{3} [A-Z]{1}")
           if(searchFilter.getCourseCode().matches("[A-Z]{4} \\d{3} [A-Z]"))
           {
               if(!c.getSubjCode().equalsIgnoreCase(searchFilter.getCourseCode().trim().substring(0, 4)) ||
                   !(c.getCourseNum() == Integer.parseInt(searchFilter.getCourseCode().trim().substring(5, 8)) )||
                  !c.getSection().equalsIgnoreCase(searchFilter.getCourseCode().trim().substring(9)))
               {
                   return false;
               }
           }
           else if(searchFilter.getCourseCode().matches("[A-Z]{4} \\d{3}"))
           {
               if(!c.getSubjCode().equalsIgnoreCase(searchFilter.getCourseCode().trim().substring(0, 4)) ||
                       !(c.getCourseNum() == Integer.parseInt(searchFilter.getCourseCode().trim().substring(5, 8))))
               {
                   return false;
               }
           }
           else if(searchFilter.getCourseCode().matches("[A-Z]{4}"))
           {
               if(!c.getSubjCode().equalsIgnoreCase(searchFilter.getCourseCode().trim().substring(0, 4)))
               {
                   return false;
               }
           }
           else{
               return false;
           }
        }
        if(searchFilter.getWeekDays() != null && !searchFilter.getWeekDays().isEmpty())
        {
            for(timeBlock time : c.getTimes()) {
                if(!searchFilter.getWeekDays().contains(time.getDay())) {
                    return false;
                }
            }
        }
        if(searchFilter.getStartTime() != null && !searchFilter.getStartTime().trim().isEmpty())
        {
            for(timeBlock time : c.getTimes()) {
                if(time.getStartTime().compareTo(searchFilter.getStartTime()) < 0) {
                    return false;
                }
            }
        }
        if(searchFilter.getEndTime() != null && !searchFilter.getEndTime().trim().isEmpty())
        {
            for(timeBlock time : c.getTimes()) {
                if(time.getEndTime().compareTo(searchFilter.getEndTime()) > 0) {
                    return false;
                }
            }
        }
        if(searchFilter.getSemester() != "") {
            if(!c.getSemester().equalsIgnoreCase(searchFilter.getSemester())) {
                return false;
            }
        }
        // If all filters pass, return true
        return true;
    }

}
