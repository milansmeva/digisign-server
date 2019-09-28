package com.infosys.mediaplayer;

import java.util.Date;

public class MediaPlayerPoller {
	public String id;
	public String password;
	public Date Timestamp;
	public MediaPlayerPoller(String id ,String password,Date timestamp) {
		this.id=id;
		this.password=password;
		this.Timestamp=timestamp;
	}
}
