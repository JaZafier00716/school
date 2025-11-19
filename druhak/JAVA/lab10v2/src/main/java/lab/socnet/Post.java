package lab.socnet;

import java.util.List;

public class Post {

	private String hashtag;
	private String text;
	private String author;
	private boolean disabled;

	public Post(String author, String hashtag, String text) {
		this.author = author;
		this.hashtag = hashtag;
		this.text = text;
	}

	public Post disable() {
		disabled = true;
		return this;
	}

	public String getText() {
		return text;
	}

	public String getAuthor() {
		return author;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public String getHashtag() {
		return hashtag;
	}

	@Override
	public String toString() {
		return "Post [hashtag=" + hashtag + ", author=" + author + ", disabled=" + disabled + ", text=" + text + "]";
	}

	public static void print(List<Post> posts) {
		for (Post post : posts) {
			System.out.println(post);
		}
	}

}
