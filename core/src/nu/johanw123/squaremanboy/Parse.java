package nu.johanw123.squaremanboy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;


public class Parse implements HttpResponseListener, Input.TextInputListener
{
    private String app_id;
    private String app_key;

    public static boolean inputShowing = false;

    private static String sInputUserName = "noname";

    public Parse(){
        app_id = "8X5SpUyzyAOumAAOx95iqorcLkHflWqMpk3T7n6x";
        app_key = "P3DKtsrrhr0RKGH6XYjScQmQwSSvmcbTSnMWGsKV";
    }

    public void add_net_score(){
        inputShowing = true;
        //Gdx.input.getTextInput(this, "Enter name", sInputUserName);
        Gdx.input.getPlaceholderTextInput(this, "Enter name", sInputUserName);
        //add_net_score("lol");
    }
    private void add_net_score(String userName)
    {
        sInputUserName = userName;
        HttpRequest httpPost = new HttpRequest(HttpMethods.POST);
        httpPost.setUrl("https://api.parse.com/1/classes/score/");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("X-Parse-Application-Id", app_id);
        httpPost.setHeader("X-Parse-REST-API-Key", app_key);
        int score = SGame.currentLevelId - 10;
        httpPost.setContent("{\"score\": " + score + ", \"user\": \"" + userName + "\", \"time\": " + Extensions.FormatFloatPrecision(SGame.survivalTotalTime, 3) +"}");
        Gdx.net.sendHttpRequest(httpPost,Parse.this);
    }
    /*
    public void clean_net_score()
    {
    	HttpRequest httpPost = new HttpRequest(HttpMethods.POST);
    	httpPost.setUrl("https://api.parse.com/1/functions/deleteLowScores");
    	httpPost.setHeader("Content-Type", "application/json");
    	httpPost.setHeader("X-Parse-Application-Id", app_id);
        httpPost.setHeader("X-Parse-REST-API-Key", app_key);
        httpPost.setContent("{}");
        
        Gdx.net.sendHttpRequest(httpPost,Parse.this);
    }
    */
    public void get_net_score(){
        // LibGDX NET CLASS
        HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
        httpGet.setUrl("https://api.parse.com/1/classes/score/");
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("X-Parse-Application-Id", app_id);
        httpGet.setHeader("X-Parse-REST-API-Key", app_key);
        httpGet.setContent("order=-score,time");
        //httpGet.setContent("limit=6");
        Gdx.net.sendHttpRequest(httpGet,Parse.this);
    }

    @Override
    public void handleHttpResponse(HttpResponse httpResponse) {
        final int statusCode = httpResponse.getStatus().getStatusCode();
        String message = httpResponse.getResultAsString();
        
        
        System.out.println(statusCode + " " + message);
        
        if(message.contains("Delete successfully"))
        	return;

         switch(statusCode)
         {
             case 200: //200 = get score
                
            	ArrayList<String> userScores = new ArrayList<String>();
                                
                JSONObject o = (JSONObject)JSONValue.parse(message);
                JSONArray a = (JSONArray)o.get("results");                
            
                for(int i = 0; i < a.size(); ++i)
                {
                	JSONObject scoreObject = (JSONObject)a.get(i);
                	Object userName = scoreObject.get("user");
                	Object score = scoreObject.get("score");
                	Object time = scoreObject.get("time");
                	
                	userScores.add(userName + " - " + score + " .................... " + time + " s");
                	
                	if(i >= 12)
                   	 break;
                }
/*
                for(int i = 0; i < data.results.size(); ++i)
                {
                	//System.out.println(data.results.get(i).score);
                	
                	 String userScore = ((JsonResults)data.results.get(i)).user + " - " + ((JsonResults)data.results.get(i)).score + " .................... " + ((JsonResults)data.results.get(i)).time + " s";
                     userScores.add(userScore);

                     if(i >= 12)
                    	 break;
                }
                
                
                */
                /*
                        for(int i = 0; i < 12; ++i)
                    {
                        String s = message.substring(message.indexOf("score") + "score".length() + 2);
                        String score = s.substring(0, s.indexOf(','));
                        String s2 = message.substring(message.indexOf("user") + "user".length() + 3);
                        String user = s2.substring(0, s2.indexOf(',') - 1);

                        String userScore = user + " - " + score;
                        userScores.add(userScore);
                        //userScores[i] = userScore;

                        //System.out.println(userScore);

                        message = s2;
                    }
                        */
                        
                        
                        
                        
                 //userScores.add("hejj");
                    SGame.ExternalHandler.showAd(true);
                    HighscoreMenuScreen.sScores = userScores;
                 break;

             case 201: //201 = uploaded score
                 SurvivalEndScreen.uploadCode = 2;
                 break;
             case 400: //400 = error
            	 SurvivalEndScreen.uploadCode = -2;
                 break;
         }

    }

    @Override
    public void failed(Throwable t) {
        System.out.println(t.getMessage());
    }

    @Override
    public void input(String text) {
        //do checks on input here
        SurvivalEndScreen.uploadCode = 1;
        add_net_score(text);
        SGame.ExternalHandler.showFullAd(true);
        SGame.ExternalHandler.showAd(true);

        inputShowing =false;
    }

    @Override //input canceled
    public void canceled() {

        inputShowing =false;
    }

	@Override
	public void cancelled() {
		// TODO Auto-generated method stub
		
	}

}


