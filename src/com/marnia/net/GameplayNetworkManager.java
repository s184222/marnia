package com.marnia.net;

import java.util.List;
import java.util.UUID;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import com.marnia.net.packet.INetworkHandler;
import com.marnia.net.packet.IPacket;
import com.marnia.util.SpaceHelper;

public abstract class GameplayNetworkManager<H extends INetworkHandler> {

	public static final int PACKET_TO_HANDLE = 0;
	public static final int PACKET_TO_SEND = 1;
	public static final int RUNTIME_FIELDS = 2;
	
	public static final ActualField PACKET_TO_HANDLE_MATCH = new ActualField(PACKET_TO_HANDLE);
	public static final ActualField PACKET_TO_SEND_MATCH = new ActualField(PACKET_TO_SEND);
	public static final ActualField RUNTIME_FIELDS_MATCH = new ActualField(RUNTIME_FIELDS);
	
	public static final int FIELD_RUNNING = 2;
	
	public static final ActualField FIELD_RUNNING_MATCH = new ActualField(FIELD_RUNNING);
	
	public static final FormalField PACKET_CLASS_MATCH = new FormalField(IPacket.class);
	
	private final UUID identifier;
	private final PacketRegistry registry;

	private final Space localGameplaySpace;
	
	private final GameplayReceiverThread<H> receiverThread;
	private final GameplaySenderThread<H> senderThread;
	
	private boolean started;
	private boolean running;
	
	public GameplayNetworkManager(Space publicGameplaySpace, UUID identifier, PacketRegistry registry) {
		this.identifier = identifier;
		this.registry = registry;

		localGameplaySpace = new SequentialSpace();
	
		receiverThread = new GameplayReceiverThread<H>(this, publicGameplaySpace, localGameplaySpace, identifier);
		senderThread = new GameplaySenderThread<H>(this, publicGameplaySpace, localGameplaySpace, identifier);
	}
	
	public void start() {
		if (started)
			throw new IllegalStateException("Network manager has already been started!");
		
		started = true;
		
		try {
			localGameplaySpace.put(RUNTIME_FIELDS, FIELD_RUNNING);
		} catch (InterruptedException e) {
		}
		
		receiverThread.start();
		senderThread.start();
		
		running = true;
	}
	
	public void stop() {
		if (!running)
			throw new IllegalStateException("Network manager is not running!");

		running = false;
		
		try {
			localGameplaySpace.getp(RUNTIME_FIELDS_MATCH, FIELD_RUNNING_MATCH);
			
			receiverThread.interrupt();
			senderThread.interrupt();
			
			receiverThread.join();
			senderThread.join();
		} catch (InterruptedException e) {
		}
	}
	
	public void sendPacket(IPacket<?> packet, UUID receiver) {
		try {
			localGameplaySpace.put(PACKET_TO_SEND, receiver, packet);
		} catch (InterruptedException e) {
		}
	}
	
	public void tick() {
		if (running) {
			try {
				List<Object[]> packetsToHandle = localGameplaySpace.getAll(PACKET_TO_HANDLE_MATCH, 
						SpaceHelper.UUID_MATCH, PACKET_CLASS_MATCH);

				for (Object[] packetToHandle : packetsToHandle) {
					if (packetToHandle != null) {
						UUID sender = (UUID)packetToHandle[1];
						@SuppressWarnings("unchecked")
						IPacket<H> packet = (IPacket<H>)packetToHandle[2];

						try {
							handlePacket(sender, packet);
						} catch (Exception e) {
							e.printStackTrace();
	
							System.err.println("Error when handling packet: " + packet.getClass());
						}
					}
				}
			} catch (InterruptedException e) {
			}
		}
	}
	
	protected abstract void handlePacket(UUID sender, IPacket<H> packet);

	public IPacket<?> getPacketByType(int packetType) {
		@SuppressWarnings("rawtypes")
		IPacketProvider packetProvider = registry.getPacketProviderFromId(packetType);
		if (packetProvider == null)
			return null;
		return packetProvider.getPacketInstance();
	}

	public abstract NetworkSide getNetworkSide();
	
	@SuppressWarnings("unchecked")
	public int getPacketType(IPacket<?> packet) {
		return registry.getIdFromPacket((Class<? extends IPacket<?>>)packet.getClass());
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public boolean hasStarted() {
		return started;
	}
	
	public UUID getIdentifier() {
		return identifier;
	}
}
