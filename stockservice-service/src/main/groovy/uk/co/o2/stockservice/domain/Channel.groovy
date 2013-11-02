package uk.co.o2.stockservice.domain


public enum Channel {
    AFU,
    AFA,
    CFU,
    CFA

    public static Channel fromString(String channel) {
        if (channel) {
            try {
                return Channel.valueOf(channel)
            } catch (IllegalArgumentException e) {}
        }

    }
}