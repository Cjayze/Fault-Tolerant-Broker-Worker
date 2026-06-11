import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private final JTextArea area = new JTextArea();

    private final List<Task> tasks = new ArrayList<>();
    private final List<Worker> workers = new ArrayList<>();

    private Broker broker;

    private int taskCounter = 1;

    public MainFrame() {

        setTitle("Distributed Broker Worker Demo");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton addTask = new JButton("Add Task");

        JButton killW1 = new JButton("Kill Worker 1");
        JButton killW2 = new JButton("Kill Worker 2");
        JButton killW3 = new JButton("Kill Worker 3");

        JPanel panel = new JPanel();

        panel.add(addTask);
        panel.add(killW1);
        panel.add(killW2);
        panel.add(killW3);

        add(panel, BorderLayout.NORTH);

        area.setFont(new Font("Monospaced", Font.PLAIN, 16));

        add(new JScrollPane(area), BorderLayout.CENTER);

        Worker w1 = new Worker(1);
        Worker w2 = new Worker(2);
        Worker w3 = new Worker(3);

        workers.add(w1);
        workers.add(w2);
        workers.add(w3);

        w1.start();
        w2.start();
        w3.start();

        broker = new Broker(workers, tasks, this);
        broker.start();

        addTask.addActionListener(e -> {
            tasks.add(new Task(taskCounter++));
        });

        killW1.addActionListener(e -> w1.killWorker());

        killW2.addActionListener(e -> w2.killWorker());

        killW3.addActionListener(e -> w3.killWorker());

        Timer timer = new Timer(1000, e -> refresh());
        timer.start();
    }

    public void refresh() {

        SwingUtilities.invokeLater(() -> {

            StringBuilder sb = new StringBuilder();

            sb.append("===== WORKERS =====\n\n");

            for (Worker worker : workers) {

                sb.append("Worker ")
                        .append(worker.getWorkerId())
                        .append(" : ")
                        .append(worker.isActiveWorker() ? "ONLINE" : "OFFLINE")
                        .append("\n");
            }

            sb.append("\n===== TASKS =====\n\n");

            for (Task task : tasks) {

                sb.append(task)
                        .append("\n");
            }

            area.setText(sb.toString());
        });
    }
}