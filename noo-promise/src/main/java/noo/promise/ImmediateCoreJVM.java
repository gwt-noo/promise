package noo.promise;

import java.util.LinkedList;
import java.util.List;

/**
 * An implementation of immediate on the JVM platform for pure JVM testing of promises
 *
 * @author Tal Shani
 */
final class ImmediateCoreJVM extends ImmediateCore {

    static final List<ImmediateCommand> COMMANDS = new LinkedList<ImmediateCommand>();

    private static int currentIndex = 0;

    private static final ImmediateCommand CLEARED_COMMAND = new ImmediateCommand() {
        @Override
        public void execute() {

        }
    };

    @Override
    void clearImmediate(int handle) {
        if (handle < 0 || handle >= COMMANDS.size()) return;
        COMMANDS.set(handle, CLEARED_COMMAND);
    }

    @Override
    int setImmediate(ImmediateCommand command) {
        if (COMMANDS.add(command)) {
            return COMMANDS.size() - 1;
        }
        return -1;
    }

    @Override
    String getImplementationName() {
        return "JVM";
    }


    /**
     * Will call all commands that didn't execute till now
     * Usually will be called after the body of the test and before the assertions
     */
    public static void exit() {
        while (currentIndex < COMMANDS.size()) {
            ImmediateCommand cmd = COMMANDS.get(currentIndex);
            try {
                cmd.execute();
            } catch (Exception ignored) {
                // just continue
            }
            currentIndex++;
        }
    }
}
