package lab.docs;

public class Encrypted extends SigneDocument {
    String text;
    public Encrypted(String content) {
        this.text = content;
    }

    @Override
    public String getNotSignedContent() {
        return text.toUpperCase();
    }
}
