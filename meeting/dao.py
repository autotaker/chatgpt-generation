import os
import psycopg2

from reservation import ReservationManager

class ReservationDAO:
    def __init__(self):
        # PostgresDBへの接続情報を環境変数から取得
        host = os.environ['POSTGRES_HOST']
        port = os.environ['POSTGRES_PORT']
        user = os.environ['POSTGRES_USER']
        password = os.environ['POSTGRES_PASSWORD']
        dbname = os.environ['POSTGRES_DBNAME']

        # PostgresDBへの接続を確立
        self.conn = psycopg2.connect(host=host, port=port, user=user, password=password, dbname=dbname)
        # 予約管理者のIDをキー、予約管理者を値とする辞書
        self.managers = {}

        # 予約の枠をキー、予約を値とする辞書
        self.reservations = {}

    def add_manager(self, manager):
        """
        予約管理者を追加する
        """
        self.managers[manager.id] = manager

    def find_manager_by_id(self, manager_id):
        """
        IDから予約管理者を検索する
        """
        cursor = self.conn.cursor()
        cursor.execute('SELECT * FROM reservation_managers WHERE id = %s', (manager_id,))
        row = cursor.fetchone()
        cursor.close()

        if row is None:
            return None

        return ReservationManager(*row)
    
    def add_reservation(self, time_slot, reservation):
        """
        予約を追加する
        """
        # 指定された予約枠がまだ予約がない場合は、新しいリストを作成する
        if time_slot not in self.reservations:
            self.reservations[time_slot] = []

        # 予約枠の予約リストに予約を追加する
        self.reservations[time_slot].append(reservation)

        # 予約をDBに保存する
        with self.conn.cursor() as cur:
            # INSERT文を作成
            insert_query = '''
                INSERT INTO reservations(manager_id, name, phone_number, email, time_slot, purpose)
                VALUES (%s, %s, %s, %s, %s, %s) RETURNING id
            '''

            # INSERT文に値をバインドして実行する
            cur.execute(insert_query, (reservation.manager.id, reservation.name, reservation.phone_number, reservation.email, reservation.time_slot, reservation.purpose))

            reservation_id = cur.fetchone()[0]

            # 変更をDBに保存する
            self.conn.commit()

            return reservation_id

