package com.rpsg.rpg.io;

import java.io.Serializable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.rpsg.gdxQuery.CustomRunnable;
import com.rpsg.rpg.core.RPG;
import com.rpsg.rpg.view.GameViews;

public class Account implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public User user;
	
	/**
	 * @param name
	 * @param pass
	 * @param hold
	 * @param callback
	 */
	public void login(String name, String pass, boolean hold, CustomRunnable<State> callback){
		HttpRequest request = new HttpRequest();
		//request.setUrl(Setting.NET_LOGIN_URL+"?name=小苹果！");
		request.setUrl(String.format( "http://localhost/RPSGWeb/RPSGJsonWCFService.svc/login"));
		//request.setUrl("http://localhost:16274/RPSGJsonWCFService.svc/login");
		request.setMethod(HttpMethods.POST);
		
		request.setContent(String.format("{\"UserName\":\"%s\",\"Password\":\"%s\"}", name,pass));
		request.setHeader("content-type", "application/json");
		String command = "sendHttpRequest";
		
		HttpResponseListener listener = new HttpResponseListener() {
			public void handleHttpResponse(HttpResponse response) {
				RPG.toast.add(response.getResultAsString(), Color.GREEN,13);
				GameViews.loadview.stop(command);
			}
			
			public void failed(Throwable t) {
				t.printStackTrace();
			}
			
			public void cancelled() {
				System.out.println("cancelled");
			}
		};

		Gdx.net.sendHttpRequest(request, listener);
		GameViews.loadview.start(command);
	}
	
	public void logout(){
		user = null;
	}
	
	public boolean isLogin(){
		return user == null;
	}
	
	public static class User{
		public String username,password,email,session;
	}
	
	public static enum State{
		success,wrongPassword,timeout,cancel
	}
}
