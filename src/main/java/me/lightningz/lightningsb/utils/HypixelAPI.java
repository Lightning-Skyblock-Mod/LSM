package me.lightningz.lightningsb.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;

public class HypixelAPI {
    private static final Gson gson = new Gson();
    private static final ExecutorService es = Executors.newFixedThreadPool(3);

    private static final int FAILS_BEFORE_SWITCH = 3;
    private int currentUrl = 0;
    private long lastPrimaryUrl = 0;
    private final Integer[] myApiSuccesses = {0, 0, 0, 0};

    public CompletableFuture<JsonObject> getHypixelApiAsync(String apiKey, String method, HashMap<String, String> args) {
        return getApiAsync(generateApiUrl(apiKey, method, args));
    }

    public void getHypixelApiAsync(
            String apiKey,
            String method,
            HashMap<String, String> args,
            Consumer<JsonObject> consumer
    ) {
        getHypixelApiAsync(apiKey, method, args, consumer, () -> {
        });
    }

    public static void getHypixelApiAsync(
            String apiKey,
            String method,
            HashMap<String, String> args,
            Consumer<JsonObject> consumer,
            Runnable error
    ) {
        getApiAsync(generateApiUrl(apiKey, method, args), consumer, error);
    }



    public CompletableFuture<JsonObject> getApiAsync(String urlS) {
        CompletableFuture<JsonObject> result = new CompletableFuture<>();
        es.submit(() -> {
            try {
                result.complete(getApiSync(urlS));
            } catch (Exception e) {
                result.completeExceptionally(e);
            }
        });
        return result;
    }

    public static void getApiAsync(String urlS, Consumer<JsonObject> consumer, Runnable error) {
        es.submit(() -> {
            try {
                consumer.accept(getApiSync(urlS));
            } catch (Exception e) {
                error.run();
            }
        });
    }

    public static String getUUID(String name, Consumer<JsonObject> consumer) {
        es.submit(() -> {

            try {
                consumer.accept(UsernameToUUID(name));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        return name;
    }


    public void getApiGZIPAsync(String urlS, Consumer<JsonObject> consumer, Runnable error) {
        es.submit(() -> {
            try {
                consumer.accept(getApiGZIPSync(urlS));
            } catch (Exception e) {
                error.run();
            }
        });
    }

    public static JsonObject getApiSync(String urlS) throws IOException {
        URL url = new URL(urlS);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        String response = IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8);

        JsonObject json = gson.fromJson(response, JsonObject.class);
        if (json == null) throw new ConnectException("Invalid JSON");
        return json;
    }

    public JsonObject getApiGZIPSync(String urlS) throws IOException {
        URL url = new URL(urlS);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        String response = IOUtils.toString(new GZIPInputStream(connection.getInputStream()), StandardCharsets.UTF_8);

        JsonObject json = gson.fromJson(response, JsonObject.class);
        return json;
    }

    public static JsonObject UsernameToUUID(String name) throws IOException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        String response = IOUtils.toString(new GZIPInputStream(connection.getInputStream()), StandardCharsets.UTF_8);

        JsonObject json = gson.fromJson(response, JsonObject.class);
        if (json == null) throw new ConnectException("Invalid JSON");
        return json;
    }

    public static String generateApiUrl(String apiKey, String method, HashMap<String, String> args) {
        if (apiKey != null)
            args.put("key", apiKey.trim().replace("-", ""));
        StringBuilder url = new StringBuilder("https://api.hypixel.net/" + method);
        boolean first = true;
        for (Map.Entry<String, String> entry : args.entrySet()) {
            if (first) {
                url.append("?");
                first = false;
            } else {
                url.append("&");
            }
            try {
                url.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name())).append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
            }
        }
        return url.toString();
    }

}
