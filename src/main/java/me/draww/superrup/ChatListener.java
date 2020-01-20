package me.draww.superrup;

import me.draww.superrup.group.SRGroupManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatListener implements EventExecutor, Listener {

    private final EventPriority priority;
    private final AtomicBoolean active = new AtomicBoolean(true);

    public ChatListener(EventPriority priority) {
        this.priority = priority;
    }

    @Override
    public void execute(Listener listener, Event event) {
        if (event.getClass() != AsyncPlayerChatEvent.class) return;

        if  (!this.active.get()) {
            event.getHandlers().unregister(listener);
            return;
        }

        AsyncPlayerChatEvent eventInstance = (AsyncPlayerChatEvent) event;
        onEvent(eventInstance);
    }

    private void onEvent(AsyncPlayerChatEvent event) {
        String format = event.getFormat();
        format = format.replace("%superrup_group%", ((SRGroupManager) Main.INSTANCE.getGroupManager()).getPrefix(event.getPlayer()));
        event.setFormat(format);
    }

    public void register() {
        Bukkit.getPluginManager().registerEvent(AsyncPlayerChatEvent.class, this, priority, this, Main.INSTANCE, false);
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean unregister() {
        // already unregistered
        if (!this.active.getAndSet(false)) {
            return false;
        }

        // also remove the handler directly, just in case the event has a really low throughput.
        // (the event would also be unregistered next time it's called - but this obviously assumes
        // the event will be called again soon)
        try {
            // unfortunately we can't cache this reflect call, as the method is static
            Method getHandlerListMethod = AsyncPlayerChatEvent.class.getMethod("getHandlerList");
            HandlerList handlerList = (HandlerList) getHandlerListMethod.invoke(null);
            handlerList.unregister(this);
        } catch (Throwable ignored) {
            // ignored
        }

        return true;
    }
}
