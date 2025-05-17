import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Expense {
    private String category;
    private double amount;
    private LocalDate date;
    private String description;

    public Expense(String category, double amount, LocalDate date, String description) {
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    // Getters
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }

    // File format: category,amount,date,description
    public String toFileFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.join(",",
            category,
            String.valueOf(amount),
            date.format(formatter),
            description.replace(",", ";")  // Avoid CSV conflicts
        );
    }

    public static Expense fromFileFormat(String line) {
        String[] parts = line.split(",", 4);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new Expense(
            parts[0],
            Double.parseDouble(parts[1]),
            LocalDate.parse(parts[2], formatter),
            parts.length > 3 ? parts[3].replace(";", ",") : ""
        );
    }

    @Override
    public String toString() {
        return String.format("[%s] %-15s $%.2f - %s",
            date,
            category,
            amount,
            description
        );
    }
}
