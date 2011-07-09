/*
 *  LocalisationService.java  - Uses Google translation service to get translations for "Today" and "No Date"
 *  Copyright (C) 2011 Kai Toedter
 *  kai@toedter.com
 *  www.toedter.com
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package com.toedter.jcalendar.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Locale;

import javax.ws.rs.core.MultivaluedMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class LocalizationService {

	private static String apiKey = "AIzaSyCzxWHfr9oNzP-2u9fUoIiHbSCKwgd9HjM";
	private static String googleTranslationService = "https://www.googleapis.com/language/translate/v2";

	private static String translateString(String sourceString,
			String sourceLanguage, String targetLanguage) {
		ClientConfig config = new DefaultClientConfig();

		Client c = Client.create(config);

		WebResource r = c.resource(googleTranslationService);
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();

		params.add("key", apiKey);
		params.add("q", sourceString);
		params.add("source", sourceLanguage);
		params.add("target", targetLanguage);

		String response = r.queryParams(params).get(String.class);
		return response;
	}

	private static String extractTranslationFromJSON(String response) {
		final JSONObject jsonObj = (JSONObject) JSONValue.parse(response);
		String translation = null;
		if (jsonObj != null && jsonObj.containsKey("data")) {
			final JSONObject data = (JSONObject) jsonObj.get("data");
			final JSONArray array = (JSONArray) data.get("translations");
			final JSONObject keyValuePair = (JSONObject) array.get(0);
			translation = keyValuePair.get("translatedText").toString();
		}
		return translation;
	}

	private static void createResourceBundle(Locale locale, String today, String noDate) {
		try {
		    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("jcalendar_" + locale + ".properties"), "UTF-8"));
		    out.write("todayButton.text=" + today);
		    out.newLine();
		    out.write("nullDateButton.text=" + noDate);
		    out.newLine();
		    out.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void main(String[] args) {
		Locale[] locales = Calendar.getAvailableLocales();

		for (Locale locale : locales) {
			if (locale.getCountry().length() > 0) {

				int count = 0;
				boolean translationOK = false;
				while (!translationOK && count < 1) {
					count++;
					String language = locale.getLanguage();
					try {
						String today = "Today";
						String noDate = "No Date";
						if (!language.equals("en")) {
							today = extractTranslationFromJSON(translateString(
									"Today", "en", language));
							noDate = extractTranslationFromJSON(translateString(
									"No Date", "en", language));
						}
						System.out.println(locale + ", " + language + ": "
								+ today + ", " + noDate);
						translationOK = true;
						createResourceBundle(locale, today, noDate);
					} catch (Exception e) {
						System.out.println("Cannot translate to locale: "
								+ locale + ", language: " + language);
						translationOK = false;
					}
				}
			}
		}
	}

}