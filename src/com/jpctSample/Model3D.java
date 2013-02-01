package com.jpctSample;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;

import edu.dhbw.andar.ARObject;

import android.content.res.Resources;
import android.opengl.GLUtils;
import android.util.Log;

public class Model3D extends ARObject implements Serializable {

	private FrameBuffer buffer = null;
	private World world = null;
	private Object3D thing3D = null;
	private int fps = 0;
	private int thingScale = 2;

	private Camera cam = null;
	private Light sun = null;
	private RGBColor back = new RGBColor(0, 0, 0, 0);
	private Resources res;

	public Model3D(Resources res, String patternName) {
		super("model", patternName, 80.0, new double[] { 0, 0 });
		this.res = res;		
		// model.finalize();
	}

	@Override
	public void init(GL10 gl) {
		//initJPCT(gl);
		if (buffer != null) {
			buffer.dispose();
		}
		int w = 960;
		int h = 464;
		buffer = new FrameBuffer(gl, w, h);



			world = new World();
    		world.setAmbientLight(150, 150, 150);

			sun = new Light(world);
			sun.setIntensity(250, 250, 250);
			
	

			try {
				thing3D = loadObjModel("com.jpctSample:raw/laoshufinal_obj","com.jpctSample:raw/laoshufinal_mtl", thingScale);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Create a texture out of the icon...:-)
			Texture texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(res.getDrawable(R.drawable.laoshu)), 64, 64));
			TextureManager.getInstance().addTexture("texture", texture);
			thing3D.setTexture("texture");

			thing3D.build();

			world.addObject(thing3D);

			cam = world.getCamera();
			cam.moveCamera(Camera.CAMERA_MOVEOUT, 100);
			cam.lookAt(thing3D.getTransformedCenter());

			SimpleVector sv = new SimpleVector();
			sv.set(thing3D.getTransformedCenter());
			sv.y -= 100;
			sv.z -= 100;
			sun.setPosition(sv);
			//MemoryHelper.compact();		
	}

	@Override
	public void draw(GL10 gl) {
		super.draw(gl);
		//drawJPCT();
		
		buffer.clear(back);
		world.renderScene(buffer);
		world.draw(buffer);
		buffer.display();
	}
	
	private Object3D loadObjModel(String objFileName, String mtlFileName, float scale) throws FileNotFoundException {
    	InputStream objStream = resourceToInputStream(objFileName);
    	InputStream mtlStream = resourceToInputStream(mtlFileName);
        Object3D[] model = Loader.loadOBJ(objStream, mtlStream, scale);
        
		// Create a texture out of the icon...:-)
		//Texture texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(getResources().getDrawable(R.drawable.laoshu)), 64, 64));
		//TextureManager.getInstance().addTexture("texture", texture);

		
       // Object3D[] model = Loader.load3DS(is,scale);
        Object3D o3d = new Object3D(0);
        Object3D temp = null;
        for (int i = 0; i < model.length; i++) {
            temp = model[i];
            temp.setCenter(SimpleVector.ORIGIN);
            //temp.rotateX((float)( -.5*Math.PI));
            temp.rotateX((float)( -15.5*Math.PI));
            temp.rotateY((float)( -5.5*Math.PI));
            temp.rotateMesh();
            temp.setRotationMatrix(new Matrix());
            o3d = Object3D.mergeObjects(o3d, temp);
            o3d.build();
        }
        return o3d;
    }

	private void initJPCT(GL10 gl) {
		if (buffer != null) {
			buffer.dispose();
		}
		int w = 960;
		int h = 464;
		buffer = new FrameBuffer(gl, w, h);

		// if (master == null) {

		world = new World();
		world.setAmbientLight(20, 20, 20);

		sun = new Light(world);
		sun.setIntensity(250, 250, 250);

		// Create a texture out of the icon...:-)
		//Texture texture = new Texture(BitmapHelper.rescale(
			//	BitmapHelper.convert(res.getDrawable(R.drawable.icon)), 64, 64));
		//Texture texture = new Texture(resourceToInputStream("com.jpctSample:raw/motocycle_jpg"));
		//TextureManager.getInstance().addTexture("texture", texture);
		

		thing3D = loadModel("com.jpctSample:raw/laoshufinal_obj","com.jpctSample:raw/laoshufinal_mtl", thingScale);
		thing3D.build();
        world.addObject(thing3D);
        world.getCamera().setPosition(-20, -20, -20);
        world.getCamera().lookAt(thing3D.getTransformedCenter());

		/*
		 * if (master == null) { Logger.log("Saving master Activity!"); master =
		 * HelloWorld.this; }
		 */
	}

	// }
	// }

	private void drawJPCT() {
		buffer.clear(back);
		world.renderScene(buffer);
		world.draw(buffer);
		buffer.display();

		/*
		 * if (System.currentTimeMillis() - time >= 1000) { Logger.log(fps +
		 * "fps"); fps = 0; time = System.currentTimeMillis(); }
		 */
		fps++;
	}
	
    private Object3D loadModel(String objFileName, String mtlFileName,float scale) {
    	InputStream objStream = resourceToInputStream(objFileName);
    	InputStream mtlStream = resourceToInputStream(mtlFileName);
        Object3D[] model = Loader.loadOBJ(objStream, mtlStream, scale);
       //Object3D[] model = Loader.load3DS(objStream, scale);
        Object3D o3d = new Object3D(0);
        Object3D temp = null;
        for (int i = 0; i < model.length; i++) {
            temp = model[i];
            temp.setCenter(SimpleVector.ORIGIN);
            temp.rotateX((float)( -.5*Math.PI));
            temp.rotateMesh();
            temp.setRotationMatrix(new Matrix());
            o3d = Object3D.mergeObjects(o3d, temp);
            o3d.build();
        }
        return o3d;
    	//return Loader.loadMD2(objStream, scale);
    }
    
    private InputStream resourceToInputStream(String resourceID){
    	 InputStream fileIn = res.openRawResource(res.getIdentifier(resourceID, null, null));
    	 
    	 return fileIn;
    }
}