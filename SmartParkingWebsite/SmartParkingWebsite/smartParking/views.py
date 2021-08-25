import sqlite3
from django.shortcuts import render,redirect
from django.contrib import messages
from django.contrib.auth.decorators import login_required
import pytesseract
from .VideoCameraClass import *
from .VideoCameraClass import *
from .numberPlatedetector import VideoCameras
from .forms import UserUpdateForm, ProfileUpdateForm
from .models import *
from django.http import StreamingHttpResponse, HttpResponse
from rest_framework.decorators import api_view
from rest_framework.response import Response
from .serializers import *
from datetime import datetime
#API
@api_view(['GET'])
def showall(request):

    slot1 = slots.objects.filter(status="emp")

    reservations = reserves.objects.all()
    for rese in reservations:
        slot1 = slot1.exclude(id=rese.slots_id)
    # print(slot)
    serializer = SlotSerializer(slot1, many=True)
    return Response(serializer.data)
@api_view(['GET'])
def reservation_api(request, id, p_no, d_id, s_id):
    d= driver.objects.get(id=d_id)
    s= slots.objects.get(id=s_id)
    obj = reserves()
    obj.id= id
    obj.res_id= id
    obj.plate_no = p_no
    obj.driver=d
    obj.slots=s
    obj.save()
    serializer = ReservationSerializer(obj)
    return Response(serializer.data)
@api_view(['GET'])
def driver_api(request, id, p_no, name, email, password):

    obj = driver()
    obj.id= id

    obj.p_no = p_no
    obj.name = name
    obj.Email = email
    obj.password = password
    obj.save()
    serializer = DriverSerializer(obj)
    return Response(serializer.data)


@api_view(['GET'])
def cancel_reservation_api(request, id):
    # d = driver.objects.get(id=id)
    obj = reserves.objects.get(id=id)
    obj.delete()
    serializer = ReservationSerializer(obj)
    return Response(serializer.data)


# Create your views here.
@login_required
def home(request):
    # show()
    settime()
    return render(request,'smartParking/home.html')

@login_required
def timee(request):
    t= timetime.objects.all()
    time = {"time": t}
    return render(request,'smartParking/time.html', time)



def settime():
    # n=1
    s= slots.objects.all()
    for slot in s:
        obj = timetime()
        obj.tid= slot.id
        obj.status= slot.status
        now = datetime.now()
        obj.time = now
        obj.save()




# def show():
#     slot = slots.objects.filter(status="emp")
#     slott = {"slots": slot}
#     print(slott)




def detectorpage(request):
    return render(request,'smartParking/detectorpage.html')

@login_required
def cancel_reservations(request, id):
    print(id)
    con = sqlite3.connect("db.sqlite3")
    co = con.cursor()
    co.execute(f"DELETE FROM smartParking_reserves WHERE ID = {id}")
    con.commit()
    con.close()
    reserve = {"reservations": reserves.objects.all(), "slots": slots.objects.all()}
    return render(request, 'smartParking/slotdetails.html', reserve)


@login_required
def profile(request):
    if request.method == 'POST':
        u_form = UserUpdateForm(request.POST,instance=request.user)
        p_form = ProfileUpdateForm(request.POST,
                                   request.FILES,
                                   instance=request.user.profile)
        if u_form.is_valid() and p_form.is_valid():
            u_form.save()
            p_form.save()
            messages.success(request, f'Your Account has been Updated!')
            return redirect('profile')
    else:
        u_form = UserUpdateForm(instance=request.user)
        p_form = ProfileUpdateForm(instance=request.user.profile)


    context = {
        'u_form': u_form,
        'p_form': p_form


    }

    return render(request, 'smartParking/profile.html', context)

@login_required
def showSlotDetails(request):
    # setSlotDetails()
    driversDetails = {"drivers" : driver.objects.all()}
    details = {"slots": slots.objects.all()}
    reserve = {"reservations" : reserves.objects.all(),"slots": slots.objects.all(),"drivers" : driver.objects.all()}

    return render(request, 'smartParking/slotdetails.html', reserve)


#
# def settime(id,s,t):
#     # n=1
#     # d= driver.objects.get(id=1)
#     # s= slots.objects.get(id=1)
#     obj = time()
#     obj.id= id
#     obj.status= s
#     obj.time = t
#     obj.save()
#     # d= driver.objects.get(id=1)
#     # s= slots.objects.get(id=1)
#     # obj = reserves()
#     # obj.id= 2
#     # obj.res_id= 1
#     # obj.plate_no = "ptb123"
#     # obj.driver=d
#     # obj.slots=s
#
#     # obj.save()


@login_required
def camereas(request):
    try:
        cam = VideoCamera()
        return StreamingHttpResponse(gen(cam), content_type="multipart/x-mixed-replace;boundary=frame")
    except:
        pass
    return render(request, 'cameras.html')

@login_required
def def_slots(request):
    try:
        cam = VideoCamera()

        return StreamingHttpResponse(gen1(cam), content_type="multipart/x-mixed-replace;boundary=frame")
    except:
        pass

    return render(request, 'ppp.html')
@login_required
def detect_number(request):
    try:
        cam = VideoCameras()

        return StreamingHttpResponse(gen(cam), content_type="multipart/x-mixed-replace;boundary=frame")
    except:
        pass

    return render(request, 'numberplate.html')



#to capture video class

def gen1(camera):
    while True:
        frame = camera.get_frame1()

        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n\r\n')

def gen(camera):
    while True:
        frame = camera.get_frame()
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n\r\n')



