package lab.docs;

public abstract class SigneDocument implements Document {
    public abstract String getNotSignedContent();

    public String getSignature() {
        return "Signature:" + getNotSignedContent().length();
    }

    @Override
    public String getContent() {
        return getNotSignedContent() + "\n" + getSignature();
    }
}
