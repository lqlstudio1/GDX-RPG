package com.rpsg.rpg.io;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.rpsg.gdxQuery.$;
import com.rpsg.gdxQuery.CustomRunnable;
import com.rpsg.rpg.core.RPG;
import com.rpsg.rpg.core.Setting;
import com.rpsg.rpg.object.base.IOMode;
import com.rpsg.rpg.system.base.Res;
import com.rpsg.rpg.view.GameViews;

public class Input implements InputProcessor {
	public static IOMode.GameInput state = IOMode.GameInput.normal;
	private static List<Integer> pressList = new ArrayList<>();
	private List<InputProcessorEx> register = new ArrayList<>(),removeList = new ArrayList<>();

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.O){
			RPG.toast.add("获得道具\ntest", Color.SKY ,22,true,Res.getNP(Setting.IMAGE_ICONS+"i1.png"));
//			RPG.ctrl.animation.removeAll();
//			Animation a = RPG.ctrl.animation.add(2);
//			a.setPosition(435,1700);
//			a.setScale(1f);
//			a.layer = 3;
		}
		if (state == IOMode.GameInput.hover)
			return RPG.popup.keyDown(keycode);
		switch (GameViews.state) {
		case GameViews.STATE_TITLE: {
			GameViews.titleview.onkeyDown(keycode);
			break;
		}
		case GameViews.STATE_GAME: {
			GameViews.gameview.onkeyDown(keycode);
			break;
		}
		}
		each(register, r -> r.keyDown(keycode));
		return false;
	}
	
	public InputProcessorEx addListener(InputProcessorEx ex){
		register.add(ex);
		return ex;
	}
	
	public void removeListener(InputProcessorEx ex){
		removeList.add(ex);
	}
	
	public void clearListener(){
		register.clear();
	}

	@Override
	public boolean keyUp(int keycode) {
		if (state == IOMode.GameInput.hover)
			return RPG.popup.keyUp(keycode);
		switch (GameViews.state) {
		case GameViews.STATE_GAME: {
			GameViews.gameview.onkeyUp(keycode);
			break;
		}
		}
		each(register, r -> r.keyUp(keycode));
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if (state == IOMode.GameInput.hover)
			return RPG.popup.keyTyped(character);
		switch (GameViews.state) {
		case GameViews.STATE_LOGO: {
			GameViews.logoview.onkeyTyped(character);
			break;
		}
		case GameViews.STATE_GAME: {
			GameViews.gameview.onkeyTyped(character);
			break;
		}
		}
		each(register, r -> r.keyTyped(character));
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		RPG.touch.start().setPosition(screenX, screenY);
		if (state == IOMode.GameInput.hover)
			return RPG.popup.touchDown(screenX, screenY, pointer, button);
		switch (GameViews.state) {
		case GameViews.STATE_GAME: {
			GameViews.gameview.touchDown(screenX, screenY, pointer, button);
			break;
		}
		}
		each(register, r -> r.touchDown(screenX, screenY, pointer, button));
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		RPG.touch.stop().setPosition(screenX, screenY);
		if (state == IOMode.GameInput.hover)
			return RPG.popup.touchUp(screenX, screenY, pointer, button);
		switch (GameViews.state) {
		case GameViews.STATE_GAME: {
			GameViews.gameview.touchUp(screenX, screenY, pointer, button);
			break;
		}
		}
		each(register, r -> r.touchUp(screenX, screenY, pointer, button));
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		RPG.touch.setPosition(screenX, screenY);
		if (state == IOMode.GameInput.hover)
			return RPG.popup.touchDragged(screenX, screenY, pointer);
		switch (GameViews.state) {
		case GameViews.STATE_GAME: {
			GameViews.gameview.touchDragged(screenX, screenY, pointer);
			break;
		}
		}
		each(register, r -> r.touchDragged(screenX, screenY, pointer));
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (state == IOMode.GameInput.hover)
			return RPG.popup.mouseMoved(screenX, screenY);
		switch (GameViews.state) {
		case GameViews.STATE_GAME: {
			GameViews.gameview.mouseMoved(screenX, screenY);
			break;
		}
		}
		each(register, r -> r.mouseMoved(screenX, screenY));
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if (state == IOMode.GameInput.hover)
			return RPG.popup.scrolled(amount);
		switch (GameViews.state) {
		case GameViews.STATE_GAME: {
			GameViews.gameview.scrolled(amount);
			break;
		}
		}
		each(register, r -> r.scrolled(amount));
		return false;
	}
	
	private void each(List<InputProcessorEx> list,CustomRunnable<InputProcessorEx> test){
		if(!removeList.isEmpty()){
			list.removeAll(removeList);
			removeList.clear();
		}
		$.each(list, test);
	}

	public static boolean isPress(int keyCode) {
		for(Integer code:pressList)
			if(code.equals(keyCode))
				return true;
		return Gdx.input.isKeyPressed(keyCode);
	}
	
	public static void press(int key){
		if(!pressList.contains(key))
			pressList.add(key);
	}
	
	public static void cleanPress(){
		pressList.clear();
	}
	
	public static void cleanPress(int key){
		pressList.remove((Integer)key);
	}

}
