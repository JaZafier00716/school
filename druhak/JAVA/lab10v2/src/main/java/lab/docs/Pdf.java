package lab.docs;

public class Pdf extends SigneDocument {
    private String text;

    public Pdf(String content) {
        this.text = content;
    }


    @Override
    public String getNotSignedContent() {
        return "--PDF--" + text + "--PDF--";
    }
}
