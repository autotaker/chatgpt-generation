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
  - エラー時には例外ServiceExceptionを投げる
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
- AppResponse
  - エンドポイントの実行結果を表すインターフェース
  - getStatus()
- ReservationResponse implements AppResponse
  - 予約作成時の成功結果を表すクラス
  - getReservationId(): 予約ID
- ReservationManagerResponse implements AppResponse
  - 予約管理者登録時の成功結果を表すクラス
  - getReservationManagerId(): 予約管理者ID
- ErrorResponse implements AppResponse
  - エラー時の結果を表すクラス
  - getErrorCode(): エラーコード
  - getReason(): エラーの原因を表す文字列
- ServiceException
  - Serviceが送出する、ビジネスロジックの異常系を表す例外クラス
  - getErrorResponse()
- ErrorCode
  - エラーコードを表すenum

# テーブル構成

## reservationsテーブル
- reservation_id
- name
- email
- phone_number
- time_slot: DateTime
- purpose
- manager_id
- created_at
- updated_at

## reservation_managersテーブル
- manager_id
- name
- email
- phone_number
- created_at
- updated_at

# エンドポイント
これから以下のエンドポイントを実装する。

- エンドポイント: `POST /managers`
- Form
  - name: 名前
  - email: 連絡先メールアドレス
  - phone_number: 連絡先電話番号
- Response
  - 成功時: 200 OK
    - status: "ok"
    - reservation_manager_id: 予約管理者ID
  - 失敗時: 409 Conflict
    - status: "error"
    - error_code: エラーコードを表す識別子
    - reason: エラーの原因を説明する文字列
- ビジネスロジック
  - フォームから予約管理者のデータを読み取る。
  - 連絡先メールアドレスで予約管理者を検索し、既に存在していればエラーを返す
  - 予約管理者が存在しない場合、新規に作成する

# 実装
## 記法
```java:{ClassName}.java
package org.autotaker.gpt_gen.meeting.reservations;

import ...

public class {ClassName} {
    ...
}
```
