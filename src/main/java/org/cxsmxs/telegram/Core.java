package org.cxsmxs.telegram;

import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.regex.qual.Regex;
import org.cxsmxs.Auth;
import org.cxsmxs.telegram.entities.*;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Core {
  private final Auth auth;
  private final YamlConfiguration config;
  private final OkHttpClient client;
  private final Api api;
  private final ExecutorService executorService;
  private long currentOffset = -1;
  private User me;
  private Map<String, Consumer<Context>> commandMap;
  private Regex commandRegex = null;

  public Core(Auth auth, YamlConfiguration config) {
    this.auth = auth;
    this.config = config;

    this.client = new OkHttpClient.Builder()
      .readTimeout(Duration.ZERO)
      .addInterceptor(new HttpLoggingInterceptor()
        .setLevel(config.getBoolean("isDebugHttp") ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
      .build();

    this.api = (Api) new Retrofit.Builder()
      .baseUrl(config.getString("getApiOrigin") + "/bot" + config.getString("getBotToken") + "/")
      .client(client)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(Core.class);

    this.executorService = Executors.newCachedThreadPool();

    initializeCommandMap();
  }

  private void initializeCommandMap() {
    commandMap = new HashMap<>();
    // Add command handlers to commandMap
  }

  public void startPolling() {
    executorService.submit(this::initialize);
    executorService.submit(this::poll);
    executorService.submit(this::handleUpdates);
  }

  private void initialize() {
    me = (User) api.getMe();
    if (me == null) {
      throw new NullPointerException("getMe().result is null");
    }

    // I intentionally don't put optional @username in regex
    // since bot is only used in group chats
    String username = me.getUsername();
    if (username == null) {
      throw new NullPointerException("username is null");
    }
    this.commandRegex = Pattern.compile("^/(\\w+)@" + username + "(?:\\s+(.+))?$");

    List<String> commandList = Arrays.asList(config.time, config.online, config.chatID);
    List<String> descriptionList = Arrays.asList(C.timeDesc, C.onlineDesc, C.chatIDDesc);

    List<BotCommand> commands = IntStream.range(0, commandList.size())
      .mapToObj(i -> new BotCommand(commandList.get(i), descriptionList.get(i)))
      .collect(Collectors.toList());

    api.deleteWebhook(true);
    api.setMyCommands(commands);
  }

  private void poll() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        // Polling logic
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void handleUpdates() {
    // Handling updates logic
  }

  // Additional methods for handling commands, updates, etc.

  public void stop() {
    executorService.shutdownNow();
  }
}