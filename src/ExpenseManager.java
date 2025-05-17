import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class ExpenseManager {
    private List<Expense> expenses;
    private Scanner scanner;

    public ExpenseManager() {
        this.expenses = FileHandler.loadExpenses();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("💰 ExpenseEase - Personal Finance Tracker");
        
        while (true) {
            printMenu();
            String input = scanner.nextLine().trim();
            
            if (input.equals("6")) {
                FileHandler.saveExpenses(expenses);
                System.out.println("✅ Expenses saved. Goodbye!");
                break;
            }

            switch (input) {
                case "1" -> addExpense();
                case "2" -> removeExpense();
                case "3" -> viewAllExpenses();
                case "4" -> viewMonthlyTotal();
                case "5" -> viewCategoryReport();
                default -> System.out.println("⚠️ Invalid choice");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. ➕ Add Expense");
        System.out.println("2. ➖ Remove Expense");
        System.out.println("3. 📋 View All Expenses");
        System.out.println("4. 📅 Monthly Summary");
        System.out.println("5. 🗂️ Category Report");
        System.out.println("6. ❌ Exit");
        System.out.print("Select an option: ");
    }

    private void addExpense() {
        System.out.println("\n➕ Add New Expense");
        
        String category = getInput("Category (e.g., Food, Transport): ");
        double amount = getDoubleInput("Amount: $");
        LocalDate date = getDateInput("Date [YYYY-MM-DD] (today if blank): ");
        String description = getInput("Description: ");

        expenses.add(new Expense(category, amount, date, description));
        System.out.println("✅ Expense added!");
    }

    private void removeExpense() {
        if (expenses.isEmpty()) {
            System.out.println("ℹ️ No expenses to remove");
            return;
        }

        System.out.println("\n➖ Remove Expense");
        listExpensesWithNumbers();
        
        try {
            int choice = Integer.parseInt(getInput("Enter expense number to remove (0 to cancel): "));
            if (choice > 0 && choice <= expenses.size()) {
                Expense removed = expenses.remove(choice - 1);
                System.out.println("✅ Removed: " + removed);
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Invalid number");
        }
    }

    private void viewAllExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("ℹ️ No expenses recorded");
            return;
        }

        System.out.println("\n📋 All Expenses (" + expenses.size() + ")");
        expenses.forEach(System.out::println);
        
        double total = expenses.stream()
            .mapToDouble(Expense::getAmount)
            .sum();
        System.out.printf("\n💵 Total: $%.2f\n", total);
    }

    private void viewMonthlyTotal() {
        System.out.println("\n📅 Monthly Summary");
        int year = getYearInput();
        int month = getMonthInput();

        List<Expense> monthlyExpenses = expenses.stream()
            .filter(e -> e.getDate().getYear() == year && e.getDate().getMonthValue() == month)
            .toList();

        if (monthlyExpenses.isEmpty()) {
            System.out.printf("ℹ️ No expenses for %s %d\n", Month.of(month), year);
            return;
        }

        monthlyExpenses.forEach(System.out::println);
        double total = monthlyExpenses.stream()
            .mapToDouble(Expense::getAmount)
            .sum();
        System.out.printf("\n💵 Total for %s %d: $%.2f\n", Month.of(month), year, total);
    }

    private void viewCategoryReport() {
        if (expenses.isEmpty()) {
            System.out.println("ℹ️ No expenses recorded");
            return;
        }

        Map<String, Double> report = expenses.stream()
            .collect(Collectors.groupingBy(
                Expense::getCategory,
                Collectors.summingDouble(Expense::getAmount)
            ));

        System.out.println("\n🗂️ Category Report");
        report.forEach((category, total) ->
            System.out.printf("%-15s: $%.2f\n", category, total));

        double grandTotal = report.values().stream().reduce(0.0, Double::sum);
        System.out.printf("\n💵 Grand Total: $%.2f\n", grandTotal);
    }

    // Helper methods
    private String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(getInput(prompt));
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Please enter a valid number");
            }
        }
    }

    private LocalDate getDateInput(String prompt) {
        while (true) {
            String input = getInput(prompt);
            if (input.isEmpty()) return LocalDate.now();
            
            try {
                return LocalDate.parse(input);
            } catch (Exception e) {
                System.out.println("⚠️ Please use YYYY-MM-DD format");
            }
        }
    }

    private int getYearInput() {
        while (true) {
            try {
                return Integer.parseInt(getInput("Enter year (YYYY): "));
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid year");
            }
        }
    }

    private int getMonthInput() {
        while (true) {
            try {
                int month = Integer.parseInt(getInput("Enter month (1-12): "));
                if (month >= 1 && month <= 12) return month;
                System.out.println("⚠️ Month must be 1-12");
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid month");
            }
        }
    }

    private void listExpensesWithNumbers() {
        for (int i = 0; i < expenses.size(); i++) {
            System.out.printf("%2d. %s\n", i + 1, expenses.get(i));
        }
    }
}
