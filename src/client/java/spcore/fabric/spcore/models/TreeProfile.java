package spcore.fabric.spcore.models;

import java.time.OffsetDateTime;

public class TreeProfile {
    private String userId;
    private OffsetDateTime createAt;
    private InnerData inner;

    public static class InnerData {
        private int balance;

    }
}
