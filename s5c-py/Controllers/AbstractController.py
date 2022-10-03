import pygame

class AbstractController:
    def __init__(self):
        self.Ranges = {
            'XY': 0x30,
            'Z': 0x10
        }
        self.Delta = {
            'Flag': 0x0,
            'X': 0x0,
            'Y': 0x0,
            'Z': 0x0,
            'Rotation': 0x0
        }
        self.input_thread = None

    def GetDelta(self, Label : str):
        return self.Delta[Label]
