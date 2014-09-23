package net.joesteele.tissue;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import net.joesteele.tissue.models.Comment;
import net.joesteele.tissue.models.Issue;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import rx.Observable;

import java.util.List;
import java.util.Map;

/**
 * Created by joesteele on 9/5/14.
 */
public class Client {
  private static final String ENDPOINT = "https://api.github.com";

  private static GitHubApi service;

  interface GitHubApi {
    @GET("/repos/{owner}/{repo}/issues")
    Observable<List<Issue>> issues(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/issues")
    Observable<List<Issue>> issues(@Path("owner") String owner, @Path("repo") String repo, @QueryMap Map<String, Object> options);

    @GET("/repos/{owner}/{repo}/issues/{number}")
    Observable<Issue> issue(@Path("owner") String owner, @Path("repo") String repo, @Path("number") int number);

    @GET("/repos/{owner}/{repo}/issues/{number}/comments")
    Observable<List<Comment>> comments(@Path("owner") String owner, @Path("repo") String repo, @Path("number") int number);
  }

  public static GitHubApi get() {
    if (service == null) {
      service = buildAdapter().create(GitHubApi.class);
    }
    return service;
  }

  private static RestAdapter buildAdapter() {
    return new RestAdapter.Builder()
      .setEndpoint(ENDPOINT)
      .setRequestInterceptor(r -> r.addHeader("Authorization", "token " + Tissue.token))
      .setErrorHandler(cause -> {
        if (cause.getResponse().getStatus() == 401) {
          System.out.println("Authentication Failed!");
        }
        return cause;
      })
      .setConverter(new GsonConverter(new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()))
      .build();
  }
}
