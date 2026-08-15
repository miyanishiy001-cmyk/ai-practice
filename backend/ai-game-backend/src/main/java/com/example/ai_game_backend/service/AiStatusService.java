package com.example.ai_game_backend.service;

import com.example.ai_game_backend.dto.AiStatusResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AiStatusService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * ユーザーの行動テキストを受け取り、AIでステータス数値に変換して返す
     */
    public AiStatusResult analyzeAction(String actionText) {
        try {
            // 1. Gemini API に送るプロンプト（指示文）を作成
            String prompt = buildPrompt(actionText);

            // 2. Gemini API に送るデータ構造（JSON）を組み立てる
            Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                    Map.of("parts", List.of(Map.of("text", prompt)))
                ),
                "generationConfig", Map.of("responseMimeType", "application/json")
            );

            // 3. HTTPヘッダーの設定（JSON形式で送る指定）
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 4. Gemini API へ送信 (POST)
            String fullUrl = apiUrl + "?key=" + apiKey;
            String responseString = restTemplate.postForObject(fullUrl, entity, String.class);

            // 5. 返ってきたレスポンスからAIの回答を取り出して AiStatusResult に変換
            JsonNode root = objectMapper.readTree(responseString);
            String jsonText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

            return objectMapper.readValue(jsonText, AiStatusResult.class);

        } catch (Exception e) {
            e.printStackTrace();
            // 通信エラーなどが起きた場合の安全用のフォールバック（デフォルト値）
            AiStatusResult fallback = new AiStatusResult();
            fallback.setComment("AIの解析に失敗しました: " + e.getMessage());
            return fallback;
        }
    }

    /**
     * キャラクター名と10日間の行動テキストを受け取り、AIでオリジナルの図鑑説明文を生成して返す
     */
    public String generateDexDescription(String characterName, String actionLogs) {
        try {
            String prompt = buildDexPrompt(characterName, actionLogs);

            Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                    Map.of("parts", List.of(Map.of("text", prompt)))
                )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            String fullUrl = apiUrl + "?key=" + apiKey;
            String responseString = restTemplate.postForObject(fullUrl, entity, String.class);

            JsonNode root = objectMapper.readTree(responseString);
            return root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return characterName + "。このキャラクターは様々な経験を経て逞しく成長した！";
        }
    }

    /**
     * AIへの指示文（プロンプト）を作るヘルパーメソッド
     */
    private String buildPrompt(String actionText) {
        return """
            あなたは育成ゲームのステータス判定AIです。
            ユーザーの行動テキストを読み、7つのステータス経験値（合計20〜40ポイント程度）とコメントを決定してください。

            【ステータス基準】
            - hp: 休む、食べる、のんびり
            - atk: 戦う、競い合う
            - def: 守る、片付け、計画
            - str: 運動、冒険、体を動かす
            - intStat: 勉強、読書、プログラミング
            - chm: 芸術、会話、おしゃれ
            - spd: 集中、掃除、素早い行動

            【ユーザーの行動】
            「%s」

            【返却形式】必ず以下のJSON形式のみで返してください。
            {
              "hp": 0, "atk": 0, "def": 0, "str": 0, "intStat": 0, "chm": 0, "spd": 0,
              "comment": "一言フィードバック"
            }
            """.formatted(actionText);
    }

    private String buildDexPrompt(String characterName, String actionLogs) {
        return """
            あなたはクスッと笑える面白いゲームの図鑑テキストを執筆する図鑑作家です。
            以下のキャラクター名と育成中の10回分の行動履歴をもとに、このキャラクターの生態や性格、思い出を語るオリジナルの図鑑説明文（120文字〜200文字程度）を作成してください。

            【キャラクター名】
            %s

            【10回分の行動履歴】
            %s

            【ルール】
            - 行動履歴の要素を面白おかしく盛り込んでください。
            - 説明文テキストのみを直接出力してください（余計な挨拶やコードブロックは不要です）。
            """.formatted(characterName, actionLogs);
    }
}
