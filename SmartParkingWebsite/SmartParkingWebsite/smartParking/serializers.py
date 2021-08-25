from rest_framework import serializers
from .models import *


class SlotSerializer(serializers.ModelSerializer):
    class Meta:
        model = slots
        fields = '__all__'

class ReservationSerializer(serializers.ModelSerializer):
    class Meta:
        model = reserves
        fields = '__all__'

class DriverSerializer(serializers.ModelSerializer):
    class Meta:
        model = driver
        fields = '__all__'









