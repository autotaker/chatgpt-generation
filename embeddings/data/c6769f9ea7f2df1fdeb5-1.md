---
tags: AI gpt-3 ChatGPT
title: ChatGPTによるプログラム生成の可能性と限界（後編）- 1
---
# はじめに
この記事では最近話題のChatGPTによってプログラムを生成する際のコツについて解説します。

前編はこちら
https://qiita.com/autotaker1984/items/5b5ac8c01d11fbbbc4a7


# コードを生成するのではなく、コードを生成する過程を生成する
ChatGPTは言語モデルベースのAIです。言語モデルとは、お題（プロンプト）に沿った文章を生成するモデルです。それ以上でもそれ以下でもありません。

従ってなんらかの機能を実装してもらう際も**いきなり「機能」から「コード」の生成だとあまり満足いく結果は得られません。** もちろんChatGPTはかなり博識なのでそれっぽいコードは出してきます。ただ、そのような生成の仕方だとChatGPTが学習したコードにかなり依存したものが出力されるため、実際のユースケースとはズレたものが生成されますし、生成物の著作権リスクが大きくなります。

一方で実装しようとしているものの仕様や、ドメイン知識を十分に多く含めれば、ChatGPTは驚くほど賢くそれらの文脈を理解して適切な実装を生成してくれます。
そのため、コードを生成する際にはChatGPTとペアプロしているように、何回も対話を重ねながら少しずつ目的のものに近づけていくとうまくいきます。

# デモ
今回ChatGPTを使って簡単なWEBアプリを作成してみることにしました。
最終的には以下の画面のようなプロトタイプをつくることができました。
フォームを表示し、POSTされたデータをDBに登録することができています。

![chatgpt.gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/67228/6a293e51-e239-4e1a-ee25-ae34d3492f3d.gif)

詳細な対話の内容とコードは下記レポジトリにて公開しています。対話全体は少し長いため、この記事ではそのハイライトをお伝えします。

https://github.com/autotaker/chatgpt-generation/tree/main/meeting

## 要件定義
まずは作りたいもののアイデアをChatGPTに伝えてアイデアを深めてもらいました。
以下、見出しで私が入力したプロンプト、`note info`でその結果を表記します。


### カジュアル面談を予約する機能を実装したいです。要件を整理してください

:::note info
カジュアル面談を予約するための要件を整理します。

- 用意された時間帯から予約可能な時間を選択できるようにする。
- 予約する際に、利用者の名前、電話番号、メールアドレス、および予約したい時間を入力するフォームを用意する。
- 予約が完了したら、利用者に予約の確認メールを送信する。
- 予約が完了すると、管理者にも予約内容が通知されるようにする。
- 管理者は予約を確認、承認、またはキャンセルすることができるようにする。
- 予約がキャンセルされた場合は、利用者に通知する。
上記のような要件を実装することで、カジュアル面談の予約を管理することができます。
:::

いきなり、それっぽいてユーザストーリーを生成してくれました。利用者、管理者がいるみたいですね。管理者は面談をホストする側ということのようです。

予約フォームの仕様について聞いてみましょう。

### 入力フォームではどのような情報を入力しますか

:::note info
入力フォームでは、利用者の名前、電話番号、メールアドレス、および予約したい時間を入力する必要があります。これらの情報は、カジュアル面談の予約を管理するために必要です。また、予約が完了したときに、予約内容を利用者と管理者に通知するために利用されます。
:::

ちょっと味気ないフォームですね。自由記述欄もつけてもらいます。

### フォームでは面談の目的も入力したいです

::::note info
それは良いアイデアです。カジュアル面談の目的をフォームに入力することで、管理者は予約を確認しやすくなります。また、面談についての情報を提供することで、利用者が予約を行う前に自分が望む面談の目的を確認できるようになります。

ただし、面談の目的を入力する場合は、利用者にわかりやすいようにフォームをデザインし、適切な入力項目を用意する必要があります。例えば、面談の目的を選択式で選択できるようにするか、テキスト入力で自由に入力できるようにするなどが考えられます。

また、面談の目的が不明確な場合や、利用者が自由に入力できる場合は、入力内容を管理者が確認し、適切な面談の目的を決める必要があります。

以上を踏まえると、面談の目的を入力することで、カジュアル面談の予約をよりスムーズかつ円滑に管理することができるようになります。
::::

