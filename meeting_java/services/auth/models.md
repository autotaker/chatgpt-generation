# Entity Relation 一覧

## {クラス名}
{エンティティの説明}

- {フィールド名}
  - 説明: {フィールドの説明}
  - 型: {フィールドの型}
  - 制約: {フィールドの制約、カンマ区切り}

## User
ユーザ

- id
  - 説明: ユーザID
  - 型: int
  - 制約: 主キー, 自動採番
- name
  - 説明: ユーザ名
  - 型: string
  - 制約: 必須, 最大文字数: 255
- email
  - 説明: メールアドレス
  - 型: string
  - 制約: 必須, 最大文字数: 255
- password
  - 説明: ハッシュ化されたパスワード
  - 型: string
  - 制約: 必須, 最大文字数: 255、ユニーク
- phone_number
  - 説明: 電話番号
  - 型: string
  - 制約: 最大文字数: 255
- role
  - 説明: ユーザ権限
  - 型: string
  - 制約: 必須, 最大文字数: 255
- created_at
  - 説明: 作成日時
  - 型: datetime
  - 制約: 
- updated_at
  - 説明: 更新日時
  - 型: datetime
  - 制約: 