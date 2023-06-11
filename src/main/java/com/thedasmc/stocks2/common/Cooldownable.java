package com.thedasmc.stocks2.common;

import java.time.Duration;
import java.time.Instant;

public interface Cooldownable {

    Long getLastPurchaseTime();

    default boolean hasCooldown(Duration cooldownDuration) {
        return getCooldownExpireTime(cooldownDuration).compareTo(Instant.now()) > 0;
    }

    default Instant getCooldownExpireTime(Duration cooldownDuration) {
        Long lastPurchaseTimeMillis = getLastPurchaseTime();

        if (lastPurchaseTimeMillis == null)
            return Instant.now();

        Instant lastPurchaseTime = Instant.ofEpochMilli(lastPurchaseTimeMillis);
        return lastPurchaseTime.plus(cooldownDuration);
    }

}
