package com.rpsg.rpg.view.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.rpsg.gdxQuery.$;
import com.rpsg.gdxQuery.CustomRunnable;
import com.rpsg.gdxQuery.GdxQuery;
import com.rpsg.gdxQuery.GdxQueryRunnable;
import com.rpsg.rpg.core.RPG;
import com.rpsg.rpg.core.Setting;
import com.rpsg.rpg.object.base.items.BaseItem.Context;
import com.rpsg.rpg.object.base.items.Equipment;
import com.rpsg.rpg.system.base.Res;
import com.rpsg.rpg.system.ui.CheckBox;
import com.rpsg.rpg.system.ui.HeroImage;
import com.rpsg.rpg.system.ui.IMenuView;
import com.rpsg.rpg.system.ui.Icon;
import com.rpsg.rpg.system.ui.Image;
import com.rpsg.rpg.system.ui.ImageButton;
import com.rpsg.rpg.system.ui.ImageList;
import com.rpsg.rpg.system.ui.Label;
import com.rpsg.rpg.utils.game.GameUtil;
import com.rpsg.rpg.view.hover.ThrowItemView;

public class EquipView extends IMenuView{
	
	Group inner,data,description;
	ImageList ilist;
	String currentFilter = Equipment.EQUIP_CLOTHES;
	ImageButton takeButton,throwButton;
	Image take=Res.get(Setting.IMAGE_MENU_EQUIP+"but_take.png"),off=Res.get(Setting.IMAGE_MENU_EQUIP+"but_off.png").a(.3f),throwImg=Res.get(Setting.IMAGE_MENU_EQUIP+"but_remove.png").a(.3f);
	public EquipView init() {
		stage=new Stage(new ScalingViewport(Scaling.stretch, GameUtil.stage_width, GameUtil.stage_height, new OrthographicCamera()),MenuView.stage.getBatch());
		$.add(Res.get(Setting.UI_BASE_IMG)).setSize(137,79).setColor(0,0,0,.52f).setPosition(240,470).appendTo(stage);
		$.add(Res.get(Setting.UI_BASE_IMG)).setSize(680,48).setColor(0,0,0,.85f).setPosition(377,486).appendTo(stage);
		CheckBoxStyle cstyle=new CheckBoxStyle();
		cstyle.checkboxOff=Res.getDrawable(Setting.IMAGE_MENU_EQUIP+"info.png");
		cstyle.checkboxOn=Res.getDrawable(Setting.IMAGE_MENU_EQUIP+"info_p.png");// help button press
		cstyle.font = Res.font.get(20);
		$.add(new CheckBox("", cstyle)).appendTo(stage).setPosition(880,486).run(new GdxQueryRunnable() {public void run(final GdxQuery self) {self.click(new Runnable() {public void run() {
			data.addAction(self.isChecked()?Actions.fadeIn(.3f):Actions.fadeOut(.3f));
		}});}});
		
		$.add(Res.get(Setting.UI_BASE_IMG)).setSize(755,282).setColor(.2f,.2f,.2f,.85f).setPosition(240,178).appendTo(stage);
		
		$.add(Res.get(Setting.UI_BASE_IMG)).setSize(155,155).setColor(0,0,0,.55f).setPosition(240,12).appendTo(stage);
		$.add(Res.get(Setting.UI_BASE_IMG)).setSize(597,103).setColor(0,0,0,.55f).setPosition(398,64).appendTo(stage);
		
		$.add(takeButton=new ImageButton(Res.getDrawable(Setting.IMAGE_MENU_GLOBAL+"button.png"),Setting.UI_BUTTON).setFg(take)).appendTo(stage).setSize(297,49).setPosition(398, 12).getCell().prefSize(297,49);
		
		$.add(throwButton=new ImageButton(Res.getDrawable(Setting.IMAGE_MENU_GLOBAL+"button.png"),Setting.UI_BUTTON).setFg(throwImg)).appendTo(stage).setSize(297,49).setPosition(698, 12).getCell().prefSize(297,49);
		
		$.add(Res.get(Setting.IMAGE_MENU_GLOBAL+"m_right.png")).appendTo(stage).setScale(.8f).setPosition(367, 483).click(this::next).addAction(Actions.fadeIn(.2f)).setColor(1,1,1,0);
		$.add(Res.get(Setting.IMAGE_MENU_GLOBAL+"m_right.png")).setScale(.8f).setScaleX(-.8f).appendTo(stage).setPosition(196, 483).click(this::prev).addAction(Actions.fadeIn(.2f)).setColor(1,1,1,0);
		
		inner = (Group) $.add(new Group()).appendTo(stage).getItem();
		description = (Group) $.add(new Group()).appendTo(stage).getItem();
		data=(Group) $.add(new Group()).setColor(1,1,1,0).appendTo(stage).getItem();
		
		CheckBoxStyle cstyle2 = new CheckBoxStyle();
		cstyle2.checkboxOff=Res.getDrawable(Setting.IMAGE_MENU_GLOBAL+"button.png");
		cstyle2.checkboxOff.setMinHeight(47);
		cstyle2.checkboxOff.setMinWidth(75);
		cstyle2.checkboxOn=Res.getDrawable(Setting.IMAGE_MENU_GLOBAL+"menu_button_select.png");
		cstyle2.checkboxOn.setMinHeight(47);
		cstyle2.checkboxOn.setMinWidth(75);
		cstyle2.font = Res.font.get(18);
		
		int offset = -1,padding = 54,_top = 400;
		ButtonGroup<CheckBox> bg = new ButtonGroup<CheckBox>(
			(CheckBox)($.add(new CheckBox(cstyle2).hideText(true)).setUserObject(Equipment.EQUIP_WEAPON).setPosition(250, _top-(++offset * padding)).getItem()),
			(CheckBox)($.add(new CheckBox(cstyle2).hideText(true)).setUserObject(Equipment.EQUIP_CLOTHES).setPosition(250, _top-(++offset * padding)).getItem()),
			(CheckBox)($.add(new CheckBox(cstyle2).hideText(true)).setUserObject(Equipment.EQUIP_SHOES).setPosition(250, _top-(++offset * padding)).getItem()),
			(CheckBox)($.add(new CheckBox(cstyle2).hideText(true)).setUserObject(Equipment.EQUIP_ORNAMENT1).setPosition(250, _top-(++offset * padding)).getItem()),
			(CheckBox)($.add(new CheckBox(cstyle2).hideText(true)).setUserObject(Equipment.EQUIP_ORNAMENT2).setPosition(250, _top-(++offset * padding)).getItem())
		);
		
		for(final CheckBox box:bg.getButtons()){
			stage.addActor(box);
			box.setForeground(Res.get(Setting.IMAGE_MENU_EQUIP+box.getUserObject().toString()+".png").size(40, 40)).setFgOff(0).onClick(new Runnable(){
				public void run() {
					currentFilter = box.getUserObject().toString();
					generate();
				}
			});
			box.setWidth(box.getStyle().checkboxOff.getMinWidth());
			if(box.equals(bg.getButtons().get(0)))
				box.click().setChecked(true);
		}
		
		stage.setDebugAll(Setting.persistence.uiDebug);
		
		return this;
	}
	
