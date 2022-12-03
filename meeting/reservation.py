class Reservation:
    def __init__(self, manager, name, phone_number, email, time_slot, purpose):
        self.manager = manager
        self.name = name
        self.phone_number = phone_number
        self.email = email
        self.time_slot = time_slot
        self.purpose = purpose

class ReservationManager:
    def __init__(self, id, name, phone_number, email, permission):
        self.id = id
        self.name = name
        self.phone_number = phone_number
        self.email = email
        self.permission = permission

        # 予約情報のリスト
        self.reservations = []

    def add_reservation(self, reservation):
        """
        予約を追加する
        """
        self.reservations.append(reservation)

    def remove_reservation(self, reservation):
        """
        予約を削除する
        """
        self.reservations.remove(reservation)

    def get_reservations(self):
        """
        登録されているすべての予約を取得する
        """
        return self.reservations
