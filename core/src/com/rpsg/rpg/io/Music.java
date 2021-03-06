package com.rpsg.rpg.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.rpsg.rpg.core.Setting;
import com.rpsg.rpg.object.script.BaseScriptExecutor;
import com.rpsg.rpg.object.script.Script;
import com.rpsg.rpg.object.script.ScriptExecutor;
import com.rpsg.rpg.utils.game.Logger;

public class Music {
	public static com.badlogic.gdx.audio.Music MUSIC;
	public static Map<String, com.badlogic.gdx.audio.Music> bgm = new HashMap<String, com.badlogic.gdx.audio.Music>();
	public static Map<String, SE> se = new HashMap<String, SE>();

	public static void playMusic(String music) {
		if (null == bgm.get(music)) {
			bgm.put(music, Gdx.audio.newMusic(Gdx.files.internal(Setting.MUSIC_BGM + music)));
			Logger.info("成功创建音乐：" + music);
		}
		bgm.get(music).play();
		MUSIC = bgm.get(music);
	}

	public static BaseScriptExecutor playMusic(Script script, final String music) {
		return script.set(new BaseScriptExecutor() {
			public void init() {
				playMusic(music);
			}
		});
	}

	public static BaseScriptExecutor playSE(Script script, final String se) {
		return script.set(new BaseScriptExecutor() {
			public void init() {
				playSE(se,false);
			}
		});
	}
	
	public static BaseScriptExecutor playSE(Script script, final String se,final boolean loop) {
		return script.set(new BaseScriptExecutor() {
			public void init() {
				playSE(se,loop);
			}
		});
	}

	public static void stopCurrentMusic() {
		if (MUSIC != null && MUSIC.isPlaying())
			MUSIC.stop();
	}

	public static void playSE(String name){
		playSE(name,false);
	}
	
	public static void playSE(String name,boolean loop) {
		if(name.indexOf(".")<0)
			name += ".wav";
		
		if (null == se.get(name)) {
			se.put(name, new SE(Gdx.audio.newSound(Gdx.files.internal(Setting.MUSIC_SE + name))).setPath(name));
			Logger.info("成功创建音效：" + name);
		}
		se.get(name).setId(se.get(name).getSound().play());
		se.get(name).setVolume(0.7f);
		
			se.get(name).getSound().setLooping(se.get(name).getId(), loop);
	}

	public static BaseScriptExecutor stopAllSE(Script script, final float time) {
		return stopAllSE(script, time, null);
	}

	public static BaseScriptExecutor setSEVolume(Script script, final float color, final float time) {
		return script.set(new ScriptExecutor(script) {
			Actor proxy = new Actor();

			public void init() {
				proxy.getColor().a = se.size() == 0 ? .7f : se.values().iterator().next().getVolume();
				proxy.addAction(Actions.color(new Color(1, 1, 1, color), time));
			}

			public void step() {
				proxy.act(Gdx.graphics.getDeltaTime());
				for (String key : se.keySet()) {
					se.get(key).setVolume(proxy.getColor().a);
				}
				if (proxy.getColor().a == color || se.isEmpty()) {
					dispose();
				}
			}
		});
	}

	public static BaseScriptExecutor stopAllSE(Script script, final float time, final String without) {
		return script.set(new ScriptExecutor(script) {
			Actor proxy = new Actor();

			public void init() {
				proxy.getColor().a = se.size() == 0 ? .7f : se.values().iterator().next().getVolume();
				proxy.addAction(Actions.fadeOut(time));
			}

			public void step() {
				proxy.act(Gdx.graphics.getDeltaTime());
				for (String key : se.keySet()) {
					if (without == null || !without.equals(se.get(key).getPath()))
						se.get(key).setVolume(proxy.getColor().a);
				}
				if (proxy.getColor().a == 0 || se.isEmpty()) {
					List<SE> removeList = new ArrayList<SE>();
					for (String key : se.keySet()) {
						if (without == null || !without.equals(se.get(key).getPath())) {
							se.get(key).getSound().dispose();
							removeList.add(se.get(key));
						}
					}
					for (SE s : removeList)
						se.remove(s);
					dispose();
				}
			}
		});
	}

}
