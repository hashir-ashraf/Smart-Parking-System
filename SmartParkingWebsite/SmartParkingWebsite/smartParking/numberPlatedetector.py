import sqlite3
from difflib import SequenceMatcher
from django.conf import settings
import cv2
import threading
import numpy as np
import os
from keras.preprocessing import image
import tensorflow as tf
from .models import reserves
import pytesseract


class VideoCameras(object):
    def __init__(self):
        # self.pytesseract.pytesseract.tesseract_cmd = 'C:\\Program Files\\Tesseract-OCR\\tesseract.exe'
        self.video = cv2.VideoCapture(0)
        self.n = 1
        (self.grabbed, self.frame) = self.video.read()
        threading.Thread(target=self.update, args=()).start()

    def __del__(self):
        self.video.release()

    # use to get car picure
    def get_frame(self):
        if self.n == 1:
            image = self.frame
            save(image)
            x = num()
            reserve = {"reservations": reserves.objects.all()}
            for reservation in reserve['reservations']:
                a = reservation.plate_no
                c = SequenceMatcher(None, x, a).ratio()
                print(c)


                if c > 0.7:
                    print(reservation.id)
                    # print(reservation.driver.name)
                    # print(reservation.slots.id)
                    obj = reserves.objects.get(id=reservation.id)
                    obj.delete()
            # print(reserve)
            self.n = 2
            _, jpeg = cv2.imencode('.jpg', image)
            return jpeg.tobytes()
        else:
            self.__del__()

    def dell(self):
        self.__del__()

    def update(self):
        while True:
            (self.grabbed, self.frame) = self.video.read()



def save(frame):
    im = frame
    path = os.path.join(settings.BASE_DIR, 'sample1')
    num = "car"
    name = num + '.jpg'
    imgpath = os.path.join(path, name)
    cv2.imwrite(imgpath, im)
    cv2.waitKey(0)


def num():
    pytesseract.pytesseract.tesseract_cmd = 'C:\\Program Files\\Tesseract-OCR\\tesseract.exe'

    # read the image file
    image = cv2.imread(r'C:\Users\mg\PycharmProjects\SmartParkingWebsite\SmartParkingWebsite\sample1\car.jpg')

    # convert to grayscale image
    gray_image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    # cv2.imshow('License Plate Detection', gray_image)
    # cv2.waitKey(0)

    # remove noise
    gray_image = cv2.bilateralFilter(gray_image, 11, 17, 17)

    # canny edge detection
    canny_edge = cv2.Canny(gray_image, 100, 200)
    # cv2.imshow('License Plate Detection', canny_edge)
    # cv2.waitKey(0)

    # find contours based edge
    contours, new = cv2.findContours(canny_edge.copy(), cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)
    contours = sorted(contours, key=cv2.contourArea, reverse=True)[:30]

    # inilialize licence plate counter and x,y cordinates
    contour_with_license_plate = None
    license_plate = None
    x = None
    y = None
    w = None
    h = None

    # find the cordinates with 4 potential corners and create a region of interest around it
    for contour in contours:

        perimeter = cv2.arcLength(contour, True)
        approx = cv2.approxPolyDP(contour, 0.02 * perimeter, True)

        # this check if it is rectangle
        if len(approx) == 4:
            contour_with_license_plate = approx
            x, y, w, h = cv2.boundingRect(contour)
            license_plate = gray_image[y:y + h, x:x + w]
            break

    (thresh, blackAndWhiteImage) = cv2.threshold(license_plate, 80, 255, cv2.THRESH_BINARY)
    # cv2.imshow('License Plate Detection', blackAndWhiteImage)
    # cv2.waitKey(0)

    # text Recognization
    text = pytesseract.image_to_string(blackAndWhiteImage,
                                       config='--psm 6 -c tessedit_char_whitelist=0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ')

    # draw plate and write text
    image = cv2.rectangle(image, (x, y), (x + w, y + h), (0, 255, 0), 3)
    image = cv2.putText(image, text.upper(), (x + 50, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2, cv2.LINE_AA)

    print("Licence Plate :", text.upper())
    cv2.imshow('License Plate Detection', image)
    cv2.waitKey(0)

    t = text.upper()
    x = t.splitlines()
    # x = x[0] + x[1]
    print(x)
    if len(x) > 1:
        x = x[0] + x[1]

    return x






