# 🚀 リリース・Web公開ガイド (DEPLOY.md)

本ドキュメントは「AI一体型育成図鑑ゲーム」をインターネット上に無料/低コストでWeb公開（デプロイ）するための手順書です。

---

## 🏗️ 全体構成

- **フロントエンド (React / Vite)**: Render Static Site または Vercel（無料）
- **バックエンド (Java 17 / Spring Boot)**: Render Web Service（無料〜低コスト）
- **データベース**: H2 ファイル保存モード (デフォルト) または Render PostgreSQL

---

## 📌 手順 1: GitHub へのコードプッシュ

1. ローカルの変更を Git でコミットし、GitHub にプッシュします。
   ```bash
   git add .
   git commit -m "Web公開用の本番設定とレスポンシブ対応完了"
   git push origin dev
   ```

---

## 📌 手順 2: 永続化データベース (PostgreSQL) の作成 (Render.com)

1. [Render.com](https://render.com/) にログインし、「**New +**」→「**PostgreSQL**」を選択。
2. 以下の設定を入力して「**Create Database**」をクリック（無料プラン対応）：
   - **Name**: `ai-game-db`
   - **Database**: `aigamedb`
   - **User**: `aigameuser`
3. 作成完了画面で **Internal Database URL**（例: `postgresql://aigameuser:...@dpg-...-a/aigamedb`）をコピーします。
   ※ JDBC形式のURL: `jdbc:postgresql://dpg-...-a.render.com:5432/aigamedb?ssl=true&sslmode=require`

---

## 📌 手順 3: バックエンド (Java) のデプロイ (Render.com)

1. Render で「**New +**」→「**Web Service**」を選択。
2. GitHub リポジトリ (`ai-practice`) を連携し、`dev` ブランチを選択。
3. 以下の設定を入力：
   - **Name**: `ai-game-backend`
   - **Root Directory**: `backend/ai-game-backend`
   - **Build Command**: `./mvnw clean package -DskipTests`
   - **Start Command**: `java -jar target/ai-game-backend-0.0.1-SNAPSHOT.jar`
4. **Environment Variables（環境変数）** を設定：
   - `GEMINI_API_KEY`: お手持ちの Gemini API キー
   - `SPRING_DATASOURCE_URL`: `jdbc:postgresql://[PostgreSQLのホスト]:5432/aigamedb?sslmode=require`
   - `SPRING_DATASOURCE_DRIVER`: `org.postgresql.Driver`
   - `SPRING_DATASOURCE_USERNAME`: PostgreSQLのユーザー名
   - `SPRING_DATASOURCE_PASSWORD`: PostgreSQLのパスワード
5. 「**Create Web Service**」をクリック。ビルド完了後、バックエンドのURL（例: `https://ai-game-backend.onrender.com`）が発行されます。

---

## 📌 手順 4: フロントエンド (React) のデプロイ (Vercel または Render)

1. Render で「**New +**」→「**Static Site**」を選択（または Vercel でプロジェクトインポート）。
2. リポジトリを設定：
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Publish Directory**: `frontend/dist`
3. 発行されたフロントエンドURL（例: `https://ai-game.onrender.com`）にアクセスすればプレイ可能です！

---

## 🔐 環境変数の設定一覧

| 環境変数名 | 説明 | 本番推奨設定例（Render PostgreSQL） | ローカルデフォルト値 |
| :--- | :--- | :--- | :--- |
| `GEMINI_API_KEY` | Google Gemini API キー | `AQ.Ab8RN...` | (デフォルト値あり) |
| `SPRING_DATASOURCE_URL` | 本番用DB URL | `jdbc:postgresql://dpg-xxxx.render.com:5432/aigamedb?sslmode=require` | `jdbc:h2:file:./data/aigamedb` |
| `SPRING_DATASOURCE_DRIVER` | DB ドライバー | `org.postgresql.Driver` | `org.h2.Driver` |
| `SPRING_DATASOURCE_USERNAME` | DB ユーザー名 | Renderで作成したUser名 | `sa` |
| `SPRING_DATASOURCE_PASSWORD` | DB パスワード | Renderで作成したPassword | `(空)` |
