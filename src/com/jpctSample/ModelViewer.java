package com.jpctSample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.CompressFormat;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;
import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.exceptions.AndARException;

public class ModelViewer extends AndARActivity implements
		SurfaceHolder.Callback {

	public static final int TYPE_INTERNAL = 0;

	public static final int TYPE_EXTERNAL = 1;

	public static final boolean DEBUG = false;

	private final int MENU_SCALE = 0;
	private final int MENU_ROTATE = 1;
	private final int MENU_TRANSLATE = 2;
	private final int MENU_SCREENSHOT = 3;

	private int mode = MENU_SCALE;

	// private Model model;
	/*
	 * private Model model2; private Model model3; private Model model4; private
	 * Model model5;
	 */
	private Model3D model3d;
	/*
	 * private Model3D model3d2; private Model3D model3d3; private Model3D
	 * model3d4; private Model3D model3d5;
	 */
	private ProgressDialog waitDialog;
	private Resources res;

	ARToolkit artoolkit;

	public ModelViewer() {
		super(false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		super.setNonARRenderer(new LightingRenderer());
		res = getResources();
		artoolkit = getArtoolkit();
		getSurfaceView().getHolder().addCallback(this);
	}

	public void uncaughtException(Thread thread, Throwable ex) {
		System.out.println("");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		super.surfaceCreated(holder);

		// if(model == null) {
		waitDialog = ProgressDialog.show(this, "",
				getResources().getText(R.string.loading), true);
		waitDialog.show();
		new ModelLoader().execute();
		// }
	}

	private class ModelLoader extends AsyncTask<Void, Void, Void> {

		private String modelName2patternName(String modelName) {
			/*
			 * String patternName = "android";
			 * 
			 * if (modelName.equals("plant.obj")) { patternName =
			 * "marker_rupee16"; } else if (modelName.equals("chair.obj")) {
			 * patternName = "marker_fisch16"; } else if
			 * (modelName.equals("tower.obj")) { patternName = "marker_peace16";
			 * } else if (modelName.equals("bench.obj")) { patternName =
			 * "marker_at16"; } else if (modelName.equals("towergreen.obj")) {
			 * patternName = "marker_hand16"; }
			 */
			String patternName = "4x4_1";

			return patternName;
		}

		@Override
		protected Void doInBackground(Void... params) {

			model3d = new Model3D(getResources(),modelName2patternName("modelFileName") + ".patt");

			if (Config.DEBUG)
				Debug.stopMethodTracing();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			waitDialog.dismiss();

			try {
				if (model3d != null) {
					artoolkit.registerARObject(model3d);
				}
			} catch (AndARException e) {
				e.printStackTrace();
			}
			startPreview();
		}
	}
}