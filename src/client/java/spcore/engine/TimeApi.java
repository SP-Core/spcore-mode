package spcore.engine;

public class TimeApi {
    public static float deltaTime;

    public static Long start_time;

    public static void start(){
        start_time = System.nanoTime();
    }

    public static void end(){
        long endTime = System.nanoTime();
        deltaTime = (endTime - start_time) / 1_000_000_000.0f;
        start_time = null;
    }
}