ChatGPT君が誉めてくれました。こんな気配りまでできるなんて普通に嬉しいですね。
しかも、入力方法として、選択式か自由記述かの二つの案を提案してくれています。確かに選択式の方が入力しやすい気もします。まあ一旦は自由記述で進めることにします。

## テーブル設計

### それでは予約フォームをPostgresqlデータベースに登録するテーブル定義を設計してください

:::note info
予約フォームをPostgresqlデータベースに登録するためのテーブル定義を提案します。

まず、予約内容を保存するためのreservationsテーブルを作成します。このテーブルには、利用者の名前、電話番号、メールアドレス、予約したい時間、および面談の目的などの情報を保存します。

```sql
CREATE TABLE reservations (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  email TEXT NOT NULL,
  time_slot TIMESTAMP NOT NULL,
  purpose TEXT NOT NULL
);
```

次に、予約の管理を行うためのreservation_managersテーブルを作成します。このテーブルには、管理者の名前、電話番号、メールアドレス、および管理権限などの情報を保存します。

```sql
CREATE TABLE reservation_managers (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  email TEXT NOT NULL,
  permission BOOLEAN NOT NULL
);
```

最後に、reservationsテーブルとreservation_managersテーブルを紐付けるためのreservation_manager_reservationsテーブルを作成します。このテーブルには、予約を行った管理者と予約内容を紐付ける情報を保存します。

```sql
CREATE TABLE reservation_manager_reservations (
  id SERIAL PRIMARY KEY,
  reservation_id INTEGER REFERENCES reservations(id),
  reservation_manager_id INTEGER REFERENCES reservation_managers(id),
  status TEXT NOT NULL
);
```

以上のようなテーブル定義を用いることで、カジュアル面談の予約内容を管理することができます。
:::

サクッとDBのテーブル設計をしてくれました。カラム名も適切なものになっていますね。ただ、`reservation_managers`と`resevations`がN:M関係なのはやりすぎな気もしますのでリテイクをお願いしましょう。

### reservationsとreservation_managersはN:1対応にしたいです

:::note info
N:1対応を実現するには、reservationsテーブルにreservation_manager_idというカラムを追加し、これをreservation_managersテーブルのidと紐付けるようにします。また、reservation_manager_reservationsテーブルは不要となるため、削除します。

次のようなテーブル定義になります。

```sql
CREATE TABLE reservations (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  email TEXT NOT NULL,
  time_slot TIMESTAMP NOT NULL,
  purpose TEXT NOT NULL,
  reservation_manager_id INTEGER REFERENCES reservation_managers(id)
);
CREATE TABLE reservation_managers (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  email TEXT NOT NULL,
  permission BOOLEAN NOT NULL
);
```
以上のようにすることで、reservationsテーブルとreservation_managersテーブルはN:1の対応となります。
:::

嫌味ひとつ言わずにリテイクに対応してくれました。結果も完全に期待通りです。優秀ですね。

## フォーム設計
テーブル設計ができたので予約フォームの移りましょう。

### 次に予約フォームのHTMLはどのようになりますか？

:::note info
予約フォームのHTMLは次のようになります。

```html
<form action="[予約フォームを送信するURL]" method="post">
  <label for="name">名前:</label>
  <input type="text" id="name" name="name">
  <br>
  <label for="phone_number">電話番号:</label>
  <input type="text" id="phone_number" name="phone_number">
  <br>
  <label for="email">メールアドレス:</label>
  <input type="text" id="email" name="email">
  <br>
  <label for="time_slot">予約時間:</label>
  <input type="datetime-local" id="time_slot" name="time_slot">
  <br>
  <label for="purpose">面談の目的:</label>
  <input type="text" id="purpose" name="purpose">
  <br>
  <input type="submit" value="予約する">
</form>
```
上記のフォームでは、名前、電話番号、メールアドレス、予約時間、および面談の目的を入力することができます。また、フォームの送信ボタンを押すと、入力された内容が送信されます。

なお、上記のHTMLはあくまでも一例であり、実際に利用する際には、フォームのデザインや入力項目を適切に調整する必要があります。
:::

先にテーブル設計を済ませていたので、空気を読んでカラム名とフォームのフィールド名を対応させてくれていますね。こういう気遣いがありがたい。