	private void generate() {
		float oldTop = 0f;
		if(ilist!=null)
			oldTop = ilist.getScrollPercentY();
		inner.clear();
		data.clear();
		description.clear();
		
		$.add(Res.get(Setting.IMAGE_MENU_EQUIP+"data.png").disableTouch()).setSize(187, 312).setPosition(838,174).appendTo(data);
		
		ilist=((ImageList) $.add(new ImageList(getEquips(currentFilter))).setSize(655, 266).setPosition(328, 185).appendTo(inner).getItem());
		
		Equipment currentHeroEquip = RPG.ctrl.item.getHeroEquip(parent.current, currentFilter);
		
		ilist.generate(new Icon().generateIcon(currentHeroEquip, true).setCurrent(true));
		ilist.onChange(new CustomRunnable<Icon>() {
			public void run(Icon t) {
				Equipment currentHeroEquip = RPG.ctrl.item.getHeroEquip(parent.current, currentFilter);
				
				description.clear();
				
				String append = "";
				if(!t.enable){
					takeButton.setFg(take.a(.3f)).fgSelfColor(true).onClick(()->{});
					append += "当前角色无法使用此装备。";
				}else{
					if(t.current)
						takeButton.setFg(off.a(1)).onClick(()->{
							RPG.ctrl.item.takeOff(parent.current, currentFilter);
							generate();
						});
					else
						takeButton.setFg(take.a(1)).fgSelfColor(true).onClick(()->useEquip());
				}
				
				if(!t.getItem().throwable || t.current){
					throwButton.setFg(throwImg.a(.3f)).fgSelfColor(true).onClick(()->{});
					append += "无法丢弃本装备。";
				}else{
					throwButton.setFg(throwImg.a(1)).fgSelfColor(true).onClick(()->removeEquip());
				}
				
				if(append.length()!=0)
					append = "\n[#d38181]"+append+"[]";
				
				Label name;
				$.add(name = new Label(t.getItem().name,30)).setPosition(410, 130).setColor(Color.valueOf("ff6600")).appendTo(description);
				$.add(new Label(("("+(t.getItem()==currentHeroEquip?"当前，":"")+"拥有"+t.getItem().count+"个")+")",16).position((int) (name.getX()+name.getWidth()+15), 130)).appendTo(description).setColor(Color.LIGHT_GRAY);
				ScrollPane pane = new ScrollPane(new Label(t.getItem().description+append+"\n"+((Equipment)t.getItem()).description2,17).warp(true).markup(true));
				pane.setupOverscroll(20, 200, 200);
				pane.getStyle().vScroll=Res.getDrawable(Setting.IMAGE_MENU_EQUIP+"mini_scrollbar.png");
				pane.getStyle().vScrollKnob=Res.getDrawable(Setting.IMAGE_MENU_EQUIP+"mini_scrollbarin.png");
				pane.setFadeScrollBars(false);
				pane.layout();
				$.add(pane).setSize(578, 60).setPosition(410, 68).appendTo(description);
				$.add(new Image(t)).setPosition(246,18).setSize(143,143).appendTo(description);
			}
		});
		
		ilist.setScrollPercentY(oldTop!=oldTop?0:oldTop);
		
		ilist.setCurrent(currentHeroEquip);
		
		$.add(new HeroImage(parent.current,0)).appendTo(inner).setPosition(285, 480);
		$.add(new Label(parent.current.name,30)).setPosition(420, 495).appendTo(inner);
		$.add(new Label(parent.current.jname,24)).setAlpha(.1f).setPosition(420, 483).appendTo(inner);
		
		$.add(Res.get(Setting.UI_BASE_PRO)).setSize((float)parent.current.target.getProp("hp")/(float)parent.current.target.getProp("maxhp")*161,20).setPosition(851, 456).appendTo(data).setColor(Color.valueOf("c33737"));
		$.add(Res.get(Setting.UI_BASE_PRO)).setSize((float)parent.current.target.getProp("mp")/(float)parent.current.target.getProp("maxmp")*161,20).setPosition(851, 429).appendTo(data).setColor(Color.valueOf("3762c3"));
		$.add(new Label(parent.current.target.getProp("hp")+"/"+parent.current.target.getProp("maxhp"),18).align(851, 455).width(161)).setColor(Color.WHITE).appendTo(data);
		$.add(new Label(parent.current.target.getProp("mp")+"/"+parent.current.target.getProp("maxmp"),18).align(851, 428).width(161)).setColor(Color.WHITE).appendTo(data);
		
		int pad = 38,off = 421,x = 942;
		$.add(new Label(parent.current.target.getProp("hit"),22).align(x, off-=pad).width(80)).appendTo(data);
		$.add(new Label(parent.current.target.getProp("speed"),22).align(x, off-=pad).width(80)).appendTo(data);
		$.add(new Label(parent.current.target.getProp("defense"),22).align(x, off-=pad).width(80)).appendTo(data);
		$.add(new Label(parent.current.target.getProp("magicDefense"),22).align(x, off-=pad).width(80)).appendTo(data);
		$.add(new Label(parent.current.target.getProp("attack"),22).align(x, off-=pad).width(80)).appendTo(data);
		
		$.add(new Label(parent.current.target.getProp("magicAttack"),22).align(x, off-=pad).width(80)).appendTo(data);
		$.add(data).children().setTouchable(Touchable.disabled);
	}
	
