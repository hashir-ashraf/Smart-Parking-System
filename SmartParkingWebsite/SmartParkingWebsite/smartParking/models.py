from django.db import models
from django.contrib.auth.models import User
from PIL import Image


class Profile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    image = models.ImageField(default='default.jpg', upload_to='profile_pics')

    def __str__(self):
        return f'{self.user.username} Profile'

    def save(self,*args, **kwargs):
        super().save(*args, **kwargs)

        img = Image.open(self.image.path)

        if img.height > 300 or img.width > 300:
            output_size = (300, 300)
            img.thumbnail(output_size)
            img.save(self.image.path)


# class slot(models.Model):
#
#     status = models.CharField(max_length=100)
#     s_id = models.IntegerField()
#     def __str__(self):
#
#         return self.status
class slots(models.Model):

    status = models.CharField(max_length=100)
    s_id = models.IntegerField()

    def __str__(self):

        return self.s_id, self.status
class time(models.Model):
    status = models.CharField(max_length=100)
    time = models.CharField(max_length=100)

    def __str__(self):

        return self.status
class timetime(models.Model):
    tid = models.CharField(max_length=100)
    status = models.CharField(max_length=100)
    time = models.CharField(max_length=100)

    def __str__(self):

        return self.status



class driver(models.Model):

    p_no = models.IntegerField()
    name = models.CharField(max_length=100)
    Email = models.CharField(max_length=100)
    password = models.CharField(max_length=20)
    def __str__(self):

        return self.name



class reserves(models.Model):
    res_id = models.IntegerField()
    driver = models.ForeignKey(driver, on_delete=models.CASCADE)
    slots = models.ForeignKey(slots, on_delete=models.CASCADE)
    plate_no = models.CharField(max_length=10)


    def __str__(self):

        return self.plate_no