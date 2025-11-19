package lab.socnet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SocialNetwork {

	private List<Post> posts;

	public SocialNetwork() {
		this.posts = new LinkedList<>();
		posts.add(new Post(authors.get(3), hashtags.get(1), samplePosts.get(5)));
		posts.add(new Post(authors.get(1), hashtags.get(5), samplePosts.get(6)));
		posts.add(new Post(authors.get(8), hashtags.get(8), samplePosts.get(8)));
		posts.add(new Post(authors.get(4), hashtags.get(7), samplePosts.get(11)));
		posts.add(new Post(authors.get(6), hashtags.get(2), samplePosts.get(15)));
		posts.add(new Post(authors.get(3), hashtags.get(2), samplePosts.get(0)));
		posts.add(new Post(authors.get(7), hashtags.get(6), samplePosts.get(18)));
		posts.add(new Post(authors.get(0), hashtags.get(9), samplePosts.get(4)));
		posts.add(new Post(authors.get(2), hashtags.get(7), samplePosts.get(13)));
		posts.add(new Post(authors.get(5), hashtags.get(1), samplePosts.get(2)));
		posts.add(new Post(authors.get(4), hashtags.get(3), samplePosts.get(17)));
		posts.add(new Post(authors.get(3), hashtags.get(2), samplePosts.get(10)));
	}

	public void processPosts(PostFilter filter) {
		for (Post battery : posts) {
			if (filter.apply(battery)) {
				filter.process(battery);
			}
		}

	}

	public interface PostFilter {
		boolean apply(Post post);
		void process(Post post);
	}

	public void printAll() {
		for (Post post : posts) {
            if(!post.isDisabled()) {
                System.out.println(post);
            }
		}
	}

	public static final List<String> hashtags = List.of("#Inspiration", "#Motivation", "#LifeGoals", "#Success",
			"#Travel", "#HealthyLifestyle", "#LoveLife", "#TechTrends", "#NaturePhotography", "#PositiveVibes");

	public static final List<String> authors = List.of("J.K. Rowling", "George Orwell", "Jane Austen", "Mark Twain",
			"Ernest Hemingway", "Agatha Christie", "F. Scott Fitzgerald", "Charles Dickens", "Virginia Woolf",
			"Leo Tolstoy");

	public static final List<String> samplePosts = List.of(
		    "Just finished an amazing book! Highly recommend it. 📚✨",
		    "Exploring new places is the best therapy. 🌍✈️",
		    "Nothing beats a morning run to start the day right. 🏃‍♂️🌞",
		    "Stay positive and keep chasing your dreams. 💪🌟",
		    "Enjoying the little moments that make life beautiful. 🌸🌿",
		    "Tech is evolving faster than ever! What’s your favorite gadget of the year? 🤔📱",
		    "Fresh coffee and good vibes to kick off the week! ☕💼",
		    "Nature never ceases to amaze. Look at this stunning sunset! 🌅😍",
		    "Life is about creating memories, not just milestones. ❤️🌟",
		    "Your vibe attracts your tribe. Stay positive! 🌈💖",
		    "Dream big, work hard, and make it happen. 💼🚀",
		    "Happiness is homemade. Cooking something special tonight! 🍳❤",
		    "Let’s take care of our planet. Every little effort counts. 🌱🌎",
		    "Music is life. What’s your go-to playlist today? 🎶🎧",
		    "Sometimes all you need is a little break and a lot of coffee. ☕📖",
		    "Weekend vibes: chill, recharge, repeat. 🌴🌞",
		    "Every sunrise brings new opportunities. Go grab them! 🌅💫",
		    "When words fail, let the photos do the talking. 📸✨",
		    "Kindness is free, spread it everywhere. 🌈🤝",
		    "Keep your face to the sunshine, and shadows will fall behind you. 🌞💛"
		);

}
