package com.goldsprite.gdxcore.ui;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.utils.Null;

/**
 封装GSplitPane用于设置绝对切割量，以此解决分割时size被拉伸问题
 */
public class GSplitPane extends SplitPane{
	public float absoluteSplitAmount = 0.5f;

	private Actor firstWidget;
	private Actor secondWidget;

	public GSplitPane(@Null Actor firstWidget, @Null Actor secondWidget, boolean vertical, Skin skin) {
		super(firstWidget, secondWidget, vertical, skin);
		this.firstWidget = firstWidget;
		this.secondWidget = secondWidget;
		init();
	}

	private void init() {
		addListener(new InputListener() {
				public boolean touchDown(InputEvent ev, float x, float y, int p, int b) { return hit(x, y, false).equals(GSplitPane.this); }
				public void touchDragged(InputEvent event, float x, float y, int pointer) { updateAbsoluteSplitAmount(); }
			});
	}

	//mode: 0-藏于左边或上边, 0-藏于右边或下边
	public void setToggleData(int mode, float expandSplitAmount){
		setToggleData(mode, 0, expandSplitAmount);
	}
	public void setToggleData(int mode, float minSplitAmount, float expandSplitAmount){
		this.mode = mode;
		this.minSplitAmount = minSplitAmount;
		this.expandSplitAmount = expandSplitAmount;
		oldSplitAmount = expandSplitAmount;
	}

	int mode;
	boolean expand;
	float minSplitAmount, expandSplitAmount, oldSplitAmount;
	//根据
	public void toggle(){
		if((mode==0&&getAbsoluteSplitAmount()<=minSplitAmount) || (mode==1&&getAbsoluteSplitAmount()>=1-minSplitAmount)) expand = false;
		else expand = true;
		if (expand) {
			oldSplitAmount = getAbsoluteSplitAmount();
		}
		expand = !expand;
		toggle(expand);
	}
	public void toggle(boolean expand){
		this.expand = expand;
		float nowSplitAmount = expand ? oldSplitAmount : mode;
		setAbsoluteSplitAmount(nowSplitAmount);
	}


	public void setAbsoluteSplitAmount(float absoluteSplitAmount) {
		super.setSplitAmount(absoluteSplitAmount);
		this.absoluteSplitAmount = absoluteSplitAmount;
		applyAbsoluteSplit();
	}
	public float getAbsoluteSplitAmount(){
		return absoluteSplitAmount;
	}

	@Override
	protected void sizeChanged() {
		super.sizeChanged();
		applyAbsoluteSplit();
	}


	//根据当前布局大小与舞台大小更新实际绝对分割量
	public void updateAbsoluteSplitAmount(){
		if(getStage()==null) return;
		//横向切割
		if(!isVertical()){
			float firstSplitWidth = getSplitAmount() * getWidth();
			float stageWidth = getStage().getWidth();
			float absoluteSplitAmount = MathUtils.clamp(firstSplitWidth / stageWidth, 0f, 1f);
			setAbsoluteSplitAmount(absoluteSplitAmount);
			//LogViewerService.logDebug("[GSplitPane-更新绝对切割量] 当前宽度: %.1f, 舞台宽度: %.1f, 绝对分割宽度计算结果: %.2f", firstSplitWidth, stageWidth, absoluteSplitAmount);
		}
		//纵向切割
		else {
			float firstSplitHeight = getSplitAmount() * getHeight();
			float stageHeight = getStage().getHeight();
			float absoluteSplitAmount = MathUtils.clamp(firstSplitHeight / stageHeight, 0f, 1f);
			setAbsoluteSplitAmount(absoluteSplitAmount);
			//LogViewerService.logDebug("[GSplitPane-更新绝对切割量] 当前高度: %.1f, 舞台高度: %.1f, 绝对分割高度计算结果: %.2f", firstSplitHeight, stageHeight, absoluteSplitAmount);
		}
	}

	//根据舞台大小与归一化绝对分割量，计算预计大小，然后转为预计分割量并应用
	private void applyAbsoluteSplit() {
		if(getStage()==null) return;
		//横向切割
		if(!isVertical()){
			float stageWidth = getStage().getWidth();
			float absoluteSplitAmount = getAbsoluteSplitAmount();
			float prevWidth = stageWidth * absoluteSplitAmount;
			float splitPaneWidth = getWidth();
			float prevSplitAmount = MathUtils.clamp(prevWidth / splitPaneWidth, 0f, 1f);
			setSplitAmount(prevSplitAmount);
			//LogViewerService.logDebug("[GSplitPane-应用绝对切割量] 舞台宽度: %.1f, 设定绝对切割量: %.2f, 预计宽度: %.1f, 分割栏宽度: %.1f, 预计实际切割量: %.2f", stageWidth, absoluteSplitAmount, prevWidth, splitPaneWidth, prevSplitAmount);
		}
		//纵向切割
		else {
			float stageHeight = getStage().getHeight();
			float absoluteSplitAmount = getAbsoluteSplitAmount();
			float prevHeight = stageHeight * absoluteSplitAmount;
			float splitPaneHeight = getHeight();
			float prevSplitAmount = MathUtils.clamp(prevHeight / splitPaneHeight, 0f, 1f);
			setSplitAmount(prevSplitAmount);
			//LogViewerService.logDebug("[GSplitPane-应用绝对切割量] 舞台高度: %.1f, 设定绝对切割量: %.2f, 预计高度: %.1f, 分割栏高度: %.1f, 预计实际切割量: %.2f", stageHeight, absoluteSplitAmount, prevHeight, splitPaneHeight, prevSplitAmount);
		}
	}

}
