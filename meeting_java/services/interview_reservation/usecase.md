# ユースケース
## ユースケース名
カジュアル面談予約

## 登場人物
- 予約者: カジュアル面談を利用する人
- 予約管理者: 採用担当人事等、予約者とカジュアル面談の日程を調整する人
- 面談担当者: カジュアル面談に参加する現場社員

## 画面一覧
- 面談一覧管理画面
  - 予約管理者が閲覧する
  - 自分宛の予約フォームの一覧を確認する
- 面談予約フォーム入力画面
  - 予約者が閲覧する
  - フォームに入力し、送信する
- 面談予約フォーム結果確認画面
  - 予約者が閲覧する
  - 予約フォームを正常に送信したことを確認する
- 面談詳細確認画面
  - 予約管理者が閲覧する
  - 一つの予約フォームの詳細を確認する
  - 面談日時の調整後、決定された日時を入力する

## ユースケース
1. 予約者と予約管理者が何らかの手段で連絡先を交換する。
2. 予約管理者が予約者に対して、カジュアル面談予約フォームのリンクを送信する。
3. 予約者は送られてきたリンクを開き、希望する日時を１つから３つ指定し、予約フォームを送信する
4. 予約管理者は希望日時をもとに面談日時と面談担当者を調整する
  - 調整に失敗した場合、2からやり直す
5. 決定した日時をシステムに入力する。システムから予約者、面談担当者にメールで通知する。

## 機能要件
- 予約管理者はシステムのログインである。
- 予約管理者は面談一覧管理画面から予約フォームのリンクを発行できる。予約フォームは認証なしで一回のみ送信可能。
- 予約者は予約フォームから予約者の氏名、連絡先メールアドレス、希望日時、自由記述欄を入力する
- 予約管理者は面談詳細確認画面から確定した面談日時、面談方法を入力し確定ボタンを押すことで面談日時を確定する
- 面談予約が確定した時点でシステムから予約者、面談担当者にメールを送信する。