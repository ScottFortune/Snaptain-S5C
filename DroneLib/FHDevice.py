class FHDevice:
    def __init__(self):
        self.Baud = 19200
        self.ID = 0
        self.IP = '172.17.10.1'
        self.Port = 8888
        self.Username = 'guanxukeji'
        self.Password = 'gxrdw60'
        self.Username2 = 'guanxukeji2'
        self.Password2 = 'gxrdw602'
        self.AES = 'guanxukj@fh8620.'
    
    def GetAES(self):
        return self.AES

    def GetIP(self):
        return self.IP

    def GetUsername(self, ID):
        return self.Username if ID == 0 else self.Username2
    
    def GetPassword(self, ID):
        return self.Password if ID == 0 else self.Password2

    def GetID(self):
        return self.ID
    
