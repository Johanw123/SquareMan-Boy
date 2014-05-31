package nu.johanw123.squaremanboy.android;


import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import nu.johanw123.squaremanboy.SGame;


public class MainActivity extends AndroidApplication {
	
	protected AdView adView; //small ad
	protected AdView fullAdView; //big ad
	
    private final String ADCODE = "ca-app-pub-4286110477420635/6978020412";
    private final String FULLADCODE = "ca-app-pub-4286110477420635/8454753610";
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//config.useGL20 = true;
		
		initialize(new SGame(), config);
		
		*/
		SGame.CurrentPlatform = SGame.ePlatform.Android;
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//config.resolutionStrategy.calcMeasures(1280, 720);
	          
        adView = new AdView(this); // Put in your secret key here
        fullAdView = new AdView(this); // Put in your secret key here
        
    	adView.setAdSize(AdSize.BANNER);
	    adView.setAdUnitId(ADCODE);
	    
	    fullAdView.setAdSize(AdSize.FULL_BANNER); 
	    fullAdView.setAdUnitId(FULLADCODE);
	    
	    AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        
        AdRequest fullAdRequest = new AdRequest.Builder().build();
        fullAdView.loadAd(fullAdRequest);
		
	    // Create the layout
	    RelativeLayout layout = new RelativeLayout(this);

	    // Request fullscreen
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

	    // Create the libGDX view
	    View gameView = initializeForView(new SGame(new RequestHandler(adView, fullAdView)), config);
	    
	    /*
	    AdRequest adRequest = new AdRequest(null);
        AdRequest fadReq = new AdRequest(null);
        adView.loadAd(adRequest);
        fullAdView.loadAd(fadReq);
	    */

	    // Create the AdMob view
/*
	    adView.setAdListener(new AdListener() {
	        @Override
	            public void onAdLoaded() {
	                //lastAdTime = System.currentTimeMillis();
	            }
	        });
*/
	    
	    // Add the libGDX view
	    layout.addView(gameView);

	    // Setup position of ads
	    RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
	        RelativeLayout.LayoutParams.WRAP_CONTENT,
	        RelativeLayout.LayoutParams.WRAP_CONTENT);
	    adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	    adParams.addRule(RelativeLayout.CENTER_IN_PARENT);
	    
	    RelativeLayout.LayoutParams fadParams = 
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
	    fadParams.addRule(RelativeLayout.CENTER_IN_PARENT);
	    
	    
	    layout.addView(adView, adParams);
	    layout.addView(fullAdView, fadParams);
	    adView.setVisibility(View.GONE);
	    fullAdView.setVisibility(View.GONE); //closed at the start

	    setContentView(layout);	    
	}
}