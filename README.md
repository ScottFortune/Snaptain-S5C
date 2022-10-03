# Snaptain S5C
![](https://www.edrones.review/wp-content/uploads/2018/10/SNAPTAIN-S5C-1024x664.jpg)

This is my attempt at reverse engineering the control stack for the *Snaptain S5C* drone. I am doing this not only to understand the internal structure of the drone more, but also to control it from any IoT device.

See [here](Notes.md) for notes. They are volatile, so be warned.

See the directory `s5c-py` for the library/control interface I wrote in Python. This interface is designed to test commands, and is not fully fleshed out yet.

# To-Do
- Clean-up code.
- Make API a bit more programmer-friendly.

# Credits
- [Ibrahima Keita](https://github.com/Tensor497) - Packet sniffing, testing, datagram command research, transmission control protocol packet research, video decoding research, Dualsense controller implementation.
- [Scott Fortune](https://github.com/ScottFortune) - Testing, transmission control protocol packet research, video decoding research, Dualsense controller implementation.
- [Amine Lemaizi](https://lemaizi.com/blog/hacking-a-toy-drone-to-put-artificial-intelligence-on-it-part-i-the-hack/) - Initial research.