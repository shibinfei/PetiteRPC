package io.hahahahaha.petiterpc.common.lambdatemplate;

import java.util.function.Supplier;

/**
 * @author shibinfei
 *
 */
public class Conditions {

    /**
     * Do command when the condition fits. Makes grammar simpler, but using in case of short command
     * @param condition
     * @param cmd
     */
    public static void doWhen(boolean condition, Runnable cmd) {
        if (condition) {
            cmd.run();
        }
    }
    
    
    /**
     * Do command when the condition fits. Makes grammar simpler, but using in case of short command
     * @param condition
     * @param cmd
     */
    public static void doWhen(Supplier<Boolean> condition, Runnable cmd) {
        if (condition.get()) {
            cmd.run();
        }
    }
    
}
