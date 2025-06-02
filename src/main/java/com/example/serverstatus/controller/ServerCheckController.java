package com.example.serverstatus.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.serverstatus.model.ServerCheck;
import com.example.serverstatus.servise.ServerCheckService;
import com.example.serverstatus.repository.ServerCheckRepository;
import com.example.serverstatus.cache.SimplePageCache;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@RestController
@RequestMapping("/checks")
public class ServerCheckController {
    private final ServerCheckRepository repository;
    private final ServerCheckService service;

    public ServerCheckController(ServerCheckRepository repository, ServerCheckService service) {
        this.repository = repository;
        this.service = service;
    }

    // Проверка статуса сервера по URL
    @GetMapping("/status")
    public ServerCheck checkStatus(@RequestParam("url") String url) {
        ServerCheck check = service.checkStatus(url);
        repository.save(check);
        return check;
    }

    // Проверка статуса популярных сайтов
    @GetMapping("/status/google")
    public ServerCheck checkGoogle() {
        return checkStatus("https://google.com");
    }

    @GetMapping("/status/wikipedia")
    public ServerCheck checkWikipedia() {
        return checkStatus("https://wikipedia.org");
    }

    @GetMapping("/status/microsoft")
    public ServerCheck checkMicrosoft() {
        return checkStatus("https://microsoft.com");
    }

    @GetMapping("/status/spotify")
    public ServerCheck checkSpotify() {
        return checkStatus("https://spotify.com");
    }

    // Универсальный эндпоинт для популярных сайтов
    @GetMapping("/status/{site}")
    public ServerCheck checkPopularSite(@PathVariable String site) {
        String url;
        switch (site.toLowerCase()) {
            case "google":
                url = "https://google.com";
                break;
            case "wikipedia":
                url = "https://wikipedia.org";
                break;
            case "microsoft":
                url = "https://microsoft.com";
                break;
            case "spotify":
                url = "https://spotify.com";
                break;
            default:
                throw new IllegalArgumentException("Unknown site: " + site);
        }
        return checkStatus(url);
    }

    // CRUD: получить все проверки
    @GetMapping
    public List<ServerCheck> all() {
        return repository.findAll();
    }

    // CRUD: получить одну проверку
    @GetMapping("/{id}")
    public ServerCheck one(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    // CRUD: удалить
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping("/server-check")
    @ResponseBody
    public String serverCheckPage() {
        String cacheKey = "server-check";
        String cached = SimplePageCache.get(cacheKey);
        if (cached != null) return cached;
        String result;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("https://www.google.com");
            try (CloseableHttpResponse response = client.execute(request)) {
                String preview = EntityUtils.toString(response.getEntity());
                preview = preview.substring(0, Math.min(500, preview.length()));
                result = "<h2>Server Check page</h2><p>Google.com preview:</p><pre>" + preview + "</pre>";
            }
        } catch (Exception e) {
            result = "<h2>Server Check page</h2><p>Failed to fetch Google.com: " + e.getMessage() + "</p>";
        }
        SimplePageCache.put(cacheKey, result);
        return result;
    }

    @GetMapping("/server-statuses")
    @ResponseBody
    public String checkMultipleServers() {
        String cacheKey = "server-statuses";
        String cached = SimplePageCache.get(cacheKey);
        if (cached != null) return cached;
        Map<String, String> servers = new LinkedHashMap<>();
        servers.put("Google", "https://www.google.com");
        servers.put("Wikipedia", "https://www.wikipedia.org");
        servers.put("Microsoft", "https://www.microsoft.com");
        servers.put("Spotify", "https://www.spotify.com");
        StringBuilder result = new StringBuilder("<h2>Server Statuses</h2><table border='1'><tr><th>Service</th><th>Status</th></tr>");
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            for (Map.Entry<String, String> entry : servers.entrySet()) {
                String name = entry.getKey();
                String url = entry.getValue();
                String status;
                try (CloseableHttpResponse response = client.execute(new HttpGet(url))) {
                    int code = response.getStatusLine().getStatusCode();
                    status = code == 200 ? "<span style='color:green'>UP</span>" : "<span style='color:orange'>" + code + "</span>";
                } catch (Exception e) {
                    status = "<span style='color:red'>DOWN</span>";
                }
                result.append("<tr><td>").append(name).append("</td><td>").append(status).append("</td></tr>");
            }
        } catch (Exception e) {
            result.append("<tr><td colspan='2'>Error: ").append(e.getMessage()).append("</td></tr>");
        }
        result.append("</table>");
        String html = result.toString();
        SimplePageCache.put(cacheKey, html);
        return html;
    }

    @GetMapping("/server-check/{site}")
    @ResponseBody
    public String checkSiteStatus(@PathVariable String site) {
        String cacheKey = "server-check-" + site.toLowerCase();
        String cached = SimplePageCache.get(cacheKey);
        if (cached != null) return cached;
        String url;
        switch (site.toLowerCase()) {
            case "google":
                url = "https://www.google.com";
                break;
            case "wikipedia":
                url = "https://www.wikipedia.org";
                break;
            case "microsoft":
                url = "https://www.microsoft.com";
                break;
            case "spotify":
                url = "https://www.spotify.com";
                break;
            default:
                return "<h2>Unknown site</h2>";
        }
        String result;
        try (org.apache.http.impl.client.CloseableHttpClient client = org.apache.http.impl.client.HttpClients.createDefault()) {
            org.apache.http.client.methods.HttpGet request = new org.apache.http.client.methods.HttpGet(url);
            try (org.apache.http.client.methods.CloseableHttpResponse response = client.execute(request)) {
                int code = response.getStatusLine().getStatusCode();
                if (code == 200) {
                    result = "<h2>" + site.substring(0,1).toUpperCase() + site.substring(1) + ": Available</h2>";
                } else {
                    result = "<h2>" + site.substring(0,1).toUpperCase() + site.substring(1) + ": Unavailable (HTTP " + code + ")</h2>";
                }
            }
        } catch (Exception e) {
            result = "<h2>" + site.substring(0,1).toUpperCase() + site.substring(1) + ": Unavailable</h2><p>" + e.getMessage() + "</p>";
        }
        SimplePageCache.put(cacheKey, result);
        return result;
    }
}
