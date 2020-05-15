#import mqttclient from paho library
import paho.mqtt.client as mqtt

#import MotorKit class from CircuitPython library
from adafruit_motorkit import MotorKit

#Provided by documentation of Motor Driver HAT
#0x40 is default address for I2C WSR-13304 (sterownik silnikow)
motorkit = MotorKit(0x40)


#define client and server for mqtt
client = "malinkaseba"
server = "malinkaseba"
mqtt_client = mqtt.Client(client)

#Speed variables
top_speed_flag = False
speed = 0.7
turning_speed = 0.7
stop_speed = 0.0

#Status of connection function and subscribtion for RabbitMQ query
def connect_to_mqtt(client, userdata, flags, rc):
    print("Subscribing")
    mqtt_client.subscribe("malinkaseba/move")
    print("Subscribed")

#Function used to interpret recieved messages
def process_message(client, userdata, msg):
    global speed
    global top_speed_flag
    message = msg.payload.decode(encoding='UTF8')

    #Check what message was reciewed
    if message == "forward":
        motorkit.motor1.throttle = speed
        motorkit.motor2.throttle = speed
        print("Going FORWARD")
    elif message == "backward":
        motorkit.motor1.throttle = -speed
        motorkit.motor2.throttle = -speed
        print("Going BACKWARD")
    elif message == "left":
        motorkit.motor1.throttle = -turning_speed
        motorkit.motor2.throttle = turning_speed
        print("Going LEFT")
    elif message == "right":
        motorkit.motor1.throttle = turning_speed
        motorkit.motor2.throttle = -turning_speed
        print("Going RIGHT")
    elif message == "stop":
        motorkit.motor1.throttle = stop_speed
        motorkit.motor2.throttle = stop_speed
        print("STOP!")
    elif message == "speed":
        if top_speed_flag:
            top_speed_flag = False
            speed = 0.7
            print("Top Speed mode OFF!")
            print(speed)
            print(top_speed_flag)
        else:
            top_speed_flag = True
            speed = 1.0
            print("Top Speed mode ON!")
            print(speed)
            print(top_speed_flag)
    else:
        print("Undefined message")

#Function which are used
mqtt_client.on_connect = connect_to_mqtt
mqtt_client.on_message = process_message

#Connect to mqtt server and loop forever
mqtt_client.connect(server)
mqtt_client.loop_forever()