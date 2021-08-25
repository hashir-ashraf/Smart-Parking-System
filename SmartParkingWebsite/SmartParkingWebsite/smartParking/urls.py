from django.urls import path
from . import views
urlpatterns = [
    path('', views.home ),
    path('home/', views.home, name='SmartParking-home'),
    path('detectorpage/', views.detectorpage, name='SmartParking-detector'),
    path('ppp/', views.def_slots, name='ppp'),
    path('numberplate/', views.detect_number, name='SmartParking_numberplate'),
    path('cameras/', views.camereas, name='SmartParking-cameras'),
    path('Showslot-details/', views.showSlotDetails, name='SmartParking-slotDetails'),
    path("Showslot-details/<int:id>", views.cancel_reservations, name='SmartParking-cancel'),
    path('profile/', views.profile, name='profile'),
    path('showall/', views.showall, name='showall'),
    path('reservation_api/<int:id>&<str:p_no>&<int:d_id>&<int:s_id>', views.reservation_api, name='reservation_api'),
    path('cancel_reservation_api/<int:id>', views.cancel_reservation_api, name='cancel_reservation_api'),
    path('time/', views.timee, name='SmartParking-time'),
    path('driver_api/<int:id>&<int:p_no>&<str:name>&<str:email>&<str:password>', views.driver_api, name='driver_api')
]
