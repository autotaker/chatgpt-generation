これから実装するウェブアプリはフロントエンドとバックエンドに分かれており、
バックエンドはREST APIサーバであり、JSON形式で結果を返す。

# アーキテクチャ
バックエンドサーバのアーキテクチャは以下の三層構造である
- Controller
  - リクエストのバリデーションを担当
- Service
  - ビジネスロジックを担当
- Repository
  - データベースアクセスを担当する
  
またデータモデルは以下の２種類がある。
- ValueObject
  - 不変のデータを表す。
  - データ構造によって区別される
- Entity
  - ライフサイクルのあるデータを表現する
  - 識別子によって区別される
  
# 技術スタック
- 言語: Java17
- Web Framework: SpringBoot
- O/R Mapper: Spring Data JPA
- ビルドツール: Gradle

これから以下のエンドポイントを実装する。

- エンドポイント: `POST /reservations`
- URL Parameters
  - manager_id: 予約管理者のID
- Form
  - name: 名前
  - email: 連絡先メールアドレス
  - phone_number: 連絡先電話番号
  - time_slot: 予約時間
  - purpose: 面談の目的
- Response
  - 成功時:
    - status: "ok"
    - id: 予約ID
  - 失敗時:
    - status: "error"
    - error_code: int
    - reason: string
- テーブル構成
  - reservationsテーブル
    - reservation_id
    - name
    - email
    - phone_number
    - time_slot: DateTime
    - purpose
    - manager_id
    - created_at
    - updated_at
  - reservation_managersテーブル
    - manager_id
    - name
    - email
    - phone_number
    - created_at
    - updated_at
- ビジネスロジック
  - フォームから予約情報を読み取る。
  - リクエストパラメータから予約管理者のIDを読み取る。
  - IDから予約管理者を検索し、予約情報を登録し、予約IDを返す
  - 予約管理者が存在しない場合、エラーを返す

# クラス一覧
## Controller
- ReservationController
  - リクエストのバリデーションを担当
  - リクエストをServiceに渡す
  - Serviceからのレスポンスを返す
## Service
- ReservationService
  - ビジネスロジックを担当
  - Repositoryからデータを取得し、ビジネスロジックを実行する
  - ビジネスロジックの結果を返す
## Entity
- Reservation
  - 予約情報を表現する
- ReservationManager
  - 予約管理者を表現する
## Repository
- ReservationRepository
- ReservationManagerRepository
## ValueObject
- ReservationForm
  - 予約情報を表現する
- ReservationResponse
  - 予約結果を表すインターフェース
- ReservationSuccessResponse implements ReservationResponse
  - 予約情報を表現する
- ReservationErrorResponse implements ReservationResponse
  - エラー情報を表現する

# 実装
## 記法
```java:{ClassName}.java
package org.autotaker.gpt_gen.meeting.reservations;

import ...

public class {ClassName} {
    ...
}
```
