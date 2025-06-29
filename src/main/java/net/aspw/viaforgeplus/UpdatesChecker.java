package net.aspw.viaforgeplus;

import net.aspw.viaforgeplus.vfphook.OutdatedJavaTLSFixer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.X509TrustManager;

public class UpdatesChecker {

    public boolean isModLatest = false;

    public void check() {
        try {
            OutdatedJavaTLSFixer tlsPatcher = new OutdatedJavaTLSFixer();

            tlsPatcher.patchTLS();

            OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(tlsPatcher.sslContext.getSocketFactory(), (X509TrustManager) tlsPatcher.trustAllCerts[0])
                .build();

            Request request = new Request.Builder()
                .url("https://nattogreatapi.pages.dev/ViaForgePlus/database/latest_version.txt")
                .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.body() != null) {
                    String latestVersion = response.body().string();
                    final String VERSION = "alphaAntiLeak22";
                    isModLatest = latestVersion.equals(VERSION);
                }
            }
        } catch (Exception ignored) {
            isModLatest = false;
        }
    }
}
