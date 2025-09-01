package com.goldsprite.gdxcore.logs;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

public class RealtimeInfoViewer {
	public static RealtimeInfoViewer instance;
	public String[] infoMsg = new String[50];
	public Label realtimeInfoLabel;

	public RealtimeInfoViewer(Skin skin){
		instance = this;
		realtimeInfoLabel = new Label("", skin.get("loggerLabelStyle", Label.LabelStyle.class));
		realtimeInfoLabel.setAlignment(Align.topLeft);
		setInfo(0, "实时信息板...");
	}

	public static void setInfo(int line, Object objMsg, Object... args){
		instance.infoMsg[line] = String.format((String)objMsg, args);
		instance.realtimeInfoLabel.setText(getInfoMessages());
	}

	public static String getInfoMessages(){
		String msg = "";
		int i = 0;
		for(String str : instance.infoMsg){
			msg += (i++)+" "+(str==null?"":str)+"\n";
		}
		return msg;
	}

	public Label getLabel(){
		return realtimeInfoLabel;
	}

}
