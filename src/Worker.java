import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Worker extends Thread {

    private final int workerId;
    private final BlockingQueue<Task> queue = new LinkedBlockingQueue<>();

    private volatile boolean active = true;
    private volatile long lastHeartbeat;
    private volatile Task currentTask;

    public Worker(int workerId) {
        this.workerId = workerId;
        this.lastHeartbeat = System.currentTimeMillis();
    }

    public int getWorkerId() {
        return workerId;
    }

    public boolean isActiveWorker() {
        return active;
    }

    public long getLastHeartbeat() {
        return lastHeartbeat;
    }

    public BlockingQueue<Task> getQueue() {
        return queue;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void assignTask(Task task) {
        queue.offer(task);
    }

    public void killWorker() {
        active = false;
    }

    @Override
    public void run() {

        while (active) {

            try {

                lastHeartbeat = System.currentTimeMillis();

                Task task = queue.poll();

                if (task != null) {

                    currentTask = task;

                    task.setStatus("Running on Worker " + workerId);

                    for (int i = 0; i < 8; i++) {

                        if (!active) {
                            task.setStatus("Waiting");
                            currentTask = null;
                            return;
                        }

                        Thread.sleep(500);
                    }

                    task.setStatus("Completed by Worker " + workerId);

                    currentTask = null;
                }

                Thread.sleep(1000);

            } catch (Exception e) {
                break;
            }
        }
    }
}