---
tags: AI gpt-3 ChatGPT
title: ChatGPTによるプログラム生成の可能性と限界（後編） - 3
---
## ビジネスロジック実装

### create_reservationの実装に戻ります。予約情報を保持するReservationクラスを作成してください。

:::note info
次のように、予約情報を保持するReservationクラスを実装します。

```python
class Reservation:
    def __init__(self, name, phone_number, email, time_slot, purpose):
        self.name = name
        self.phone_number = phone_number
        self.email = email
        self.time_slot = time_slot
        self.purpose = purpose
```

このクラスは、次の5つのインスタンス変数を持ちます。

- name: 予約者の名前
- phone_number: 予約者の電話番号
- email: 予約者のメールアドレス
- time_slot: 予約枠
- purpose: 予約の目的
このクラスを使用することで、予約情報を1件ずつ保持することができます。
:::

ここでDBに登録する際には予約管理者のIDを登録する必要があることに気づきました。困りましたね。ChatGPT君と相談しましょう。

### 予約を登録する際に、予約管理者のIDを指定する必要がありました。どのように指定するべきかアイデアを出してください。
:::note info
予約管理者のIDを指定するには、次のような方法が考えられます。

予約フォームに、予約管理者のIDを入力する欄を追加する
予約フォームから送信されたデータを受け取る際に、リクエストのrequest.formから予約管理者のIDを取得する
例えば、予約管理者のIDを入力する欄を追加した場合、次のようなフォームが作成できます。

```html
<form action="/reservations" method="post">
  <label for="name">名前:</label><br>
  <input type="text" id="name" name="name"><br>
  <label for="phone_number">電話番号:</label><br>
  <input type="text" id="phone_number" name="phone_number"><br>
  <label for="email">メールアドレス:</label><br>
  <input type="email" id="email" name="email"><br>
  <label
```
:::

うーんフォームで入力させるのはユーザに優しくない気がしますね。

### 他の案はありますか
:::note info
他の案として、予約管理者のIDをURLのクエリパラメータとして渡す方法が考えられます。この場合、予約フォームのHTMLは次のようになります。

```html
<form action="/reservations?manager_id=1" method="post">
  <label for="name">名前:</label><br>
  <input type="text" id="name" name="name"><br>
  <label for="phone_number">電話番号:</label><br>
  <input type="text" id="phone_number" name="phone_number"><br>
  <label for="email">メールアドレス:</label><br>
  <input type="email" id="email" name="email"><br>
  <label for="time_slot">予約枠:</label><br>
  <input type="text" id="time_slot" name="time_slot"><br>
  <label for="purpose">予約の目的:</label><br>
  <input type="text" id="purpose" name="purpose"><br><br>
  <button type="submit">送信する</button>
</form>
```

この方法では、/reservationsエンドポイントにリクエストが送られる際に、manager_idという名前のクエリパラメータが含まれていることが前提になります。そのため、/reservationsエンドポイントの実装では、リクエストからmanager_idを取得し、それを使って予約管理者を特定することができます。

```python
@app.route('/reservations')
def reservations():
    # リクエストからmanager_idを取得
    manager_id = request.args.get('manager_id')
    # manager_idを使って予約管理者を特定する
    manager = find_manager_by_id(manager_id)
    # 予約管理者のIDを使って予約を登録する
    create_reservation(manager, request.form)
    return '予約が完了しました'
```

上記のようにして、予約管理者のIDを指定することができます。
:::

これはよさそう。

いや本当に君ただの言語モデルなの？だんだん意識があるんじゃないかと思えてくる・・・

実際にはDAOを実装したり、まだもう少しやり取りが続きますが、残りは割愛します。

再掲ですが最終的には以下のようなプロトタイプが動きました。
![chatgpt.gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/67228/6a293e51-e239-4e1a-ee25-ae34d3492f3d.gif)

https://github.com/autotaker/chatgpt-generation/tree/main/meeting


## オチ

### # ありがとうございます。機能が実装できました！
:::note warn
Your authentication token has expired. Please try signing in again.
:::

仕事が終わったらChatGPT君はプライベートに戻っていきました・・・



# 所感
ChatGPTを使えば、実際のコード実装の大部分を自動化することができます。ただし対話によって生成したもののリテイク方法を指示していくスキルが必要なので、お絵描きAIと同じように既にコードを書ける人にとっては非常に強力なツールとなることが予想されます。著作権に関する問題はありますが、今回の例のように十分なコンテキストを与えれば、既存のコードにはあまり依存しないコードが生成できますので、使い方次第かなという感じがします。

一方で出力される内容の信頼性はあまり高くありません。嘘っぽいところ（あまりに自然に嘘をつくので見抜くのも大変です）は別途Google検索で裏を取る必要があります。未経験者がプログラミングの教師として使ったり、ノーコードツールとして使う用途にはまだ向かないでしょう。

使っていて改善を希望するところは部分的な修正をお願いしたときにファイル全体を出力するため、途中で見切れてしまうことが多いところです。diff形式で修正するようになればさらに使いやすくなると思われます。

これは確実に「プログラミング」を変えるツールです。早く正式公開が待ち遠しいです。

