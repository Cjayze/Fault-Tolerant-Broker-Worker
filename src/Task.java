public class Task {
    private final int id;
    private String status;

    public Task(int id) {
        this.id = id;
        this.status = "Waiting";
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task " + id + " - " + status;
    }
}