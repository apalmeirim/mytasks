import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Manager m = new Manager();
        
        boolean running = true;

        while(running) {
            if(!m.loggedIn()) {
                System.out.println("\n Multi-User Task Manager");
                System.out.println("1. Register User");
                System.out.println("2. Login");
                System.out.println("3. Quit (closes program)");
                System.out.println("4. Clear All Data (affects all users)");

                System.out.println("Select an option: ");
                String c = s.nextLine();

                switch (c) {

                    case "1":
                        System.out.println("Desired Username: ");
                        String newUser = s.nextLine();
                        System.out.println("Desired Password: ");
                        String newPass = s.nextLine();

                        if(m.register(newUser, newPass)) {
                            System.out.println("User registerd.");
                        } else {
                            System.out.println("User already exists.");
                        }
                        break;

                    case "2":
                        while(true) {
                            System.out.print("Enter username: ");
                            String user = s.nextLine();
                            System.out.print("Enter password: ");
                            String pass = s.nextLine();

                            if (m.login(user, pass)) {
                                System.out.println("Logged in as " + user);
                                break;
                            } else {
                                System.out.println("Invalid credentials.");
                            }
                        }   
                        break;

                    case "3":
                        running = false;
                        break;

                    case "4":
                        m.clearAllInformation();
                        System.out.println("All information has been cleared.");
                        break;
                    
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } else {
                System.out.println("\n--- Task Menu ---");
                System.out.println("1. View Tasks");
                System.out.println("2. Add Task");
                System.out.println("3. Mark Task Completed");
                System.out.println("4. Remove Task");
                System.out.println("5. Search (using keywords)");
                System.out.println("6. Logout");

                System.out.print("Choose an option: ");
                String c = s.nextLine();

                switch (c) {

                    case "1":
                        List<Task> tasks = m.getList();
                        if (tasks.isEmpty()) {
                            System.out.println("No tasks yet.");
                        } else {
                            for (int i = 0; i < tasks.size(); i++) {
                                System.out.println(i + ". " + tasks.get(i));
                            }
                        }
                        break;

                    case "2":
                        while (true) {
                            System.out.print("Enter task title (or B to go back): ");
                            String title = s.nextLine();
                            if (title.equalsIgnoreCase("B")) break;
                
                            System.out.print("Enter category (or B to go back): ");
                            String category = s.nextLine();
                            if (category.equalsIgnoreCase("B")) break;
                
                            System.out.print("Enter due date (YYYY-MM-DD) (or B to go back): ");
                            String dateInput = s.nextLine();
                            if (dateInput.equalsIgnoreCase("B")) break;
                
                            try {
                                LocalDate dueDate = LocalDate.parse(dateInput);
                                m.addTask(title, category, dueDate);
                                System.out.println("Task added!");
                                break;
                            } catch (DateTimeParseException e) {
                                System.out.println("Invalid date format. Try again or type B to cancel.");
                            }
                        }
                        break;
            

                    case "3":
                        while (true) {
                            System.out.print("Enter task number to mark completed (or B to go back): ");
                            String input = s.nextLine().trim();
                        
                            if (input.equalsIgnoreCase("B")) {
                                break;
                            }
                        
                            try {
                                int completeIndex = Integer.parseInt(input);
                                if (m.completeTask(completeIndex)) {
                                    System.out.println("Task marked as completed.");
                                    break;
                                } else {
                                    System.out.println("Invalid task number. Try again.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Please enter a valid number or B to go back.");
                            }
                        } 
                        break;

                    case "4":
                        while (true) {
                            System.out.print("Enter task number to remove (or B to go back): ");
                            String input = s.nextLine().trim();
                        
                            if (input.equalsIgnoreCase("B")) {
                                break;
                            }
                        
                            try {
                                int removeIndex = Integer.parseInt(input);
                                if (m.removeTask(removeIndex)) {
                                    System.out.println("Task removed.");
                                    break;
                                } else {
                                    System.out.println("Invalid task number. Try again.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Please enter a valid number or B to go back.");
                            }
                        } 
                        break;
                    
                    case "5":
                        while (true) {
                            System.out.print("Enter keyword to search (or B to go back): ");
                            String kw = s.nextLine().trim();
                            if (kw.equalsIgnoreCase("B")) break;
                            List<Task> results = m.searchTasks(kw);
                            if (results.isEmpty()) System.out.println("No tasks match.");
                            else for (int i=0;i<results.size();i++) System.out.println(i + ". " + results.get(i));
                        } 
                        break;
                    
                    case "6":
                        m.logout();
                        System.out.println("Logged out.");
                        break;

                    default:
                        System.out.println("Invalid option.");
                }
            }
        }

        s.close();
        System.out.println("To restart program, re-run.");
    }

    
}