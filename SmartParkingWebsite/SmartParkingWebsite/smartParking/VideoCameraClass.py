import sqlite3
from django.conf import settings
import cv2
import threading
import numpy as np
import os
from keras.preprocessing import image
import tensorflow as tf
from datetime import datetime
from .models import *
class VideoCamera(object):
    def __init__(self):
        self.model = tf.keras.models.load_model('slot_tunned_model.h5')
        self.video = cv2.VideoCapture(0)
        self.n = 1
        (self.grabbed, self.frame) = self.video.read()
        threading.Thread(target=self.update, args=()).start()

    def __del__(self):
        self.video.release()

    # use to only edit slot positions
    def get_frame1(self):

        if self.n == 1:
            image = self.frame
            def_slots(image)
            self.n = 2
            _, jpeg = cv2.imencode('.jpg', image)
            setNoOfSlotsInDB()
            return jpeg.tobytes()
        else:
            self.__del__()
    # used for live detections
    def get_frame(self):

        image = self.frame

        crop(image)
        d, slist = predictions(self.model)
        # res = reserves.objects.all()
        # print(res)


        print(slist)
        loc = get_locations()
        for i in loc:
            index = int(loc.index(i))
            s = d[index]
            if s == "occ":

                image = cv2.rectangle(image, (i[0], i[1]), (i[2], i[3]),(0, 0, 255) , 3)
            elif s == "emp":
                image = cv2.rectangle(image, (i[0], i[1]), (i[2], i[3]), (0, 255, 255), 3)
        _, jpeg = cv2.imencode('.jpg', image)
        return jpeg.tobytes()

    def update(self):
        while True:
             (self.grabbed, self.frame) = self.video.read()

# use to make ROI
def def_slots(frame):
    refPt = []
    cropping = False
    a = []

    def click_and_crop(event, x, y, flags, param):
        # grab references to the global variables
        global refPt, cropping
        # if the left mouse button was clicked, record the starting
        # (x, y) coordinates and indicate that cropping is being
        # performed
        if event == cv2.EVENT_LBUTTONDOWN:
            refPt = [(x, y)]
            cropping = True
        # check to see if the left mouse button was released
        elif event == cv2.EVENT_LBUTTONUP:
            # record the ending (x, y) coordinates and indicate that
            # the cropping operation is finished
            refPt.append((x, y))
            a.append(refPt)
            cropping = False
            # draw a rectangle around the region of interest
            # print("value of a", a)
            cv2.rectangle(image, refPt[0], refPt[1], (0, 255, 0), 2)
            cv2.imshow("image", image)
    image = frame
    image1 = frame
    clone = image1
    cv2.namedWindow("image")
    cv2.setMouseCallback("image", click_and_crop)
    # keep looping until the 'q' key is pressed
    while True:
        # display the image and wait for a keypress
        cv2.imshow("image", image)
        key = cv2.waitKey(1) & 0xFF
        # if the 'r' key is pressed, reset the cropping region
        if key == ord("r"):
            image = clone.copy()
            a = []
        # if the 'c' key is pressed, break from the loop
        elif key == ord("c"):
            # print(refPt)
            break
    # if there are two reference points, then crop the region of interest
    # from teh image and display it

    if len(refPt) == 2:
        roi = clone[refPt[0][1]:refPt[1][1], refPt[0][0]:refPt[1][0]]
        print("....Roi")
        cv2.imshow("ROI", roi)
        cv2.waitKey(0)
    # close all open windows

    cv2.destroyAllWindows()
    f = open("demo.txt", "w")
    for i in a:
        f.write(str(i[0][0]) + "," + str(i[0][1]) + "," + str(i[1][0]) + "," + str(i[1][1]) + '\n')
    f.close()

# use for pridictions
def predictions(model):
    con = sqlite3.connect("db.sqlite3")
    co = con.cursor()
    c = 1
    slotlist=[]
    pre_list = {}
    path = os.path.join(settings.BASE_DIR, 'sample')
    for imag in os.listdir(path):

        img = image.load_img(os.path.join(path, imag), target_size=(50, 50))
        print(imag)
        x = image.img_to_array(img)
        x = x / 255
        x = np.expand_dims(x, axis=0)
        images = np.vstack([x])
        classes = model.predict(images)
        # print(c)
        c1 = c - 1
        if classes[0] > 0.5:
            pre_list[c1] = "occ"
            d = c1+1
            slotlist.append(d)
            now = datetime.now()
            print("occ", now)
            sta="occ"

            # settime(d,sta,now)



            # co.execute(f"UPDATE smartParking_slots SET status='occ' WHERE ID = {d} ")

            # co.execute(f"INSERT INTO smartParking_time VALUES ({d},'occ',{now} )")

        else:
            pre_list[c1] = "emp"
            d = c1 + 1
            slotlist.append(d)
            now = datetime.now()
            print("emp", now)
            sta="emp"
            # settime(d, sta, now)
            # co.execute(f"INSERT INTO smartParking_time VALUES ({d},'emp',{now} )")
            co.execute(f"UPDATE smartParking_slots SET status='emp' WHERE ID = {d} ")
        c += 1

    con.commit()
    con.close()
    return pre_list, slotlist

def settime(id,s,t):
    # n=1
    # d= driver.objects.get(id=1)
    # s= slots.objects.get(id=1)
    obj = timetime()
    obj.tid= id
    obj.status= s
    obj.time = t
    obj.save()


#use to get locations of slots
def get_locations():
    f = open("demo.txt", "r")
    num = 1
    c = []
    for i in f:
        b = i.split(',')
        c1 = []
        for k in b:
            c1.append(int(k))
        num = num + 1
        c.append(c1)
    f.close()
    return c

#use to crop ROIS
def crop(frame):
    # im = cv2.imread(frame)
    im = frame
    path = os.path.join(settings.BASE_DIR, 'sample')
    f = open("demo.txt", "r")
    # a = f.readlines()
    num = 1
    for i in f:
        b = i.split(',')
        c = []
        for k in b:
            c.append(int(k))
        # print(c)
        im_cropped = im[c[1]:c[3], c[0]:c[2]]
        name = str(num) + '.png'
        imgpath = os.path.join(path, name)
        cv2.imwrite(imgpath, im_cropped)
        num = num + 1
        cv2.waitKey(0)
    f.close()

#use to initially set no of slots
def setNoOfSlotsInDB():
    f = open("demo.txt", "r")
    con = sqlite3.connect("db.sqlite3")
    print('123')

    j=1
    c = con.cursor()
    c.execute("DELETE FROM smartParking_slots")
    for i in f:
        # c.execute("UPDATE smartParking_slot SET status='occ' WHERE ID = '1' ")
        c.execute(f"INSERT INTO smartParking_slots VALUES ({j},'',{j} )")
        # c.execute("DELETE FROM smartParking_slots")
        j = j+1

    con.commit()
    con.close()
    f.close()


# def setNoOfSlotsIntimeDB():
#     f = open("demo.txt", "r")
#     con = sqlite3.connect("db.sqlite3")
#     print('123')
#
#     j=1
#     c = con.cursor()
#     c.execute("DELETE FROM smartParking_time")
#     for i in f:
#         # c.execute("UPDATE smartParking_slot SET status='occ' WHERE ID = '1' ")
#         c.execute(f"INSERT INTO smartParking_time VALUES ({j},'','' )")
#         # c.execute("DELETE FROM smartParking_slots")
#         j = j+1
#
#     con.commit()
#     con.close()
#     f.close()
