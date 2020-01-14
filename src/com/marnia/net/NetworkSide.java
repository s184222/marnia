package com.marnia.net;

public enum NetworkSide {

	CLIENT(0),
	SERVER(1);
	
	private final int index;
	
	private NetworkSide(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public NetworkSide getOpposite() {
		return (this == CLIENT) ? SERVER : CLIENT;
	}
}
