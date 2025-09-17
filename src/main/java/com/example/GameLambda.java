package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

public class GameLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String BUCKET_NAME = System.getenv("BUCKET");
    private final ObjectMapper mapper = new ObjectMapper();
    private final S3Client s3 = S3Client.create();


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        context.getLogger().log("handleRequest triggered");
        try {
            String method = request.getHttpMethod();
            String path = request.getPath();
            context.getLogger().log("method : " + method);
            context.getLogger().log("path : " + path);
            if ("POST".equalsIgnoreCase(method) && path.endsWith("/saveGame")) {
                return saveGame(request,  context.getLogger());
            } else if ("GET".equalsIgnoreCase(method) && path.endsWith("/getGames")) {
                return getGames(context.getLogger());
            } else {
                return response(404, "Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return response(500, "Error: " + e.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent saveGame(APIGatewayProxyRequestEvent request, LambdaLogger logger) throws Exception {
        String body = request.getBody();
        String key = "game-" + Instant.now().toEpochMilli() + ".json";
        logger.log("body : " + body);
        logger.log("key : " + key);
        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .contentType("application/json")
                .build();

        s3.putObject(putReq, software.amazon.awssdk.core.sync.RequestBody.fromBytes(body.getBytes(StandardCharsets.UTF_8)));

        Map<String, String> result = Map.of(
                "message", "Game saved",
                "key", key
        );
        return response(200, mapper.writeValueAsString(result));
    }

    private APIGatewayProxyResponseEvent getGames(LambdaLogger logger) throws Exception {
        ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                .bucket(BUCKET_NAME)
                .build();
        ListObjectsV2Response listRes = s3.listObjectsV2(listReq);

        List<GameResult> games = new ArrayList<>();
        Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
        for (S3Object obj : listRes.contents()) {
            GetObjectRequest getReq = GetObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(obj.key())
                    .build();

            String json = new String(s3.getObjectAsBytes(getReq).asByteArray(), StandardCharsets.UTF_8);
            logger.log("json received : "+ json);
            GameData gameData = mapper.readValue(json, GameData.class);
            Integer[] totals = new Integer[gameData.getTotals().size()];
            String[] names = new String[gameData.getPlayers().size()];
            gameData.getPlayers().toArray(names);
            gameData.getTotals().toArray(totals);
            Map<String, Integer> score = new HashMap<>();
            for (int i = 0; i < names.length; i++) {
                score.put(names[i], totals[i]);
            }
            GameResult gameResult = new GameResult(score);
            games.add(gameResult);
        }
        games.stream().forEach(game -> {
            Map<String, Integer> scores = game.getScores();
            Integer max = Collections.max(scores.values());
            for (Map.Entry<String, Integer> stringIntegerEntry : scores.entrySet()) {
                String name = stringIntegerEntry.getKey();
                name = name.toLowerCase();
                if(name.equals("appu")) name = "arpita";
                if(name.contains("jain")) name = name.split(" ")[0];
                name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                PlayerInfo playerInfo = playerInfoMap.getOrDefault(name, new PlayerInfo(name));
                playerInfo.setNoOfGamesPlayed(playerInfo.getNoOfGamesPlayed() + 1);
                playerInfo.setTotalScore(playerInfo.getTotalScore() + stringIntegerEntry.getValue());
                if(Objects.equals(stringIntegerEntry.getValue(), max)) {
                    playerInfo.setTotalWins(playerInfo.getTotalWins() + 1);
                }
                if(playerInfo.getHighestScore() < stringIntegerEntry.getValue()) {
                    playerInfo.setHighestScore(stringIntegerEntry.getValue());
                }
                if(playerInfo.getLowestScore() > stringIntegerEntry.getValue()) {
                    playerInfo.setLowestScore(stringIntegerEntry.getValue());
                }
                playerInfoMap.put(name, playerInfo);
            }
        });
        List<PlayerInfo> playerInfoList = new ArrayList<>(playerInfoMap.values());
        return response(200, mapper.writeValueAsString(playerInfoList));
    }

    private APIGatewayProxyResponseEvent response(int statusCode, String body) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(Map.of(
                        "Content-Type", "application/json",
                        "Access-Control-Allow-Origin", "*", // Allow all or specific origin
                        "Access-Control-Allow-Headers", "Content-Type,Authorization",
                        "Access-Control-Allow-Methods", "OPTIONS,GET,POST"
                ))
                .withBody(body);
    }
}
