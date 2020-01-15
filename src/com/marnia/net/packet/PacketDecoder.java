package com.marnia.net.packet;

import java.io.IOException;
import java.util.UUID;

import org.jspace.ActualField;
import org.jspace.Space;
import org.jspace.TemplateField;

public class PacketDecoder extends PacketCoder {

	private static final ActualField UNIQUE_PACKET_OVERHEAD_MATCH = new ActualField(UNIQUE_PACKET_OVERHEAD);
	
	private Object[] data;
	
	public PacketDecoder(UUID receiver, UUID sender, Space space) {
		super(receiver, sender, space);
	}
	
	public void fetchData(TemplateField... packetFields) throws InterruptedException {
		TemplateField[] fields = new TemplateField[PACKET_OVERHEAD + packetFields.length];
		fields[0] = new ActualField(receiver);
		fields[1] = new ActualField(sender);
		fields[2] = UNIQUE_PACKET_OVERHEAD_MATCH;
		System.arraycopy(packetFields, 0, fields, PACKET_OVERHEAD, packetFields.length);
		data = space.get(fields);
	}
	
	public <T> T getData(Class<T> clazz, int index) throws IOException {
		if (data == null)
			throw new IOException("Data has not been fetched!");
		@SuppressWarnings("unchecked")
		T value = (T)data[PACKET_OVERHEAD + index];
		return value;
	}
}
