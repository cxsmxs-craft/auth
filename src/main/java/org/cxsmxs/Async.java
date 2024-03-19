package org.cxsmxs;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public abstract class Async extends JavaPlugin {
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public void onEnable()  {
        CompletableFuture.runAsync(this::onEnableAsync, executor);
    }

    @Override
    public void onDisable() {
        CompletableFuture.runAsync(() -> {
            onDisableAsync();
            executor.shutdownNow(); // Attempt to stop all actively executing tasks
        }, executor);
    }

    public void onEnableAsync() {
    }

    public void onDisableAsync() {
    }

    public <T> CompletableFuture<T> launch(Supplier<T> function) {
        return CompletableFuture.supplyAsync(function, executor);
    }
}