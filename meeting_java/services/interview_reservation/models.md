# Entity Relation 一覧
## {クラス名}
{エンティティの説明}

- {フィールド名}
  - 説明: {フィールドの説明}
  - 型: {フィールドの型}
  - 制約: {フィールドの制約、カンマ区切り}

## User
ユーザー

- id
  - 説明: ユーザーID
  - 型: int
  - 制約: 自動採番
- name
  - 説明: ユーザー名
  - 型: string
  - 制約: 最大文字数100
- email
  - 説明: メールアドレス
  - 型: string
  - 制約: 最大文字数100
- password
  - 説明: パスワード
  - 型: string
  - 制約: 最大文字数100
- role
  - 説明: ユーザー権限
  - 型: string
  - 制約: 最大文字数100

## Interviewer
面談担当者

- id
  - 説明: 面談担当者ID
  - 型: int
  - 制約: 自動採番
- name
  - 説明: 面談担当者名
  - 型: string
  - 制約: 最大文字数100
- email
  - 説明: 面談担当者メールアドレス
  - 型: string
  - 制約: 最大文字数100

## InterviewDateCandidate
面談日時候補

- id
  - 説明: 面談日時候補ID
  - 型: int
  - 制約: 自動採番
- interview_id
  - 説明: 面談ID
  - 型: int
  - 制約: 外部キー
- interview_date
  - 説明: 面談日時
  - 型: datetime
  - 制約: null不可

## Interview
面談

- id
  - 説明: 面談ID
  - 型: int
  - 制約: 自動採番
- interviewer_id
  - 説明: 面談担当者ID
  - 型: int
  - 制約: 外部キー
- manager_id
  - 説明: 面談管理者ID
  - 型: int
  - 制約: 外部キー
- interviewee_name
  - 説明: 面談者名
  - 型: string
  - 制約: 最大文存数100
- interviewee_email
  - 説明: 面談者メールアドレス
  - 型: string
  - 制約: 最大文字数100
- interviewee_comment
  - 説明: 面談者コメント
  - 型: string
  - 制約: 最大文字数1000
- interview_date
  - 説明: 面談日時
  - 型: datetime
  - 制約: null可
- interview_method
  - 説明: 面談方法
  - 型: string
  - 制約: 最大文字数100
- interview_status
  - 説明: 面談ステータス
  - 型: string
  - 制約: 最大文字数100
- interview_comment
  - 説明: 面談コメント
  - 型: string
  - 制約: 最大文字数1000
