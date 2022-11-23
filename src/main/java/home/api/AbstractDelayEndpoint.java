package home.api;

import lombok.SneakyThrows;

public class AbstractDelayEndpoint {

    private final boolean delayEnabled;
    private final long delayMillis;

    public AbstractDelayEndpoint(boolean delayEnabled, long delayMillis) {
        this.delayEnabled = delayEnabled;
        this.delayMillis = delayMillis;
    }

    @SneakyThrows
    void delayResponse() {
        if (delayEnabled) {
            Thread.sleep(delayMillis);
        }
    }

}
