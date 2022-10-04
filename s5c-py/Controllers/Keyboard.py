from Controllers.AbstractController import *


class Keyboard(AbstractController):
    def __init__(self):
        super().__init__()

    def start(self):
        if self.joystick != None:
            self.joystick.init()
            self.input_thread = threading.Thread(target=self.__poll, args=())
            self.input_thread.start()

    def __poll(self):
        while True:
            if self.killswitch_endtime == None or time.time() >= self.killswitch_endtime:
                if self.killswitch_endtime != None:
                    self.killswitch_endtime = None
                if not (self.joystick.get_button(4) or self.joystick.get_button(5)):
                    self.Delta['Rotation'] = 0x0
                if not self.joystick.get_button(1) and not self.joystick.get_button(2) and not self.joystick.get_button(9):
                    self.Delta['Flag'] = 0x0
                for event in pygame.event.get():
                    match event.type:
                        case pygame.JOYAXISMOTION:
                            self.Delta['X'] = int(
                                self.joystick.get_axis(0) * self.Ranges['XY'])
                            self.Delta['Y'] = - \
                                int(self.joystick.get_axis(
                                    1) * self.Ranges['XY'])
                            self.Delta['Z'] = 0
                            if self.joystick.get_axis(5) > 0.0:
                                self.Delta['Z'] += int(self.joystick.get_axis(5)
                                                       * self.Ranges['Z'])
                            if self.joystick.get_axis(2) > 0.0:
                                self.Delta['Z'] -= int(self.joystick.get_axis(2)
                                                       * self.Ranges['Z'])
                        case pygame.JOYBUTTONDOWN:
                            match event.button:
                                case 1:  # Options button on PS5 controller.
                                    self.Delta['Flag'] = 0x40
                                case 2:  # Options button on PS5 controller.
                                    self.Delta['Flag'] = 0x80
                                case 9:  # Options button on PS5 controller.
                                    self.Delta['Flag'] = 0x1
                                case 4:
                                    self.Delta['Rotation'] = -0x20
                                case 5:
                                    self.Delta['Rotation'] = 0x20
                                case 6:  # Share button on PS5 controller.
                                    self.Delta['Flag'] = 0x0
                                    self.Delta['X'] = 0
                                    self.Delta['Y'] = 0
                                    self.Delta['Z'] = -0x80
                                    self.Delta['Rotation'] = 0x0
                                    self.killswitch_endtime = time.time() + self.killswitch_dt