	private List<Icon> getEquips(String equipType){
		List<Equipment> equips=RPG.ctrl.item.search(Equipment.class.getSimpleName(),Equipment.class);
		List<Icon> io= new ArrayList<>();
		for(Equipment e:equips){
			if(!e.equipType.equalsIgnoreCase(equipType))
				continue;
			Icon obj=new Icon().generateIcon(e, true);
			if(e.onlyFor!=null && !e.onlyFor.equals(parent.current.getName()))//filter by hero class
				obj.enable=false;
			io.add(obj);
		}
		Collections.sort(io);//将无效的内容放在后面
		return io;
	}
	
	private void prev(){
		if(parent.prev())
			generate();
	}
	
	private void next(){
		if(parent.next())
			generate();
	}

	public void draw(SpriteBatch batch) {
		stage.draw();
	}
	
	private void useEquip(){
		if(ilist.getCurrent()!=null && !ilist.getCurrent().current && ilist.getCurrent().getItem()!=null)
			if(!ilist.getCurrent().getItem().use(Context.map(parent.current, null, null, null)).success)
				RPG.putMessage("装备失败。", Color.RED);
		generate();
	}
	
	@SuppressWarnings("serial")
	private void removeEquip(){
		if(ilist.getCurrent()!=null && !ilist.getCurrent().current && ilist.getCurrent().getItem()!=null)
			
		RPG.popup.add(ThrowItemView.class,new HashMap<Object, Object>(){{
			put("title","丢弃物品");
			put("width",100);
			put("item",ilist.getCurrent());
			put("callback",new CustomRunnable<Integer>() {
				public void run(Integer t) {
					RPG.putMessage("成功丢弃道具 "+ilist.getCurrent().getItem().name+" "+t+" 个", Color.RED);
					RPG.ctrl.item.remove(ilist.getCurrent().getItem(), t);
					generate();
				}
			});
		}});
		
	}

	public void logic() {
		stage.act();
	}
	
	public void onkeyDown(int keyCode) {
		if(Keys.ESCAPE==keyCode || Keys.X==keyCode)
			this.disposed=true;
		stage.keyDown(keyCode);
	}

}
