package com.rpsg.rpg.utils.display;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.rpsg.rpg.object.script.BaseScriptExecutor;
import com.rpsg.rpg.object.script.Script;
import com.rpsg.rpg.system.base.Res;
import com.rpsg.rpg.system.ui.Image;
import com.rpsg.rpg.utils.game.GameUtil;
import com.rpsg.rpg.utils.game.Logger;
import com.rpsg.rpg.view.GameViews;


public class FG {
	Image currentImageL;
	Image currentImageR;

	public static final int LEFT=0;
	public static final int RIGHT=1;

	boolean leftFade=true;
	boolean rightFade=true;
	public Stage stage;
	
	public void init() {
		stage=new Stage(new ScalingViewport(Scaling.stretch, GameUtil.stage_width, GameUtil.stage_height, new OrthographicCamera()),GameViews.gameview.stage.getBatch());
		currentImageL=currentImageR=null;
		Logger.info("立绘控制器初始化完成。");
	}

	public BaseScriptExecutor show(final Script script,final String imgPath,final int position){
		return script.set((BaseScriptExecutor) new BaseScriptExecutor() {
			@Override
			public void init() {
				if(position==LEFT){
					boolean nul=currentImageL==null;
					currentImageL=Res.get(imgPath);
					currentImageL.setScale(0.35f);
					currentImageL.setOrigin(Align.bottomLeft);
					if(nul)
						currentImageL.setColor(1,1,1,0f);
					leftFade=true;
				} else {
					boolean nul = currentImageR == null;
					currentImageR = Res.get(imgPath);
					currentImageR.setScale(0.35f);
					currentImageR.setScaleX(-0.35f);
					currentImageR.setX(GameUtil.stage_width);
					currentImageR.setOrigin(Align.bottomLeft);
					if (nul)
						currentImageR.setColor(1, 1, 1, 0f);
					rightFade = true;
				}
			}
		});
	}

	public BaseScriptExecutor hide(final Script script,final String imgPath,final int position){
		return script.set((BaseScriptExecutor) new BaseScriptExecutor() {
			@Override
			public void init() {
				if (position == LEFT)
					currentImageL = null;
				else
					currentImageR = null;
			}
		});
	}

	public BaseScriptExecutor hideAll(final Script script){
		return script.set((BaseScriptExecutor) new BaseScriptExecutor() {
			@Override
			public void init() {
				leftFade = false;
				rightFade = false;
			}
		});
	}

	public BaseScriptExecutor hide(final Script script,final int position){
		return script.set((BaseScriptExecutor) new BaseScriptExecutor() {
			@Override
			public void init() {
				if (position == LEFT)
					leftFade = false;
				else
					rightFade = false;
			}
		});
	}

	public void draw(SpriteBatch sb){
		sb.end();
		sb.begin();
		if(currentImageL!=null){
			if(leftFade && currentImageL.getColor().a<1)
				currentImageL.setColor(1,1,1,currentImageL.getColor().a+0.1f);
			if(!leftFade && currentImageL.getColor().a>0)
				currentImageL.setColor(1,1,1,currentImageL.getColor().a-0.2f);
			if(!leftFade && currentImageL.getColor().a<=0)
				currentImageL=null;
			if(currentImageL!=null)
				currentImageL.draw(sb);
		}
		if(currentImageR!=null){
			if(rightFade && currentImageR.getColor().a<1)
				currentImageR.setColor(1,1,1,currentImageR.getColor().a+0.1f);
			if(!rightFade && currentImageR.getColor().a>0)
				currentImageR.setColor(1,1,1,currentImageR.getColor().a-0.2f);
			if(!rightFade && currentImageR.getColor().a<=0)
				currentImageR=null;
			if(currentImageR!=null)
				currentImageR.draw(sb);
		}
	}

}

