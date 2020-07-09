package com.nec.edgedisplay.enums;

public enum CmsPlayer {

	STRATOS("STRATOS"), BRIGHTSIGN("BRIGHTSIGN"), NAVORI("NAVORI"),SIGNAGELIVE("SIGNAGELIVE");

	private String player;

	private CmsPlayer(final String player) {
		this.player = player;
	}

	public String getPlayer() {
		return this.player;
	}

}
