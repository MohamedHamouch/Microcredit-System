package helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputHelper {
    private final static Scanner sc = new Scanner(System.in);

    public static int getInt(String message) {
        int number;
        while (true) {
            System.out.print(message);
            try {
                number = Integer.parseInt(sc.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a valid integer.");
            }
        }
        return number;
    }
    
    public static int getPositiveInt(String message) {
        int number;
        while (true) {
            System.out.print(message);
            try {
                number = Integer.parseInt(sc.nextLine());
                if (number > 0) {
                    break;
                } else {
                    System.out.println("Enter a positive number greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a valid integer.");
            }
        }
        return number;
    }

    public static double getDouble(String message) {
        double d;
        while (true) {
            System.out.print(message);
            try {
                d = Double.parseDouble(sc.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a valid decimal number.");
            }
        }
        return d;
    }
    
    public static double getPositiveDouble(String message) {
        double d;
        while (true) {
            System.out.print(message);
            try {
                d = Double.parseDouble(sc.nextLine());
                if (d > 0) {
                    break;
                } else {
                    System.out.println("Enter a positive number greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a valid decimal number.");
            }
        }
        return d;
    }
    
    public static double getPercentage(String message, double min, double max) {
        double d;
        while (true) {
            System.out.print(message);
            try {
                d = Double.parseDouble(sc.nextLine());
                if (d >= min && d <= max) {
                    break;
                } else {
                    System.out.println("Enter a percentage between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a valid decimal number.");
            }
        }
        return d;
    }

    public static String getString(String message) {
        String input;
        while (true) {
            System.out.print(message);
            input = sc.nextLine().trim();
            if (!input.isEmpty()) break;
            System.out.println("Input cannot be empty.");
        }
        return input;
    }
    
    public static String getOptionalString(String message) {
        System.out.print(message);
        return sc.nextLine().trim();
    }

    public static boolean getBoolean(String message) {
        while (true) {
            System.out.print(message + " (true/false): ");
            String input = sc.nextLine();
            if (input.equals("true")) return true;
            if (input.equals("false")) return false;
            System.out.println("Invalid input. Enter true or false.");
        }
    }

    public static int getUserChoice(String message, int min, int max) {
        int choice;
        while (true) {
            choice = getInt(message);
            if (choice >= min && choice <= max) break;
            System.out.println("Invalid choice. Enter a number between " + min + " and " + max);
        }
        return choice;
    }

    public static String getEmail(String message) {
        String input;
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        while (true) {
            System.out.print(message);
            input = sc.nextLine().trim();
            if (!input.isEmpty() && input.matches(emailRegex)) {
                return input;
            }
            System.out.println("Invalid email format. Please enter a valid email address.");
        }
    }

    public static LocalDateTime getDateTimeInput(String message) {
        LocalDateTime dateTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        while (true) {
            System.out.print(message + " (yyyy-MM-dd HH:mm:ss): ");
            try {
                String input = sc.nextLine().trim();
                dateTime = LocalDateTime.parse(input, formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date/time format. Please use yyyy-MM-dd HH:mm:ss");
            }
        }
        return dateTime;
    }
    public static String getDateInput(String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (true) {
            System.out.print(message + " (yyyy-MM-dd): ");
            try {
                String input = sc.nextLine().trim();
                // Validate the format by parsing it, but return the string
                LocalDate.parse(input, formatter);
                return input; // Return the validated string
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd");
            }
        }
    }
}
