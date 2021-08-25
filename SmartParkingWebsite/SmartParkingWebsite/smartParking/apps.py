from django.apps import AppConfig


class SmartparkingConfig(AppConfig):
    default_auto_field = 'django.db.models.BigAutoField'
    name = 'smartParking'
    def ready(self):
        import smartParking.signals