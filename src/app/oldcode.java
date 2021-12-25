package app;

import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class oldcode {

//    HttpRequest request = HttpRequest.newBuilder()
//            .GET()
//            .uri(URI.create("https://gutendex.com/books/?search=death"))
//            .setHeader("User-Agent", "Java 11 HttpClient Bot")
//            .build();
//
//    HttpClient httpClient = HttpClient.newBuilder()
//            .version(HttpClient.Version.HTTP_2)
//            .build();
//    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//        System.out.println(response.statusCode());
//        System.out.println(response.body());
//
//    JLabel labelGET = new JLabel(response.body());
//    add(labelGET);
}
