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
