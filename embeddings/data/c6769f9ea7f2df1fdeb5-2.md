---
tags: AI gpt-3 ChatGPT
title: ChatGPTによるプログラム生成の可能性と限界（後編） - 2
---
## サーバ実装
フォームもできたので実際に画面を作って表示させてみましょう。まずはPOSTエンドポイントから

### それではエンドポイント名は/reservationsにしましょう。サーバのエンドポイント実装の雛形をPythonのflaskフレームワークで実装してください
:::note info
次のようなPythonのflaskフレームワークを用いたエンドポイント実装の雛形を提案します。

```python
from flask import Flask, request
app = Flask(__name__)
@app.route('/reservations', methods=['POST'])
def create_reservation():
  # リクエストから予約内容を取得
  name = request.form['name']
  phone_number = request.form['phone_number']
  email = request.form['email']
  time_slot = request.form['time_slot']
  purpose = request.form['purpose']

  # 予約内容をデータベースに保存

  # TODO: 保存処理の実装

  # 予約完了のレスポンスを返す
  return '予約が完了しました'
if __name__ == '__main__':
  app.run()
```

上記のコードでは、/reservationsエンドポイントに対してPOSTメソッドでリクエストが送信された場合、create_reservation関数が実行されます。

create_reservation関数では、リクエストから予約内容を取得し、データベースに保存します。また、予約完了のレスポンスを返します。

なお、上記のコードはあくまでも雛形であり、実際に利用する際には、必要な処理を追加して適切に実装する必要があります。
:::

それっぽいコードが生成されています。「雛形」としたのでスタブだけの実装になっているのも意図通りです。次はGETエンドポイントを定義しましょう。

### /にアクセスしたらフォームのHTMLを返すようにしてください。

:::note info
次のように、/エンドポイントに対してGETメソッドでリクエストが送信された場合、予約フォームのHTMLを返すようにします。

```python
from flask import Flask, request
app = Flask(__name__)
@app.route('/', methods=['GET'])
def get_reservation_form():
  # 予約フォームのHTML
  form = """
  <form action="/reservations" method="post">
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
  """

  # 予約フォームのHTMLを返す
  return form
```
:::

完璧ですね・・・末恐ろしい子です。ただフォームを文字列でハードコードされると辛いので
別ファイルに切り出したいところです。

### フォームのHTMLをjinja2テンプレートを使って外部ファイルから読み込んでください

:::note info
次のように、jinja2テンプレートを用いて予約フォームのHTMLを外部ファイルから読み込むようにします。

まず、予約フォームのHTMLを、reservation_form.htmlという名前のファイルに保存します。

```html
<form action="/reservations" method="post">
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
次に、jinja2テンプレートを用いて、上記のreservation_form.htmlファイルを読み込むようにします。

```python
from flask import Flask, request, render_template
app = Flask(__name__)
@app.route('/', methods=['GET'])
def get_reservation_form():
  # jinja2テンプレートを用いてreservation_form.htmlを読み込む
  form = render_template('reservation_form.html')
  # 予約フォームのHTMLを返す
  return form
@app.route('/reservations', methods=['POST'])
def create_reservation():
  # リクエストから予約内容を取得
  name = request.form['name']
  phone_number = request.form['phone_number']
  email = request.form['email']
  time_slot = request.form['time_slot']
  purpose = request.form['purpose']
  # 予約内容をデー
```
:::

マジで賢いですね。jinja2テンプレートなので継承もできるかお願いしてみましょう。

### reservation_form.htmlはHTMLの構造が不足しています。baseテンプレートを継承するように修正してください

:::note info
次のように、reservation_form.htmlを修正することで、HTMLの構造を完成させることができます。

まず、base.htmlという名前のテンプレートを作成します。このテンプレートでは、HTMLの基本構造や必要なCSSやJavaScriptなどを記述します。

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>カジュアル面談予約フォーム</title>
    <!-- 必要なCSSやJavaScriptなど -->
  </head>
  <body>
    <h1>カジュアル面談予約フォーム</h1>
    {% block content %}{% endblock %}
  </body>
</html>
```

次に、reservation_form.htmlを修正します。このファイルでは、予約フォームのHTMLを記述し、base.htmlを継承するようにします。

```html
{% extends "base.html" %}

{% block content %}
  <form action="/reservations" method="post">
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
```
:::


素晴らしいですが、下の方が見切れてしまっています。こんなときは「続けて」というと続きを書いてくれます。

