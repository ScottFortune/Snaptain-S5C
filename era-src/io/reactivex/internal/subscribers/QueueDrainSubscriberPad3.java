package io.reactivex.internal.subscribers;

import java.util.concurrent.atomic.AtomicLong;

class QueueDrainSubscriberPad3 extends QueueDrainSubscriberPad2 {
  final AtomicLong requested = new AtomicLong();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscribers/QueueDrainSubscriberPad3.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */