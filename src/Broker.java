import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Broker extends Thread {

    private final List<Worker> workers;
    private final List<Task> tasks;
    private final MainFrame frame;

    private final Set<Integer> failedWorkers = new HashSet<>();

    public Broker(List<Worker> workers, List<Task> tasks, MainFrame frame) {
        this.workers = workers;
        this.tasks = tasks;
        this.frame = frame;
    }

    private Worker findAvailableWorker() {

        Worker best = null;

        for (Worker worker : workers) {

            if (!worker.isActiveWorker()) {
                continue;
            }

            if (best == null ||
                    worker.getQueue().size() < best.getQueue().size()) {

                best = worker;
            }
        }

        return best;
    }

    @Override
    public void run() {

        while (true) {

            try {

                for (Worker worker : workers) {

                    if (!worker.isActiveWorker()
                            && !failedWorkers.contains(worker.getWorkerId())) {

                        failedWorkers.add(worker.getWorkerId());

                        Task runningTask = worker.getCurrentTask();

                        if (runningTask != null &&
                                !runningTask.getStatus().startsWith("Completed")) {

                            runningTask.setStatus("Waiting");
                        }

                        for (Task task : worker.getQueue()) {
                            task.setStatus("Waiting");
                        }

                        worker.getQueue().clear();
                    }
                }

                for (Task task : tasks) {

                    if (task.getStatus().equals("Waiting")) {

                        Worker worker = findAvailableWorker();

                        if (worker != null) {

                            worker.assignTask(task);

                            task.setStatus(
                                    "Assigned to Worker "
                                            + worker.getWorkerId()
                            );
                        }
                    }
                }

                long now = System.currentTimeMillis();

                for (Worker worker : workers) {

                    if (worker.isActiveWorker()) {

                        long diff = now - worker.getLastHeartbeat();

                        if (diff > 10000) {

                            worker.killWorker();

                            Task runningTask = worker.getCurrentTask();

                            if (runningTask != null) {
                                runningTask.setStatus("Waiting");
                            }

                            for (Task task : worker.getQueue()) {
                                task.setStatus("Waiting");
                            }

                            worker.getQueue().clear();
                        }
                    }
                }

                frame.refresh();

                Thread.sleep(1000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}