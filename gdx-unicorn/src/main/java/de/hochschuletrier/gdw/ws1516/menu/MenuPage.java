package de.hochschuletrier.gdw.ws1516.menu;

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
    

     protected final void addSlider(float min, float max,float stepSize,int x, int y,String text){
        
        Slider slider = new Slider(min,max,stepSize,false,skin,"default-horizontal");
        Label label = new Label(text,skin,"default");
        int size = 250; 
        slider.setValue(50);
//        slider.setColor(Color.PINK);
        

        label.setBounds(x, y+1, 100, 100);
        slider.setBounds(x+70,y,size,100);
        
        
        Label value= new Label(""+(int)slider.getValue(),skin,"default");
        value.setBounds(x+size+80,y+1,100,100);   
        slider.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                value.setText(""+(int)slider.getValue());
            }
        });
        addActor(slider);
        addActor(label);
        addActor(value);
    }

    protected final void addLeftAlignedButton(int x, int y, int width, int height, String text,Runnable runnable) {
        TextButton button = addButton(x, y, width, height, text, runnable, "default");
        button.getLabel().setAlignment(Align.left);
    }

    protected final void addCenteredButton(int x, int y, int width, int height, String text, Runnable runnable) {
        TextButton button = addButton(x - width / 2, y - height / 2, width, height, text, runnable, "default");
        button.getLabel().setAlignment(Align.center);
    }

    protected final TextButton addButton(int x, int y, int width, int height, String text, Runnable runnable, String style) {
        TextButton button = new TextButton(text, skin, style);
        button.setBounds(x, y, width, height);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runnable.run();
            }
        });
        addActor(button);
        return button;
    }
    
    protected ImageButton createImageButton(Texture texture, float x, float y, float width, float height, Runnable runnable, boolean add) {
        ImageButton ib = new ImageButton(new SpriteDrawable(new Sprite(texture)));
        
        ib.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runnable.run();
            }
        });
        ib.setPosition(x, y);
       // ib.setBounds(x, y, width, height);
        
        if(add==true) {
        super.addActor(ib);
        }
        return ib;
     }
}
