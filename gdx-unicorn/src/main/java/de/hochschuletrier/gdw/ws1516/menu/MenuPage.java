package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;





import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ws1516.Main;

public class MenuPage extends Group {

    protected Main main = Main.getInstance();
    protected AssetManagerX assetManager = main.getAssetManager();
    protected final Skin skin;

    public MenuPage(Skin skin, String background) {
        super();
        this.skin = skin;

        addActor(new DecoImage(assetManager.getTexture(background)));

        setVisible(false);
    }

    @Override
    public void act(float delta) {
        if (isVisible()) {
            super.act(delta);
        }
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        if (clipBegin(0, 0, getWidth(), getHeight())) {
            super.drawChildren(batch, parentAlpha);
            clipEnd();
        }
    }
    
    
     protected final Slider addLabeledSlider(float min, float max,float stepSize,int x, int y,String text,boolean add,float sliderValue){
        Label label = new Label(text,skin,"default");
        int size = 250; 
        label.setPosition(x, y+7);
        Slider slider = new Slider(min,max,stepSize,false,skin,"default-horizontal");
        slider.setValue(sliderValue);
        slider.setBounds(x+80,y,size,35);
        Label value= new Label(""+(int)slider.getValue(),skin,"default");
        value.setPosition(x+size+90,y+7);   
        slider.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                value.setText(""+(int)slider.getValue());
            }
        });
        if(add==true)
        {
         addActor(slider);
         addActor(label);
         addActor(value);
        }
        return slider;
    }

    

    protected final void addLeftAlignedButton(int x, int y, int width, int height, String text,Runnable runnable,String sound) {
        TextButton button = addButton(x, y, width, height, text, runnable, "default",sound);
        button.getLabel().setAlignment(Align.left);
    }

    protected final void addCenteredButton(int x, int y, int width, int height, String text, Runnable runnable,String sound) {
        TextButton button = addButton(x - width / 2, y - height / 2, width, height, text, runnable, "default",sound);
        button.getLabel().setAlignment(Align.center);
    }

    protected final TextButton addButton(int x, int y, int width, int height, String text, Runnable runnable, String style,String sound) {
        TextButton button = new TextButton(text, skin, style);
        button.setBounds(x, y, width, height);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundEmitter.playGlobal(assetManager.getSound(sound), false);
                runnable.run();
            }
        });
        addActor(button);
        return button;
    }

    
    protected ImageButton createImageButton(Texture texture, float x, float y, float width, float height, Runnable runnable, String sound, boolean addToActor, boolean tintable) {
        
        ImageButton ib;
        if(tintable==true) {
            Sprite downSprite = new Sprite(texture);
            downSprite.setColor(Color.MAGENTA);
            // NICHT GUT FÜR PERFORMANCE!!
            ib = new ImageButton(new SpriteDrawable(new Sprite(texture)), new SpriteDrawable(downSprite));
        }
        
        else {
            // NICHT GUT FÜR PERFORMANCE!!
            ib = new ImageButton(new SpriteDrawable(new Sprite(texture)));
        }
        ib.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundEmitter.playGlobal(assetManager.getSound(sound), false);
                runnable.run();
            }
        });
        ib.setPosition(x, y);
               
        if(addToActor==true) {
        super.addActor(ib);
        }
        return ib;
     }
}
