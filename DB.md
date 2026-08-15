# データベース設計書 (DB.md)

本ドキュメントは「AI一体型育成図鑑ゲーム」のデータベース（H2 Database / JPA）に関するテーブル定義および運用ルールを記録した仕様書です。
`ai-rules.md` に従い、本仕様書で決定したテーブル名・カラム名・データ型は以降のフェーズでも厳格に順守します。

---

## 1. 全体構成概要

本システムでは、以下の 3 つのテーブルでユーザー管理・リアルタイム育成・完成図鑑コレクションを保持します。

1. **`users`**: ユーザーのログイン認証およびアカウント情報
2. **`current_character`**: ユーザーが現在育てている1体のリアルタイム状態（タマゴ〜成長期）
3. **`character_dex`**: 育成完了したキャラクターの図鑑コレクション（全30種スロット＋同一キャラの全世代履歴保持）

---

## 2. テーブル定義詳細

### 2.1. `users` テーブル（ユーザー情報）

ユーザーのログイン認証およびアカウント識別情報を管理します。

| カラム名 (Column) | データ型 (Type) | 制約                        | 説明                 |
| :---------------- | :-------------- | :-------------------------- | :------------------- |
| `id`              | BIGINT          | PRIMARY KEY, AUTO_INCREMENT | ユーザー識別用主キー |
| `username`        | VARCHAR(50)     | UNIQUE, NOT NULL            | ログインID           |
| `password`        | VARCHAR(255)    | NOT NULL                    | パスワード           |
| `nickname`        | VARCHAR(50)     | NOT NULL                    | プレイヤー表示名     |
| `created_at`      | TIMESTAMP       | DEFAULT CURRENT_TIMESTAMP   | アカウント作成日時   |

---

### 2.2. `current_character` テーブル（現在育成中のキャラクター）

各ユーザーが現在進行形で育てている1体のキャラクター情報を管理します。行動10回完了時に判定が行われ、`character_dex` へ保存された後リセットされます。

| カラム名 (Column) | データ型 (Type) | 制約                        | 説明                                                         |
| :---------------- | :-------------- | :-------------------------- | :----------------------------------------------------------- |
| `id`              | BIGINT          | PRIMARY KEY, AUTO_INCREMENT | 育成レコード識別用主キー                                     |
| `user_id`         | BIGINT          | FOREIGN KEY, NOT NULL       | どのユーザーの育成データか (`users.id` 参照)                 |
| `stage`           | VARCHAR(20)     | NOT NULL                    | 育成段階 (`EGG`, `BABY`, `CHILD`, `ADULT`)                   |
| `generation`      | INT             | NOT NULL                    | 何世代目の育成か (1, 2, 3...)                                |
| `action_count`    | INT             | NOT NULL                    | 現在の行動回数 (0 〜 10)                                     |
| `hp`              | INT             | NOT NULL                    | HP (体力経験値)                                              |
| `atk`             | INT             | NOT NULL                    | ATK (攻撃力経験値)                                           |
| `def`             | INT             | NOT NULL                    | DEF (防御力経験値)                                           |
| `str`             | INT             | NOT NULL                    | STR (筋力経験値)                                             |
| `int_stat`        | INT             | NOT NULL                    | INT (知力経験値) ※ `INT` はSQL予約語のため `int_stat` とする |
| `chm`             | INT             | NOT NULL                    | CHM (魅力経験値)                                             |
| `spd`             | INT             | NOT NULL                    | SPD (素早さ経験値)                                           |
| `action_logs`     | TEXT            | -                           | 10回分の行動テキスト履歴                                     |

---

### 2.3. `character_dex` テーブル（図鑑コレクション＆育成履歴）

10回の行動が完了し、Javaで進化先が確定＋AIで図鑑説明文が生成されたキャラクターを保存します。同一キャラクター（同じ `character_number`）を複数回育てた場合も別行として蓄積されます。

| カラム名 (Column)  | データ型 (Type) | 制約                        | 説明                                         |
| :----------------- | :-------------- | :-------------------------- | :------------------------------------------- |
| `id`               | BIGINT          | PRIMARY KEY, AUTO_INCREMENT | 図鑑レコード識別用主キー                     |
| `user_id`          | BIGINT          | FOREIGN KEY, NOT NULL       | どのユーザーの図鑑データか (`users.id` 参照) |
| `generation`       | INT             | NOT NULL                    | 育成完了時の世代数                           |
| `character_number` | INT             | NOT NULL                    | モンスター種族番号 (No.1 〜 No.30)           |
| `character_name`   | VARCHAR(100)    | NOT NULL                    | モンスター種族名 (例: 「ドラゴンマスター」)  |
| `description`      | TEXT            | NOT NULL                    | AIが行動履歴から生成した図鑑解説文           |
| `hp`               | INT             | NOT NULL                    | 完成時 HP                                    |
| `atk`              | INT             | NOT NULL                    | 完成時 ATK                                   |
| `def`              | INT             | NOT NULL                    | 完成時 DEF                                   |
| `str`              | INT             | NOT NULL                    | 完成時 STR                                   |
| `int_stat`         | INT             | NOT NULL                    | 完成時 INT (知力)                            |
| `chm`              | INT             | NOT NULL                    | 完成時 CHM                                   |
| `spd`              | INT             | NOT NULL                    | 完成時 SPD                                   |
| `history_summary`  | TEXT            | -                           | 育成時の全行動テキストまとめ                 |
| `created_at`       | TIMESTAMP       | DEFAULT CURRENT_TIMESTAMP   | 図鑑登録完了日時                             |

---
