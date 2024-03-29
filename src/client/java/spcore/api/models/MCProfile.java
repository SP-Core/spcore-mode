package spcore.api.models;

import java.time.OffsetDateTime;

public class MCProfile {
    private String userId;
    private OffsetDateTime createAt;
    private InnerData inner;

    public static class InnerData {
        private String uid;
        private boolean trackOnline;
        private OffsetDateTime lastOnlineUpdate;
    }
}