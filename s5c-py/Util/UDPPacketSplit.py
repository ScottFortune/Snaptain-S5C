data = []
with open('packet.bin', 'rb') as packet:
    for x in range(0xEA):
        buffer = packet.read(0x8)
        if buffer not in data:
            data.append(buffer)
            with open(f'packet_{x}.bin', 'wb') as packet_spl:
                packet_spl.write(buffer)
                
        