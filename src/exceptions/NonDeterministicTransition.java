package exceptions;

public class NonDeterministicTransition extends Exception {

    public NonDeterministicTransition(String transition) {
        super(transition);
    }
}
