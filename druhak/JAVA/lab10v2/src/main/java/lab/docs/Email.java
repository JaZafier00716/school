package lab.docs;

public class Email implements  Document {
    private String content;

    public Email(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}
