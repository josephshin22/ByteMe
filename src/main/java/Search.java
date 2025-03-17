import java.util.ArrayList;
import java.util.Scanner;

public class Search {

    ArrayList<Course> results;
    Filter searchFilter;

    public Search() {

    }
    public ArrayList<Course> conductSearchLoop(ArrayList<Course> masterList, Filter searchFilter) {
       boolean loop = true;
        while(loop) {
            System.out.println("Welcome to course search!");
            System.out.println(searchFilter);
            System.out.println("Type 'exit' to exit. Type f to modify filters, r to reset, or s to search.");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("exit")){
                loop = false;
            } else if (input.equalsIgnoreCase("f")) {
                System.out.println("Modify filters:");
                // Add code to modify filters here
            } else if (input.equalsIgnoreCase("r")) {
                searchFilter = new Filter(); // Reset the filter
                System.out.println("Filters reset.");
            } else if (input.equalsIgnoreCase("s")) {
                results = singleSearch(masterList, searchFilter);
                if(results != null) {
                    for(Course c : results) {
                        System.out.println(c);
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
    public ArrayList<Course> singleSearch(ArrayList<Course> masterList, Filter searchFilter) {
       return null;
    }
}