:::note info
そうですね。次のように、reservation_form.htmlを修正します。

```html
{% extends "base.html" %}

{% block content %}
  <form action="/reservations" method="post">
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
{% endblock %}
```
:::

特に問題ないですね。実際に動かしてみましょう。

```console
flask --debug run 
```

あれ、エラーになりました。エラーの治し方も聞いてみます。

### `jinja2.exceptions.TemplateNotFound: reservation_form.html` というエラーが発生しました

:::note info
このエラーは、reservation_form.htmlという名前のテンプレートが見つからないことを示しています。
このエラーが発生した場合は、次のことを確認してください。
reservation_form.htmlが保存されているディレクトリが、Flaskアプリケーションのtemplatesディレクトリの中にあるか
Flaskアプリケーション側で、templatesディレクトリをテンプレートの保存先として指定しているか
また、Flaskアプリケーション側で、次のように、render_template()関数を使ってreservation_form.htmlを読み込んでいるかも確認してください。
```python
from flask import Flask, render_template
app = Flask(__name__)
@app.route('/')
def index():
    return render_template('reservation_form.html')
```
上記の問題を修正することで、このエラーを解消することができます。
:::

`templates`以下にテンプレートを配置しないといけないのですね。ファイルの場所を修正したら画面が表示され、フォームの送信も成功しました。


## デザイン
画面が味気ないのでいい感じにしてもらいましょう。

#### bootstrap4を使ってフォームのデザインをしてください

:::note info
次のように、reservation_form.htmlを修正します。ここでは、Bootstrap 4を使って予約フォームのデザインをしています。

まず、base.htmlで必要なCSSとJavaScriptを読み込むようにします。

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>カジュアル面談予約フォーム</title>
    <!-- Bootstrap 4を読み込む -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN"
```

次のように、reservation_form.htmlを修正します。

```html
{% extends "base.html" %}

{% block content %}
  <!-- BootstrapのContainerクラスを用いて、フォームを中央寄せする -->
  <div class="container">
    <h1>カジュアル面談予約フォーム</h1>
    <form action="/reservations" method="post">
      <div class="form-group">
        <label for="name">名前:</label>
        <!-- BootstrapのForm Controlクラスを用いて、フォームの入力欄をデザインする -->
        <input type="text" class="form-control" id="name" name="name">
      </div>
      <div class="form-group">
        <label for="phone_number">電話番号:</label>
        <input type="text" class="form-control" id="phone_number" name="phone_number">
      </div>
      <div class="form-group">
        <label for="email">メールアドレス:</label>
        <input type="text" class="form-control" id="email" name="email">
      </div>
      <div class="form-group">
        <label for="time_slot">予約時間:</label>
        <input type="datetime-local" class="form-control" id="time_slot" name="time
```
:::


いやー画面のデザインってめんどくさかったんですが、ChatGPT君に任せると本当に楽です。てかCDNのURLとかintegrityまで含めてよくそらで言えるね。記憶力半端ない。

ただ後半が見切れているのがちょっと困りますね。何度かトライしたのですが文字数が多いせいかどうしても見切れてしまいます。しょうがないので裏技を使います。


### buttonを一番上に持ってきて


:::note info
次のように、reservation_form.htmlを修正します。
```html
{% extends "base.html" %}
{% block content %}
  <!-- BootstrapのContainerクラスを用いて、フォームを中央寄せする -->
  <div class="container">
    <!-- BootstrapのButtonクラスを用いて、送信ボタンをデザインする -->
    <button type="submit" class="btn btn-primary">予約する</button>
    <h1>カジュアル面談予約フォーム</h1>
    <form action="/reservations" method="post">
      <div class="form-group">
        <label for="name">名前:</label>
        <!-- BootstrapのForm Controlクラスを用いて、フォームの入力欄をデザインする -->
        <input type="text" class="form-control" id="name" name="name">
      </div>
      <div class="form-group">
        <label for="phone_number">電話番号:</label>
        <input type="text" class="form-control" id="phone_number" name="phone_number">
      </div>
      <div class="form-group">
        <label for="email">メールアドレス:</label>
        <input type="text" class="form-control" id="email" name="email">
      </div>
      <div class="form-group">
        <label for="time_slot">予約時間
```
:::


これで送信ボタンのdivも確認できたので切り貼りしてフォームのHTMLを完成させました。
