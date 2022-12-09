# REST API Endpoint一覧
## 記法
- {HTTP Method} {Endpoint Path}
  - {説明}
  - {アクセス権}

## 一覧
- POST /api/v1/auth/signup
  - ユーザを登録する
  - 認証なし
- POST /api/v1/auth/signin
  - ログインする
  - 認証なし
- POST /api/v1/auth/password_reset_link
  - パスワードリセットリンクを発行し、メールで送る
  - 認証なし
- POST /api/v1/auth/password_reset
  - パスワードをリセットする
  - 認証なし
- GET /api/v1/user
  - 自身のユーザ情報を取得する
  - 認証あり
- PATCH /api/v1/user
  - 自身のユーザ情報を変更する
  - 認証あり
- DELETE /api/v1/user
  - 自身のユーザを削除する
  - 認証あり
- GET /api/v1/admin/users
  - ユーザ一覧を取得する
  - 管理者ユーザのみ
- GET /api/v1/admin/users/{id}
  - ユーザを取得する
  - 管理者ユーザのみ
- PATCH /api/v1/admin/users/{id}
  - ユーザ情報を変更する
  - 管理者ユーザのみ
- DELETE /api/v1/admin/users/{id}
  - ユーザを削除する
  - 管理者ユーザのみ
