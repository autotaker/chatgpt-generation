from flask import Flask, request, render_template, make_response

from dao import ReservationDAO
from reservation import Reservation, ReservationManager
app = Flask(__name__)

@app.route('/', methods=['GET'])
def get_reservation_form():
    # jinja2テンプレートを用いてreservation_form.htmlを読み込む
    form = render_template('reservation_form.html')

    # 予約フォームのHTMLを返す
    return form

@app.route('/reservations', methods=['GET','POST'])
def create_reservation():
    # 予約DAOを作成
    dao = ReservationDAO()

    manager_id = request.args.get('manager_id')

    if manager_id is not None:
        manager = dao.find_manager_by_id(manager_id)
        if manager is None:
            return make_response('予約管理者が見つかりません', 400)
    else:
        manager = None

    if request.method == 'GET':
        # GETリクエストの場合は、予約フォームを返す
        return render_template('reservation_form.html', manager=manager)
    elif request.method == 'POST':
        # POSTリクエストの場合は、予約を追加する
        # フォームから名前、電話番号、メールアドレス、予約枠、予約目的を取得
        name = request.form['name']
        phone_number = request.form['phone_number']
        email = request.form['email']
        time_slot = request.form['time_slot']
        purpose = request.form['purpose']

        # 予約を作成し、予約DAOに追加する
        reservation = Reservation(manager, name, phone_number, email, time_slot, purpose)
        reservation_id = dao.add_reservation(time_slot, reservation)

        # 予約を登録したことを示すメッセージを返す
        return f"予約が登録されました。予約IDは{reservation_id}です。"

if __name__ == '__main__':
  app.run(debug=True)
