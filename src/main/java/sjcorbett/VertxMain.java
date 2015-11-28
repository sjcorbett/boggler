package sjcorbett;

import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import sjcorbett.boggle.Boggler;
import sjcorbett.boggle.Grid;
import sjcorbett.boggle.GridReference;
import sjcorbett.boggle.GridWord;
import sjcorbett.boggle.Stats;
import sjcorbett.trie.Trie;
import sjcorbett.trie.Tries;

public class VertxMain {

    public static void main(String[] args) throws Exception {

        Vertx vertx = Vertx.vertx();

        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        URL file = VertxMain.class.getResource("/sowpods.txt");
        final Trie trie = Tries.from(file);
        final Boggler boggler = new Boggler(trie);

        router.route("/bogglemetimbers").handler(BodyHandler.create());

        router.route().handler(CorsHandler.create("*").allowedMethod(HttpMethod.GET));
        router.route("/bogglemetimbers").handler(routingContext -> {

            final String grid = routingContext.request().getParam("grid").toUpperCase();
            final Integer minLength = Integer.valueOf(routingContext.request().getParam("minLength"));

            Grid chars = new Grid(Arrays.stream(grid.split(" "))
                    .map(String::toCharArray)
                    .collect(Collectors.toList())
                    .toArray(new char[0][0]));

            Iterable<GridWord> solved = boggler.solve(chars);
            JsonObject response = makeResponse(chars, solved, minLength);
            routingContext.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString());
            routingContext.response().end(response.encode());
        });

        StaticHandler staticHandler = StaticHandler.create("webroot").setCachingEnabled(false);
        router.route("/static/*").handler(staticHandler);
        router.route("/").handler(staticHandler);

        final Integer port = Integer.getInteger("http.port", 8080);
        final String host = System.getProperty("http.address", "localhost");
        server.requestHandler(router::accept).listen(port, host);

    }

    public static JsonObject makeResponse(Grid grid, Iterable<GridWord> solution, int minLength) {
        // just return a list of all the words.
        Stats stats = new Stats(solution, minLength);

        JsonObject response = new JsonObject();

        JsonObject data = new JsonObject();
        data.put("totalWords", stats.getTotalWords());
        data.put("longestWord", stats.getLongestWord());

        JsonObject bestPoint = summarisePoint(grid, stats.getBestPoint());
        bestPoint.put("count", stats.getBestPointCount());
        data.put("bestPoint", bestPoint);

        JsonObject bestStart = summarisePoint(grid, stats.getBestStart());
        bestStart.put("count", stats.getBestStartCount());
        data.put("bestStart", bestStart);
        response.put("data", data);

        JsonArray words = new JsonArray();
        for (GridWord r : stats.getWords()) {
            if (r.getWord().length() >= minLength) {
                JsonObject obj = new JsonObject();
                obj.put("word", r.getWord());
                JsonArray path = new JsonArray();
                for (GridReference ref : r.getPath()) {
                    path.add(ref.getX());
                    path.add(ref.getY());
                }
                obj.put("path", path);
                words.add(obj);
            }
        }
        response.put("words", words);
        return response;
    }

    private static JsonObject summarisePoint(Grid grid, GridReference reference) {
        JsonObject point = new JsonObject();
        point.put("x", reference.getX());
        point.put("y", reference.getY());
        point.put("letter", grid.letterAt(reference));
        return point;
    }

}
