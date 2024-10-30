package net.toiletmc.toiletcore;

import com.google.gson.Gson;
import okhttp3.*;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.logging.Level;

public class HttpHelper {
    static Gson gson = new Gson();
    static OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static void sendHttpRequest(String url, net.toiletmc.toiletcore.http.request.Request requestObject) {
        RequestBody requestBody = RequestBody.create(gson.toJson(requestObject), JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Bukkit.getScheduler().runTaskAsynchronously(ToiletCore.getInstance(), () -> {
            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : null;

                if (response.isSuccessful()) {
                    Bukkit.getScheduler().runTask(ToiletCore.getInstance(), () -> {
                        if (ToiletCore.getInstance().isDebugMode()) {
                            ToiletCore.getInstance().getLogger().warning("[HttpHelper] [" + url + "] 请求成功: " + response.code() + " 响应内容：" + responseBody);
                        }
                    });
                } else {
                    Bukkit.getScheduler().runTask(ToiletCore.getInstance(), () -> {
                        ToiletCore.getInstance().getLogger().warning("[HttpHelper] [" + url + "] 请求失败: " + response.code() + " 响应内容：" + responseBody);
                    });
                }

            } catch (IOException e) {
                Bukkit.getScheduler().runTask(ToiletCore.getInstance(), () -> {
                    ToiletCore.getInstance().getLogger().log(Level.WARNING, "[HttpHelper] 请求 url [" + url + "] 时出现异常。", e);
                });
            }

        });
    }
}
