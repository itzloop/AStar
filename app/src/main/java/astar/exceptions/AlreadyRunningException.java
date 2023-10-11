package astar.exceptions;

public class AlreadyRunningException extends Exception {
    public AlreadyRunningException(String taskName) {
        super(String.format("Task %s, already running.", taskName));
    }
}
