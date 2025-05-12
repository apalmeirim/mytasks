import java.time.LocalDate;

public class Task {
    
    private String name;
    private LocalDate deadline;
    private LocalDate creationDate;
    private boolean complete;
    private String category;

    public Task (String name, LocalDate deadline, String category) {
        this.name = name;
        this.deadline = deadline;
        this.category = category;
        this.complete = false;
        this.creationDate = LocalDate.now();
    }

    public String getName() {
        return name;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public boolean isComplete() {
        return complete;
    }

    public String getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setComplete() {
        this.complete = true;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return String.format(
            "[%s] %s (Deadline: %s), Category: %s, created: %s",
            complete ? "X" : " ",
            name,
            deadline,
            category,
            creationDate
        );
    }


}
