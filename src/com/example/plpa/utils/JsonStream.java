package com.example.plpa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.os.Message;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class JsonStream {
	public List<Message> readJsonStream(InputStream in) throws IOException {
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try {
			return readMessagesArray(reader);
		} finally {
			reader.close();
		}
	}

	public List<Message> readMessagesArray(JsonReader reader)
			throws IOException {
		List<Message> messages = new ArrayList<Message>();

		reader.beginArray();
		while (reader.hasNext()) {
			messages.add(readMessage(reader));
		}
		reader.endArray();
		return messages;
	}

	public Message readMessage(JsonReader reader) throws IOException {
		long id = -1;
		String text = null;
		// User user = null;
		List<Double> geo = null;

		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("id")) {
				id = reader.nextLong();
			} else if (name.equals("text")) {
				text = reader.nextString();
			} else if (name.equals("geo") && reader.peek() != JsonToken.NULL) {
				geo = readDoublesArray(reader);
				// } else if (name.equals("user")) {
				// user = readUser(reader);
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return new Message();
		// return new Message(id, text, user, geo);
	}

	public List<Double> readDoublesArray(JsonReader reader) throws IOException {
		List<Double> doubles = new ArrayList<Double>();

		reader.beginArray();
		while (reader.hasNext()) {
			doubles.add(reader.nextDouble());
		}
		reader.endArray();
		return doubles;
	}

	// public User readUser(JsonReader reader) throws IOException {
	// String username = null;
	// int followersCount = -1;
	//
	// reader.beginObject();
	// while (reader.hasNext()) {
	// String name = reader.nextName();
	// if (name.equals("name")) {
	// username = reader.nextString();
	// } else if (name.equals("followers_count")) {
	// followersCount = reader.nextInt();
	// } else {
	// reader.skipValue();
	// }
	// }
	// reader.endObject();
	// return new User(username, followersCount);
	// }
	/*
	 * [ { "id": 912345678901, "text": "How do I stream JSON in Java?", "geo":
	 * null, "user": { "name": "json_newb", "followers_count": 41 } }, { "id":
	 * 912345678902, "text": "@json_newb just use JsonWriter!", "geo":
	 * [50.454722, -104.606667], "user": { "name": "jesse", "followers_count": 2
	 * } } ]
	 */

	public void writeJsonStream(OutputStream out, List<Message> messages)
			throws IOException {
		JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
		writer.setIndent("    ");
		writeMessagesArray(writer, messages);
		writer.close();
	}

	public void writeMessagesArray(JsonWriter writer, List<Message> messages)
			throws IOException {
		writer.beginArray();
		for (Message message : messages) {
			writeMessage(writer, message);
		}
		writer.endArray();
	}

	public void writeMessage(JsonWriter writer, Message message)
			throws IOException {
		// writer.beginObject();
		// writer.name("id").value(message.getId());
		// writer.name("text").value(message.getText());
		// if (message.getGeo() != null) {
		// writer.name("geo");
		// writeDoublesArray(writer, message.getGeo());
		// } else {
		// writer.name("geo").nullValue();
		// }
		// writer.name("user");
		// writeUser(writer, message.getUser());
		// writer.endObject();
	}

	// public void writeUser(JsonWriter writer, User user) throws IOException {
	// writer.beginObject();
	// writer.name("name").value(user.getName());
	// writer.name("followers_count").value(user.getFollowersCount());
	// writer.endObject();
	// }

	public void writeDoublesArray(JsonWriter writer, List<Double> doubles)
			throws IOException {
		writer.beginArray();
		for (Double value : doubles) {
			writer.value(value);
		}
		writer.endArray();
	}
}
