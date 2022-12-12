# アーキテクチャ
これから実装するウェブアプリはフロントエンドとバックエンドに分かれており、
バックエンドはREST APIサーバであり、JSON形式で結果を返す。

## クラス設計
バックエンドサーバのアーキテクチャは以下の三層構造である
- Controller
  - ApplicationServiceに依存
  - リクエストのバリデーションを担当
  - 返り値の型はHttpEntity<AppResponse>
    - AppResponseはgetMessage()を持つインターフェース
    - 各ResponseクラスはAppResponseを実装する
  - Serviceが送出した例外を補足し、ServiceExceptionの場合はそのstatusCodeをもとにエラーレスポンスを返す。それ以外の例外の場合は500 Internal Server Errorを返す。
- Service
  - DomainService
    - Repositoryに依存
    - ドメインオブジェクト間の関係性に関するロジックを記述する
  - ApplicationService
    - DomainService, Repositoryを用いてユースケースを組み立てる
- Repository
  - データベースアクセスを担当する
  
## データモデル
データモデルは以下の２種類がある。
- ValueObject
  - 不変のデータを表す。
  - データ構造によって区別される
- Entity
  - ライフサイクルのあるデータを表現する
  - 識別子によって区別される
  
## 例外処理
- ServiceException
  - 検査例外
  - statusCode: HTTPステータスコード
  - errorCode: エラーコード
  - message: エラーメッセージ
  - サブクラス
    - ValidationException
      - statusCode: 400
      - ドメインオブジェクトに由来するエラー
    - ConflictException
      - statusCode: 409
      - データ競合に由来するエラー
      
 ## Dependency Injection
 - 実装クラス
   - {インターフェース名}Implという命名規則に従い、{インターフェース名}を実装する
   - インターフェースに依存し、実装クラスには依存しない。
   - 依存性注入は@Autowiredで行う
  
# 技術スタック
- 言語: Java17
- Web Framework: SpringBoot
- O/R Mapper: Spring Data JPA
- ビルドツール: Gradle
