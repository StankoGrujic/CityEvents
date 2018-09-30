package com.szmasters.meetup;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;



class App 
{
    public static void main( String[] args )
    {
    	try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(new URI("https://api.meetup.com/2/cities?country=rs&offset=0&format=json&photo-host=public&page=500&radius=50&order=size&desc=false&sig_id=264269988&sig=16e7ac8f872bff3a6f8c9b6962267b10b5cf1e7f"))
			        .GET()
			        .build();
		HttpResponse<String> response = HttpClient.newHttpClient()
				.send(request, BodyHandlers.ofString());
		Gson gson = new Gson();
		JsonObject obj = gson.fromJson(response.body(), JsonObject.class);
		JsonElement g = obj.get("results");        
        JsonArray obj1 = gson.fromJson(g.toString(), JsonArray.class);
        int i = 1;
        List<City> gradovi = new ArrayList<City>();
        List<Event> events = new LinkedList<Event>();
        System.out.println("List of cities in Serbia:");
        for (JsonElement gr : obj1) {
        	String grad = gr.getAsJsonObject().get("city").toString();
        	grad = grad.substring(1, grad.length()-1);
			System.out.println(i + ". " + grad);
			gradovi.add(new City(i,grad));
			i++;
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter city number: ");
        int n = 1;
        try {
        n = sc.nextInt();
        }catch(InputMismatchException e ) {
        	System.out.println("Wrong input.");
        	System.exit(1);
        }
        if(n<1 || n>=i) {
        	System.out.println("There is no city of that number!");
        }else {
        	System.out.println("Loading...");
        	List<String> grupe = new LinkedList<String>();
        	HttpRequest request1 = HttpRequest.newBuilder()
			        .uri(new URI("https://api.meetup.com/find/groups?photo-host=public&country=rs&sig_id=264269988&sig=781b43e647178fc30c21e6ad5917caae2f554448"))
			        .GET()
			        .build();
		HttpResponse<String> response1 = HttpClient.newHttpClient()
				.send(request1, BodyHandlers.ofString());
		JsonArray obj2 = gson.fromJson(response1.body(), JsonArray.class);
		for (JsonElement o : obj2) {
			if(o.getAsJsonObject().get("city").toString().equals("\"" + gradovi.get(n-1).getName() + "\"") ) {
				String s = o.getAsJsonObject().get("urlname").toString();
				grupe.add(s.substring(1, s.length()-1));
			}
		}
		for (String urlname : grupe) {
			HttpRequest request2 = HttpRequest.newBuilder()
			        .uri(new URI("https://api.meetup.com/"+ urlname +"/events"))
			        .GET()
			        .build();
			HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response2 = client.send(request2, BodyHandlers.ofString());
		String es = response2.body();
		if(!es.equals("[]")) {
		JsonArray obj3 = gson.fromJson(es, JsonArray.class);
		for (JsonElement o : obj3) {
			String name = "";
			String description = "";
			String local_time = "";
			String local_date = "";
			String address = "";
			String place = "";
			if(o.getAsJsonObject().get("name") != null) {
			name = o.getAsJsonObject().get("name").toString();
			name = name.substring(1, name.length()-1);
			}
			if(o.getAsJsonObject().get("description") != null) {
			description = o.getAsJsonObject().get("description").toString();
			description = description.substring(1, description.length()-1);
			}
			if(o.getAsJsonObject().get("local_time") != null) {
			local_time = o.getAsJsonObject().get("local_time").toString();
			local_time = local_time.substring(1, local_time.length()-1);
			}
			if(o.getAsJsonObject().get("local_date") != null) {
			local_date = o.getAsJsonObject().get("local_date").toString();
			local_date = local_date.substring(1, local_date.length()-1);
			}
			if(o.getAsJsonObject().get("venue") != null) {
				JsonObject obj4 = o.getAsJsonObject().get("venue").getAsJsonObject();
				if(obj4.get("address_1") != null) {
					address = obj4.get("address_1").toString();
					address = address.substring(1, address.length()-1);
				}
				if(obj4.get("name") != null) {
					place = obj4.get("name").toString();
					place = place.substring(1, place.length()-1);
				}
			}
			events.add(new Event(name, description, local_time, local_date, address, place));
		}
	}
	}
		if(!events.isEmpty()) {
		System.out.println("List of events in " + gradovi.get(n-1).getName() + ":");
		for(Event e : events) {
		System.out.println("Event name: " + e.getName());
		System.out.println("Description of event: \n" + e.getDescription());
		System.out.println("Date and time: " + e.getLocal_date() + ", " + e.getLocal_time());
		System.out.println("Address: " + e.getAddress() + ", " + e.getPlace());
		System.out.println();
		}
		}else
			System.out.println("City "+ gradovi.get(n-1).getName() + " has no events.");
       }
        
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}
