import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String FILE_PATH = "expenses.txt";

    public static void saveExpenses(List<Expense> expenses) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Expense expense : expenses) {
                writer.println(expense.toFileFormat());
            }
        } catch (IOException e) {
            System.err.println("⚠️ Error saving expenses: " + e.getMessage());
        }
    }

    public static List<Expense> loadExpenses() {
        List<Expense> expenses = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return expenses;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    expenses.add(Expense.fromFileFormat(line));
                }
            }
        } catch (IOException e) {
            System.err.println("⚠️ Error loading expenses: " + e.getMessage());
        }
        return expenses;
    }
}
